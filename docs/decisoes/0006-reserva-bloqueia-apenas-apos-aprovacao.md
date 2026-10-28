# ADR 0006 — Reserva bloqueia disponibilidade apenas após aprovação

- **Data:** 27/10/2026
- **Status:** 🟡 Proposta
- **Decidido por:** — *aguardando aprovação da equipe*

> ⚠️ **Esta decisão ainda não vale.** Corresponde à decisão pendente **#2** de
> [`casos-de-uso.md § 6`](../casos-de-uso.md#6-decisões-pendentes) e ao parâmetro pendente de
> [RN02](../regras-de-negocio.md). Enquanto o status for 🟡 Proposta, não implemente como
> aprovada — ver `CLAUDE.md`, regra 2.

---

## Contexto

Uma reserva nasce no estado `SOLICITADA` e pode virar `APROVADA`, `RECUSADA` ou `CANCELADA` (UC07, UC08). A pergunta: **a partir de qual estado a reserva tira a ferramenta do estoque disponível?**

Isto define a cláusula de sobreposição da consulta de disponibilidade (UC05) — a primeira história com lógica real do projeto, estimada em **8 pontos** — e os índices da tabela `reserva`.

RN02 já fixa a aritmética da sobreposição: dois períodos conflitam quando `inicioA < fimB` **e** `inicioB < fimA`. O que está em aberto é **quais registros entram nessa comparação**.

### O que já está registrado

[`regras-de-negocio.md`](../regras-de-negocio.md) traz a recomendação em dois lugares — no corpo de RN02 e na tabela "Resumo das pendências":

> **Recomendação da equipe técnica:** bloquear somente a partir de `APROVADA`. Bloquear já na solicitação permite que qualquer cliente trave o catálogo sem compromisso.

Este ADR **formaliza o que o documento já recomenda**; não introduz critério novo.

## Decisão proposta

**Somente reservas `APROVADA` bloqueiam disponibilidade.**

A consulta de disponibilidade (UC05) considera conflitante:

- `locacao` em `APROVADA` ou `EM_ANDAMENTO`
- `reserva` em `APROVADA`
- `ferramenta` cuja situação não seja `DISPONIVEL` (RN03)

Uma reserva `SOLICITADA` **não** reserva nada. Ela é um pedido, e o pedido pode ser recusado.

**Consequência operacional que precisa estar na interface:** duas pessoas podem solicitar o mesmo período. O Atendente aprova uma e recusa a outra (UC08). A tela de análise de reservas deve mostrar que existe pedido concorrente para o mesmo período e a mesma ferramenta — sem isso, o Atendente aprova as duas e cria o conflito que o sistema existe para evitar.

## Alternativas consideradas

### A — `SOLICITADA` já bloqueia

- ✅ Impossível aprovar duas reservas conflitantes por descuido
- ✅ O cliente que pede primeiro tem garantia
- ❌ **Qualquer cliente trava o catálogo sem compromisso algum**
- ❌ Exige prazo de expiração da solicitação, e portanto um mecanismo que expira pedidos — infraestrutura que [ADR 0003](0003-estado-atrasada-derivado.md) justamente evitou para `ATRASADA`
- ❌ Com expiração, surge um estado `EXPIRADA` que não existe no enum atual

### B — Somente `APROVADA` bloqueia ← **proposta**

- ✅ É a recomendação já registrada em RN02
- ✅ Nenhum mecanismo de expiração, nenhum estado novo, nenhum processo agendado
- ✅ O Atendente mantém o controle do estoque, o que corresponde ao negócio
- ❌ Dois pedidos concorrentes podem existir, e a interface precisa evidenciar isso
- ❌ O cliente pode ter o pedido recusado por outro ter sido aprovado antes

## Consequências

**Positivas**

- O enum `estado_reserva` da migration inicial permanece como está: `SOLICITADA`, `APROVADA`, `RECUSADA`, `CANCELADA`
- Sem processo agendado — coerente com a linha adotada em ADR 0003
- A consulta de UC05 filtra por igualdade de estado, sem lógica temporal adicional

**Negativas**

- UC08 ganha um requisito: sinalizar pedidos concorrentes ao Atendente
- A aprovação precisa **revalidar** a disponibilidade no momento do aprovar, não apenas no momento do solicitar — entre o pedido e a aprovação o mundo pode ter mudado

**Mitigação**

- A revalidação na aprovação é regra de serviço (RN02 em UC08), não checagem de interface
- Teste obrigatório: aprovar a segunda de duas reservas concorrentes deve ser **recusado** pela API

## Nota para o diagrama de estados da `Reserva`

O diagrama deve anotar: *"o bloqueio de disponibilidade começa na transição `SOLICITADA → APROVADA`"*. Sem essa anotação, o diagrama não distingue as duas opções e volta a ser ambíguo.
