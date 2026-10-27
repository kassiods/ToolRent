package br.edu.toolrent.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API do ToolRent.
 *
 * <p>Esta aplicação concentra <b>todas</b> as regras de negócio do sistema
 * (RN01 a RN12). O aplicativo Flutter não decide nada: ele exibe, coleta
 * entrada e chama esta API, que valida e revalida tudo.
 *
 * <p>O motivo está registrado em
 * {@code docs/decisoes/0004-regra-de-negocio-na-api.md}: o app roda no
 * dispositivo do usuário e, portanto, pode ser modificado por ele.
 *
 * <p>Organização dos pacotes:
 * <ul>
 *   <li>{@code config}     — configuração, segurança e parâmetros de negócio</li>
 *   <li>{@code domain}     — entidades e enums do domínio</li>
 *   <li>{@code repository} — acesso a dados</li>
 *   <li>{@code service}    — regras de negócio</li>
 *   <li>{@code web}        — controllers REST e DTOs</li>
 * </ul>
 */
@SpringBootApplication
public class ToolRentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolRentApiApplication.class, args);
    }
}
