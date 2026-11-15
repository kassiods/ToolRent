package br.edu.toolrent.api.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Seguranca da API — autenticacao por JWT do Supabase Auth (RN09, RN10).
 *
 * <p>O contrato e o do diagrama de sequencia do UC01 (parte 2): o filtro JWT
 * apenas estabelece <b>QUEM</b> e o usuario — a autorizacao (qual perfil pode
 * fazer o que) e verificada em {@code service/}, nunca aqui e nunca no app
 * (RN09, ADR 0004).
 *
 * <ul>
 *   <li>Sem token ou token invalido → HTTP 401</li>
 *   <li>Token valido → requisicao segue para o controller e o service</li>
 *   <li>{@code /api/health} e o unico caminho publico</li>
 * </ul>
 *
 * <p>A assinatura do Supabase e HS256 com o <i>JWT secret</i> do projeto. O
 * valor parece base64, mas o Supabase assina com os bytes UTF-8 da propria
 * string — <b>nao decodificar</b> (e como as bibliotecas oficiais tratam).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain cadeiaDeSeguranca(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessao -> sessao
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> {})
                .authorizeHttpRequests(autorizacao -> autorizacao
                        .requestMatchers("/api/health").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> {})
                        .authenticationEntryPoint((requisicao, resposta, excecao) ->
                                escreverJson(resposta, HttpServletResponse.SC_UNAUTHORIZED,
                                        "Não autenticado: informe um token válido"))
                        .accessDeniedHandler((requisicao, resposta, excecao) ->
                                escreverJson(resposta, HttpServletResponse.SC_FORBIDDEN,
                                        "Você não tem permissão para esta ação")))
                .exceptionHandling(excecoes -> excecoes
                        .authenticationEntryPoint((requisicao, resposta, excecao) ->
                                escreverJson(resposta, HttpServletResponse.SC_UNAUTHORIZED,
                                        "Não autenticado: informe um token válido")));

        return http.build();
    }

    /**
     * Valida a assinatura HS256 do JWT emitido pelo Supabase Auth.
     *
     * <p>Usado sem qualquer decodificacao: os bytes do secret em UTF-8 sao a
     * propria chave de assinatura. Sem segredo configurado a API nao sobe —
     * falhar cedo é melhor do que rodar sem validar token (RN09).
     */
    @Bean
    JwtDecoder jwtDecoder(@Value("${toolrent.supabase.jwt-secret}") String segredo) {
        if (segredo == null || segredo.isBlank()) {
            throw new IllegalStateException(
                    "SUPABASE_JWT_SECRET não configurado. Preencha o .env da API "
                            + "(ver api/.env.example) antes de subir o servico.");
        }
        SecretKeySpec chave = new SecretKeySpec(
                segredo.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(chave)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * CORS restrito a origens locais de desenvolvimento — o app Flutter web
     * roda em localhost. Fora disso (desktop, mobile) CORS nao se aplica.
     */
    @Bean
    CorsConfigurationSource fonteDeCors() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOriginPatterns(
                List.of("http://localhost:*", "http://127.0.0.1:*"));
        configuracao.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(
                List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE));

        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/api/**", configuracao);
        return fonte;
    }

    private static void escreverJson(HttpServletResponse resposta, int status,
                                     String mensagem) throws IOException {
        resposta.setStatus(status);
        resposta.setHeader(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        resposta.getWriter().write("{\"mensagem\":\"" + mensagem + "\"}");
    }
}

