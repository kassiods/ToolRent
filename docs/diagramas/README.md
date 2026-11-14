# Diagramas — ToolRent

> **Regra de ouro da disciplina:** o código entregue implementa exatamente o que o diagrama especifica.
> Diagrama e implementação são atualizados **no mesmo Pull Request**. Nunca um sem o outro.
>
> **O diagrama vem primeiro.** Nenhuma história vai para implementação antes de o diagrama dela
> estar aprovado em PR — ver [ADR 0010](../decisoes/0010-diagrama-antes-da-implementacao.md)
> e a coluna 📐 do [quadro no Trello](https://trello.com/b/m3gakhP8/toolrent-backlog).

---

## Quais diagramas — o conjunto do livro

A disciplina usa como referência o livro **[Engenharia de Software Moderna](https://engsoftmoderna.info/)**,
de Marco Tulio Valente — ver [ADR 0015](../decisoes/0015-diagramas-conforme-engenharia-de-software-moderna.md).

O repertório do projeto são os **cinco diagramas que o livro apresenta**. O autor cobre quatro no
capítulo 4 de propósito, por entender que são *"os 20% dos recursos responsáveis por 80% do uso
prático"*; o quinto está no capítulo 3:

| Diagrama | Capítulo | Para que serve |
|---|---|---|
| Casos de uso | [3.4.1](https://engsoftmoderna.info/cap3.html) | A fronteira do sistema: atores e o que cada um faz |
| Classes | [4](https://engsoftmoderna.info/cap4.html) | Classes, atributos, métodos e relacionamentos |
| Pacotes | [4](https://engsoftmoderna.info/cap4.html) | Alto nível: grupos de classes e dependências |
| Sequência | [4](https://engsoftmoderna.info/cap4.html) | Um cenário: a sequência de chamadas entre objetos |
| Atividades | [4](https://engsoftmoderna.info/cap4.html) | Processos e fluxos de execução em alto nível |

**Diagrama fora dessa lista só entra com justificativa escrita no próprio arquivo.**

> **Exceção registrada:** os diagramas de estados da `Reserva` e da `Locacao` não estão entre os
> cinco. Foram mantidos porque especificam RN08 — as transições válidas e as proibidas —, já
> estão aprovados, e TT01/TT02 dependem deles. Máquina de estados é UML legítima; o livro não a
> cobre por recorte de escopo, não por rejeição. Ver [ADR 0015](../decisoes/0015-diagramas-conforme-engenharia-de-software-moderna.md).

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
├── sprint-2/     pacotes · atividades · sequência · classes · ciclos de vida
├── sprint-3/     sequência de UC01 — autenticação e autorização por perfil
├── sprint-4/     (a criar) sequência de UC05
└── sprint-5/     (a criar) sequência de UC09
```

**Toda sprint tem os seus diagramas, e a implementação da sprint segue o que eles especificam.**
A pasta diz em que entrega o diagrama **nasceu**, não em que sprint ele é implementado; um
diagrama continua valendo depois da sprint dele — o de casos de uso, da Sprint 1, é referência até
o fim do projeto.

| Nasceu na | Diagrama | Tipo | Arquivo | Implementado em | Status |
|---|---|---|---|---|---|
| 1 | Casos de uso do ToolRent | **casos de uso** | [`sprint-1/casos-de-uso.md`](sprint-1/casos-de-uso.md) | referência permanente | ✅ |
| 2 | Camadas de `app/` e `api/` | **pacotes** | [`sprint-2/pacotes.md`](sprint-2/pacotes.md) | Sprint 2 | ✅ |
| 2 | Da abertura do app à tela inicial | **atividades** | [`sprint-2/atividades-tela-inicial.md`](sprint-2/atividades-tela-inicial.md) | Sprint 2 | ✅ |
| 2 | Abrir tela inicial e acionar o menu | **sequência** | [`sprint-2/sequencia-abrir-tela-inicial.md`](sprint-2/sequencia-abrir-tela-inicial.md) | Sprint 2 | ✅ |
| 2 | Classes de domínio | **classes** | [`sprint-2/classes.md`](sprint-2/classes.md) | **Sprint 3** | ✅ |
| 2 | Estados da Reserva | estados ⚠️ | [`sprint-2/estados-reserva.md`](sprint-2/estados-reserva.md) | **Sprint 3** | ✅ |
| 2 | Estados da Locação | estados ⚠️ | [`sprint-2/estados-locacao.md`](sprint-2/estados-locacao.md) | **Sprint 3** | ✅ |
| 3 | Autenticar e autorizar por perfil (UC01) | **sequência** | [`sprint-3/sequencia-uc01.md`](sprint-3/sequencia-uc01.md) | Sprint 3 | ✅ |
| 4 | Consultar disponibilidade (UC05) | **sequência** | `sprint-4/sequencia-uc05.md` | Sprint 4 | ⬜ |
| 5 | Formalizar locação (UC09) | **sequência** | `sprint-5/sequencia-uc09.md` | Sprint 5 | ⬜ |

⚠️ fora dos cinco do livro — exceção justificada acima.

> O diagrama de classes e os dois de estados nasceram na Sprint 2 e continuam nesta pasta, mas a
> implementação deles passou para a Sprint 3 quando a Sprint 2 foi redefinida como entrega de
> front-end — ver [`../sprints/sprint-2.md`](../sprints/sprint-2.md). O diagrama não se move de
> pasta por isso.

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
| Pacotes | `flowchart` com `subgraph` como pacote e `-.->` como dependência | [flowchart](https://mermaid.js.org/syntax/flowchart.html) |
| Atividades | `flowchart TD` — retângulo é ação, losango é decisão | [flowchart](https://mermaid.js.org/syntax/flowchart.html) |
| Estados | `stateDiagram-v2` | [stateDiagram](https://mermaid.js.org/syntax/stateDiagram.html) |
| Sequência | `sequenceDiagram` | [sequenceDiagram](https://mermaid.js.org/syntax/sequenceDiagram.html) |
| Entidade-relacionamento | `erDiagram` | [erDiagram](https://mermaid.js.org/syntax/entityRelationshipDiagram.html) |

Mermaid não tem diagrama de casos de uso nativo. A convenção adotada aqui é `flowchart LR`:
atores como retângulos à esquerda, casos de uso como caixas arredondadas dentro de um
`subgraph` que representa a fronteira do sistema, `«include»` como seta tracejada.

Mermaid também não tem diagrama de pacotes nativo. Mesma saída: `subgraph` faz o papel do pacote
(que o livro desenha como retângulo com aba) e `-.->` faz o papel da seta tracejada de
dependência.

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
- **Sem acento nem caractere especial dentro do bloco Mermaid** — rótulo e nota em ASCII, como
  nos diagramas já entregues. O texto acentuado vai nas tabelas de fora, onde é Markdown comum
- Em `stateDiagram-v2`, **um nível de aninhamento** e nenhuma transição saindo de um estado que
  está dentro de um composto: as duas coisas são o que costuma falhar na renderização do GitHub

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
