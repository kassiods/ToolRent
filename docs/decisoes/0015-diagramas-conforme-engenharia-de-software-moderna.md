# ADR 0015 — Diagramas e especificação conforme *Engenharia de Software Moderna*

- **Data:** 07/11/2026
- **Status:** ✅ Aceita
- **Decidido por:** professor Anthony Irlan Marques Luz, informado à equipe em 07/11/2026

---

## Contexto

A **decisão #8** era a última pendência do projeto, aberta desde o guia mestre:

> *O professor exige um modelo específico de especificação textual de caso de uso?*

Ela não era escolha de equipe — só o professor poderia respondê-la, e por isso ficou em aberto
enquanto as outras sete viravam ADR. A resposta chegou: o material de referência da disciplina é
o livro **[Engenharia de Software Moderna](https://engsoftmoderna.info/)**, de Marco Tulio
Valente (UFMG), disponível on-line de forma gratuita.

Até aqui a equipe escolhia o tipo de diagrama por conta própria, caso a caso. Isso produziu
diagramas corretos em UML mas fora do conjunto que a disciplina cobre — e, pior, deixou de fora
três dos cinco que o livro apresenta.

## Decisão

**Os diagramas do ToolRent seguem o conjunto apresentado no livro, e a especificação textual de
caso de uso segue o modelo do capítulo 3.**

### Os diagramas

O capítulo 4 (*Modelos*) apresenta **quatro** diagramas, deliberadamente — o autor afirma ter
selecionado *"os 20% (ou menos) dos recursos responsáveis por 80% (ou mais) do uso prático"*. O
capítulo 3 (*Requisitos*) apresenta o quinto:

| Diagrama | Capítulo | Para que serve |
|---|---|---|
| Casos de uso | 3.4.1 | A fronteira do sistema: atores e o que cada um pode fazer |
| Classes | 4 | Classes, atributos, métodos e relacionamentos |
| Pacotes | 4 | Visão de alto nível: grupos de classes e dependências |
| Sequência | 4 | Comportamento dinâmico de um cenário: a sequência de chamadas |
| Atividades | 4 | Processos e fluxos de execução em alto nível |

**Esses cinco são o repertório do projeto.** Diagrama fora dessa lista só entra com justificativa
escrita no próprio arquivo.

### A especificação textual de caso de uso

O modelo do capítulo 3 é enxuto:

| Campo | Obrigatório |
|---|---|
| Nome — verbo no infinitivo | sim |
| Ator principal | sim |
| Fluxo normal — passos numerados do cenário de sucesso | sim |
| Extensões — alternativas e tratamento de erro | sim |
| Propósito, pré-condições, pós-condições, casos relacionados | opcionais |

O [`casos-de-uso.md`](../casos-de-uso.md) atual já contém **todos os campos obrigatórios** e
vários dos opcionais — é um superconjunto do modelo do livro, não um formato divergente. **Não
há reescrita a fazer**, apenas o alinhamento de nomenclatura: o que o documento chama *"Fluxos
alternativos e exceções"* o livro chama **Extensões**.

## Consequências

### O que passa a existir

Faltavam três dos cinco diagramas. Os três entram na Sprint 2, um por integrante:

| Diagrama | Arquivo | Sprint |
|---|---|---|
| Pacotes | [`sprint-2/pacotes.md`](../diagramas/sprint-2/pacotes.md) | 2 |
| Atividades | [`sprint-2/atividades-tela-inicial.md`](../diagramas/sprint-2/atividades-tela-inicial.md) | 2 |
| Sequência | [`sprint-2/sequencia-abrir-tela-inicial.md`](../diagramas/sprint-2/sequencia-abrir-tela-inicial.md) | 2 |

### O que foi removido

Os três diagramas de navegação criados mais cedo nesta mesma data
(`navegacao-administrador.md`, `navegacao-atendente.md`, `navegacao-cliente.md`) **não são
nenhum dos cinco** — eram máquinas de estado adaptadas para descrever telas. O conteúdo útil
deles (itens de menu, ícones, rastreabilidade a cada UC) foi preservado dentro do diagrama de
atividades e do plano da sprint; os arquivos saíram.

Eles continuam recuperáveis no histórico do Git.

### O que permanece, com ressalva

Os diagramas de estados da [`Reserva`](../diagramas/sprint-2/estados-reserva.md) e da
[`Locacao`](../diagramas/sprint-2/estados-locacao.md) **também não estão entre os cinco** do
livro. Diferente dos de navegação, eles foram **mantidos**:

- Especificam RN08 — as transições válidas e as proibidas — que é regra de negócio testável
- Já estão aprovados e mergeados, e TT01/TT02 dependem deles
- Máquina de estados é UML legítima; o livro não a cobre por recorte de escopo, não por rejeição

A ressalva está registrada no [`README` dos diagramas](../diagramas/README.md).

### Sobre a notação

**O Mermaid continua** — o [ADR 0008](0008-mermaid-no-lugar-do-plantuml.md) segue valendo, e a
troca de PlantUML por Mermaid foi recomendação do próprio professor. O livro não prescreve
ferramenta; prescreve quais diagramas desenhar.

O Mermaid não tem diagrama de pacotes nativo, então `pacotes.md` usa `flowchart` com `subgraph` —
a mesma convenção já adotada no diagrama de casos de uso, documentada no arquivo.

## Alternativas descartadas

**Reescrever os 16 casos de uso no formato mínimo do livro.** O documento atual é um superconjunto
do modelo: tem os quatro campos obrigatórios e mais alguns opcionais que o próprio livro prevê.
Reescrever removeria informação útil para ganhar conformidade que já existe.

**Apagar os diagramas de estados.** São a especificação de RN08 e estão mergeados. O livro não os
cobrir não os torna errados.

**Manter os diagramas de navegação em paralelo aos do livro.** Duas fontes descrevendo a mesma
navegação é exatamente o defeito que a migração para Mermaid resolveu na Sprint 1.

## Uma tensão que vale registrar

O livro é explicitamente cético quanto a UML como *blueprint*: o autor defende diagramas como
**esboços informais** para comunicação, critica *big design up front* em contexto ágil, e observa
que gerar código a partir de modelos *"não se tornou comum"*.

Já o enunciado da disciplina — e o [ADR 0010](0010-diagrama-antes-da-implementacao.md) — exigem
correspondência entre diagrama e código, e o diagrama **antes** do código.

**Não há contradição a resolver aqui, e sim uma precedência:** o que vale para a entrega é o
enunciado. Os diagramas do ToolRent são mais detalhados do que o livro recomenda para o dia a dia
da indústria, de propósito, porque a disciplina avalia a correspondência. Vale a equipe saber
disso para não generalizar a prática do trabalho para fora dele.

## Referências

- [Engenharia de Software Moderna](https://engsoftmoderna.info/) — Marco Tulio Valente, UFMG
- [Capítulo 3 — Requisitos](https://engsoftmoderna.info/cap3.html) — casos de uso e o modelo textual
- [Capítulo 4 — Modelos](https://engsoftmoderna.info/cap4.html) — os quatro diagramas UML
