# ADR 0013 — Devolução sempre integral

- **Data:** 07/11/2026
- **Status:** ✅ Aceita
- **Decidido por:** Kassio de Sousa Dias, em nome da equipe

> ✅ Resolve a decisão pendente **#6**. A equipe delegou a escolha.

---

## Contexto

Uma locação pode ter vários `ItemLocacao`. A pergunta aberta: o cliente pode devolver **parte**
dos itens e ficar com o resto?

O modelo já respondia implicitamente — `Locacao "1" --> "0..1" Devolucao`, uma devolução por
locação — mas isso nunca tinha sido decidido, apenas desenhado.

## Decisão

**A devolução é do contrato inteiro.** Não existe devolução parcial na primeira versão.

- Uma `Locacao` tem **no máximo uma** `Devolucao`
- Registrar a devolução encerra a locação inteira: `EM_ANDAMENTO → DEVOLVIDA`
- A condição e os eventuais danos são registrados **por item**, dentro da mesma devolução — o que se avalia item a item é o estado, não o momento de voltar
- Se o cliente quer ficar com parte das ferramentas por mais tempo, o caminho é **devolver tudo e abrir uma nova locação** para o que continua

## Alternativas consideradas

### A — Permitir devolução parcial
- ✅ Mais flexível para o cliente
- ❌ Exige estado por item, não por locação: `ItemLocacao` ganharia seu próprio ciclo de vida
- ❌ O cálculo de atraso passaria a ser por item, com prazos diferentes no mesmo contrato
- ❌ `Devolucao` viraria 1..*, e "a locação está devolvida?" deixaria de ter resposta simples
- ❌ Empurra o projeto para além das 13 classes de domínio permitidas

### B — Devolução integral ← **escolhida**
- ✅ Um contrato, um encerramento, uma data — o cálculo de atraso é um só
- ✅ Mantém `Devolucao` com multiplicidade 0..1 e o diagrama de estados com 4 estados
- ✅ Cabe no escopo acadêmico sem inventar complexidade
- ❌ O cliente que quer devolver metade precisa de dois contratos

## Consequências

**Positivas**
- UC11 tem um fluxo só; o cálculo de atraso (RN05) usa uma única data de devolução
- `ItemLocacao` continua sem estado próprio — permanece um item de contrato, não uma entidade com ciclo de vida
- O diagrama de classes e o de estados já aprovados **não mudam**: a decisão confirma o que eles desenham

**Negativas**
- Devolver parte exige encerrar e reabrir. Operacionalmente é mais trabalho no balcão

**Limite explícito**
- Se a equipe quiser devolução parcial depois, isso é um **novo ADR** e muda o modelo: estado em `ItemLocacao` e `Devolucao` passando a 1..*. Não é um ajuste pequeno, e por isso fica registrado como fora do escopo desta versão
