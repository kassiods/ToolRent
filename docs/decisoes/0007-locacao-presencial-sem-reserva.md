# ADR 0007 — Locação presencial direta, sem reserva prévia

- **Data:** 27/10/2026
- **Status:** 🟡 Proposta
- **Decidido por:** — *aguardando aprovação da equipe*

> ⚠️ **Esta decisão ainda não vale.** Corresponde à decisão pendente **#5** de
> [`casos-de-uso.md § 6`](../casos-de-uso.md#6-decisões-pendentes). Enquanto o status for
> 🟡 Proposta, não implemente como aprovada — ver `CLAUDE.md`, regra 2.

---

## Contexto

UC09 formaliza a locação. Falta confirmar se **toda** locação precisa nascer de uma reserva aprovada, ou se o Atendente pode criar uma locação direto no balcão.

A escolha determina se `locacao.reserva_id` é obrigatório ou opcional — e isso entra no diagrama de classes e na migration da Sprint 2.

### O que a especificação já diz

A própria especificação de UC09 em [`casos-de-uso.md`](../casos-de-uso.md) já descreve o atendimento direto:

> **Gatilho:** Uma reserva aprovada avança **ou um cliente chega sem reserva prévia**.
>
> 1. O atendente seleciona uma reserva aprovada **ou inicia um atendimento direto**.

A pendência #5, portanto, não é uma questão aberta de verdade: é a **confirmação** de um comportamento que o caso de uso já especifica. Este ADR existe para fechar a lacuna formal.

## Decisão proposta

**A locação presencial direta é permitida.** UC09 aceita dois caminhos de entrada:

1. A partir de uma reserva `APROVADA` — a locação referencia a reserva
2. Atendimento direto — a locação nasce sem reserva

`locacao.reserva_id` é **opcional** (aceita nulo).

No caminho 2, a disponibilidade é verificada no ato da formalização, com as mesmas regras do caminho 1: RN01 (período válido), RN02 (sobreposição) e RN03 (situação da ferramenta). O atendimento direto **não** é atalho que dispensa validação — é apenas outra porta de entrada para a mesma regra.

## Alternativas consideradas

### A — Permitir locação direta ← **proposta**

- ✅ É o que UC09 já especifica, em dois pontos do fluxo
- ✅ Corresponde ao negócio: locadora de ferramentas atende balcão
- ✅ Não exige alterar a especificação já entregue
- ❌ `reserva_id` nulo é um caso a mais nas consultas e nos testes
- ❌ Locação sem reserva de origem não tem rastro de quem pediu e quando

### B — Exigir reserva sempre

- ✅ Toda locação tem origem registrada; histórico uniforme
- ✅ `reserva_id` obrigatório simplifica consultas e relatórios
- ❌ **Contradiz o fluxo de UC09 já especificado** — exigiria reescrever o caso de uso
- ❌ No balcão, obriga o Atendente a criar uma reserva e aprová-la em seguida: dois passos de teatro para registrar um atendimento presencial
- ❌ Produz reservas que nunca foram pedidos de cliente, poluindo os indicadores de UC15

## Consequências

**Positivas**

- UC09, o diagrama de classes e a migration passam a dizer a mesma coisa
- O fluxo de balcão é um passo, não dois

**Negativas**

- Consultas e relatórios precisam tratar `reserva_id` nulo
- Relatórios de UC15 que cruzam reserva e locação não cobrem as locações diretas

**Mitigação**

- Teste obrigatório nos dois caminhos de UC09: com reserva e sem reserva
- UC15 deve declarar explicitamente, em cada relatório, se considera locações sem reserva — do contrário o número engana quem lê

## Relação com a decisão #2

Se [ADR 0006](0006-reserva-bloqueia-apenas-apos-aprovacao.md) for aprovado, a locação direta e a aprovação de reserva passam a disputar o mesmo estoque pelo mesmo critério. A revalidação de disponibilidade no momento da formalização **não é opcional** — é o que impede o balcão de alugar uma ferramenta que acabou de ser prometida a uma reserva aprovada.
