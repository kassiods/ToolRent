# Sessão — Sprint 2 redefinida: três telas iniciais, três pessoas

- **Data:** 07/11/2026
- **Integrante:** Kassio de Sousa Dias
- **Ferramenta:** Claude Code
- **Relacionado a:** Sprint 2 · Issues #26, #27 e #28 · PR desta branch

---

## Objetivo

Ler todos os arquivos de IA e reportar o estado real do projeto. Em seguida, **redefinir a
Sprint 2**: ela deixa de ser as classes de domínio e passa a ser três telas iniciais de front-end,
uma por perfil, uma por integrante.

## O que mudou no escopo da sprint

| Antes | Agora |
|---|---|
| 10 classes de domínio em Java, 2 pacotes | **3 telas iniciais em Flutter, 3 pacotes** |
| Nailan e Lincoln | **Nailan, Lincoln e Victor** |
| Back-end (POJOs de domínio) | **Só front-end** — nada de API, banco ou autenticação |

A equipe pediu três coisas, três diagramas, três pessoas. As três telas são as dos **três atores**
do documento de casos de uso — Administrador, Atendente e Cliente
([ADR 0002](../../../docs/decisoes/0002-remocao-do-ator-tecnico.md)). Fecha em três sem sobra e sem
inventar escopo.

### Por que front-end primeiro faz sentido aqui

O critério de entrega da Sprint 3 já era *"login funcionando, com menus diferentes por perfil"*.
Esses menus são exatamente o que esta sprint constrói. Quando o login entrar, ele decide **qual**
das três telas abre — as telas já existirão.

## O que foi feito

| Mudança | Onde |
|---|---|
| **Victor convidado para o quadro** e os três atribuídos aos seus cartões | Trello (navegador) |
| Diagrama de navegação da tela do Administrador | `docs/diagramas/sprint-2/navegacao-administrador.md` |
| Diagrama de navegação da tela do Atendente | `docs/diagramas/sprint-2/navegacao-atendente.md` |
| Diagrama de navegação da tela do Cliente | `docs/diagramas/sprint-2/navegacao-cliente.md` |
| Plano da Sprint 2 reescrito do zero: 3 pacotes, critérios de pronto, ordem de integração | `docs/sprints/sprint-2.md` |
| Issues #26, #27 e #28, uma por pacote | GitHub |
| 3 cartões novos na lista 📐 Diagrama, com responsável no título | Trello |
| TT01 e TT02 movidos para 📥 Backlog, renomeados e remarcados como Sprint 3 | Trello |
| Comentário em #21 e #22 explicando a mudança de sprint | GitHub |
| Sprint 3 passou a ser "Domínio e autenticação", com TT01 e TT02 | `docs/agil/sprints.md` |
| Matriz: TT05–TT07 na Sprint 2, TT01–TT02 na Sprint 3, e os 3 diagramas novos | `docs/agil/rastreabilidade.md` |
| Índice de diagramas: coluna "implementado em", separada de "nasceu na" | `docs/diagramas/README.md` |
| Estado atual + aviso de que a Sprint 2 é front-end | `CLAUDE.md` |

## A divisão do trabalho

| Pacote | Tela | Itens de menu | Responsável | Issue |
|---|---|---|---|---|
| 1 | Administrador **+ casca compartilhada** | 2 + 4 arquivos comuns | Nailan | [#26](https://github.com/kassiods/ToolRent/issues/26) |
| 2 | Atendente | 10 | Lincoln | [#27](https://github.com/kassiods/ToolRent/issues/27) |
| 3 | Cliente **+ teste de referência** | 2 | Victor | [#28](https://github.com/kassiods/ToolRent/issues/28) |

Os pacotes **não têm o mesmo tamanho**, e isso está declarado em vez de disfarçado: o Atendente
concentra dez dos dezesseis casos de uso. O Pacote 1 compensa carregando a casca
(`CartaoAcao`, `EmConstrucaoScreen`, `SeletorPerfilScreen`) e o Pacote 3 compensa com o primeiro
teste de widget de tela do projeto.

O Pacote 1 entra primeiro; 2 e 3 não dependem um do outro e podem entrar em qualquer ordem. Os
três escrevem em paralelo porque o diagrama publica os construtores do `CartaoAcao` e do
`EmConstrucaoScreen` — mesma lógica que valeu na divisão anterior.

## Decisões tomadas

- **O diagrama de navegação é `stateDiagram-v2`**, com tela como estado e toque como transição. É
  a notação da tabela de `diagramas/README.md` que corresponde a navegação, e mantém a entrega em
  UML — não existe "diagrama de tela" na UML, e inventar uma notação nova numa disciplina de
  modelagem seria pior
- **Os três diagramas de domínio ficam na pasta `sprint-2/`**, mesmo com a implementação na
  Sprint 3. A convenção de `diagramas/README.md` já dizia que a pasta registra onde o diagrama
  **nasceu**; ganhou uma coluna "implementado em" para a diferença ficar explícita
- **TT01 e TT02 não foram fechadas nem reescritas**, só remarcadas como Sprint 3. Elas eram
  trabalho válido com diagrama aprovado; a sprint mudou, a tarefa não
- **Os IDs novos são TT05, TT06 e TT07.** TT03 e TT04 foram fechadas na redivisão anterior e o
  guia mestre veda reaproveitar ID já usado

## O que foi rejeitado

- **Dar um item de menu ao UC12 na tela do Atendente.** UC12 é `«include»` de UC09 e UC11: só
  acontece dentro deles. Botão próprio inventaria uma relação que o diagrama de casos de uso não
  tem. São dez itens, não onze — e o diagrama explica por quê
- **Inventar itens para a tela do Cliente.** "Minhas reservas" e "meus pagamentos" seriam o
  caminho natural para equilibrar o tamanho dos pacotes, mas **não existe caso de uso do Cliente
  para nenhum dos dois** — UC13 e UC16 são do Atendente. O diagrama registra a ausência e diz que
  o caminho é criar o caso de uso primeiro. `CLAUDE.md` regra 2
- **Mover os diagramas de domínio para uma pasta `sprint-3/`.** Seria reescrever a história de
  onde eles nasceram, e quebraria referências em vários arquivos sem ganho
- **Aninhar seções dentro de seções no Mermaid.** A primeira versão do diagrama do Atendente tinha
  composto dentro de composto e transição saindo de estado interno — as duas coisas que mais
  falham na renderização do GitHub. Achatado para um nível antes de commitar
- **Começar a implementar qualquer tela.** Os diagramas estão em revisão; implementar antes do
  merge violaria o ADR 0010

## Correções em documentos que estavam desatualizados

Encontradas ao conferir as referências, não pedidas:

| Arquivo | Estava | Está |
|---|---|---|
| `CLAUDE.md` | "implementação dividida em TT01–**TT03**" | TT03 foi fechada na redivisão anterior; corrigido |
| `docs/README.md` | "Regras de negócio · 🟡 4 pendências de parâmetro" | RN02, RN04, RN05 e RN06 foram definidas em 07/11 |
| `docs/README.md` | "**8 decisões pendentes** aguardam a equipe" | Resta a #8. E o link apontava para uma âncora que não existe mais — a seção foi renomeada |
| `docs/README.md` | Diagramas de classes e estados "⬜ Sprint 2" | Estão prontos e mergeados |
| `supabase/README.md` | "Decidir como reservas SOLICITADA afetam a disponibilidade (decisão #2)" | Decidida — ADR 0006 |
| `supabase/README.md` | "precisa ser confirmado contra o diagrama de classes" | A conferência 10 × 10 aconteceu em 07/11; o que falta é o projeto Supabase existir |

## Verificação

```
cd api && ./mvnw --batch-mode clean verify   ->  BUILD SUCCESS - 2 testes, 0 falhas (7,2 s)
cd app && flutter analyze                    ->  No issues found! (30,5 s)
cd app && flutter test                       ->  All tests passed! (3 testes)
Links relativos de todos os .md              ->  0 quebrados (varredura do repositorio inteiro)
Cercas ``` de todos os .md                   ->  todas balanceadas - 18 blocos Mermaid
Blocos Mermaid dos 3 diagramas novos         ->  100% ASCII, sem acento nem caractere especial
Renderizacao no GitHub dos 3 diagramas novos ->  conferida no navegador, na pagina do PR
```

Nenhum arquivo de código foi alterado nesta sessão — só documentação, diagramas e ferramentas de
acompanhamento. O build e os testes foram rodados mesmo assim, para confirmar que a `main`
continua verde no ponto de partida da sprint.

## Pendências

- [x] ~~Convidar o Victor no Trello~~ — **feito**. O e-mail chegou nesta sessão e o convite foi
      enviado pela interface do navegador, porque o MCP do Trello não adiciona membro a quadro:
      só tem `create` em `trelloWriteBoard`. O quadro agora tem 4 membros; o Victor aparece como
      *"a conta ainda não foi criada"* até ele aceitar o convite por e-mail
- [x] ~~Atribuir os três como membros dos cartões~~ — **feito**, também pela interface, pela mesma
      limitação do MCP. Nailan no TT05, Lincoln no TT06, Victor no TT07
- [ ] Revisar e mergear o PR desta branch — **a implementação não começa antes disso**
- [ ] Decisão #8: perguntar ao professor o modelo de especificação textual usado em aula
- [ ] Criar o projeto Supabase do ToolRent quando o back-end entrar
- [ ] Desenhar `sprint-4/sequencia-uc05.md` e `sprint-5/sequencia-uc09.md` antes do código delas
- [ ] As pastas de sessão de Lincoln, Victor e Nailan continuam vazias — todo o registro de uso de
      IA do projeto está sob um único integrante
