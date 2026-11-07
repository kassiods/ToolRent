# Decisões de Arquitetura (ADRs)

Um **ADR** (*Architecture Decision Record*) registra uma decisão relevante: o contexto, as alternativas consideradas, o que foi escolhido e as consequências.

**Por que registrar:** daqui a dois meses alguém vai perguntar "por que foi feito assim?". Sem registro, a resposta é um encolher de ombros — e a decisão acaba sendo refeita ou revertida por engano.

---

## Decisões registradas

| # | Decisão | Data | Status |
|---|---|---|---|
| [0001](0001-stack-flutter-java-supabase.md) | Stack: Flutter + Java/Spring Boot + Supabase | 27/10/2026 | ✅ Aceita · aprovada pelo professor |
| [0002](0002-remocao-do-ator-tecnico.md) | Remoção do ator "Técnico de manutenção" | 27/10/2026 | ✅ Aceita |
| [0003](0003-estado-atrasada-derivado.md) | `ATRASADA` como estado derivado | 27/10/2026 | ✅ Aceita |
| [0004](0004-regra-de-negocio-na-api.md) | Regra de negócio exclusivamente na API | 27/10/2026 | ✅ Aceita |
| [0005](0005-login-proprio-do-cliente.md) | Cliente com login próprio | 27/10/2026 | ✅ Aceita em 07/11 — decisão #1 |
| [0006](0006-reserva-bloqueia-apenas-apos-aprovacao.md) | Reserva bloqueia disponibilidade só após aprovação | 27/10/2026 | ✅ Aceita em 07/11 — decisão #2 |
| [0007](0007-locacao-presencial-sem-reserva.md) | Locação presencial direta, sem reserva prévia | 27/10/2026 | ✅ Aceita em 07/11 — decisão #5 |
| [0008](0008-mermaid-no-lugar-do-plantuml.md) | Mermaid no lugar do PlantUML | 07/11/2026 | ✅ Aceita · recomendação do professor |
| [0009](0009-trello-no-lugar-do-github-projects.md) | Trello no lugar do GitHub Projects | 07/11/2026 | ✅ Aceita |
| [0010](0010-diagrama-antes-da-implementacao.md) | Diagrama aprovado antes da implementação | 07/11/2026 | ✅ Aceita |
| [0011](0011-diaria-iniciada-como-criterio-de-cobranca.md) | Diária iniciada como critério de cobrança | 07/11/2026 | ✅ Aceita — decisão #3 |
| [0012](0012-multa-por-atraso-e-politica-de-danos.md) | Multa por atraso e política de danos | 07/11/2026 | ✅ Aceita — decisão #4 |
| [0013](0013-devolucao-sempre-integral.md) | Devolução sempre integral | 07/11/2026 | ✅ Aceita — decisão #6 |
| [0014](0014-tres-relatorios-obrigatorios.md) | Três relatórios obrigatórios | 07/11/2026 | ✅ Aceita — decisão #7 |

---

## Status possíveis

| Status | Significado |
|---|---|
| 🟡 **Proposta** | Em discussão, ainda não vale |
| ✅ **Aceita** | Vale e deve ser seguida |
| ❌ **Rejeitada** | Foi considerada e descartada — o registro evita rediscutir |
| 🔄 **Substituída** | Uma decisão posterior a substituiu; o ADR aponta qual |

**ADR aceito não é editado para mudar a decisão.** Se a decisão mudar, crie um novo ADR e marque o antigo como Substituída. O histórico importa.

---

## Quando escrever um ADR

Escreva quando a decisão:

- Afeta a estrutura do código ou a escolha de tecnologia
- É difícil de reverter depois
- Teve alternativas razoáveis que foram descartadas
- Alguém vai questionar no futuro

**Não** escreva para: escolha de nome de variável, formatação, decisão trivial com uma única opção óbvia.

---

## Modelo

```markdown
# ADR NNNN — Título curto no imperativo

- **Data:** DD/MM/AAAA
- **Status:** 🟡 Proposta | ✅ Aceita | ❌ Rejeitada | 🔄 Substituída por ADR NNNN
- **Decidido por:** equipe | nome

## Contexto

Qual é a situação? Que forças estão em jogo? O que torna essa decisão necessária?
Fatos, não opinião.

## Decisão

O que foi decidido. Em uma frase se possível, seguida do detalhamento.

## Alternativas consideradas

### A — Nome da alternativa
- ✅ Vantagem
- ❌ Desvantagem

### B — Nome da alternativa ← escolhida
- ✅ Vantagem
- ❌ Desvantagem

## Consequências

**Positivas** — o que melhora
**Negativas** — o que piora ou fica mais caro
**Riscos** — o que pode dar errado, e como mitigar
```

Salve como `NNNN-titulo-em-kebab-case.md`, com numeração sequencial de quatro dígitos.
