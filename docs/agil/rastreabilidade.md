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

O acompanhamento diário fica no [GitHub Project](https://github.com/users/kassiods/projects/2),
cujo campo `Status` usa a mesma legenda da coluna Status desta matriz. **Esta matriz continua
sendo a fonte da entrega** — o Project é conveniência de quadro, não substituto do documento.

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
- Diagramas afetados: casos-de-uso.puml, estados-reserva.puml
```

**Ao concluir:** mude o status para ✅.

**Se um diagrama mudar sem que a matriz mude**, a matriz está mentindo. Atualize no mesmo PR.
