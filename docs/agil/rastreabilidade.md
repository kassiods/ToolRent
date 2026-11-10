# Matriz de Rastreabilidade — ToolRent

> **Exigência da disciplina:** cada commit de entrega deve ser rastreável até um elemento de diagrama.
> Esta matriz é a prova dessa linha. Mantenha-a atualizada — ela é consultada na avaliação.

---

## A cadeia

```
História (US) → Caso de uso (UC) → Regra (RN) → Issue → Branch → Commit → PR → Diagrama
```

Cada elo responde uma pergunta:

| Elo | Pergunta que responde |
|---|---|
| **US** | Quem precisa disso e por quê? |
| **UC** | Como o sistema se comporta? |
| **RN** | O que o sistema impede? |
| **Issue** | Quem está fazendo e em qual sprint? |
| **Branch** | Onde o código está sendo escrito? |
| **Commit** | Que mudança específica foi feita? |
| **PR** | Quem revisou e aprovou? |
| **Diagrama** | O código corresponde ao que foi especificado? |

---

## Matriz principal

Preencha as colunas de Issue e PR conforme o trabalho avança.

O acompanhamento diário fica no [quadro do Trello](https://trello.com/b/m3gakhP8/toolrent-backlog),
cujas listas correspondem à legenda da coluna Status desta matriz. **Esta matriz continua sendo a
fonte da entrega** — o quadro é conveniência, não substituto do documento. O GitHub Projects foi
apagado; ver [ADR 0009](../decisoes/0009-trello-no-lugar-do-github-projects.md).

| US | Caso de uso | Regras | Sprint | Issue | Branch | PR | Diagrama | Status |
|---|---|---|---|---|---|---|---|---|
| US01 | UC01 | RN09, RN10 | 3 | [#1](https://github.com/kassiods/ToolRent/issues/1) | `feat/US01-autenticar-usuario` | — | Casos de uso, Classes | ⬜ |
| US02 | UC02 | RN09, RN10, RN11 | 3 | [#2](https://github.com/kassiods/ToolRent/issues/2) | `feat/US02-gerenciar-usuarios` | — | Casos de uso, Classes | ⬜ |
| US03 | UC03 | RN09, RN11 | 4 | [#3](https://github.com/kassiods/ToolRent/issues/3) | `feat/US03-gerenciar-clientes` | — | Casos de uso, Classes | ⬜ |
| US04 | UC04 | RN04, RN09, RN11, RN12 | 4 | [#4](https://github.com/kassiods/ToolRent/issues/4) | `feat/US04-gerenciar-ferramentas` | — | Casos de uso, Classes | ⬜ |
| US05 | UC05 | RN01, RN02, RN03 | 4 | [#5](https://github.com/kassiods/ToolRent/issues/5) | `feat/US05-consultar-disponibilidade` | — | Casos de uso, Sequência | ⬜ |
| US06 | UC06 | RN09 | 4 | [#6](https://github.com/kassiods/ToolRent/issues/6) | `feat/US06-consultar-catalogo` | — | Casos de uso | ⬜ |
| US07 | UC07 | RN01, RN02, RN03, RN08 | 5 | [#7](https://github.com/kassiods/ToolRent/issues/7) | `feat/US07-solicitar-reserva` | — | Casos de uso, Estados | ⬜ |
| US08 | UC08 | RN02, RN03, RN08, RN09 | 5 | [#8](https://github.com/kassiods/ToolRent/issues/8) | `feat/US08-analisar-reserva` | — | Casos de uso, Estados | ⬜ |
| US09 | UC09 | RN01–04, RN08, RN11, RN12 | 5 | [#9](https://github.com/kassiods/ToolRent/issues/9) | `feat/US09-formalizar-locacao` | — | Casos de uso, Estados, Sequência | ⬜ |
| US10 | UC10 | RN03, RN08, RN09 | 5 | [#10](https://github.com/kassiods/ToolRent/issues/10) | `feat/US10-registrar-retirada` | — | Casos de uso, Estados | ⬜ |
| US11 | UC11 | RN04–06, RN08, RN11, RN12 | 6 | [#11](https://github.com/kassiods/ToolRent/issues/11) | `feat/US11-registrar-devolucao` | — | Casos de uso, Estados | ⬜ |
| US12 | UC09, UC11, UC12 | RN04, RN05, RN06, RN12 | 6 | [#12](https://github.com/kassiods/ToolRent/issues/12) | `feat/US12-calcular-valores` | — | Casos de uso, Classes | ⬜ |
| US13 | UC13 | RN07, RN09, RN11, RN12 | 6 | [#13](https://github.com/kassiods/ToolRent/issues/13) | `feat/US13-registrar-pagamento` | — | Casos de uso, Classes | ⬜ |
| US14 | UC14 | RN03, RN08, RN09, RN11 | 7 | [#14](https://github.com/kassiods/ToolRent/issues/14) | `feat/US14-gerenciar-manutencao` | — | Casos de uso, Estados | ⬜ |
| US15 | UC15 | RN01, RN09, RN12 | 7 | [#15](https://github.com/kassiods/ToolRent/issues/15) | `feat/US15-consultar-relatorios` | — | Casos de uso | ⬜ |
| US16 | UC16 | RN09 | 7 | [#16](https://github.com/kassiods/ToolRent/issues/16) | `feat/US16-historico-cliente` | — | Casos de uso | ⬜ |

**Legenda:** ⬜ não iniciado · 🟡 em andamento · 🔍 em revisão · ✅ concluído

---

## Tarefas técnicas

Trabalho que não é história de usuário, mas que as histórias dependem. Mesma cadeia de
rastreabilidade — a diferença é que a origem é um **diagrama**, não uma US.

| TT | O que entrega | Diagrama que especifica | Sprint | Issue | Branch | Responsável | Status |
|---|---|---|---|---|---|---|---|
| TT05 | Diagrama de **pacotes** + casca compartilhada + tela do Administrador | [`pacotes.md`](../diagramas/sprint-2/pacotes.md) | 2 | [#26](https://github.com/kassiods/ToolRent/issues/26) | `feat/TT05-pacotes-e-tela-administrador` | Nailan | ⬜ |
| TT06 | Diagrama de **atividades** + tela do Atendente, 10 itens | [`atividades-tela-inicial.md`](../diagramas/sprint-2/atividades-tela-inicial.md) | 2 | [#27](https://github.com/kassiods/ToolRent/issues/27) | `feat/TT06-atividades-e-tela-atendente` | Lincoln | ⬜ |
| TT07 | Diagrama de **sequência** + tela do Cliente + teste de referência | [`sequencia-abrir-tela-inicial.md`](../diagramas/sprint-2/sequencia-abrir-tela-inicial.md) | 2 | [#28](https://github.com/kassiods/ToolRent/issues/28) | `feat/TT07-sequencia-e-tela-cliente` | [#34](https://github.com/kassiods/ToolRent/pull/34) | Victor | 🔍 |
| TT01 | Classes de cadastros, catálogo e manutenção | [`classes.md`](../diagramas/sprint-2/classes.md) | 3 | [#21](https://github.com/kassiods/ToolRent/issues/21) | `feat/TT01-dominio-cadastros-e-catalogo` | Nailan | ⬜ |
| TT02 | Classes de locação ponta a ponta | [`classes.md`](../diagramas/sprint-2/classes.md) · [`estados-reserva.md`](../diagramas/sprint-2/estados-reserva.md) · [`estados-locacao.md`](../diagramas/sprint-2/estados-locacao.md) | 3 | [#22](https://github.com/kassiods/ToolRent/issues/22) | `feat/TT02-dominio-locacao` | Lincoln | ⬜ |

> **TT03 e TT04 não aparecem porque foram fechadas** na redivisão de 07/11/2026, e os IDs não são
> reaproveitados. TT01 e TT02 continuam existindo: só mudaram de sprint quando a Sprint 2 passou a
> ser entrega de front-end.

Plano completo da Sprint 2, com os critérios de pronto de cada pacote e a ordem de integração:
[`../sprints/sprint-2.md`](../sprints/sprint-2.md).

---

## Quais histórias cada diagrama habilita

| Diagrama | Habilita |
|---|---|
| [`casos-de-uso.md`](../diagramas/sprint-1/casos-de-uso.md) | Todas — é a fronteira do sistema |
| [`pacotes.md`](../diagramas/sprint-2/pacotes.md) | Todas — define onde o código de cada uma mora |
| [`atividades-tela-inicial.md`](../diagramas/sprint-2/atividades-tela-inicial.md) | A porta de entrada de US02–US11 e US13–US16 |
| [`sequencia-abrir-tela-inicial.md`](../diagramas/sprint-2/sequencia-abrir-tela-inicial.md) | O contrato de UI que as telas das próximas sprints reaproveitam |
| [`classes.md`](../diagramas/sprint-2/classes.md) | US01–US04, US06, US12, US13, US14 |
| [`estados-reserva.md`](../diagramas/sprint-2/estados-reserva.md) | US07, US08 |
| [`estados-locacao.md`](../diagramas/sprint-2/estados-locacao.md) | US09, US10, US11 |
| `sprint-4/sequencia-uc05.md` ⬜ | US05 |
| `sprint-5/sequencia-uc09.md` ⬜ | US09 |

**Uma história só entra em implementação depois que o diagrama que a especifica está mergeado**
([ADR 0010](../decisoes/0010-diagrama-antes-da-implementacao.md)).

---

## Cobertura das regras de negócio

Toda regra precisa ser exercitada por pelo menos um caso de uso. Nenhuma regra órfã.

| Regra | Casos de uso que a exercitam | Cobertura |
|---|---|---|
| RN01 — Período válido | UC05, UC07, UC09, UC15 | ✅ |
| RN02 — Disponibilidade | UC05, UC07, UC08, UC09 | ✅ |
| RN03 — Manutenção bloqueia | UC05, UC07, UC08, UC09, UC10, UC14 | ✅ |
| RN04 — Valor-base | UC04, UC09, UC11, UC12 | ✅ |
| RN05 — Atraso | UC11, UC12 | ✅ |
| RN06 — Danos | UC11, UC12 | ✅ |
| RN07 — Pagamento | UC13 | ✅ |
| RN08 — Transições | UC07–UC11, UC14 | ✅ |
| RN09 — Autorização | Todos | ✅ |
| RN10 — Senhas | UC01, UC02 | ✅ |
| RN11 — Integridade | UC02, UC03, UC04, UC09, UC11, UC13, UC14 | ✅ |
| RN12 — Dinheiro e datas | UC04, UC09, UC11, UC12, UC13, UC15 | ✅ |

---

## Cobertura dos atores

| Ator | Casos de uso | Total |
|---|---|---|
| Administrador | UC01, UC02, UC15 | 3 |
| Atendente | UC01, UC03, UC04, UC05, UC08, UC09, UC10, UC11, UC12, UC13, UC14, UC16 | 12 |
| Cliente | UC06, UC07 | 2 |

Nenhum caso de uso está sem ator e nenhum ator está sem caso de uso.

---

## Conformidade com o enunciado

| Exigência | Limite | ToolRent | Status |
|---|---|---|---|
| Atores | 3 a 6 | 3 | ✅ |
| Casos de uso | 12 a 20 | 16 | ✅ |
| CRUD puro | ≤ metade (8) | 3 (UC02, UC03, UC04) | ✅ |
| Classes de domínio | 9 a 13 | 11 previstas | 🟡 confirmar na Sprint 2 |
| Entidade com 4+ estados | ≥ 1 | `Locacao` com 5 | ✅ |
| Regras com cálculo ou validação | ≥ 2 | 6 (RN01, RN02, RN04, RN05, RN08, RN12) | ✅ |
| Autenticação e perfis | obrigatório | Supabase Auth, 3 perfis | ✅ |

---

## Como manter

**Ao criar uma Issue:** preencha a coluna Issue com `#N`.

**Ao abrir um PR:** preencha a coluna PR e escreva no corpo:

```markdown
Closes #12

**Rastreabilidade**
- História: US07
- Caso de uso: UC07 (inclui UC05)
- Regras: RN01, RN02, RN03, RN08
- Diagramas afetados: docs/diagramas/sprint-1/casos-de-uso.md, docs/diagramas/sprint-2/estados-reserva.md
```

**Ao concluir:** mude o status para ✅.

**Se um diagrama mudar sem que a matriz mude**, a matriz está mentindo. Atualize no mesmo PR.
