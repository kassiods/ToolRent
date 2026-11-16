package br.edu.toolrent.api.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import br.edu.toolrent.api.service.UsuarioInativoException;
import br.edu.toolrent.api.service.UsuarioSemAcessoException;
import br.edu.toolrent.api.service.UsuarioService;

/**
 * Contrato 401/403 da US01 — o diagrama de sequencia do UC01, parte 2.
 *
 * <p>Exercita a cadeia de seguranca de verdade (filtro Bearer, entry point,
 * service). O {@link JwtDecoder} real busca as chaves no JWKS do Supabase
 * (ES256) em runtime — aqui ele e substituido por um mock: o que se valida
 * e o MAPEAMENTO 401/403 da cadeia, nao a criptografia, que e responsabilidade
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
}
