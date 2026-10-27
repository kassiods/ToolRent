package br.edu.toolrent.api.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Teste do endpoint de verificacao.
 *
 * <p>Nao sobe servidor: o controller e exercitado diretamente. Essa e a
 * fronteira que o projeto mantem — logica testavel sem infraestrutura.
 */
class HealthControllerTest {

    private final HealthController controller = new HealthController();

    @Test
    @DisplayName("informa que o servico esta no ar")
    void informaQueEstaNoAr() {
        Map<String, Object> resposta = controller.verificar();

        assertThat(resposta).containsEntry("status", "no ar");
        assertThat(resposta).containsEntry("servico", "toolrent-api");
    }

    @Test
    @DisplayName("informa a versao e o momento da consulta")
    void informaVersaoEMomento() {
        Map<String, Object> resposta = controller.verificar();

        assertThat(resposta).containsKey("versao");
        assertThat(resposta.get("momento")).asString().isNotBlank();
    }
}
