# ADR 0009 — Trello no lugar do GitHub Projects

- **Data:** 07/11/2026
- **Status:** ✅ Aceita
- **Decidido por:** equipe, a partir de recomendação do professor

---

## Contexto

O Product Backlog está em **GitHub Issues** (`#1` a `#16`, uma Issue por história US01–US16). O acompanhamento de quadro estava em um **GitHub Project** criado na Sprint 1, com os campos `Sprint`, `Pontos`, `Prioridade`, `Caso de uso` e `Decisão pendente`.

Duas limitações apareceram na prática:

1. **A API do GitHub não cria views de quadro.** O Project foi criado por API com todos os campos preenchidos, mas a visualização em colunas teve de ser montada à mão na interface — e ficou pendente.
2. **Dar acesso de escrita aos quatro integrantes** exigia configuração separada do repositório.

O enunciado da disciplina pede "Issues **ou** GitHub Projects como Product Backlog" — ou seja, o Backlog em Issues já cumpre a exigência; o quadro é acompanhamento, não a entrega.

## Decisão

**O quadro de acompanhamento passa a ser o Trello. As Issues do GitHub continuam.**

Quadro: **[ToolRent — Backlog](https://trello.com/b/m3gakhP8/toolrent-backlog)**

| Ferramenta | Papel | Continua? |
|---|---|---|
| **GitHub Issues** `#1`–`#16` | Backlog formal, rastreabilidade de commit/PR, critérios de aceite | ✅ Sim |
| **Trello** | Quadro Kanban: onde cada história está no fluxo, quem está nela | ✅ Novo |
| **GitHub Projects** | — | ❌ Apagado |
| **`docs/backlog.md`** | **Fonte da verdade do escopo**: história, prioridade, pontos, UC, sprint | ✅ Sim |

Cada cartão do Trello traz na descrição o link da Issue, o caso de uso, as regras de negócio, a sprint, os pontos, a branch prevista, os diagramas obrigatórios e o aviso de decisão pendente, quando houver.

### A etiqueta de Sprint saiu das Issues

As labels `sprint-3` a `sprint-7` foram **removidas das Issues e apagadas do repositório**. A sprint de cada história agora vive no Trello e em `docs/backlog.md`.

Motivo: sprint é informação que muda de lugar conforme o planejamento; manter a mesma informação em três lugares (label, Project, documento) garante que dois deles fiquem errados. As labels que descrevem a **natureza** da Issue, essas não mudam, e continuam: `historia`, `tarefa-tecnica`, `defeito`, `P0`, `P1`, `decisao-pendente`.

### As listas do quadro

| Lista | O que significa |
|---|---|
| 📥 Backlog | Existe, não está pronta para a sprint |
| 🎯 Pronto para a sprint | [Definition of Ready](../agil/definition-of-ready.md) cumprida |
| 📐 **Diagrama** | **Portão obrigatório** — ver [ADR 0010](0010-diagrama-antes-da-implementacao.md) |
| 🔨 Em andamento | Código sendo escrito, com diagrama já aprovado |
| 🔍 Em revisão | Pull Request aberto |
| ✅ Concluído | [Definition of Done](../agil/definition-of-done.md) cumprida |

## Alternativas consideradas

### A — Manter o GitHub Projects
- ✅ Vive junto das Issues, sincroniza automaticamente
- ✅ Campos tipados (número, data, seleção)
- ❌ View de quadro não é criável por API
- ❌ Acesso de escrita para a equipe exige configuração extra
- ❌ Interface menos familiar para a equipe

### B — Trello ← **escolhida**
- ✅ Recomendação do professor
- ✅ Quadro Kanban sem configuração: listas e cartões existem desde o primeiro minuto
- ✅ Equipe já conhece a ferramenta
- ✅ Permite codificar o portão do diagrama como uma **lista** — o fluxo fica visível, não só escrito
- ❌ Não sincroniza com as Issues automaticamente: mover cartão e fechar Issue são dois atos
- ❌ A API do Trello não cria nem renomeia etiquetas pelo MCP disponível — foram usadas as seis etiquetas padrão, por cor

### C — Os dois
- ❌ Dois quadros para o mesmo trabalho; um fica abandonado e mente

## Consequências

**Positivas**

- O portão do diagrama deixou de ser uma regra escrita num documento e passou a ser uma coluna que o cartão tem de atravessar
- A equipe acompanha o trabalho em uma ferramenta que já usa
- Uma informação, um lugar: sprint sai das labels

**Negativas**

- **Trello e Issues não se sincronizam.** Quem move o cartão para ✅ fecha a Issue no mesmo momento. Está no [Definition of Done](../agil/definition-of-done.md)
- O acompanhamento passa a depender de uma ferramenta fora do GitHub, onde o professor precisa de acesso concedido à parte

**Riscos**

- O quadro virar ficção: cartões paradas em 🔨 enquanto o PR já foi mergeado. Mitigação: a sincronização diária é checada na cerimônia de sincronização, 2× por semana, e `docs/agil/rastreabilidade.md` continua sendo a prova da entrega — não o quadro.

## Pendências desta decisão

- [ ] Nomear as seis etiquetas do quadro na interface do Trello (as cores já estão aplicadas; ver [`docs/agil/rastreabilidade.md`](../agil/rastreabilidade.md))
- [ ] Convidar Lincoln, Victor e Nailan como membros do quadro
- [ ] Decidir a visibilidade do quadro e dar acesso ao professor — hoje está restrito à área de trabalho
