package br.edu.toolrent.api.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
 * <p>Exercita a cadeia de seguranca de verdade com tokens assinados no
 * proprio teste, na mesma chave que o Supabase usaria.
 */
@WebMvcTest({UsuarioController.class, HealthController.class})
@Import(SecurityConfig.class)
@TestPropertySource(properties = "toolrent.supabase.jwt-secret=" + UsuarioControllerSegurancaTest.SEGREDO)
class UsuarioControllerSegurancaTest {

    /** Mesmo formato do JWT secret do Supabase: string UTF-8 de >= 256 bits. */
    static final String SEGREDO = "ferramentas-toolrent-segredo-de-teste-2026";

    private static final String SUB = "22222222-2222-2222-2222-222222222222";
    private static final String OUTRO_SEGREDO = "outro-segredo-completamente-diferente-01";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    // ---------------------------------------------------------------
    // Tokens
    // ---------------------------------------------------------------

    private static String assinar(String sub, Date emitidoEm, Date expiraEm,
                                  String segredo) throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(sub)
                .issuer("supabase")
                .issueTime(emitidoEm)
                .expirationTime(expiraEm)
                .build();
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        jwt.sign(new MACSigner(segredo.getBytes(StandardCharsets.UTF_8)));
        return jwt.serialize();
    }

    private static String tokenValido() throws Exception {
        return assinar(SUB,
                new Date(System.currentTimeMillis() - 60_000),
                new Date(System.currentTimeMillis() + 3_600_000),
                SEGREDO);
    }

    private static String tokenExpirado() throws Exception {
        return assinar(SUB,
                new Date(System.currentTimeMillis() - 7_200_000),
                new Date(System.currentTimeMillis() - 3_600_000),
                SEGREDO);
    }

    private static String tokenDeOutroSegredo() throws Exception {
        return assinar(SUB,
                new Date(System.currentTimeMillis() - 60_000),
                new Date(System.currentTimeMillis() + 3_600_000),
                OUTRO_SEGREDO);
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
        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer nao-e-um-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("token expirado recebe 401")
    void tokenExpiradoRecebe401() throws Exception {
        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenExpirado()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("token assinado com outro segredo recebe 401")
    void tokenAssinaturaInvalidaRecebe401() throws Exception {
        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + tokenDeOutroSegredo()))
                .andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------
    // 403 — autenticado mas sem acesso
    // ---------------------------------------------------------------

    @Test
    @DisplayName("usuario ativo recebe 200 com o perfil e sem senha")
    void usuarioAtivoRecebe200ComPerfilSemSenha() throws Exception {
        given(usuarioService.carregarSessao(UUID.fromString(SUB)))
                .willReturn(usuarioAtivo());

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + tokenValido())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("victor@toolrent.dev"))
                .andExpect(jsonPath("$.perfil").value("ADMINISTRADOR"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    @DisplayName("usuario inativo recebe 403 com mensagem especifica")
    void usuarioInativoRecebe403ComMensagemEspecifica() throws Exception {
        given(usuarioService.carregarSessao(UUID.fromString(SUB)))
                .willThrow(new UsuarioInativoException());

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + tokenValido()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem")
                        .value("Conta inativa: contate o administrador do sistema"));
    }

    @Test
    @DisplayName("token valido sem linha de usuario recebe 403")
    void tokenSemLinhaDeUsuarioRecebe403() throws Exception {
        given(usuarioService.carregarSessao(UUID.fromString(SUB)))
                .willThrow(new UsuarioSemAcessoException());

        mockMvc.perform(get("/api/usuarios/me")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + tokenValido()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem").value("Usuário sem acesso ao sistema"));
    }
}
