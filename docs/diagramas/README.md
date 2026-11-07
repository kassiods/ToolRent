# Diagramas — ToolRent

> **Regra de ouro da disciplina:** o código entregue implementa exatamente o que o diagrama especifica.
> Diagrama e implementação são atualizados **no mesmo Pull Request**. Nunca um sem o outro.
>
> **O diagrama vem primeiro.** Nenhuma história vai para implementação antes de o diagrama dela
> estar aprovado em PR — ver [ADR 0010](../decisoes/0010-diagrama-antes-da-implementacao.md)
> e a coluna 📐 do [quadro no Trello](https://trello.com/b/m3gakhP8/toolrent-backlog).

---

## Notação: Mermaid

Os diagramas são escritos em **Mermaid**, por recomendação do professor — ver [ADR 0008](../decisoes/0008-mermaid-no-lugar-do-plantuml.md).

A fonte fica em um bloco ```` ```mermaid ```` dentro de um arquivo `.md`. **O GitHub renderiza o bloco direto na página**, então não existe imagem a gerar nem ferramenta a instalar: a fonte *é* o diagrama publicado.

Isso resolve o problema que o PlantUML tinha aqui: antes, a fonte `.puml` e a imagem `.jpg` eram dois arquivos que podiam divergir — e divergiram na Sprint 1.

---

## Organização: uma pasta por sprint

```
docs/diagramas/
├── sprint-1/     casos de uso — a fronteira do sistema
├── sprint-2/     classes de domínio e ciclos de vida
├── sprint-4/     (a criar) sequência de UC05
└── sprint-5/     (a criar) sequência de UC09
```

**Toda sprint tem os seus diagramas, e a implementação da sprint segue o que eles especificam.**
A pasta diz em que entrega o diagrama nasceu; um diagrama continua valendo depois da sprint dele
— o de casos de uso, da Sprint 1, é referência até o fim do projeto.

| Sprint | Diagrama | Arquivo | Status |
|---|---|---|---|
| 1 | Casos de uso | [`sprint-1/casos-de-uso.md`](sprint-1/casos-de-uso.md) | ✅ |
| 2 | Classes de domínio | [`sprint-2/classes.md`](sprint-2/classes.md) | ✅ |
| 2 | Estados da Reserva | [`sprint-2/estados-reserva.md`](sprint-2/estados-reserva.md) | ✅ |
| 2 | Estados da Locação | [`sprint-2/estados-locacao.md`](sprint-2/estados-locacao.md) | ✅ |
| 4 | Sequência — Consultar disponibilidade | `sprint-4/sequencia-uc05.md` | ⬜ |
| 5 | Sequência — Formalizar locação | `sprint-5/sequencia-uc09.md` | ⬜ |

### Ao abrir uma sprint nova

1. Crie `docs/diagramas/sprint-N/`
2. Desenhe os diagramas que a entrega exige — **antes** de qualquer código
3. Abra o PR só do diagrama, e só depois do merge comece a implementar
4. Registre no plano da sprint, em [`../sprints/`](../sprints/), qual diagrama especifica qual pacote

## Como escrever cada tipo

| Diagrama UML | Sintaxe Mermaid | Documentação |
|---|---|---|
| Casos de uso | `flowchart LR` com `subgraph` como fronteira | [flowchart](https://mermaid.js.org/syntax/flowchart.html) |
| Classes | `classDiagram` | [classDiagram](https://mermaid.js.org/syntax/classDiagram.html) |
| Estados | `stateDiagram-v2` | [stateDiagram](https://mermaid.js.org/syntax/stateDiagram.html) |
| Sequência | `sequenceDiagram` | [sequenceDiagram](https://mermaid.js.org/syntax/sequenceDiagram.html) |
| Entidade-relacionamento | `erDiagram` | [erDiagram](https://mermaid.js.org/syntax/entityRelationshipDiagram.html) |

Mermaid não tem diagrama de casos de uso nativo. A convenção adotada aqui é `flowchart LR`:
atores como retângulos à esquerda, casos de uso como caixas arredondadas dentro de um
`subgraph` que representa a fronteira do sistema, `«include»` como seta tracejada.

### Como pré-visualizar

| Onde | Como |
|---|---|
| **GitHub** | Abra o `.md` — já renderiza. É a verificação que vale. |
| **VS Code** | Extensão *Markdown Preview Mermaid Support* (bierner.markdown-mermaid), depois `Ctrl+Shift+V` |
| **Navegador** | [mermaid.live](https://mermaid.live) — cole o conteúdo do bloco |

Não é preciso Java, nem PlantUML, nem gerar PNG.

---

## Convenções

**Nome do arquivo:** kebab-case, descrevendo o conteúdo — `casos-de-uso.md`, `estados-locacao.md`, `sequencia-uc09.md`. **Dentro da pasta da sprint em que nasceu.**

**Dentro do arquivo:**

- Título e nota indicando o documento correspondente
- Comentários `%%` dentro do bloco Mermaid para justificar escolhas de modelagem
- Identificadores dos elementos iguais aos da documentação (`UC01`, não `uc1` nem `autenticar`)
- Apenas relações reais — `«include»` e `«extend»` não são enfeite
- Legenda como tabela Markdown **fora** do bloco (Mermaid não tem legenda)
- Cores por `classDef`, nunca literais espalhados pelos nós

**`«include»` vs `«extend»`:**

| Relação | Quando usar | Exemplo no ToolRent |
|---|---|---|
| `«include»` | O comportamento é **sempre** executado | UC07 sempre consulta disponibilidade (UC05) |
| `«extend»` | O comportamento é **opcional**, sob condição | Nenhum no escopo atual |

Na dúvida, não use nenhuma das duas. Relação inventada é erro de modelagem visível na avaliação.

---

## Checklist antes de commitar um diagrama

- [ ] O bloco Mermaid renderiza no GitHub — conferido na página do arquivo, não só no editor
- [ ] Os atores do diagrama batem **exatamente** com os do documento
- [ ] Todos os casos de uso do documento aparecem no diagrama
- [ ] Nenhum caso de uso no diagrama está ausente do documento
- [ ] A fronteira do sistema está desenhada
- [ ] As relações `«include»`/`«extend»` correspondem a comportamento real
- [ ] A documentação afetada foi atualizada no mesmo PR
- [ ] O cartão correspondente no Trello saiu de 📐 Diagrama

---

## Estado atual — casos de uso

| Verificação | Resultado |
|---|---|
| Atores no documento | Administrador, Atendente, Cliente |
| Atores no diagrama | Administrador, Atendente, Cliente |
| Correspondem? | ✅ Sim |
| Casos de uso no documento | 16 (UC01–UC16) |
| Casos de uso no diagrama | 16 (UC01–UC16) |
| Correspondem? | ✅ Sim |
| Relações `«include»` | 4 — todas reais |
| Relações `«extend»` | 0 |

> **Nota histórica:** a versão 1.0 do documento listava 4 atores, incluindo *Técnico de manutenção*, enquanto o diagrama tinha 3. A divergência foi resolvida removendo o ator — ver [ADR 0002](../decisoes/0002-remocao-do-ator-tecnico.md).
>
> `sprint-1/casos-de-uso.jpg` é a imagem entregue na Sprint 1, gerada da antiga fonte PlantUML.
> **Está arquivada como registro da entrega daquela data e não é mais mantida** — a fonte
> vigente é [`casos-de-uso.md`](../casos-de-uso.md). A fonte `casos-de-uso.puml` foi removida na
> migração para Mermaid e continua recuperável no histórico do Git.
