# Sessão — Decisões tomadas, diagramas por sprint e Sprint 2 redividida

- **Data:** 07/11/2026
- **Integrante:** Kassio de Sousa Dias
- **Ferramenta:** Claude Code
- **Relacionado a:** Sprint 2 · ADRs 0005–0007 e 0011–0014 · PR #25

---

## Objetivo

A equipe delegou as decisões pendentes. Esta sessão: **tomar as sete decisões que travavam
implementação**, reorganizar os diagramas por sprint, redividir a Sprint 2 em duas implementações
e deixar quadro, Issues e README coerentes para o pessoal começar a programar.

## As sete decisões

Todas viraram ADR ✅ Aceita, com contexto, alternativas descartadas e consequências. Nenhuma foi
"escolhida no susto": três já tinham proposta escrita desde 27/10 e foram promovidas; quatro foram
escritas agora.

| # | Questão | Decisão | ADR |
|---|---|---|---|
| 1 | Cliente tem login próprio? | **Sim**; vínculo `Cliente ↔ Usuario` opcional, cadastro de balcão não exige conta | [0005](../../../docs/decisoes/0005-login-proprio-do-cliente.md) |
| 2 | Reserva `SOLICITADA` bloqueia? | **Não.** Só `APROVADA` bloqueia | [0006](../../../docs/decisoes/0006-reserva-bloqueia-apenas-apos-aprovacao.md) |
| 3 | Como contar diárias? | **Diária iniciada**, mínimo 1. 23h = 1; 25h = 2 | [0011](../../../docs/decisoes/0011-diaria-iniciada-como-criterio-de-cobranca.md) |
| 4 | Atraso e danos? | **20% da diária por diária de atraso**, acumulativo; dano só entra no total após aprovação de ADMINISTRADOR | [0012](../../../docs/decisoes/0012-multa-por-atraso-e-politica-de-danos.md) |
| 5 | Locação presencial sem reserva? | **Permitida**, com as mesmas validações | [0007](../../../docs/decisoes/0007-locacao-presencial-sem-reserva.md) |
| 6 | Devolução parcial? | **Não.** A devolução é do contrato inteiro | [0013](../../../docs/decisoes/0013-devolucao-sempre-integral.md) |
| 7 | Quais relatórios? | **Três**: locações por período, receita por período, ferramentas mais locadas | [0014](../../../docs/decisoes/0014-tres-relatorios-obrigatorios.md) |

**A #8 continua aberta e não é minha para decidir** — é o modelo de especificação textual que o
professor usa em aula. Só ele pode responder, e não afeta código.

Cada ADR traz a nota de que a equipe delegou a escolha e de que **ADR aceito não se edita para
mudar de ideia**: se alguém discordar, é ADR novo marcando o anterior como 🔄 Substituída.

## Onde as decisões foram propagadas

Decisão que fica só no ADR não vale nada. Foram para:

| Onde | O que mudou |
|---|---|
| `diagramas/sprint-2/classes.md` | Entrou a associação `Cliente "0..1" → "0..1" Usuario` (decisão #1) |
| `migrations/0001_*.sql` | Ganhou `cliente.usuario_id`, FK opcional — rascunho nunca aplicado, então pôde ser editado |
| `regras-de-negocio.md` | RN02, RN04, RN05 e RN06 passaram de ⚠️ pendente para ✅ definida, com fórmula |
| `application.yml` | `criterio-diaria` e `percentual-multa-atraso` deixaram de ser provisórios |
| `casos-de-uso.md § 6` | Virou tabela de decisões tomadas; só a #8 segue aberta |
| `definition-of-ready.md` | A seção "decisões pendentes bloqueiam" passou a dizer que hoje não há nenhuma |
| `backlog.md` | As quatro notas de "depende de decisão pendente" viraram "decisão tomada" |
| `CLAUDE.md` regra 2 | Reescrita: as sete foram decididas, a regra continua valendo para o que vier |
| Issues e Trello | Label `decisao-pendente` removida de 6 Issues e 6 cartões; a label foi apagada do repo |

## Diagramas agora são por sprint

```
docs/diagramas/
├── sprint-1/   casos-de-uso.md (+ a imagem arquivada da entrega)
└── sprint-2/   classes.md · estados-reserva.md · estados-locacao.md
```

**Toda sprint tem a sua pasta, e a implementação da sprint segue os diagramas dela.** O
`diagramas/README.md` ganhou a seção "ao abrir uma sprint nova" com os quatro passos. Todas as
referências em 8 arquivos foram atualizadas para os caminhos novos.

## Sprint 2 redividida: de quatro pacotes para dois

| Antes | Agora |
|---|---|
| 3 pacotes de classes + 1 de migration | **2 pacotes de 5 classes cada** |
| Lincoln, Victor, Nailan, Kassio | Nailan e Lincoln |
| Dependia de confirmar a migration | **Sem banco** — o Supabase ficou para depois |

- **Pacote 1 — cadastros, catálogo e manutenção** (Nailan): `Usuario`, `Cliente`, `Categoria`, `Ferramenta`, `Manutencao`
- **Pacote 2 — locação ponta a ponta** (Lincoln): `Reserva`, `Locacao`, `ItemLocacao`, `Devolucao`, `Pagamento`

O corte é por coesão: o pacote 1 responde *"o que existe e está disponível"*, o 2 responde
*"o contrato, do pedido ao pagamento"*. Dependência única: 2 depende de 1.

Issues #23 e #24 foram fechadas com explicação; #21 e #22 reescritas.

## Supabase: parado, e ainda bem que olhei antes

O acesso por MCP existe e funciona. A organização `kassindias` tem plano **free** e dois projetos:
`sigau` e `fabrica-integracao-inscricoes`. **Nenhum é o ToolRent.**

Ao inspecionar o `fabrica-integracao-inscricoes`, que seria reaproveitado, encontrei:

| Tabela | Linhas |
|---|---|
| `public.inscricoes` | **147** |
| `public.evento_config` | 1 |

São dados reais de outro sistema. Aplicar a migration do ToolRent ali misturaria dois projetos no
mesmo banco, com risco de colisão de tipos e tabelas. **Nada foi escrito** — só leitura — e a
equipe decidiu adiar o Supabase. A Sprint 2 não precisa dele: as classes de domínio são POJOs e o
projeto usa JDBC, não JPA.

## Trello

- Os três membros estão no quadro: Kassio (admin), **Lincoln Gustavo** (`@lincolngustavo`) e **Nailan** (`@n2il2nnobre`)
- Convidar pelo e-mail revelou de quem era `kakuoriginal2@gmail.com`: **Lincoln**. Logo, quem falta é o **Victor Rennan**
- Cartões TT03 e TT04 arquivados; TT01 e TT02 reescritos com os pacotes novos
- Os 6 cartões que diziam "bloqueada por decisão pendente" agora dizem qual foi a decisão

## Decisões de processo tomadas nesta sessão

- **Aceitar as três propostas de 27/10 como estavam.** Reli cada uma; a argumentação se sustenta. Reescrever só para deixar minha marca seria pior
- **Não aplicar nada no Supabase existente**, mesmo com acesso e com o projeto indicado — havia dado de terceiro lá dentro
- **Editar a migration rascunho** para acompanhar a decisão #1. A regra "nunca editar migration aplicada" não vale para uma que nunca rodou
- **Não decidir a #8.** Não é escolha de projeto; é pergunta para o professor

## Verificação

```
cd api && ./mvnw --batch-mode clean verify   ->  (ver abaixo)
cd app && flutter analyze / flutter test     ->  (ver abaixo)
Links relativos de todos os .md              ->  (ver abaixo)
Blocos Mermaid                               ->  (ver abaixo)
```

## Erros cometidos pela IA nesta sessão

1. **Marquei as Issues #22 e #23 com `decisao-pendente`** e no mesmo texto escrevi que não estavam
   bloqueadas — a etiqueta significa "bloqueada". Removida na hora em que percebi
2. **Um heredoc com acentuação e crases quebrou no shell**; o arquivo foi criado com a ferramenta
   de escrita direta
3. **Quase usei um projeto Supabase com dado de terceiro.** O usuário indicou o
   `fabrica-integracao-inscricoes` e eu só não escrevi nada porque inspecionei antes. Lição que
   vale registrar: **ler o destino antes de escrever nele**, mesmo quando indicado

## Pendências

- [ ] **E-mail do Victor Rennan** — é o único fora do quadro
- [ ] Atribuir Nailan e Lincoln como membros dos cartões TT01/TT02 no Trello (30 segundos na
      interface; o MCP não faz, e a extensão do Chrome caiu no fim da sessão). O nome já está no
      título e na descrição
- [ ] Revisar e mergear o PR #25 — **a implementação não começa antes disso**
- [ ] Decisão #8: perguntar ao professor o modelo de especificação textual usado em aula
- [ ] Supabase: adiado. Quando entrar, criar projeto próprio do ToolRent — **não** reaproveitar o
      `fabrica-integracao-inscricoes`
- [ ] Desenhar `sprint-4/sequencia-uc05.md` e `sprint-5/sequencia-uc09.md` antes do código delas
