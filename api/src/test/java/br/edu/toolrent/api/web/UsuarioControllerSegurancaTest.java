package br.edu.toolrent.api.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.toolrent.api.config.SecurityConfig;
import br.edu.toolrent.api.domain.PerfilUsuario;
import br.edu.toolrent.api.domain.Usuario;
import br.edu.toolrent.api.service.AutoinativacaoException;
import br.edu.toolrent.api.service.EmailJaCadastradoException;
import br.edu.toolrent.api.service.NaoAdministradorException;
import br.edu.toolrent.api.service.UsuarioInativoException;
import br.edu.toolrent.api.service.UsuarioNaoEncontradoException;
import br.edu.toolrent.api.service.UsuarioSemAcessoException;
import br.edu.toolrent.api.service.UsuarioService;

/**
 * Contrato 401/403 da US01 e do CRUD da UC02 (US02) — o diagrama de
 * sequencia do UC01, parte 2.
 *
 * <p>Exercita a cadeia de seguranca de verdade (filtro Bearer, entry point,
 * service). O {@link JwtDecoder} real busca as chaves no JWKS do Supabase
 * (ES256) em runtime — aqui ele e substituido por um mock: o que se valida
 * e o MAPEAMENTO de status da cadeia, nao a criptografia, que e responsabilidade
 * da biblioteca e do JWKS.
 */
@WebMvcTest({UsuarioController.class, HealthController.class})
@Import(SecurityConfig.class)
@TestPropertySource(properties = "toolrent.supabase.url=http://localhost")
class UsuarioControllerSegurancaTest {

    private static final String SUB = "22222222-2222-2222-2222-222222222222";
    private static final String TOKEN_VALIDO = "token-valido";
    private static final String TOKEN_EXPIRADO = "token-expirado";
    private static final String TOKEN_ASSINATURA_INVALIDA = "token-assinatura-invalida";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtDecoder jwtDecoder;

    // ---------------------------------------------------------------
    // Tokens — o decoder decide; o teste simula a decisao
    // ---------------------------------------------------------------

    private static Jwt jwtValido() {
        return new Jwt(
                TOKEN_VALIDO,
                Instant.now().minusSeconds(60),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "ES256", "kid", "chave-de-teste"),
                Map.of("sub", SUB, "iss", "supabase"));
    }

    private void decoderAceitaTokenValido() {
        given(jwtDecoder.decode(TOKEN_VALIDO)).willReturn(jwtValido());
    }

    private static Usuario usuarioAtivo() {
        return new Usuario(UUID.randomUUID(), UUID.fromString(SUB), "Victor",
                "victor@toolrent.dev", PerfilUsuario.ADMINISTRADOR,
                true, OffsetDateTime.now());
    }

    // ---------------------------------------------------------------
    // 401 — nao autenticado
    // ---------------------------------------------------------------

    @Test
    @DisplayName("health e publica — sobe sem token")
    void healthEPublicaSemToken() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("sem token recebe 401 com mensagem")
    void semTokenRecebe401ComMensagem() throws Exception {
        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").isString());
    }

    @Test
    @DisplayName("valor que nao e JWT recebe 401")
    void valorQueNaoEJwtRecebe401() throws Exception {
        given(jwtDecoder.decode("nao-e-um-jwt"))
                .willThrow(new BadJwtException("Malformed token"));

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer nao-e-um-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("token expirado recebe 401")
    void tokenExpiradoRecebe401() throws Exception {
        given(jwtDecoder.decode(TOKEN_EXPIRADO))
                .willThrow(new JwtValidationException("JWT expired",
                        List.of(new OAuth2Error("invalid_token", "exp", null))));

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_EXPIRADO))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("token com assinatura invalida recebe 401")
    void tokenAssinaturaInvalidaRecebe401() throws Exception {
        given(jwtDecoder.decode(TOKEN_ASSINATURA_INVALIDA))
                .willThrow(new BadJwtException("Failed to validate signature"));

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + TOKEN_ASSINATURA_INVALIDA))
                .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // 200 / 403 — autenticado, decisao do service
    // ---------------------------------------------------------------

    @Test
    @DisplayName("usuario ativo recebe 200 com o perfil e sem senha")
    void usuarioAtivoRecebe200ComPerfilSemSenha() throws Exception {
        decoderAceitaTokenValido();
        given(usuarioService.carregarSessao(UUID.fromString(SUB)))
                .willReturn(usuarioAtivo());

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + TOKEN_VALIDO)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("victor@toolrent.dev"))
                .andExpect(jsonPath("$.perfil").value("ADMINISTRADOR"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    @DisplayName("usuario inativo recebe 403 com mensagem especifica")
    void usuarioInativoRecebe403ComMensagemEspecifica() throws Exception {
        decoderAceitaTokenValido();
        given(usuarioService.carregarSessao(UUID.fromString(SUB)))
                .willThrow(new UsuarioInativoException());

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem")
                        .value("Conta inativa: contate o administrador do sistema"));
    }

    @Test
    @DisplayName("token valido sem linha de usuario recebe 403")
    void tokenSemLinhaDeUsuarioRecebe403() throws Exception {
        decoderAceitaTokenValido();
        given(usuarioService.carregarSessao(UUID.fromString(SUB)))
                .willThrow(new UsuarioSemAcessoException());

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem").value("Usuário sem acesso ao sistema"));
    }

    // ---------------------------------------------------------------
    // UC02 — CRUD de usuarios (US02)
    // ---------------------------------------------------------------

    @Test
    @DisplayName("CRUD sem token recebe 401")
    void crudSemTokenRecebe401() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("lista de usuarios responde 200 sem senha")
    void listaDeUsuariosResponde200SemSenha() throws Exception {
        decoderAceitaTokenValido();
        given(usuarioService.listar(UUID.fromString(SUB)))
                .willReturn(List.of(usuarioAtivo()));

        mockMvc.perform(get("/api/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("victor@toolrent.dev"))
                .andExpect(jsonPath("$[0].senha").doesNotExist());
    }

    @Test
    @DisplayName("RN09: nao-administrador na lista recebe 403 com mensagem")
    void naoAdministradorNaListaRecebe403() throws Exception {
        decoderAceitaTokenValido();
        given(usuarioService.listar(UUID.fromString(SUB)))
                .willThrow(new NaoAdministradorException());

        mockMvc.perform(get("/api/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem")
                        .value("Apenas administradores podem gerenciar usuários"));
    }

    @Test
    @DisplayName("cadastro valido responde 201 e nunca devolve senha")
    void cadastroValidoResponde201SemSenha() throws Exception {
        decoderAceitaTokenValido();
        Usuario criado = new Usuario(UUID.randomUUID(), UUID.randomUUID(),
                "Novo", "novo@toolrent.dev", PerfilUsuario.CLIENTE,
                true, OffsetDateTime.now());
        given(usuarioService.cadastrar(UUID.fromString(SUB), "Novo",
                "novo@toolrent.dev", PerfilUsuario.CLIENTE, "senha123"))
                .willReturn(criado);

        mockMvc.perform(post("/api/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Novo","email":"novo@toolrent.dev",
                                 "perfil":"CLIENTE","senha":"senha123"}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Novo"))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("cadastro com corpo invalido recebe 400 com mensagem")
    void cadastroComCorpoInvalidoRecebe400() throws Exception {
        decoderAceitaTokenValido();

        mockMvc.perform(post("/api/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"","email":"nao-e-email",
                                 "perfil":null,"senha":"123"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").isString());
    }

    @Test
    @DisplayName("RN10: e-mail duplicado recebe 409 com mensagem clara")
    void emailDuplicadoRecebe409() throws Exception {
        decoderAceitaTokenValido();
        given(usuarioService.cadastrar(UUID.fromString(SUB), "Duplicado",
                "duplicado@toolrent.dev", PerfilUsuario.CLIENTE, "senha123"))
                .willThrow(new EmailJaCadastradoException());

        mockMvc.perform(post("/api/usuarios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Duplicado","email":"duplicado@toolrent.dev",
                                 "perfil":"CLIENTE","senha":"senha123"}"""))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("E-mail já cadastrado no sistema"));
    }

    @Test
    @DisplayName("edicao com id inexistente recebe 404")
    void edicaoComIdInexistenteRecebe404() throws Exception {
        decoderAceitaTokenValido();
        UUID inexistente = UUID.randomUUID();
        given(usuarioService.editar(UUID.fromString(SUB), inexistente,
                "Nome", PerfilUsuario.ATENDENTE))
                .willThrow(new UsuarioNaoEncontradoException(inexistente));

        mockMvc.perform(put("/api/usuarios/{id}", inexistente)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Nome\",\"perfil\":\"ATENDENTE\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").isString());
    }

    @Test
    @DisplayName("DELETE inativa: 200 com ativo=false (RN11)")
    void deleteInativaComAtivoFalso() throws Exception {
        decoderAceitaTokenValido();
        Usuario alvo = new Usuario(UUID.randomUUID(), UUID.randomUUID(),
                "Cliente", "cliente@teste.com", PerfilUsuario.CLIENTE,
                false, OffsetDateTime.now());
        given(usuarioService.inativar(UUID.fromString(SUB), alvo.getId()))
                .willReturn(alvo);

        mockMvc.perform(delete("/api/usuarios/{id}", alvo.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    @DisplayName("autoinativacao recebe 403 com mensagem propria")
    void autoinativacaoRecebe403() throws Exception {
        decoderAceitaTokenValido();
        UUID idDoProprio = UUID.randomUUID();
        given(usuarioService.inativar(UUID.fromString(SUB), idDoProprio))
                .willThrow(new AutoinativacaoException());

        mockMvc.perform(delete("/api/usuarios/{id}", idDoProprio)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_VALIDO))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem")
                        .value("Não é possível inativar a própria conta"));
    }
}
