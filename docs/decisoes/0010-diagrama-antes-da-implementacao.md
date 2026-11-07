# ADR 0010 — Diagrama aprovado antes da implementação

- **Data:** 07/11/2026
- **Status:** ✅ Aceita
- **Decidido por:** equipe, a partir de recomendação do professor

---

## Contexto

O enunciado da disciplina é explícito em dois pontos:

> A cada semana, entregar diagrama UML e código que implementa exatamente o que o diagrama especifica.

> Cada commit de entrega deve ser rastreável até um elemento de diagrama.

E a regra de ouro do guia mestre: *"não criar diagramas que não correspondam ao código entregue e não implementar funcionalidades que contradigam os diagramas aprovados."*

Até aqui isso estava escrito em quatro documentos (`CLAUDE.md` regra 3, `docs/diagramas/README.md`, `definition-of-ready.md`, `guia-mestre.md`) **como princípio** — mas nenhum deles dizia *em que momento* o diagrama tem de existir, nem havia qualquer mecanismo que impedisse alguém de abrir uma branch e começar a programar antes.

Com a Sprint 2 (classes de domínio e banco) chegando, o risco é concreto: escrever as entidades Java primeiro e desenhar o diagrama de classes depois, a partir do código. Isso inverte a disciplina — o diagrama passa a documentar o código em vez de especificá-lo — e é visível na avaliação, porque o histórico do Git mostra a ordem.

## Decisão

**Nenhuma história entra em implementação antes de o diagrama correspondente estar aprovado em Pull Request.**

A ordem é obrigatória:

```
Issue com DoR cumprida
        ↓
Diagrama desenhado em docs/diagramas/*.md  →  PR  →  revisão de um colega  →  merge
        ↓
Só então: branch de implementação, código, testes
        ↓
PR de implementação, citando o diagrama que ele realiza
```

### Como a regra é aplicada

| Mecanismo | Onde |
|---|---|
| **Lista 📐 no quadro** | O cartão atravessa "📐 Diagrama" antes de "🔨 Em andamento". Mover direto é a violação visível |
| **Definition of Ready** | Item: o diagrama necessário está identificado |
| **Definition of Done** | Item: o diagrama corresponde ao código entregue |
| **Descrição do cartão** | Cada um dos 16 cartões nomeia os diagramas que precisa |
| **`CLAUDE.md`** | Regra para assistentes de IA: não implementar sem diagrama correspondente |

### O diagrama exigido depende da história

Não é "sempre o diagrama de classes". É o diagrama que especifica aquele comportamento:

| O que a história faz | Diagrama exigido |
|---|---|
| Cria ou altera entidade de domínio | **Classes** |
| Muda o ciclo de vida de Reserva, Locação ou Manutenção | **Estados** |
| Coordena vários objetos num fluxo (ex.: UC09) | **Sequência** |
| Só consulta, sem entidade nem estado novo | Basta o de **casos de uso** já aprovado |

A coluna "Diagramas" da [matriz de rastreabilidade](../agil/rastreabilidade.md) diz qual é para cada história.

### Quando o diagrama já existe

Se o diagrama aprovado já cobre a história — caso de US06 e US16, que são consulta pura — o cartão passa por 📐 apenas para a conferência: alguém confirma que o caso de uso no diagrama corresponde ao que será implementado, registra isso no cartão e segue. **Conferir é rápido; pular é o que não pode.**

## Alternativas consideradas

### A — Manter como princípio escrito
- ✅ Nada a mudar
- ❌ Já se provou insuficiente: estava em quatro documentos e ainda assim ninguém sabia *quando* o diagrama deveria existir
- ❌ Depende de cada integrante lembrar, no momento de maior pressa

### B — Portão explícito no fluxo ← **escolhida**
- ✅ A regra fica visível no lugar onde o trabalho acontece, não só num documento que se lê uma vez
- ✅ O histórico do Git passa a provar a ordem correta: PR de diagrama antes do PR de código
- ✅ Força a conversa de modelagem antes de o código criar fatos
- ❌ Dois PRs por história em vez de um
- ❌ Para histórias triviais, parece burocracia

### C — Diagrama e código no mesmo PR
- ✅ Um PR por história
- ❌ Permite escrever o código primeiro e o diagrama depois, sem que ninguém perceba
- ❌ Revisar modelagem e implementação de uma vez dilui as duas revisões

## Consequências

**Positivas**

- A exigência da disciplina — commit rastreável até elemento de diagrama — passa a ser satisfeita por construção
- Erro de modelagem é pego quando custa um desenho, não quando custa refatoração
- A Sprint 2 produz o diagrama de classes como entrega real, não como documentação retroativa

**Negativas**

- Duas rodadas de revisão por história; o fluxo fica mais lento no começo
- Histórias de consulta pura passam por um portão que acrescenta pouco

**Riscos**

- Alguém desenhar um diagrama vago só para destravar o cartão. Mitigação: o revisor do PR de diagrama cobra os mesmos itens do checklist em [`docs/diagramas/README.md`](../diagramas/README.md) — atores, elementos e relações batendo com o documento.
- O inverso também: travar trabalho esperando diagrama de algo já diagramado. Mitigação: a regra "quando o diagrama já existe", acima.

## Relação com outras decisões

- [ADR 0008](0008-mermaid-no-lugar-do-plantuml.md) — o diagrama é Mermaid, renderizado pelo GitHub, o que torna o PR de diagrama leve de revisar
- [ADR 0009](0009-trello-no-lugar-do-github-projects.md) — a lista 📐 do quadro é onde esta decisão vira prática
