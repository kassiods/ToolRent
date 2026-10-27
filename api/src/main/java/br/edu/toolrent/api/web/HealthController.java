package br.edu.toolrent.api.web;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Verificação de que a API está no ar.
 *
 * <p>Único endpoint público do sistema — todos os demais exigem autenticação
 * a partir da Sprint 3 (US01).
 *
 * <p>Serve para a equipe confirmar que a API subiu antes de investigar um
 * problema no aplicativo:
 *
 * <pre>curl http://localhost:8080/api/health</pre>
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> verificar() {
        return Map.of(
                "status", "no ar",
                "servico", "toolrent-api",
                "versao", "0.1.0",
                "momento", OffsetDateTime.now().toString()
        );
    }
}
