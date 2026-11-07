# Plano de Sprints — ToolRent

> **Sprints semanais.** O calendário oficial vem do professor — as datas abaixo são a organização interna da equipe e devem ser ajustadas quando o cronograma oficial for divulgado.

---

## Ritmo da equipe

| Cerimônia | Quando | Duração | Objetivo |
|---|---|---|---|
| **Planejamento** | Início da sprint | 30 min | Escolher as histórias e distribuir |
| **Sincronização** | 2× por semana | 10 min | O que fiz, o que farei, o que me trava |
| **Revisão** | Fim da sprint | 20 min | Demonstrar o que ficou pronto |
| **Retrospectiva** | Fim da sprint | 15 min | O que manter, o que mudar |

Capacidade estimada: **10 a 13 pontos por sprint** com 4 integrantes em disciplina paralela. Ajustar após duas sprints com dados reais.

---

## Roadmap

```mermaid
gantt
    title Sprints do ToolRent
    dateFormat YYYY-MM-DD
    axisFormat %d/%m

    section Preparação
    Sprint 0 · Repositório e docs       :done, s0, 2026-10-27, 3d
    Sprint 1 · Casos de uso e backlog   :active, s1, 2026-10-30, 7d

    section Modelagem
    Sprint 2 · Classes e banco          :s2, after s1, 7d

    section Construção
    Sprint 3 · Autenticação             :s3, after s2, 7d
    Sprint 4 · Cadastros e catálogo     :s4, after s3, 7d
    Sprint 5 · Reservas e locação       :s5, after s4, 7d
    Sprint 6 · Devolução e financeiro   :s6, after s5, 7d
    Sprint 7 · Manutenção e relatórios  :s7, after s6, 7d

    section Entrega
    Sprint 8 · Testes e fechamento      :s8, after s7, 7d
```

---

## Sprint 0 — Fundação ✅

**Objetivo:** ter um repositório que qualquer integrante consegue clonar e entender.

| Entrega | Status |
|---|---|
| Repositório criado com estrutura de pastas | ✅ |
| README explicando projeto, stack e execução | ✅ |
| `.gitignore` protegendo credenciais | ✅ |
| Documentação base (visão, casos de uso, backlog, regras, arquitetura, glossário) | ✅ |
| Templates de Issue e PR | ✅ |
| Esqueleto do app Flutter (analisado e testado) | ✅ |
| Esqueleto da API Java (build verificado, 2 testes) | ✅ |
| Migration inicial do Supabase | 🟡 rascunho, a confirmar na Sprint 2 |

---

## Sprint 1 — Casos de uso 🟡

**Objetivo:** entregar a documentação de requisitos exigida na primeira entrega da disciplina.

| Tarefa | Status |
|---|---|
| Criar as 16 Issues no GitHub a partir do backlog | ✅ #1 a #16 |
| Adicionar integrantes e professor como colaboradores | ✅ |
| Aprovação do professor para a mudança de stack | ✅ ADR 0001 |
| Revisar os 16 casos de uso com a equipe | ⬜ |
| Diagrama de casos de uso em Mermaid, renderizado no GitHub | ✅ [`casos-de-uso.md`](../diagramas/sprint-1/casos-de-uso.md) |
| Configurar o quadro de acompanhamento com as colunas do fluxo | ✅ [Trello](https://trello.com/b/m3gakhP8/toolrent-backlog) — 6 listas, 16 cartões |
| **Resolver as decisões pendentes #1, #2 e #5** | 🟡 propostas em [ADR 0005](../decisoes/0005-login-proprio-do-cliente.md), [0006](../decisoes/0006-reserva-bloqueia-apenas-apos-aprovacao.md) e [0007](../decisoes/0007-locacao-presencial-sem-reserva.md) — aguardam aprovação |
| Revisar o documento antes da entrega | ⬜ |

**Critério de conclusão:** documento e diagrama coerentes entre si, 16 Issues criadas, colaboradores adicionados.

---

## Sprint 2 — Telas iniciais por perfil

**Objetivo:** a primeira tela de cada perfil, com o menu das ações daquele perfil. **Só front-end.**

📋 **Plano detalhado, com os pacotes por integrante: [`../sprints/sprint-2.md`](../sprints/sprint-2.md)**

| Tarefa | Situação |
|---|---|
| Diagrama de navegação — tela do Administrador | ✅ [`navegacao-administrador.md`](../diagramas/sprint-2/navegacao-administrador.md) |
| Diagrama de navegação — tela do Atendente | ✅ [`navegacao-atendente.md`](../diagramas/sprint-2/navegacao-atendente.md) |
| Diagrama de navegação — tela do Cliente | ✅ [`navegacao-cliente.md`](../diagramas/sprint-2/navegacao-cliente.md) |
| [TT05](https://github.com/kassiods/ToolRent/issues/26) Tela do Administrador + casca compartilhada — Nailan | ⬜ |
| [TT06](https://github.com/kassiods/ToolRent/issues/27) Tela do Atendente, 10 itens — Lincoln | ⬜ |
| [TT07](https://github.com/kassiods/ToolRent/issues/28) Tela do Cliente + teste de referência — Victor | ⬜ |
| Banco de dados no Supabase | ⏸️ **adiado** — não há projeto Supabase do ToolRent |
| Políticas de RLS | ⏸️ adiado junto com o banco |
| Dados de demonstração fictícios | ⏸️ adiado junto com o banco |

**Três telas porque são três atores** — Administrador, Atendente e Cliente
([ADR 0002](../decisoes/0002-remocao-do-ator-tecnico.md)). Cada integrante leva uma.

> 🚫 **Nada de back-end nesta sprint.** Nenhuma tela chama a API, lê banco ou autentica. Um
> seletor de perfil provisório abre o app e deixa escolher qual das três telas ver; ele **não é
> controle de acesso** e sai na Sprint 3.

> 📐 **Primeira sprint implementada sob o [ADR 0010](../decisoes/0010-diagrama-antes-da-implementacao.md):**
> os três diagramas foram escritos **antes** de qualquer linha de código.

> ⏭️ **As classes de domínio saíram daqui para a Sprint 3** — TT01 e TT02. Os diagramas que as
> especificam continuam válidos e mergeados; só a implementação mudou de sprint.

---

## Sprint 3 — Domínio e autenticação · 10 pontos

| História | Pontos |
|---|---|
| US01 — Autenticar-se | 5 |
| US02 — Gerenciar usuários e perfis | 5 |

| Tarefa técnica | Situação |
|---|---|
| [TT01](https://github.com/kassiods/ToolRent/issues/21) Classes de cadastros, catálogo e manutenção — Nailan | ⬜ |
| [TT02](https://github.com/kassiods/ToolRent/issues/22) Classes de locação ponta a ponta — Lincoln | ⬜ |

**Por que as classes entram aqui:** autenticar exige `Usuario` e `PerfilUsuario`. Sem classe de
domínio não há o que servir nem persistir, então a Sprint 3 precisaria delas de todo jeito.

**Por que a autenticação vem antes do resto:** todo o resto depende de saber quem é o usuário.
RN09 atravessa todos os casos de uso.

**Entrega demonstrável:** login funcionando, decidindo **qual das três telas da Sprint 2** abre, e
endpoint protegido retornando 403.

> ⚠️ O enunciado limita o domínio a **9–13 classes**. O diagrama aprovado tem **10**: `Usuario`,
> `Cliente`, `Categoria`, `Ferramenta`, `Reserva`, `Locacao`, `ItemLocacao`, `Devolucao`,
> `Pagamento` e `Manutencao`. `Perfil` virou enumeração e `Funcionario` não existe — as duas
> divergências em relação ao guia mestre estão justificadas em [`classes.md`](../diagramas/sprint-2/classes.md).
> Classes de UI, DTO, service e repository **não** contam como domínio.

---

## Sprint 4 — Cadastros e catálogo · 19 pontos

| História | Pontos |
|---|---|
| US03 — Cadastrar clientes | 3 |
| US04 — Cadastrar ferramentas e categorias | 5 |
| US05 — Consultar disponibilidade | 8 |
| US06 — Consultar catálogo | 3 |

> ⚠️ **19 pontos passa da capacidade estimada.** Se a velocidade medida nas Sprints 2 e 3 não confirmar essa capacidade, mova US06 para a Sprint 5.

**Atenção:** US05 é a primeira história com lógica real (sobreposição de períodos). Reserve tempo para os testes de borda.

---

## Sprint 5 — Reservas e locação · 21 pontos

| História | Pontos |
|---|---|
| US07 — Solicitar reserva | 5 |
| US08 — Aprovar ou recusar reservas | 5 |
| US09 — Formalizar locação | 8 |
| US10 — Registrar retirada | 3 |

**Entrega demonstrável:** fluxo completo do pedido do cliente até a ferramenta saindo da loja.

---

## Sprint 6 — Devolução e financeiro · 26 pontos

| História | Pontos |
|---|---|
| US11 — Registrar devolução | 8 |
| US12 — Calcular valores e encargos | 13 |
| US13 — Registrar pagamentos | 5 |

> ⛔ **A sprint mais pesada do projeto.** Duas providências antes de começar:
> 1. **Resolver as decisões #3, #4 e #6** — sem elas a US12 não pode ser implementada
> 2. **Quebrar a US12** em pelo menos 3 histórias menores

---

## Sprint 7 — Manutenção e relatórios · 16 pontos

| História | Pontos |
|---|---|
| US14 — Gerenciar manutenções | 5 |
| US15 — Consultar relatórios | 8 |
| US16 — Consultar histórico | 3 |

---

## Sprint 8 — Fechamento

| Tarefa |
|---|
| Testes de integração dos fluxos principais |
| Revisão de todos os diagramas contra o código entregue |
| Revisão da documentação |
| Verificação de execução limpa a partir de um clone novo |
| Preparação da apresentação |

**O teste decisivo:** clonar o repositório numa máquina limpa e conseguir subir o sistema seguindo apenas o README. Se não funcionar, o README está incompleto.

---

## Riscos

| Risco | Impacto | Mitigação |
|---|---|---|
| Decisões pendentes travarem a Sprint 6 | Alto | Resolver #3 e #4 até o fim da Sprint 4 |
| US12 ser maior que o estimado | Alto | Quebrar em 3+ histórias antes de começar |
| Sprints 4, 5 e 6 acima da capacidade | Médio | Medir velocidade real nas Sprints 2 e 3 e replanejar |
| Diagrama divergir do código | Alto | Atualizar o diagrama **no mesmo PR** da implementação |
| Ambiente de desenvolvimento inconsistente | Baixo | Maven Wrapper (`./mvnw`) elimina a instalação do Maven; resta conferir JDK 21 e Flutter em cada máquina |
| Credencial do Supabase vazar em commit | Alto | `.gitignore` configurado e `git diff` antes de cada commit |
