package br.edu.toolrent.api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import br.edu.toolrent.api.service.ChaveSecretaNaoConfiguradaException;
import br.edu.toolrent.api.service.EmailJaCadastradoException;
import br.edu.toolrent.api.service.FalhaAoCriarLoginException;

/**
 * Admin API do Supabase (UC02, RN10) testada com o servidor HTTP simulado
 * — nenhum teste desta suite fala com a rede real.
 */
class SupabaseAuthUsuarioRepositoryTest {

    private static final String URL = "https://projeto-teste.supabase.co";
    private static final String SECRET = "sb_secret_de_teste";
    private static final UUID AUTH_ID =
            UUID.fromString("33333333-3333-3333-3333-333333333333");

    private MockRestServiceServer servidor;
    private SupabaseAuthUsuarioRepository repository;

    @BeforeEach
    void prepara() {
        RestClient.Builder builder = RestClient.builder();
        servidor = MockRestServiceServer.bindTo(builder).build();
        repository = new SupabaseAuthUsuarioRepository(builder, URL, SECRET);
    }

    @Test
    @DisplayName("cria o login e devolve o id do Supabase Auth")
    void criaLoginEDevolveId() {
        servidor.expect(requestTo(URL + "/auth/v1/admin/users"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("apikey", SECRET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + SECRET))
                .andExpect(content().json("""
                        {"email":"novo@toolrent.dev","password":"senha123",
                         "email_confirm":true}"""))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"id\":\"" + AUTH_ID + "\"}"));

        UUID id = repository.criarUsuario("novo@toolrent.dev", "senha123");

        assertThat(id).isEqualTo(AUTH_ID);
        servidor.verify();
    }

    @Test
    @DisplayName("422 do provedor vira e-mail ja cadastrado")
    void erro422ViraEmailJaCadastrado() {
        servidor.expect(requestTo(URL + "/auth/v1/admin/users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"code\":\"email_exists\"}"));

        assertThatThrownBy(() ->
                repository.criarUsuario("duplicado@toolrent.dev", "senha123"))
                .isInstanceOf(EmailJaCadastradoException.class);
    }

    @Test
    @DisplayName("400 com 'already registered' tambem vira 409")
    void mensagemDejaCadastradoTambemVira409() {
        servidor.expect(requestTo(URL + "/auth/v1/admin/users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"msg\":\"A user with this email address "
                                + "has already been registered\"}"));

        assertThatThrownBy(() ->
                repository.criarUsuario("duplicado@toolrent.dev", "senha123"))
                .isInstanceOf(EmailJaCadastradoException.class);
    }

    @Test
    @DisplayName("outra recusa do provedor vira 502, sem vazar detalhe")
    void outraRecusaViraFalhaAoCriarLogin() {
        servidor.expect(requestTo(URL + "/auth/v1/admin/users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"msg\":\"Password too short\"}"));

        assertThatThrownBy(() ->
                repository.criarUsuario("novo@toolrent.dev", "x"))
                .isInstanceOf(FalhaAoCriarLoginException.class)
                .hasMessageContaining("Não foi possível criar o login");
    }

    @Test
    @DisplayName("sem secret key o cadastro nao sai do servidor (503)")
    void semSecretKeyNaoSaiDoServidor() {
        SupabaseAuthUsuarioRepository semChave =
                new SupabaseAuthUsuarioRepository(RestClient.builder(), URL, "");

        assertThatThrownBy(() ->
                semChave.criarUsuario("novo@toolrent.dev", "senha123"))
                .isInstanceOf(ChaveSecretaNaoConfiguradaException.class);
        // nenhuma expectativa de HTTP: a recusa acontece antes de rede
        servidor.verify();
    }

    @Test
    @DisplayName("compensacao: falha ao excluir login nao propaga")
    void compensacaoFalhaNaoPropaga() {
        servidor.expect(requestTo(URL + "/auth/v1/admin/users/" + AUTH_ID))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        repository.excluirUsuario(AUTH_ID);

        servidor.verify();
    }
}
