# ADR 0008 — Mermaid no lugar do PlantUML

- **Data:** 07/11/2026
- **Status:** ✅ Aceita — **recomendação do professor**
- **Decidido por:** professor Anthony Irlan Marques Luz, registrada pela equipe

---

## Contexto

Os diagramas UML do projeto foram iniciados em **PlantUML**: a fonte `.puml` era versionada e a imagem (`.jpg`/`.png`) era gerada à parte e commitada junto.

O professor recomendou **Mermaid** como notação dos diagramas.

Além da recomendação, o arranjo anterior já tinha produzido um defeito concreto no repositório: a fonte `casos-de-uso.puml` e a imagem `casos-de-uso.jpg` são **dois artefatos que precisam ser atualizados juntos**, e nada garante isso. Na Sprint 1 o status do diagrama divergiu entre `sprints.md` e `diagramas/README.md` justamente porque fonte e imagem são coisas separadas (corrigido no PR #17).

O PlantUML também exigia Java instalado, extensão de VS Code ou um servidor online para gerar a imagem — atrito para quem só queria ler o diagrama.

## Decisão

**Todo diagrama do projeto é escrito em Mermaid, dentro de um bloco ` ```mermaid ` em um arquivo `.md` em `docs/diagramas/`.**

O GitHub renderiza o bloco direto na página do arquivo. Logo:

- **A fonte é o diagrama publicado.** Não há imagem a gerar nem a commitar.
- Revisar diagrama em Pull Request passa a ser ler um `git diff` de texto e olhar a prévia renderizada.
- Não é preciso Java, PlantUML nem extensão para ler ou editar.

Convenção para casos de uso, já que Mermaid não tem esse tipo de diagrama nativo: `flowchart LR`, atores como retângulos, casos de uso como caixas arredondadas dentro de um `subgraph` que é a fronteira do sistema, `«include»` como seta tracejada rotulada.

As convenções completas estão em [`docs/diagramas/README.md`](../diagramas/README.md).

## Alternativas consideradas

### A — Manter PlantUML
- ✅ Diagrama de casos de uso é nativo, com elipses e `<<include>>` canônicos
- ✅ Mais fiel à notação UML do livro
- ❌ **Contraria a recomendação do professor**
- ❌ Fonte e imagem são dois arquivos que divergem
- ❌ Exige Java ou serviço externo para gerar a imagem
- ❌ A imagem não é revisável em PR

### B — Mermaid ← **escolhida**
- ✅ Recomendação do professor
- ✅ Renderiza no GitHub: um único artefato, sem imagem derivada
- ✅ Zero ferramenta para instalar
- ✅ Revisável em PR como qualquer texto
- ✅ `classDiagram`, `stateDiagram-v2` e `sequenceDiagram` cobrem bem os diagramas das Sprints 2 e 5
- ❌ Não tem diagrama de casos de uso nativo — exige a convenção com `flowchart`
- ❌ Caso de uso vira caixa arredondada, não elipse; menos canônico visualmente

### C — Os dois, em paralelo
- ❌ Duas fontes para o mesmo diagrama é exatamente o problema que se quer eliminar
- ❌ Dobra o trabalho de manutenção e garante divergência

## Consequências

**Positivas**

- Um diagrama = um arquivo. Impossível a imagem ficar atrasada em relação à fonte
- Quem abre o repositório no GitHub vê o diagrama sem baixar nada
- O checklist de entrega "fonte do diagrama versionada" é satisfeito trivialmente

**Negativas**

- O diagrama de casos de uso fica visualmente menos parecido com o do livro didático
- Se a avaliação exigir um arquivo de imagem anexado, será preciso exportar manualmente pela prévia do GitHub ou pelo [mermaid.live](https://mermaid.live)

**Riscos**

- A convenção de `flowchart` para casos de uso pode ser lida como "não é UML". Mitigação: a legenda e a tabela de relações em [`casos-de-uso.md`](../diagramas/casos-de-uso.md) deixam explícito o que cada elemento representa.

## O que foi feito nesta decisão

| Ação | Arquivo |
|---|---|
| Diagrama de casos de uso convertido | `docs/diagramas/casos-de-uso.md` (novo) |
| Fonte PlantUML removida | `docs/diagramas/casos-de-uso.puml` (recuperável no histórico) |
| Imagem da Sprint 1 arquivada, não mais mantida | `docs/diagramas/casos-de-uso.jpg` |
| Guia de diagramas reescrito para Mermaid | `docs/diagramas/README.md` |

Os diagramas ainda não escritos — classes, estados e sequência — nascem já em Mermaid na Sprint 2.
