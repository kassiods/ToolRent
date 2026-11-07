# Sprints — ToolRent

Uma pasta por entrega. Cada arquivo de sprint responde quatro perguntas:

1. **O que a sprint entrega** — o escopo fechado
2. **Quais diagramas especificam essa entrega** — e eles vêm antes do código ([ADR 0010](../decisoes/0010-diagrama-antes-da-implementacao.md))
3. **Como o trabalho está dividido** — pacotes independentes, um por integrante
4. **O que depende de decisão da equipe** — e por que não bloqueia o resto

---

## Sprints

| Sprint | Tema | Documento | Situação |
|---|---|---|---|
| 0 | Fundação do repositório | — (ver [`../agil/sprints.md`](../agil/sprints.md)) | ✅ Concluída |
| 1 | Casos de uso e backlog | — (ver [`../agil/sprints.md`](../agil/sprints.md)) | 🟡 Em andamento |
| **2** | **Classes de domínio e banco** | [`sprint-2.md`](sprint-2.md) | 🟡 Diagramas prontos, implementação a iniciar |
| 3–8 | Construção | a escrever ao planejar cada uma | ⬜ |

O **plano geral** das oito sprints, com roadmap e pontos por sprint, continua em
[`../agil/sprints.md`](../agil/sprints.md). Esta pasta detalha a sprint **em execução**: é o
documento que o integrante abre para saber exatamente o que fazer.

---

## Relação com o quadro

| Onde | O que responde |
|---|---|
| **Este documento** | O que é o pacote de trabalho, qual diagrama o especifica, o que é "pronto" |
| **[Quadro no Trello](https://trello.com/b/m3gakhP8/toolrent-backlog)** | Em que etapa cada pacote está e quem está nele |
| **GitHub Issues** | A discussão, os critérios de aceite e o vínculo com os commits |

Os três precisam contar a mesma história. Quando divergirem, **este documento e a
[matriz de rastreabilidade](../agil/rastreabilidade.md) são a fonte** — o quadro é acompanhamento.

---

## Modelo para a próxima sprint

```markdown
# Sprint N — Tema

## Objetivo
Uma frase.

## Diagramas que especificam esta entrega
Lista, com link. Todos mergeados ANTES de a implementação começar.

## Pacotes de trabalho
Um por integrante, independentes. Para cada um: objetivo, arquivos, diagrama, pronto quando.

## Ordem de integração
Quem depende de quem, e por quê.

## O que depende de decisão da equipe
Pendência, o que ela afeta, e se bloqueia ou não.

## Definition of Done da sprint
O que precisa estar verdadeiro para a sprint fechar.
```
