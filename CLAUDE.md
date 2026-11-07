# Instruções para assistentes de IA — ToolRent

> Este arquivo é lido automaticamente por assistentes de IA que abrem o repositório.
> Humanos: comecem pelo [README.md](README.md).

---

## 0. Ao iniciar a sessão — faça isto primeiro

**Antes de responder qualquer pedido, pergunte: "quem está na sessão?"** O trabalho é
distribuído por integrante, e a resposta muda o que você deve fazer.

Com o nome em mãos:

1. **Abra o quadro** — [ToolRent — Backlog](https://trello.com/b/m3gakhP8/toolrent-backlog) — e
   localize os cartões atribuídos a essa pessoa.
2. **Veja em que lista o cartão está.** A lista determina o que é a próxima ação legítima:

| Cartão está em | A próxima ação é |
|---|---|
| 📥 Backlog | Refinar a Issue até cumprir a [DoR](docs/agil/definition-of-ready.md). **Não codifique.** |
| 🎯 Pronto para a sprint | **Desenhar o diagrama** em `docs/diagramas/`, abrir PR só do diagrama |
| 📐 Diagrama | O diagrama está em revisão. Enquanto não houver merge, **não comece o código** |
| 🔨 Em andamento | Implementar — o diagrama já está aprovado. Confira que o código corresponde a ele |
| 🔍 Em revisão | Responder à revisão do PR. Ver [receber revisão](CONTRIBUTING.md) |
| ✅ Concluído | Nada. Confirme que a Issue foi fechada e siga para o próximo cartão |

3. **Confirme a leitura do cartão com a pessoa** antes de agir. Cartão desatualizado é comum —
   o quadro não sincroniza com as Issues automaticamente ([ADR 0009](docs/decisoes/0009-trello-no-lugar-do-github-projects.md)).
4. Se o cartão tiver a etiqueta roxa ou um aviso de **decisão pendente**, pare: a história está
   bloqueada. Ver [regra 2](#2-não-invente-decisão).

Se a pessoa pedir algo que não corresponde a nenhum cartão dela, diga isso e pergunte se deve
seguir mesmo assim — não invente rastreabilidade que não existe.

---

## Leia antes de sugerir qualquer coisa

Nesta ordem:

1. [`README.md`](README.md) — visão geral e como executar
2. [`docs/casos-de-uso.md`](docs/casos-de-uso.md) — o que o sistema faz
3. [`docs/regras-de-negocio.md`](docs/regras-de-negocio.md) — o que o sistema impede
4. [`docs/arquitetura.md`](docs/arquitetura.md) — onde cada coisa mora
5. [`docs/decisoes/`](docs/decisoes/) — por que está assim
6. [`docs/diagramas/`](docs/diagramas/) — a especificação que o código deve implementar
7. [`arquivos-de-ia/guias/guia-mestre.md`](arquivos-de-ia/guias/guia-mestre.md) — contexto completo

> ⚠️ O guia mestre descreve a **stack original** (JavaFX + SQLite) e o PlantUML. Vale para
> requisitos, regras de negócio e processo; a parte de tecnologia e de diagramas está superada
> pelos ADRs [0001](docs/decisoes/0001-stack-flutter-java-supabase.md) e
> [0008](docs/decisoes/0008-mermaid-no-lugar-do-plantuml.md).

**Não presuma que o roteiro foi implementado só porque está documentado.** Inspecione o estado real antes de agir.

---

## Regras inegociáveis

### 1. Regra de negócio fica na API

Toda regra (RN01–RN12) vive em `api/src/main/java/br/edu/toolrent/api/service/`.

O app Flutter **não decide nada**. Ele pode repetir uma validação de formato para dar retorno imediato, mas isso é cortesia visual — a API revalida sempre.

Motivo: o app roda no dispositivo do usuário e pode ser modificado por ele. Ver [ADR 0004](docs/decisoes/0004-regra-de-negocio-na-api.md).

### 2. Não invente decisão

Oito decisões estão pendentes (ver [casos de uso § 6](docs/casos-de-uso.md#6-decisões-pendentes)). Onde a documentação diz "pendente" ou "proposta", **pergunte à equipe**. Não escolha por conta própria e não implemente como se estivesse aprovado.

### 3. O diagrama vem antes do código

A disciplina exige correspondência entre diagrama e código, e **exige a ordem**: o diagrama é
especificação, não documentação retroativa. Ver [ADR 0010](docs/decisoes/0010-diagrama-antes-da-implementacao.md).

Antes de escrever qualquer linha de implementação, confirme que **as três coisas existem**:

1. **Issue** no GitHub, com a DoR cumprida
2. **Diagrama** em `docs/diagramas/*.md` que especifica o comportamento — **já mergeado**
3. **Cartão no Trello** fora da lista 📐

Qual diagrama é exigido depende da história:

| A história… | Exige o diagrama de |
|---|---|
| cria ou altera entidade de domínio | **Classes** |
| muda ciclo de vida de Reserva, Locação ou Manutenção | **Estados** |
| coordena vários objetos num fluxo | **Sequência** |
| apenas consulta, sem entidade ou estado novo | basta o de **casos de uso** já aprovado |

**Se o diagrama não existe, o trabalho da vez é desenhá-lo** — em Mermaid, em `docs/diagramas/`,
num PR separado do código ([ADR 0008](docs/decisoes/0008-mermaid-no-lugar-do-plantuml.md)). Não
escreva o código "e depois o diagrama": isso inverte a disciplina e o histórico do Git mostra.

### 4. Menor alteração coerente

Não reescreva o projeto. Inspecione o que existe, preserve o que funciona, faça a menor mudança que resolve. Antes de refatoração ampla, explique o motivo e peça confirmação.

### 5. Não afirme que testou se não executou

Informe os comandos que rodou e o resultado real. Se não conseguiu executar, **diga explicitamente que não executou**.

### 6. Nunca commite credencial

`.env`, chave do Supabase, token. Se encontrar um segredo versionado, avise imediatamente — a chave precisa ser revogada, não apenas apagada.

---

## Convenções de código

### Java (`api/`)

- Domínio em português: `Locacao`, `calcularValorBase`, `tarifaDiaria`
- Dinheiro em `BigDecimal` — **nunca** `double` ou `float` (RN12)
- Estado em `enum`, com transição validada no service (RN08)
- `web/` não contém regra · `service/` não conhece HTTP · `repository/` não contém regra
- Nenhuma taxa ou percentual literal no código — vai para `config/`
- Sem captura silenciosa de exceção; sem `println` como estratégia de log

### Dart (`app/`)

- Arquivos em `snake_case`, classes em `PascalCase`
- Widget nunca chama HTTP direto — sempre via `data/repositories/`
- Nenhuma regra de negócio
- Nenhuma cor literal — tudo em `core/app_theme.dart`

### Diagramas (`docs/diagramas/`)

- **Mermaid**, em bloco ` ```mermaid ` dentro de um `.md` — nunca PlantUML, nunca imagem solta
- A fonte é o diagrama publicado: o GitHub renderiza. Não gere PNG nem JPEG
- Identificadores iguais aos da documentação: `UC01`, não `uc1`
- Cores por `classDef`; legenda como tabela Markdown fora do bloco
- Só relações reais — `«include»`/`«extend»` não são enfeite

### SQL (`supabase/`)

- Migration nova para cada mudança; **nunca** editar uma já aplicada
- Nenhuma regra de negócio em trigger

---

## Estrutura

```
app/              Flutter — interface
api/              Java 21 + Spring Boot — REGRAS DE NEGÓCIO
supabase/         PostgreSQL — persistência
docs/             documentação, diagramas (Mermaid) e decisões
arquivos-de-ia/   guias e sessões de IA
```

---

## Formato de resposta esperado

1. **Entendimento** — o que será feito
2. **Rastreabilidade** — US, UC, RN, Issue e diagrama relacionados
3. **Arquivos envolvidos** — caminhos exatos
4. **Plano curto** — sequência de mudanças
5. **Implementação** — somente o escopo pedido
6. **Verificação** — comandos executados e resultados **reais**
7. **Documentação** — o que foi ou precisa ser atualizado
8. **Pendências** — o que depende da equipe

Quando pedirem apenas análise, **não altere arquivos**.

---

## Estado atual

| Item | Situação |
|---|---|
| Sprint | 2 — classes de domínio e banco · [plano](docs/sprints/sprint-2.md) |
| Documentação | ✅ completa para a entrega atual |
| App Flutter | 🟡 esqueleto; tela de boas-vindas, sem autenticação |
| API Java | 🟡 esqueleto; só o endpoint de verificação · build verificado |
| Banco | 🟡 migration inicial em **rascunho**, a confirmar na Sprint 2 |
| Classes de domínio | ⬜ diagrama pronto; implementação dividida em TT01–TT03 |
| Diagramas | ✅ casos de uso, classes, estados da Reserva e da Locação · sequência pendente |

**Backlog:** GitHub Issues `#1` a `#16` = US01–US16. **Quadro de acompanhamento:**
[Trello](https://trello.com/b/m3gakhP8/toolrent-backlog) — o GitHub Projects foi apagado
([ADR 0009](docs/decisoes/0009-trello-no-lugar-do-github-projects.md)). A etiqueta de sprint
saiu das Issues; a sprint vive no quadro e em [`docs/backlog.md`](docs/backlog.md).

A mudança de stack foi **aprovada pelo professor** em 27/10/2026 — ver [ADR 0001](docs/decisoes/0001-stack-flutter-java-supabase.md).

Para compilar a API use `./mvnw` (Maven Wrapper); não é preciso instalar o Maven.
