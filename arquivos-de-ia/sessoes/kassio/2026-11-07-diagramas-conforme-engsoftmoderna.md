# Sessão — Decisão #8 respondida e os diagramas alinhados ao livro do Marco Tulio

- **Data:** 07/11/2026
- **Integrante:** Kassio de Sousa Dias
- **Ferramenta:** Claude Code
- **Relacionado a:** Sprint 2 · ADR 0015 · Issues #26, #27 e #28

---

## Objetivo

O professor respondeu a **decisão #8**: a referência da disciplina é o livro
[Engenharia de Software Moderna](https://engsoftmoderna.info/), de Marco Tulio Valente (UFMG), e
é dele que devem sair os diagramas. Esta sessão: descobrir o que o livro de fato prescreve,
comparar com o que o projeto tem, e corrigir a diferença.

## O que o livro prescreve — lido na fonte, não de memória

| Capítulo | Diagrama |
|---|---|
| [3.4.1](https://engsoftmoderna.info/cap3.html) | Casos de uso |
| [4](https://engsoftmoderna.info/cap4.html) | Classes · **Pacotes** · **Sequência** · **Atividades** |

São **quatro no capítulo 4, de propósito**: o autor afirma ter selecionado *"os 20% (ou menos)
dos recursos responsáveis por 80% (ou mais) do uso prático"*. Mais o de casos de uso, no
capítulo 3. **Cinco no total.**

O capítulo 3 também traz o modelo textual de caso de uso: **nome** (verbo no infinitivo), **ator
principal**, **fluxo normal**, **extensões**; propósito, pré-condições, pós-condições e casos
relacionados como opcionais.

## O diagnóstico

| Diagrama do livro | Tínhamos? |
|---|---|
| Casos de uso | ✅ |
| Classes | ✅ |
| Pacotes | ❌ |
| Sequência | ❌ |
| Atividades | ❌ |

**Faltavam três dos cinco.** E o inverso também era verdade: os três diagramas de navegação
criados mais cedo nesta mesma data não eram nenhum dos cinco — eram máquinas de estado adaptadas
para descrever telas.

## O que foi feito

| Mudança | Onde |
|---|---|
| ADR 0015 — resolve a decisão #8 e fixa o conjunto de diagramas | `docs/decisoes/0015-*.md` |
| Diagrama de **pacotes** — camadas de `app/` e `api/`, e quem depende de quem | `docs/diagramas/sprint-2/pacotes.md` |
| Diagrama de **atividades** — da abertura do app à tela inicial de cada perfil | `docs/diagramas/sprint-2/atividades-tela-inicial.md` |
| Diagrama de **sequência** — abrir a tela e acionar um item do menu | `docs/diagramas/sprint-2/sequencia-abrir-tela-inicial.md` |
| Os três diagramas de navegação removidos | `docs/diagramas/sprint-2/` |
| Plano da Sprint 2 reescrito: cada pacote é um diagrama do livro + o código dele | `docs/sprints/sprint-2.md` |
| Decisão #8 marcada como resolvida; a seção virou "todas resolvidas" | `casos-de-uso.md § 6` |
| Guia de diagramas ganhou a seção "o conjunto do livro", com a exceção registrada | `docs/diagramas/README.md` |
| Issues #26–#28 e os 3 cartões reescritos com o diagrama de cada um | GitHub e Trello |

## A distribuição não mudou de pessoa — mudou de conteúdo

| Pacote | Diagrama do livro | Tela | Responsável |
|---|---|---|---|
| 1 | **Pacotes** | Administrador + casca | Nailan |
| 2 | **Atividades** | Atendente, 10 itens | Lincoln |
| 3 | **Sequência** | Cliente + teste | Victor |

Cada um desenha — ou melhor, **implementa a partir de** — um diagrama do livro. As telas que
cada um já tinha continuam as mesmas; o que mudou é que agora cada tela está amarrada a um
diagrama que a disciplina reconhece.

## Decisões tomadas

- **Os cinco diagramas do livro são o repertório do projeto.** Diagrama fora da lista só entra
  com justificativa escrita no próprio arquivo
- **Os diagramas de navegação saem; os de estados ficam.** Parece incoerente e não é: os de
  navegação eram adaptação para descrever tela, e o diagrama de atividades faz isso melhor e é do
  livro. Já os de estados especificam **RN08** — as transições válidas e as proibidas —, estão
  mergeados e TT01/TT02 dependem deles. A ressalva ficou escrita no ADR e no README
- **Os 16 casos de uso não foram reescritos.** O documento atual tem os quatro campos
  obrigatórios do modelo do livro e vários dos opcionais: é superconjunto, não divergência. A
  única diferença é de nome — *"Fluxos alternativos e exceções"* é o que o livro chama
  **Extensões**
- **O Mermaid continua** (ADR 0008). O livro prescreve *quais* diagramas desenhar, não com que
  ferramenta. Confirmei com o Kassio antes de mexer

## O que foi rejeitado

- **Reescrever os 16 casos de uso no formato mínimo do livro.** Removeria informação útil para
  ganhar uma conformidade que já existe
- **Apagar os diagramas de estados** só por não estarem entre os cinco. São a especificação de
  RN08 e estão aprovados. O livro não os cobrir não os torna errados
- **Manter os diagramas de navegação em paralelo aos novos.** Duas fontes descrevendo a mesma
  navegação é exatamente o defeito que a migração para Mermaid resolveu na Sprint 1
- **Deduzir o conteúdo do livro de memória.** Os capítulos 3 e 4 foram lidos na fonte antes de
  qualquer arquivo ser criado. Se eu tivesse chutado "os diagramas da UML", teria incluído
  componentes e casos de uso de implantação, que o autor deliberadamente deixa de fora

## Achado — uma pasta que contraria o ADR 0004

Ao desenhar o diagrama de pacotes foi preciso conferir a estrutura real contra
`arquitetura.md`. Apareceu uma pasta a mais no app:

```
app/lib/domain/rules/     existe no disco, vazia, e NÃO está na arquitetura
```

Uma pasta chamada *rules* dentro do app Flutter é um convite para colocar ali exatamente o que o
[ADR 0004](../../../docs/decisoes/0004-regra-de-negocio-na-api.md) proíbe. Ela não aparece no
diagrama, e removê-la virou critério de pronto do Pacote 1.

## Uma tensão registrada no ADR, não escondida

O livro é **cético quanto a UML como blueprint**: defende diagramas como esboços informais,
critica *big design up front* e observa que gerar código a partir de modelos *"não se tornou
comum"*. O enunciado da disciplina e o ADR 0010 exigem o oposto — correspondência entre diagrama
e código, e o diagrama antes.

Não há contradição a resolver, e sim precedência: para a entrega vale o enunciado. Está escrito
no ADR 0015 para a equipe não generalizar a prática do trabalho para fora dele.

## Verificação

```
cd api && ./mvnw --batch-mode clean verify   ->  BUILD SUCCESS - 2 testes, 0 falhas
cd app && flutter analyze                    ->  No issues found!
cd app && flutter test                       ->  All tests passed! (3 testes)
Links relativos de todos os .md              ->  0 quebrados (repositorio inteiro)
Referencias orfas a navegacao-*              ->  nenhuma fora do ADR e deste log
Cercas ``` de todos os .md                   ->  todas balanceadas
Blocos Mermaid dos 3 diagramas novos         ->  100% ASCII
Renderizacao no GitHub dos 3 diagramas novos ->  conferida no navegador, na pagina do PR
CI do PR                                     ->  3 jobs verdes
```

Nenhum arquivo de código foi alterado — só documentação, diagramas e acompanhamento.

## Pendências

- [ ] Revisar e mergear o PR desta branch — **a implementação não começa antes disso**
- [ ] Confirmar com o professor que o recorte está certo: cinco diagramas, com os dois de estados
      mantidos como exceção justificada
- [ ] Criar o projeto Supabase do ToolRent quando o back-end entrar
- [ ] Desenhar `sprint-4/sequencia-uc05.md` e `sprint-5/sequencia-uc09.md` — os de sequência com
      `web`, `service` e `repository` como participantes, que só fazem sentido com back-end
- [ ] As pastas de sessão de Lincoln, Victor e Nailan continuam vazias
