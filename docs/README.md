# Documentação — ToolRent

Índice da pasta `docs/`. Comece pela visão do produto se é sua primeira vez no projeto.

---

## Ordem de leitura sugerida

| # | Documento | Tempo | O que você vai entender |
|---|---|---|---|
| 1 | [Visão do produto](visao-do-produto.md) | 5 min | Que problema o ToolRent resolve e para quem |
| 2 | [Glossário](glossario.md) | 5 min | O vocabulário do domínio — leia antes de programar |
| 3 | [Casos de uso](casos-de-uso.md) | 20 min | O que o sistema faz, caso a caso |
| 4 | [Regras de negócio](regras-de-negocio.md) | 15 min | O que o sistema impede, e por quê |
| 5 | [Arquitetura](arquitetura.md) | 10 min | Como o código está organizado |
| 6 | [Backlog](backlog.md) | 10 min | Em que ordem será construído |

---

## Por finalidade

### Preciso entender o escopo
→ [Visão do produto](visao-do-produto.md) · [Casos de uso](casos-de-uso.md)

### Vou implementar uma história
→ [Backlog](backlog.md) → o caso de uso correspondente em [Casos de uso](casos-de-uso.md) → as regras em [Regras de negócio](regras-de-negocio.md) → [Definition of Done](agil/definition-of-done.md)

### Vou revisar um Pull Request
→ [Definition of Done](agil/definition-of-done.md) · [Matriz de rastreabilidade](agil/rastreabilidade.md)

### Quero saber por que algo foi decidido assim
→ [Decisões (ADRs)](decisoes/)

### Vou planejar a próxima sprint
→ [Plano de sprints](agil/sprints.md) · [Definition of Ready](agil/definition-of-ready.md)

---

## Conteúdo da pasta

```
docs/
├── visao-do-produto.md       problema, proposta, escopo e critérios de sucesso
├── casos-de-uso.md           os 16 UC especificados
├── backlog.md                as 16 histórias priorizadas e estimadas
├── regras-de-negocio.md      RN01 a RN12
├── arquitetura.md            camadas, fronteiras e fluxo de requisição
├── glossario.md              vocabulário do domínio
├── casos-de-uso-v1-original.pdf   documento original da equipe, para referência
│
├── agil/
│   ├── definition-of-ready.md     quando uma história entra na sprint
│   ├── definition-of-done.md      quando uma história está concluída
│   ├── sprints.md                 roadmap, capacidade e riscos
│   └── rastreabilidade.md         matriz US ↔ UC ↔ RN ↔ Issue ↔ PR
│
├── decisoes/                 ADRs — decisões com data, alternativas e consequências
└── diagramas/                fontes .puml e imagens geradas
```

---

## Regras de manutenção

1. **Documento desatualizado é pior que documento ausente** — ele mente sobre o sistema. Se a implementação divergiu, corrija no mesmo PR.
2. **Diagrama e código andam juntos.** A disciplina avalia essa correspondência.
3. **Decisão relevante vira ADR.** Se alguém vai perguntar "por que foi feito assim?" daqui a dois meses, registre em `decisoes/`.
4. **IDs nunca são renumerados.** US07 é US07 para sempre.
5. **Pendência fica visível.** Marque com ⚠️ ou ⛔ em vez de deixar implícito.

---

## Estado atual

| Documento | Versão | Status |
|---|---|---|
| Visão do produto | 1.0 | ✅ |
| Casos de uso | 2.0 | ✅ UC04 especificado, 3 atores |
| Backlog | 2.0 | ✅ US08 restaurada |
| Regras de negócio | 2.0 | 🟡 4 pendências de parâmetro |
| Arquitetura | 1.0 | ✅ |
| Glossário | 1.0 | ✅ |
| Diagrama de casos de uso | 1.0 | ✅ fonte e imagem versionadas |
| Diagrama de classes | — | ⬜ Sprint 2 |
| Diagrama de estados | — | ⬜ Sprint 2 |

**8 decisões pendentes** aguardam a equipe — listadas em [Casos de uso § 6](casos-de-uso.md#6-decisões-pendentes).

O backlog está no [GitHub Issues](https://github.com/kassiods/ToolRent-Sistema-de-Loca-o-de-Ferramentas/issues): `#1` a `#16` correspondem a US01–US16. A matriz em [`agil/rastreabilidade.md`](agil/rastreabilidade.md) liga cada uma ao seu caso de uso.
