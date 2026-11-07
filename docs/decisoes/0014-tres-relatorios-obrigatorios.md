# ADR 0014 — Três relatórios obrigatórios na primeira versão

- **Data:** 07/11/2026
- **Status:** ✅ Aceita
- **Decidido por:** Kassio de Sousa Dias, em nome da equipe

> ✅ Resolve a decisão pendente **#7**. A equipe delegou a escolha.

---

## Contexto

UC15 (consultar relatórios) e US15 estavam sem escopo: "relatórios de locações e receitas" não diz
quantos nem quais. Relatório é o tipo de requisito que cresce sem limite se ninguém fechar a lista
— e US15 já é a segunda maior história do backlog, com 8 pontos.

## Decisão

**Três relatórios. Nem mais, nem menos, na primeira versão.**

| # | Relatório | Pergunta que responde | Filtros |
|---|---|---|---|
| 1 | **Locações por período** | O que saiu e voltou entre duas datas? | Data inicial e final · estado da locação |
| 2 | **Receita por período** | Quanto entrou, e de onde? | Data inicial e final · separado em valor-base, multa e dano |
| 3 | **Ferramentas mais locadas** | O que o cliente procura, e o que está parado? | Data inicial e final · ordenado por número de locações |

Regras comuns aos três:

- Período obrigatório, com data inicial anterior à final (RN01)
- Só `ADMINISTRADOR` acessa, validado no serviço, não escondendo o menu (RN09)
- Toda quantia em `BigDecimal`, formatada só na apresentação (RN12)
- Leitura pura: relatório não altera nada

**Fora do escopo, de propósito:** exportação para PDF ou Excel, gráficos, agendamento por e-mail,
relatório por cliente (isso é UC16) e relatório de manutenção.

## Alternativas consideradas

### A — Deixar em aberto, "os relatórios que a equipe achar necessários"
- ❌ É o estado atual e foi justamente o que travou US15
- ❌ Escopo que não fecha não entra em sprint: fere a Definition of Ready

### B — Um relatório só, o de receita
- ✅ Menor esforço
- ❌ O ator Administrador ficaria com um caso de uso quase vazio
- ❌ Não responde "o que está parado no estoque", que é metade do valor do sistema para a locadora

### C — Três relatórios ← **escolhida**
- ✅ Cobre as três perguntas reais da operação: movimento, dinheiro e demanda
- ✅ Os três saem das mesmas tabelas, com a mesma estrutura de filtro por período
- ✅ Cabe nos 8 pontos já estimados
- ❌ Fecha a porta para pedidos novos de relatório durante a sprint — o que é o objetivo

## Consequências

**Positivas**
- US15 deixa de estar bloqueada e passa a ter critério de aceite verificável
- UC15 ganha fluxo definido: escolher relatório, informar período, ver resultado

**Negativas**
- Pedido de relatório novo durante a Sprint 7 vira **escopo novo**, com ADR próprio — não entra de carona

**Impacto**
- `docs/casos-de-uso.md`: UC15 passa a listar os três relatórios
- US15 perde a etiqueta `decisao-pendente`
