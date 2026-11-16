package br.edu.toolrent.api.repository;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import br.edu.toolrent.api.service.ChaveSecretaNaoConfiguradaException;
import br.edu.toolrent.api.service.EmailJaCadastradoException;
import br.edu.toolrent.api.service.FalhaAoCriarLoginException;

/**
 * Criacao de login no Supabase Auth pela Admin API — UC02 (RN10).
 *
 * <p>A senha nasce la: o ToolRent nunca armazena nem devolve senha. A
 * secret key ({@code sb_secret_…}) fica SÓ neste backend, em variavel de
 * ambiente — se vazar do app, qualquer pessoa teria acesso total ao banco
 * (ver docs/arquitetura.md).
 *
 * <p>Camada de acesso a dados: traduz resposta do provedor em excecao de
 * negocio. Nenhuma regra mora aqui.
 */
@Repository
public class SupabaseAuthUsuarioRepository {

    private static final Logger log =
            LoggerFactory.getLogger(SupabaseAuthUsuarioRepository.class);

    private final RestClient restClient;
    private final String secretKey;

    public SupabaseAuthUsuarioRepository(
            RestClient.Builder builder,
            @Value("${toolrent.supabase.url}") String url,
            @Value("${toolrent.supabase.secret-key}") String secretKey) {
        this.secretKey = secretKey == null ? "" : secretKey;
        this.restClient = builder
                .baseUrl((url == null ? "" : url) + "/auth/v1")
                .defaultHeader("apikey", this.secretKey)
                .defaultHeader(HttpHeaders.AUTHORIZATION,
                        "Bearer " + this.secretKey)
                .build();
    }

    /**
     * Cria o login no Supabase Auth e devolve o {@code auth.users.id}.
     *
     * @param email e-mail do novo usuario (unico no provedor)
     * @param senha senha provisoria definida pelo administrador — trafega
     *              uma unica vez, sobre HTTPS, e nunca e guardada aqui
     * @return o UUID do usuario no Supabase Auth
     * @throws ChaveSecretaNaoConfiguradaException se a variavel de ambiente
     *         esta vazia
     * @throws EmailJaCadastradoException se o provedor ja tem esse e-mail
     * @throws FalhaAoCriarLoginException em qualquer outra recusa
     */
    public UUID criarUsuario(String email, String senha) {
        if (secretKey.isBlank()) {
            throw new ChaveSecretaNaoConfiguradaException();
        }

        try {
            Resposta resposta = restClient.post()
                    .uri("/admin/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "email", email,
                            "password", senha,
                            "email_confirm", true))
                    .retrieve()
                    .body(Resposta.class);
            return UUID.fromString(resposta.id());
        } catch (RestClientResponseException excecao) {
            if (ehEmailDuplicado(excecao)) {
                throw new EmailJaCadastradoException();
            }
            log.error("Supabase Auth recusou a criacao do login (HTTP {})",
                    excecao.getStatusCode().value(), excecao);
            throw new FalhaAoCriarLoginException();
        }
    }

    /**
     * Remove o login criado quando a linha da tabela {@code usuario} nao
     * pôde ser gravada — compensacao para que nenhum dado fique parcial
     * (garantia minima do UC02).
     *
     * <p>Falha aqui e registrada em log e segue: o login orfao fica para
     * limpeza manual, mas o erro original do cadastro e o que importa.
     */
    public void excluirUsuario(UUID authUserId) {
        if (secretKey.isBlank()) {
            return;
        }
        try {
            restClient.delete()
                    .uri("/admin/users/{id}", authUserId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException excecao) {
            log.error("Falha ao compensar login {} no Supabase Auth (HTTP {})",
                    authUserId, excecao.getStatusCode().value(), excecao);
        }
    }

    /**
     * O GoTrue devolve 422 quando o e-mail ja existe; versoes antigas
     * devolvem 400 com a frase "already registered".
     */
    private static boolean ehEmailDuplicado(RestClientResponseException e) {
        if (e.getStatusCode().value() == 422) {
            return true;
        }
        String corpo = e.getResponseBodyAsString();
        return corpo != null
                && corpo.toLowerCase().contains("already been registered");
    }

    private record Resposta(String id) {
    }
}
