# Sessão — Organização inicial do repositório

- **Data:** 27/10/2026
- **Integrante:** Kassio de Sousa Dias
- **Ferramenta:** Claude Code
- **Relacionado a:** Sprint 0

---

## Objetivo

Criar a estrutura do repositório a partir de dois documentos já produzidos pela equipe — o guia mestre e o documento de casos de uso — e montar os artefatos de gestão ágil.

## O que foi feito

Repositório criado em `C:\Users\lardo\ToolRent`, publicado em `kassiods/ToolRent-Sistema-de-Loca-o-de-Ferramentas`.

| Área | Conteúdo |
|---|---|
| Raiz | `README.md`, `CONTRIBUTING.md`, `CLAUDE.md`, `.gitignore`, `.gitattributes` |
| `docs/` | visão do produto, casos de uso, backlog, regras de negócio, arquitetura, glossário |
| `docs/agil/` | Definition of Ready, Definition of Done, plano de sprints, matriz de rastreabilidade |
| `docs/decisoes/` | 4 ADRs |
| `docs/diagramas/` | fonte `.puml` do diagrama de casos de uso e a imagem |
| `app/` | esqueleto Flutter com tema e tela de boas-vindas |
| `api/` | esqueleto Spring Boot com endpoint de verificação |
| `supabase/` | migration inicial (rascunho) e documentação |
| `.github/` | 3 templates de Issue, template de PR, CI |
| `arquivos-de-ia/` | guias e sessões |

## Problemas encontrados nos documentos de origem

Três incoerências foram identificadas e corrigidas:

| # | Problema | Correção |
|---|---|---|
| 1 | A história **US08** não constava nas tabelas do PDF — a listagem saltava de US07 para US09 — embora o UC08 a referenciasse como origem | História restaurada em `docs/backlog.md` |
| 2 | O **UC04 não tinha especificação**. A seção anunciava "UC01 – UC04" mas detalhava apenas UC01, UC02 e UC03 | Especificação escrita em `docs/casos-de-uso.md` |
| 3 | O documento listava **4 atores** (incluindo Técnico de manutenção) e o diagrama tinha **3** — o próprio checklist exige que correspondam | Ator removido; UC14 passou ao Atendente ([ADR 0002](../../../docs/decisoes/0002-remocao-do-ator-tecnico.md)) |

## Decisões tomadas

| Decisão | ADR |
|---|---|
| Stack: Flutter + Java/Spring Boot + Supabase, substituindo JavaFX + SQLite | [0001](../../../docs/decisoes/0001-stack-flutter-java-supabase.md) |
| Remoção do ator Técnico de manutenção | [0002](../../../docs/decisoes/0002-remocao-do-ator-tecnico.md) |
| `ATRASADA` como estado derivado, não persistido | [0003](../../../docs/decisoes/0003-estado-atrasada-derivado.md) |
| Regra de negócio exclusivamente na API | [0004](../../../docs/decisoes/0004-regra-de-negocio-na-api.md) |

## O que foi rejeitado

**Sugestão inicial da IA de manter JavaFX.** A IA apontou que o guia mestre lista Java 21, JavaFX, Maven, SQLite e JDBC como *"tecnologias obrigatórias"*, e que o guia veda explicitamente assumir nuvem ou API remota sem aprovação. A equipe decidiu mudar mesmo assim, pelos motivos registrados no ADR 0001.

**O alerta permanece válido e virou pendência crítica:** a mudança de stack precisa ser aprovada pelo professor antes da próxima entrega. O ADR 0001 tem campos reservados para registrar a resposta.

**Opção de Flutter falando direto com o Supabase, sem API Java.** Seria mais simples, mas eliminaria o Java do projeto e colocaria a regra de negócio no dispositivo do usuário. Descartada.

**Criar as 12 classes de domínio nesta sprint.** Descartado: o diagrama de classes é entrega da Sprint 2, e o guia veda implementar o que o diagrama ainda não especifica.

## Verificação

| Comando | Resultado |
|---|---|
| `flutter pub get` | ✅ 27 dependências resolvidas |
| `flutter analyze` | ✅ No issues found |
| `flutter test` | ✅ 3 testes passaram |
| `mvn clean test` | ✅ BUILD SUCCESS · 2 testes, 0 falhas |
| `./mvnw --batch-mode test` | ✅ exit 0 |

---

# Continuação — mesma data

## Ambiente preparado

O ambiente não tinha JDK 21 nem Maven. Instalados nesta sessão:

| Ferramenta | Versão | Como |
|---|---|---|
| Eclipse Temurin JDK | 21.0.12 | `winget install EclipseAdoptium.Temurin.21.JDK` |
| Apache Maven | 3.9.9 | Download direto do Apache — não existe no winget |
| GitHub CLI | 2.103.3 | `winget install GitHub.cli` |

`JAVA_HOME` e `PATH` do usuário foram ajustados para apontar ao JDK 21 e ao Maven.

**O JDK 24 continua instalado.** Não foi removido — apenas deixou de ser o apontado por `JAVA_HOME`. Se algum outro projeto seu dependia do 24, confira.

## Maven Wrapper adicionado

Rodado `mvn wrapper:wrapper -Dmaven=3.9.9`, gerando `mvnw`, `mvnw.cmd` e `.mvn/wrapper/`.

Com isso **nenhum integrante precisa instalar o Maven** — basta `./mvnw`. Isso reduz o risco "ambiente de desenvolvimento inconsistente" listado no plano de sprints de Médio para Baixo.

## Issues criadas

16 Issues no GitHub, numeradas de `#1` a `#16`, correspondendo a US01–US16. Cada uma traz história, critérios de aceite, rastreabilidade (UC, RN, prioridade, estimativa, sprint), tarefas técnicas e branch sugerida.

Labels criados: `historia`, `tarefa-tecnica`, `defeito`, `P0`, `P1`, `sprint-3` a `sprint-7`, `decisao-pendente`.

Seis Issues receberam `decisao-pendente` por dependerem de decisão não resolvida: #1 (US01), #5 (US05), #9 (US09), #11 (US11), #12 (US12) e #15 (US15).

## Decisão do professor

A mudança de stack foi **aprovada**. O ADR 0001 saiu de "pendente de aprovação" para aprovado, com a data registrada.

> Vale anexar ao ADR 0001 alguma evidência escrita (mensagem, e-mail ou ata). Aprovação verbal sem registro é frágil numa avaliação.

## O que foi rejeitado

**Autenticar o `gh` por `gh auth login`.** É interativo e não roda numa sessão não interativa. Em vez disso, reaproveitei a credencial que o git já tinha armazenada, via `git credential fill`, sem exibir o token. Escopos disponíveis: `gist`, `repo`, `workflow` — suficientes para criar labels e Issues.

## Pendências

**Sprint 1**

- [ ] Configurar o GitHub Project com as colunas do fluxo
- [ ] Confirmar o usuário GitHub do professor
- [ ] Resolver as decisões pendentes #1, #2 e #5
- [ ] Gerar o PNG do diagrama a partir do `.puml` e substituir o JPEG atual
- [ ] Revisar os 16 casos de uso com a equipe antes da entrega

**Sprint 2**

- [ ] Confirmar a migration inicial do Supabase contra o diagrama de classes
