# Sessão — Migração para Trello, Mermaid e portão do diagrama

- **Data:** 07/11/2026
- **Integrante:** Kassio de Sousa Dias
- **Ferramenta:** Claude Code
- **Relacionado a:** Sprint 1 · recomendações do professor · ADRs 0008, 0009 e 0010

---

## Objetivo

Aplicar quatro recomendações do professor: trocar o GitHub Projects pelo Trello, trocar PlantUML por Mermaid, tornar explícita a regra de que o diagrama vem antes da implementação, e criar uma rotina de início de sessão que identifique o integrante antes de agir. Também tirar a etiqueta de sprint das Issues.

## O que foi feito

| Mudança | Onde |
|---|---|
| Quadro [ToolRent — Backlog](https://trello.com/b/m3gakhP8/toolrent-backlog) criado: 6 listas, 16 cartões, etiquetas | Trello |
| GitHub Project #2 apagado | GitHub |
| Labels `sprint-3`…`sprint-7` removidas das 16 Issues e apagadas do repositório | GitHub |
| Diagrama de casos de uso convertido de PlantUML para Mermaid | `docs/diagramas/casos-de-uso.md` |
| `casos-de-uso.puml` removido | `docs/diagramas/` |
| Guia de diagramas reescrito para Mermaid | `docs/diagramas/README.md` |
| Regra de início de sessão (perguntar quem está, checar o cartão, seguir a lista) | `CLAUDE.md` § 0 |
| Regra 3 reescrita: o diagrama vem **antes** do código, com a tabela de qual diagrama cada tipo de história exige | `CLAUDE.md` |
| Seção 7 "Fluxo de trabalho", com o quadro e o fluxo em Mermaid | `README.md` |
| ADRs 0008 (Mermaid), 0009 (Trello), 0010 (diagrama antes do código) | `docs/decisoes/` |
| DoR: a DoR cumprida libera o **diagrama**, não a implementação | `definition-of-ready.md` |
| DoD: bloco "Quadro" — mover o cartão e fechar a Issue, que não sincronizam | `definition-of-done.md` |
| Referências a `.puml` e ao Project corrigidas | `CONTRIBUTING.md`, `rastreabilidade.md`, `sprints.md`, `docs/README.md`, template de Issue, `prompts.md` |
| Tabela do que está superado no guia mestre | `arquivos-de-ia/README.md` |

### O quadro

Seis listas, e a terceira é o ponto da sessão:

```
📥 Backlog → 🎯 Pronto para a sprint → 📐 Diagrama → 🔨 Em andamento → 🔍 Em revisão → ✅ Concluído
```

A lista **📐 Diagrama** transforma a regra do professor em algo que o cartão tem de atravessar. Antes, "diagrama primeiro" estava escrito em quatro documentos e não impedia nada.

Cada um dos 16 cartões traz na descrição: enunciado da história, link da Issue, caso de uso, regras de negócio, sprint, pontos, prioridade, branch prevista, diagramas exigidos e — nos seis bloqueados — qual decisão pendente os trava.

### Por que a etiqueta de sprint saiu

Sprint estava em três lugares: label da Issue, campo do Project e `docs/backlog.md`. Três cópias da mesma informação que muda a cada replanejamento garantem que duas fiquem erradas. Agora vive no quadro e no backlog. As labels que descrevem a **natureza** do item não mudam de valor e ficaram: `historia`, `tarefa-tecnica`, `defeito`, `P0`, `P1`, `decisao-pendente`.

## Defeito encontrado — a linha de resumo do backlog estava errada

Ao montar os cartões a partir de `docs/backlog.md`, dois números da linha de resumo não fecharam
com a própria tabela acima dela:

| Afirmava | Real | Confirmado por |
|---|---|---|
| 88 pontos | **92** | soma da coluna Pontos; e os totais por sprint em `sprints.md` (10+19+21+26+16 = 92) |
| 10 em P0, 6 em P1 | **12 em P0, 4 em P1** | a tabela lista US01–US12 como P0; as labels do GitHub também são 12 e 4 |

Corrigido. Os totais por sprint de `sprints.md` já estavam certos — o erro era só na linha de
resumo do backlog, e passou despercebido nas duas sessões anteriores.

## Decisões tomadas

- **PR #19 mergeado antes de abrir esta branch.** Ele registrava o Project que esta sessão apagou, mas é registro datado de 27/10 e vale como histórico — a correção veio aqui, não por reescrita
- **A imagem `casos-de-uso.jpg` foi mantida**, marcada como entrega arquivada da Sprint 1 e não mais mantida. A fonte vigente é o `.md`
- **`casos-de-uso.puml` removido em vez de mantido em paralelo.** Duas fontes do mesmo diagrama é exatamente o defeito que o Mermaid resolve; o arquivo continua no histórico do Git
- **Project #1 (`untitled project`) não foi tocado** — não é do ToolRent

## O que foi rejeitado

- **Decidir as pendências #1 a #8.** Seguem abertas; os ADRs 0005–0007 continuam em 🟡 Proposta. `CLAUDE.md` regra 2
- **Marcar os ADRs 0008–0010 como Proposta.** Diferente das pendências da equipe, estas vêm de recomendação do professor — registradas como ✅ Aceita, com a origem nomeada
- **Apagar `casos-de-uso.jpg`.** Não foi pedido, e é a imagem que acompanhou a entrega da Sprint 1
- **Criar o quadro a partir do link enviado.** O link era o template público do Kanban da Trello, não uma cópia da equipe — ver abaixo

## Verificação

```
cd api && ./mvnw --batch-mode clean verify   ->  BUILD SUCCESS (exit 0)
cd app && flutter analyze                    ->  No issues found!
cd app && flutter test                       ->  All tests passed! (3 testes)
YAML dos templates de Issue e do ci.yml      ->  5 arquivos válidos
Blocos Mermaid (nós, arestas e classDef)     ->  4 blocos consistentes, cercas balanceadas
Varredura por .puml / projects/2 restantes   ->  só ocorrências intencionais (notas de "superado")
Issues: labels após a limpeza                ->  historia + P0/P1 + decisao-pendente, sem sprint
Totais do backlog x sprints.md x labels      ->  divergência encontrada e corrigida (ver acima)
CI do PR #20                                 ->  3 jobs verdes
```

**Não verificado:** a renderização do bloco Mermaid na página do GitHub. A extensão do Chrome não
estava conectada nesta sessão, então a conferência visual exigida pelo checklist de
`docs/diagramas/README.md` ficou pendente — a verificação feita foi apenas estática (nós, arestas
e `classDef` consistentes, cercas balanceadas).

Nenhum arquivo de código foi alterado nesta sessão — só documentação, template de Issue e configuração de ferramentas.

## Erros e tropeços desta sessão

Registrados porque o `guia-mestre.md` pede honestidade sobre o uso de IA:

1. **O link do quadro não abria.** `trello.com/b/5FldI0TK/kanban-quadro-modelo` não estava na conta `kassiodias` — era o template público da Trello, não um quadro da equipe. Em vez de supor, a sessão parou e perguntou; o quadro foi então criado do zero
2. **O primeiro verificador de Mermaid deu falso positivo.** A expressão regular só reconhecia nós declarados em início de linha, e acusou `B`–`F` do diagrama de fluxo do README como inexistentes — eles são declarados no meio da aresta. O verificador foi corrigido, não o diagrama
3. **Uma seção do log foi duplicada.** Um script de edição releu o arquivo depois de já ter
   gravado a primeira alteração e aplicou a mesma substituição duas vezes. Detectado na
   conferência com `grep` e removido antes do push
4. **As etiquetas do Trello ficaram sem nome.** O MCP disponível cria cartões e anexa etiquetas, mas não cria nem renomeia definições de etiqueta. Foram usadas as seis padrão, com as cores espelhando as labels do GitHub; nomear ficou como pendência manual

## Pendências

- [ ] **Nomear as 6 etiquetas do quadro** no Trello: 🔴 vermelho = P0 · 🟡 amarelo = P1 · 🟣 roxo = bloqueada por decisão pendente · 🟢 verde = história · 🔵 azul = tarefa técnica · 🟠 laranja = defeito
- [ ] **Convidar Lincoln, Victor e Nailan** como membros do quadro
- [ ] **Decidir a visibilidade do quadro e dar acesso ao professor** — está restrito à área de trabalho, e o README linka publicamente
- [ ] Criar o projeto Supabase — segue inexistente; a migration nunca foi aplicada
- [ ] Aprovar ou rejeitar os ADRs 0005, 0006 e 0007 (decisões #1, #2 e #5)
- [ ] Decisões #3, #4, #6, #7 e #8 seguem abertas
- [ ] Revisar os 16 casos de uso com a equipe e fechar o documento da Sprint 1
- [ ] Sprint 2: diagramas de classes e de estados — **nascem em Mermaid e vêm antes do código**
