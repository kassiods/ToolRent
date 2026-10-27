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
| `mvn clean test` | ⚠️ **Não executado** — Maven não está instalado no ambiente |

> **A API não foi compilada nem testada.** O `pom.xml` e o teste foram escritos, mas sem Maven no ambiente não houve como verificar. Primeiro integrante com Maven instalado deve rodar `cd api && mvn clean test` e reportar.

## Pendências

**Crítica**

- [ ] Apresentar a mudança de stack ao professor e registrar a resposta no ADR 0001

**Ambiente**

- [ ] Instalar Maven 3.9+ e rodar `mvn clean test` na API
- [ ] Instalar JDK 21 — o ambiente atual tem JDK 24, e a disciplina exige 21

**Sprint 1**

- [ ] Criar as 16 Issues a partir de `docs/backlog.md`
- [ ] Configurar o GitHub Project
- [ ] Adicionar integrantes e professor como colaboradores
- [ ] Confirmar o usuário GitHub do professor
- [ ] Resolver as decisões pendentes #1, #2 e #5
- [ ] Gerar o PNG do diagrama a partir do `.puml` e substituir o JPEG atual
