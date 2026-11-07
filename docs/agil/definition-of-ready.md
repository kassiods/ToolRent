# Definition of Ready — ToolRent

> Quando uma história pode entrar numa sprint.
> O objetivo é simples: **ninguém deve começar a programar e descobrir no meio que falta uma decisão.**

---

## A checklist

Uma Issue só entra na sprint se todos os itens estiverem satisfeitos.

```markdown
### Definition of Ready

- [ ] Tem ID estável no título (`[US07] Solicitar reserva`)
- [ ] Está escrita no formato "Como… quero… para…"
- [ ] Tem descrição e contexto suficientes para alguém que não participou da conversa
- [ ] Tem critérios de aceite objetivos e verificáveis
- [ ] Referencia o(s) caso(s) de uso relacionados
- [ ] Referencia as regras de negócio envolvidas
- [ ] Indica **qual diagrama especifica** o comportamento (classes, estados ou sequência)
- [ ] Está estimada em pontos
- [ ] Não depende de decisão pendente ainda em aberto
- [ ] Não depende de outra história não concluída (ou a dependência está explícita)
- [ ] Cabe numa sprint — se passa de 13 pontos, foi quebrada
```

---

## Critério de aceite verificável

Um critério de aceite precisa ter resposta **sim ou não**, sem interpretação.

| ❌ Vago | ✅ Verificável |
|---|---|
| "A busca deve ser rápida" | "A busca retorna em menos de 2 segundos com 500 ferramentas cadastradas" |
| "Validar os dados" | "Data inicial igual ou posterior à final é rejeitada com mensagem indicando o campo" |
| "A tela deve ser intuitiva" | "O resumo exibe período, quantidade e valor estimado antes do botão de confirmar" |
| "Tratar erros" | "Falha de rede exibe 'Não foi possível conectar' e mantém os dados preenchidos" |
| "Calcular certo" | "Locação de 3 diárias com tarifa 50,00 e quantidade 2 resulta em 300,00" |

Se você não consegue escrever o teste a partir do critério, o critério não está pronto.

---

## O diagrama não é item de checklist: é a etapa seguinte

A DoR cumprida **não** libera a implementação. Libera o **desenho do diagrama**.

O cartão sai de 🎯 Pronto para a sprint para 📐 Diagrama, e só depois de o PR do diagrama ser
mergeado vai para 🔨 Em andamento. Ver [ADR 0010](../decisoes/0010-diagrama-antes-da-implementacao.md).

Por isso o item da checklist pede para **identificar** o diagrama, não para tê-lo pronto: quem
identifica na DoR é quem vai desenhar em seguida.

---

## Decisões pendentes bloqueiam — e hoje não há nenhuma

Uma história que depende de decisão em aberto **não está pronta**. Era o caso de US05, US11, US12
e US15 até 07/11/2026.

**As sete decisões que bloqueavam implementação foram tomadas** — ver
[`../casos-de-uso.md § 6`](../casos-de-uso.md#6-decisões--todas-resolvidas-exceto-uma). Nenhuma
história do backlog está bloqueada por decisão hoje.

A única pendência que resta é a **#8** (modelo de especificação textual do professor), que afeta o
formato do documento de casos de uso, não o comportamento do sistema — e portanto não bloqueia
nada.

Se uma decisão nova aparecer no meio de uma sprint, a regra volta a valer: a história sai da
sprint ou a parte independente é separada **por escrito** na Issue.

---

## Tamanho

| Pontos | Interpretação |
|---|---|
| 1–3 | Pequena. Um integrante resolve em uma sessão. |
| 5 | Média. O tamanho ideal. |
| 8 | Grande, mas cabe numa sprint. |
| 13 | No limite. **Tente quebrar.** |
| 20+ | Não é história, é épico. Quebre obrigatoriamente. |

A US12 (calcular valores) está em 13 pontos — a maior do backlog. Quebre-a antes da Sprint 6, por exemplo: cálculo do valor-base · cálculo de atraso · composição de danos · memória de cálculo.

---

## Como quebrar uma história grande

Quebre por **fatia de comportamento**, não por camada técnica.

| ❌ Por camada | ✅ Por comportamento |
|---|---|
| "Criar a tabela de locação" | "Calcular valor-base de uma locação de 1 item" |
| "Criar o repository" | "Calcular valor-base com múltiplos itens" |
| "Criar o service" | "Aplicar multa quando há atraso" |
| "Criar a tela" | "Exibir memória de cálculo detalhada" |

Quebrar por camada produz pedaços que não entregam nada sozinhos — não dá para demonstrar "o repository" ao professor. Cada fatia por comportamento é demonstrável e testável.
