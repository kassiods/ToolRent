# Instruções para assistentes de IA — ToolRent

> Este arquivo é lido automaticamente por assistentes de IA que abrem o repositório.
> Humanos: comecem pelo [README.md](README.md).

---

## Leia antes de sugerir qualquer coisa

Nesta ordem:

1. [`README.md`](README.md) — visão geral e como executar
2. [`docs/casos-de-uso.md`](docs/casos-de-uso.md) — o que o sistema faz
3. [`docs/regras-de-negocio.md`](docs/regras-de-negocio.md) — o que o sistema impede
4. [`docs/arquitetura.md`](docs/arquitetura.md) — onde cada coisa mora
5. [`docs/decisoes/`](docs/decisoes/) — por que está assim
6. [`arquivos-de-ia/guias/guia-mestre.md`](arquivos-de-ia/guias/guia-mestre.md) — contexto completo

**Não presuma que o roteiro foi implementado só porque está documentado.** Inspecione o estado real antes de agir.

---

## Regras inegociáveis

### 1. Regra de negócio fica na API

Toda regra (RN01–RN12) vive em `api/src/main/java/br/edu/toolrent/api/service/`.

O app Flutter **não decide nada**. Ele pode repetir uma validação de formato para dar retorno imediato, mas isso é cortesia visual — a API revalida sempre.

Motivo: o app roda no dispositivo do usuário e pode ser modificado por ele. Ver [ADR 0004](docs/decisoes/0004-regra-de-negocio-na-api.md).

### 2. Não invente decisão

Oito decisões estão pendentes (ver [casos de uso § 6](docs/casos-de-uso.md#6-decisões-pendentes)). Onde a documentação diz "pendente" ou "proposta", **pergunte à equipe**. Não escolha por conta própria e não implemente como se estivesse aprovado.

### 3. Não implemente o que o diagrama não especifica

A disciplina exige correspondência entre diagrama e código. Antes de implementar, confirme que existem Issue e diagrama correspondentes.

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

### SQL (`supabase/`)

- Migration nova para cada mudança; **nunca** editar uma já aplicada
- Nenhuma regra de negócio em trigger

---

## Estrutura

```
app/              Flutter — interface
api/              Java 21 + Spring Boot — REGRAS DE NEGÓCIO
supabase/         PostgreSQL — persistência
docs/             documentação, diagramas e decisões
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
| Sprint | 1 — casos de uso e backlog |
| Documentação | ✅ completa para a entrega atual |
| App Flutter | 🟡 esqueleto; tela de boas-vindas, sem autenticação |
| API Java | 🟡 esqueleto; só o endpoint de verificação · build verificado |
| Banco | 🟡 migration inicial em **rascunho**, a confirmar na Sprint 2 |
| Classes de domínio | ⬜ Sprint 2 |

O backlog está no GitHub Issues: `#1` a `#16` correspondem a US01–US16.

A mudança de stack foi **aprovada pelo professor** em 27/10/2026 — ver [ADR 0001](docs/decisoes/0001-stack-flutter-java-supabase.md).

Para compilar a API use `./mvnw` (Maven Wrapper); não é preciso instalar o Maven.
