# Sessão — Diagramas da Sprint 2 e distribuição do trabalho

- **Data:** 07/11/2026
- **Integrante:** Kassio de Sousa Dias
- **Ferramenta:** Claude Code
- **Relacionado a:** Sprint 2 · ADR 0010 · Issues #21 a #24

---

## Objetivo

Primeira aplicação prática do [ADR 0010](../../../docs/decisoes/0010-diagrama-antes-da-implementacao.md):
escrever os diagramas da Sprint 2 **antes** de qualquer código, dividir a implementação em pacotes
independentes para os três desenvolvedores, criar a pasta de sprints no repositório e terminar de
ajeitar o quadro no Trello.

## O que foi feito

| Mudança | Onde |
|---|---|
| Diagrama de classes de domínio — 10 classes e 5 enumerações | `docs/diagramas/classes.md` |
| Diagrama de estados da `Reserva` | `docs/diagramas/estados-reserva.md` |
| Diagrama de estados da `Locacao`, com `ATRASADA` derivado | `docs/diagramas/estados-locacao.md` |
| Pasta de sprints, com modelo para as próximas | `docs/sprints/README.md` |
| Plano da Sprint 2: 4 pacotes, donos, critérios de pronto e ordem de integração | `docs/sprints/sprint-2.md` |
| Issues #21 a #24, uma por pacote | GitHub |
| 4 cartões na lista 📐 Diagrama, com responsável no título | Trello |
| 6 etiquetas do quadro nomeadas pela interface | Trello |
| 16 descrições de cartão reescritas — ver defeito abaixo | Trello |
| Matriz ganhou a tabela de tarefas técnicas e "qual diagrama habilita o quê" | `rastreabilidade.md` |
| Sprint 2 detalhada com os 4 pacotes | `agil/sprints.md` |
| Estado atual e índices atualizados | `CLAUDE.md`, `README.md`, `docs/README.md`, `diagramas/README.md` |

## A divisão do trabalho

| Pacote | Classes | Responsável | Issue |
|---|---|---|---|
| 1 — Pessoas e catálogo | `Usuario`, `Cliente`, `Categoria`, `Ferramenta` | Lincoln | [#21](https://github.com/kassiods/ToolRent/issues/21) |
| 2 — Reserva e locação | `Reserva`, `Locacao`, `ItemLocacao` | Victor | [#22](https://github.com/kassiods/ToolRent/issues/22) |
| 3 — Devolução, pagamento e manutenção | `Devolucao`, `Pagamento`, `Manutencao` | Nailan | [#23](https://github.com/kassiods/ToolRent/issues/23) |
| 4 — Confirmar a migration | — | Kassio | [#24](https://github.com/kassiods/ToolRent/issues/24) |

4, 3 e 3 classes. A divisão por integrante é **proposta** — a equipe pode trocar quem pega o quê;
o que não deve mudar é a fronteira entre os pacotes, porque é ela que evita conflito de merge.

**Os três podem escrever em paralelo justamente porque o diagrama existe:** ele fixa nome, tipo e
multiplicidade de tudo que um referencia do outro. O que precisa de ordem é o merge — o pacote 2
não compila sem as classes do 1.

## Defeito encontrado — o Trello não renderiza tabelas Markdown

Ao abrir o quadro no navegador, os 16 cartões criados na sessão anterior mostravam a tabela de
rastreabilidade como **texto cru**: `| Issue |`, `|---|---|`, pipe por pipe. O Markdown do Trello
não suporta tabelas, e isso não aparecia pela API — o `desc` volta exatamente como foi enviado.

As 16 descrições foram reescritas em lista. Lição: **conteúdo enviado por API precisa ser
conferido na interface onde vai ser lido.**

## Decisões tomadas

- **Diagrama de classes com 10 classes, não as 12 do guia mestre.** `Perfil` virou enumeração
  (não tem atributo nem ciclo de vida próprio) e `Funcionario` não existe (`Usuario` com perfil
  ATENDENTE ou ADMINISTRADOR cumpre o papel). Continua dentro do intervalo de 9 a 13 exigido, e
  as duas divergências estão escritas no diagrama para a equipe confirmar na revisão
- **O diagrama seguiu o rascunho da migration**, não o contrário — mas com as suposições dela
  agora declaradas em vez de implícitas
- **Os quatro cartões nasceram na lista 📐 Diagrama**, não em "Pronto para a sprint": o PR dos
  diagramas está em revisão, então é exatamente aí que o trabalho está

## O que foi rejeitado

- **Decidir as pendências #1 e #5 para fechar o diagrama.** O modelo foi desenhado de forma a
  valer nos dois cenários, com a dependência anotada em cada ponto afetado. `CLAUDE.md` regra 2
- **Implementar as classes Java agora.** O diagrama está em revisão; implementar antes do merge
  violaria o ADR 0010 na primeira sprint em que ele vale
- **Dividir por camada** (um faz domínio, outro faz repositório, outro faz serviço). Criaria
  dependência total entre os três e nada entregável isoladamente. A divisão é por fatia do modelo

## Achado — a migration já assumia uma decisão não tomada

`locacao.reserva_id` estava anulável, com o comentário *"nulo em atendimento direto"*. Isso
**pressupõe a decisão #5 aprovada** (locação presencial sem reserva prévia), e ela está em
🟡 Proposta desde 27/10.

O diagrama manteve a multiplicidade `Reserva "0..1" → Locacao`, mas agora com a dependência
declarada em três lugares: no diagrama de classes, no plano da sprint e no cartão do TT04. Se o
ADR 0007 for rejeitado, a multiplicidade vira `"1" → "0..1"`.

## Verificação

```
cd api && ./mvnw --batch-mode clean verify   ->  BUILD SUCCESS (exit 0)
cd app && flutter analyze                    ->  No issues found!
cd app && flutter test                       ->  All tests passed! (3 testes)
Blocos Mermaid dos 3 diagramas novos         ->  sintaxe consistente, cercas balanceadas
classes.md: nós usados x declarados          ->  15 declarados (10 classes + 5 enums), 0 órfãos
Classes do diagrama x tabelas da migration   ->  10 x 10, correspondência um a um
Etiquetas do quadro                          ->  6 nomeadas, conferidas na interface
```

**Não verificado:** a renderização dos três diagramas novos na página do GitHub — a conferência
exigida pelo checklist de `diagramas/README.md` só pode ser feita depois do push. Foi feita a
verificação estática.

## Erros cometidos pela IA nesta sessão

1. **Marquei as Issues #22 e #23 com a etiqueta `decisao-pendente`** e, no mesmo texto, escrevi
   que elas **não** estão bloqueadas. A etiqueta significa "bloqueada por decisão não resolvida" —
   as duas coisas não podem ser verdade. Etiqueta removida; a pendência ficou só no corpo, onde
   está qualificada
2. **Um heredoc com acentuação e crases quebrou no shell** e o arquivo não foi criado na primeira
   tentativa. Refeito com a ferramenta de escrita direta

## Pendências

- [ ] **Mandar os e-mails dos três** para eu adicioná-los como membros do quadro e atribuir os
      cartões — hoje o responsável está no título e na descrição, não como membro do Trello
- [ ] Revisar e aprovar os três diagramas neste PR. **A implementação não começa antes do merge**
- [ ] Confirmar as duas divergências em relação ao guia mestre: `Perfil` como enum, sem `Funcionario`
- [ ] Confirmar os campos cadastrais de `Cliente` (pendência #9 do guia mestre)
- [ ] Criar o projeto Supabase — segue inexistente
- [ ] Aprovar ou rejeitar os ADRs 0005, 0006 e 0007
- [ ] Desenhar `sequencia-uc05.md` (Sprint 4) e `sequencia-uc09.md` (Sprint 5) — antes do código delas
