# Decisões de Arquitetura (ADRs)

Um **ADR** (*Architecture Decision Record*) registra uma decisão relevante: o contexto, as alternativas consideradas, o que foi escolhido e as consequências.

**Por que registrar:** daqui a dois meses alguém vai perguntar "por que foi feito assim?". Sem registro, a resposta é um encolher de ombros — e a decisão acaba sendo refeita ou revertida por engano.

---

## Decisões registradas

| # | Decisão | Data | Status |
|---|---|---|---|
| [0001](0001-stack-flutter-java-supabase.md) | Stack: Flutter + Java/Spring Boot + Supabase | 27/10/2026 | ✅ Aceita · ⛔ **pendente de aprovação do professor** |
| [0002](0002-remocao-do-ator-tecnico.md) | Remoção do ator "Técnico de manutenção" | 27/10/2026 | ✅ Aceita |
| [0003](0003-estado-atrasada-derivado.md) | `ATRASADA` como estado derivado | 27/10/2026 | ✅ Aceita |
| [0004](0004-regra-de-negocio-na-api.md) | Regra de negócio exclusivamente na API | 27/10/2026 | ✅ Aceita |

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
