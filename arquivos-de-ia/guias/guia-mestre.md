# TOOLRENT — Guia Mestre do Projeto
## Documento de referência para a equipe e para assistentes de IA no VS Code

**Projeto acadêmico:** Análise e Projeto de Sistemas  
**Professor:** Anthony Irlan Marques Luz  
**Nome provisório do sistema:** ToolRent — Sistema de Locação de Ferramentas  
**Repositório sugerido:** `toolrent`  
**Linguagem e tecnologias obrigatórias:** Java 21, JavaFX, Maven, SQLite (`sqlite-jdbc`), JDBC, Git e GitHub  
**Idioma do projeto e da documentação:** português brasileiro  
**Situação deste documento:** plano inicial. Decisões marcadas como “proposta” devem ser confirmadas pela equipe e podem ser ajustadas antes da implementação.

---

# 1. Instrução principal para qualquer IA que trabalhe neste repositório

Você é um assistente de desenvolvimento que está ajudando a equipe a construir o ToolRent para uma disciplina de Análise e Projeto de Sistemas. Sua responsabilidade é seguir este documento, preservar a coerência entre requisitos, diagramas, código, testes e documentação e explicar as mudanças de forma compreensível.

## Regras inegociáveis

1. **Leia primeiro este arquivo e os documentos relacionados** antes de sugerir ou alterar código. Procure também `README.md`, `docs/`, diagramas, Issues e o código já existente.
2. **Não invente decisões como se fossem aprovadas.** Quando este documento disser “proposta”, trate como hipótese inicial. Se uma decisão afetar requisitos, modelo de domínio, estados, persistência ou arquitetura e não puder ser deduzida com segurança, pergunte à equipe antes de implementá-la.
3. **Não reescreva o projeto inteiro sem necessidade.** Inspecione o estado atual, identifique a menor alteração coerente e preserve código funcional.
4. **Não altere arquivos fora do escopo solicitado.** Antes de uma refatoração grande, explique o motivo e peça confirmação.
5. **Não adicione bibliotecas, frameworks, serviços externos ou padrões complexos sem necessidade e sem informar a equipe.** A disciplina exige Java 21, JavaFX, Maven, SQLite e JDBC.
6. **O diagrama é uma especificação, não uma decoração.** O código deve implementar o comportamento descrito nos requisitos e diagramas aprovados. Se o código e o diagrama divergirem, sinalize a divergência e proponha a atualização correspondente; não deixe a inconsistência escondida.
7. **Toda funcionalidade deve ser rastreável.** Relacione-a a uma história de usuário (`USxx`), caso de uso (`UCxx`), Issue do GitHub, diagrama relevante e testes, quando aplicável.
8. **Não declare que algo funciona sem verificar.** Execute ou indique os comandos de compilação e testes. Diferencie claramente “implementado”, “testado” e “ainda não verificado”.
9. **Nunca remova dados reais, o banco local ou alterações dos colegas sem autorização explícita.** Não execute comandos destrutivos de Git nem sobrescreva trabalho alheio.
10. **Não inclua senhas, tokens, dados pessoais reais ou segredos no código, nos logs, nos exemplos ou no Git.** Use dados fictícios para demonstrações.
11. **Prefira código simples, legível, orientado a objetos e adequado ao nível de um projeto acadêmico.** Não crie abstrações só por elegância.
12. **Responda em português brasileiro**, com instruções práticas e caminhos de arquivos explícitos.
13. Se estiver trabalhando com apenas parte do repositório disponível, informe essa limitação e não presuma o conteúdo de arquivos que não leu.
14. Se a solicitação conflitar com este documento ou com o enunciado do professor, explique o conflito e priorize o enunciado da disciplina.
15. Não crie telas ou funcionalidades só para “parecer completo”. Cada elemento deve atender a uma necessidade do sistema.

## Antes de modificar código, responda internamente

- Qual história de usuário e caso de uso esta mudança atende?
- Qual regra de negócio está envolvida?
- Quais classes, diagramas, testes e documentos precisam acompanhar a mudança?
- A alteração respeita a arquitetura existente?
- Como será verificada?
- Há alguma decisão pendente que exige perguntar à equipe?

Se faltarem informações importantes, faça perguntas objetivas antes de assumir decisões irreversíveis.

---

# 2. Equipe

| Integrante | Usuário GitHub informado |
|---|---|
| Kassio de Sousa Dias | `kassiods` |
| Lincoln Gustavo de Brito Soares | `Lincoln16yyy` |
| Victor Rennan Alves Sátiro | `Rennan-aex` |
| Nailan Lucas dos Santos Nobre | `Nailan-Nobre` |

**Professor:** Anthony Irlan Marques Luz.  
**Usuário GitHub do professor:** ainda precisa ser confirmado; não inventar.

**IDE principal:** Visual Studio Code.  
**Ambiente informado pela equipe:** Java 21, Maven e JavaFX. A equipe ainda deve confirmar que cada instalação está funcional no terminal local.

Não incluir e-mails, senhas, tokens nem outros dados pessoais no documento.

---

# 3. Visão do produto

O ToolRent será um sistema desktop para apoiar a operação de uma pequena empresa de locação de ferramentas e equipamentos utilizados, por exemplo, em construção civil, manutenção e serviços técnicos. O sistema permitirá gerenciar clientes, funcionários e usuários, manter o catálogo de ferramentas, consultar disponibilidade por período, solicitar e decidir reservas, formalizar locações, registrar retirada e devolução, controlar pagamentos, calcular valores devidos, registrar manutenção e consultar relatórios.

O sistema deve ajudar a evitar reservas conflitantes, locação de ferramentas indisponíveis ou em manutenção, erros de cálculo e falta de histórico operacional.

**Escopo inicial:** aplicação desktop JavaFX, banco de dados SQLite em arquivo local e persistência por JDBC. Não presumir que haverá aplicativo web, aplicativo móvel, nuvem, API remota, envio de e-mail ou gateway de pagamento. Essas integrações só entram se a equipe e o professor aprovarem explicitamente.

---

# 4. Restrições acadêmicas obrigatórias

De acordo com o enunciado da disciplina compartilhado pela equipe:

- Equipe de até 4 integrantes.
- Tema livre, desde que cumpra a complexidade mínima.
- De 3 a 6 atores/perfis de usuário.
- De 12 a 20 casos de uso.
- No máximo metade dos casos de uso pode ser CRUD puro.
- De 9 a 13 classes de domínio.
- Pelo menos uma entidade com ciclo de vida de quatro ou mais estados.
- Pelo menos duas regras de negócio com cálculo ou validação.
- Autenticação e perfis de acesso obrigatórios.
- Java 21.
- Interface gráfica à escolha; proposta: JavaFX.
- SQLite via `sqlite-jdbc`, com arquivo local criado pelo próprio sistema na primeira execução.
- JDBC para persistência.
- Maven, com execução por `mvn javafx:run`.
- Git e GitHub; Issues ou GitHub Projects como Product Backlog.
- A cada semana, entregar diagrama UML e código que implementa exatamente o que o diagrama especifica.
- Cada commit de entrega deve ser rastreável até um elemento de diagrama.
- Entrega da semana 1: repositório, documento de casos de uso e diagrama de casos de uso.
- O repositório deve ter README, pasta `docs/`, integrantes e professor como colaboradores, histórias priorizadas e projeto Maven inicial executando uma tela JavaFX vazia.
- O documento de casos de uso deve conter visão geral, atores, casos de uso identificados e rastreabilidade até as histórias de usuário.
- O diagrama de casos de uso deve ter fronteira do sistema, todos os atores e todos os casos de uso listados no documento. Usar `<<include>>` e `<<extend>>` apenas quando a relação for real.
- O modelo de especificação detalhado é o “modelo do livro didático mostrado em sala”. O arquivo do enunciado não apresenta os campos detalhados desse modelo; pedir os slides ou exemplo ao professor/equipe antes de afirmar que o formato foi confirmado.

**Regra de ouro:** não criar diagramas que não correspondam ao código entregue e não implementar funcionalidades que contradigam os diagramas aprovados.

---

# 5. Perfis de usuário propostos

A autenticação será feita por usuário e senha. Cada usuário terá um perfil e as ações devem ser autorizadas na camada de serviço, não apenas escondidas na interface.

## 5.1 Administrador
- Gerencia usuários e perfis.
- Consulta relatórios gerais.
- Acompanha o catálogo, locações, pagamentos e manutenção.
- Não deve visualizar ou alterar senhas em texto puro.

## 5.2 Atendente
- Cadastra e consulta clientes.
- Consulta ferramentas e disponibilidade.
- Analisa reservas.
- Abre locações e registra retirada/devolução.
- Registra pagamentos e consulta histórico conforme permissões.

## 5.3 Cliente
- Consulta ferramentas e preços.
- Solicita reservas.
- Acompanha suas reservas e locações.
- Não pode consultar ou modificar dados de outros clientes.

**Decisão de interface pendente:** como o produto é desktop, a equipe precisa definir se o perfil Cliente terá login no mesmo aplicativo ou se as ações de cliente serão realizadas por um atendente em nome dele. Não implementar duas experiências distintas sem aprovação. Para a primeira versão acadêmica, recomenda-se manter os três perfis no mesmo aplicativo, com menus conforme o perfil, e confirmar essa decisão com a equipe/professor.

## 5.4 Técnico de manutenção
- Consulta ferramentas indisponíveis ou com defeito.
- Registra manutenção, diagnóstico e conclusão.
- Libera equipamento para locação somente após conclusão válida da manutenção.

**Observação:** os quatro perfis cumprem o intervalo de 3 a 6 atores. O diagrama pode representar os perfis como atores distintos. O usuário autenticado deve ter uma associação inequívoca com seu perfil.

## Sistema externo
Nenhum sistema externo é necessário no escopo inicial. Não adicionar gateway de pagamento ou serviço de e-mail por padrão.

---

# 6. Histórias de usuário iniciais (Product Backlog)

Os IDs abaixo são estáveis. Se forem cadastrados no GitHub, colocar o ID no título da Issue, por exemplo: `[US01] Autenticar usuário`. Não renumerar IDs já usados em documentação, diagramas ou commits; novas histórias recebem novos IDs.

| ID | História | Prioridade inicial |
|---|---|---|
| US01 | Como usuário, quero autenticar-me para acessar as funções permitidas ao meu perfil. | P0 |
| US02 | Como administrador, quero gerenciar usuários e perfis para controlar o acesso ao sistema. | P0 |
| US03 | Como atendente, quero cadastrar e consultar clientes para registrar quem utiliza o serviço. | P0 |
| US04 | Como atendente, quero cadastrar e consultar ferramentas para manter o catálogo atualizado. | P0 |
| US05 | Como atendente, quero consultar a disponibilidade por período para evitar conflitos de locação. | P0 |
| US06 | Como cliente, quero consultar ferramentas, categorias e preços para escolher o que preciso. | P0 |
| US07 | Como cliente, quero solicitar uma reserva para tentar garantir uma ferramenta em determinado período. | P0 |
| US08 | Como atendente, quero aprovar ou recusar reservas para controlar a demanda e a disponibilidade. | P0 |
| US09 | Como atendente, quero formalizar uma locação aprovada para registrar o acordo com o cliente. | P0 |
| US10 | Como atendente, quero registrar a retirada para marcar o início efetivo da locação. | P0 |
| US11 | Como atendente, quero registrar a devolução para encerrar a locação e atualizar a disponibilidade. | P0 |
| US12 | Como atendente, quero calcular valores de aluguel, atraso e danos conforme regras configuradas. | P0 |
| US13 | Como atendente, quero registrar pagamentos para acompanhar valores pagos e pendentes. | P1 |
| US14 | Como técnico, quero registrar defeitos e manutenções para manter os equipamentos seguros e disponíveis. | P1 |
| US15 | Como administrador, quero consultar relatórios de locações e receitas para acompanhar a operação. | P1 |
| US16 | Como atendente, quero consultar o histórico de locações de um cliente para verificar ocorrências anteriores. | P1 |

**P0:** necessário para a primeira fatia funcional acordada pela equipe.  
**P1:** importante, mas pode entrar após o fluxo principal, conforme cronograma e orientação do professor.

As prioridades são uma proposta inicial, não uma determinação do professor. A equipe deve ajustar o backlog ao plano semanal de entregas.

## Critérios de qualidade das histórias

Cada Issue deve conter:
- História de usuário.
- Descrição e contexto.
- Critérios de aceitação objetivos.
- Regras de negócio relacionadas.
- Caso(s) de uso relacionado(s), quando definidos.
- Diagrama(s) afetado(s).
- Tarefas técnicas.
- Testes esperados.
- Critério claro de conclusão.

Não considere uma Issue concluída apenas porque o código foi escrito. Deve compilar, ser testada conforme o escopo e ter documentação/diagramas atualizados.

---

# 7. Casos de uso candidatos e rastreabilidade

Os casos abaixo são uma proposta inicial para chegar a 16 casos de uso, cobrindo mais do que CRUD. Refinar após discutir os fluxos com a equipe e conferir o modelo usado em aula.

| ID | Caso de uso | Ator principal | Histórias relacionadas |
|---|---|---|---|
| UC01 | Autenticar usuário | Usuário | US01 |
| UC02 | Gerenciar usuários e perfis | Administrador | US02 |
| UC03 | Gerenciar clientes | Atendente | US03 |
| UC04 | Gerenciar ferramentas | Atendente | US04 |
| UC05 | Consultar disponibilidade por período | Atendente | US05 |
| UC06 | Consultar catálogo e preços | Cliente | US06 |
| UC07 | Solicitar reserva | Cliente | US07 |
| UC08 | Analisar reserva | Atendente | US08 |
| UC09 | Formalizar locação | Atendente | US09 |
| UC10 | Registrar retirada de ferramentas | Atendente | US10 |
| UC11 | Registrar devolução e avaliar condição | Atendente | US11, US12 |
| UC12 | Calcular valores e encargos da locação | Atendente | US12 |
| UC13 | Registrar pagamento | Atendente | US13 |
| UC14 | Registrar e concluir manutenção | Técnico de manutenção | US14 |
| UC15 | Emitir/consultar relatórios operacionais e financeiros | Administrador | US15 |
| UC16 | Consultar histórico de locações do cliente | Atendente | US16 |

Não classificar automaticamente todos os casos como CRUD. UC05, UC07, UC08, UC09, UC10, UC11, UC12, UC14 e UC15 contêm regras, validações ou fluxos de negócio. A classificação definitiva deve ser feita pela equipe com base nos fluxos especificados.

## Formato de especificação de cada caso de uso

Até a equipe fornecer o modelo exato do livro/slides, usar este esqueleto clássico apenas como rascunho:

- Identificador e nome.
- Objetivo.
- Ator principal.
- Atores secundários, se existirem.
- História(s) de usuário de origem.
- Pré-condições.
- Gatilho.
- Fluxo principal numerado.
- Fluxos alternativos e exceções.
- Regras de negócio relacionadas.
- Pós-condições/sucesso.
- Garantias mínimas em caso de falha.
- Diagrama(s) relacionados.
- Testes de aceitação relacionados.

Não afirmar que este é o modelo exato do professor sem conferir o material de aula.

---

# 8. Regras de negócio propostas

As regras seguintes são hipóteses de projeto. Confirmar os valores exatos com a equipe antes de codificá-los. Não deixar percentuais arbitrários escondidos no código.

## RN01 — Período válido
A data/hora inicial de uma reserva ou locação deve ser anterior à data/hora final. Períodos inválidos devem ser rejeitados com mensagem compreensível.

## RN02 — Disponibilidade e conflitos
Uma ferramenta não pode ser reservada ou locada para um período que se sobreponha a outra reserva aprovada ou locação ativa que a torne indisponível. Definir formalmente como reservas pendentes afetam ou não a disponibilidade.

## RN03 — Ferramenta em manutenção
Uma ferramenta em manutenção, danificada ou marcada como indisponível não pode ser incluída em uma nova locação. Ela só volta a ficar disponível após a conclusão da manutenção e validação de sua condição.

## RN04 — Cálculo base do aluguel
Para cada item, calcular o preço base a partir da tarifa acordada, quantidade e duração da locação. A equipe deve definir se a cobrança usa dias corridos, períodos de 24 horas, diárias iniciadas ou outro critério. Não assumir essa regra sem registrá-la.

**Fórmula candidata, se aprovada:**  
`subtotalItem = tarifaDiaria × quantidade × quantidadeDeDiariasCobradas`  
`valorBase = soma dos subtotais dos itens`

## RN05 — Atraso
A multa por atraso só deve ser calculada quando a devolução ocorrer depois do prazo acordado. A equipe deve decidir se há multa fixa, percentual, tarifa por diária de atraso ou combinação, e como arredondar valores monetários.

**Fórmula candidata, se aprovada:**  
`multaAtraso = diariasDeAtraso × tarifaDeMultaPorDiaria × quantidadeAplicavel`

Não contar duas vezes o mesmo período e não usar números mágicos no código.

## RN06 — Danos
Danos identificados na devolução devem ser registrados com descrição e valor estimado/aprovado, conforme a política que a equipe definir. Não calcular valores de dano automaticamente sem uma regra aprovada.

## RN07 — Pagamento
O sistema deve registrar valor, data, forma de pagamento e referência à locação. O total pago não pode ultrapassar o valor devido sem uma regra explícita para adiantamento, crédito ou reembolso. Definir como tratar pagamento parcial.

## RN08 — Transições de estado
Uma locação só pode mudar para estados permitidos. Por exemplo, uma locação recusada não pode ser retirada; uma locação já devolvida não pode ser devolvida novamente. As transições devem ser validadas na camada de serviço.

## RN09 — Autorização
Ações sensíveis exigem autenticação e perfil autorizado. A interface pode ocultar ações não permitidas, mas a validação obrigatória deve existir também na camada de serviço.

## RN10 — Senhas
Nunca armazenar senha em texto puro. Usar uma abordagem de hash apropriada para senhas, com salt e parâmetros seguros, sem criar algoritmo criptográfico próprio. A implementação escolhida deve ser compatível com as dependências aprovadas pela equipe.

## RN11 — Identificadores e integridade
Usar chaves primárias e estrangeiras no SQLite. Habilitar a verificação de chaves estrangeiras em cada conexão (`PRAGMA foreign_keys = ON`). Evitar excluir registros históricos que estejam referenciados por locações, pagamentos ou manutenções; preferir inativação quando fizer sentido.

## RN12 — Dinheiro e datas
Usar `BigDecimal` para valores monetários, evitando `double` para cálculos financeiros. Definir precisão e arredondamento em um único lugar. Armazenar datas em formato consistente e converter explicitamente entre persistência e apresentação.

---

# 9. Ciclo de vida da locação

Entidade principal: `Locacao`.

Estados candidatos:
1. `SOLICITADA`
2. `APROVADA`
3. `EM_ANDAMENTO`
4. `DEVOLVIDA`
5. `RECUSADA`
6. `CANCELADA`
7. `ATRASADA`

**Atenção:** os estados são propostas. Definir as transições válidas no diagrama de estados e implementar exatamente essas transições. `ATRASADA` pode ser um estado derivado da data de vencimento ou um estado persistido; a equipe deve escolher uma abordagem e documentá-la. Não misturar os dois modelos.

Transições candidatas:
- `SOLICITADA -> APROVADA`
- `SOLICITADA -> RECUSADA`
- `SOLICITADA -> CANCELADA` (se a política permitir)
- `APROVADA -> EM_ANDAMENTO` (na retirada)
- `APROVADA -> CANCELADA` (se permitido pela política)
- `EM_ANDAMENTO -> DEVOLVIDA`
- `EM_ANDAMENTO -> ATRASADA` (se atraso for estado persistido)
- `ATRASADA -> DEVOLVIDA`

Não permitir transições ilegais. Especificar o que acontece com a reserva, disponibilidade, pagamentos e histórico em cada transição.

---

# 10. Modelo de domínio proposto (12 classes)

Manter entre 9 e 13 classes de domínio, conforme o enunciado. A lista abaixo é inicial; não criar classes extras de domínio sem necessidade e sem verificar o limite. Classes técnicas de UI, DAO, serviço, configuração e utilitários não são classes de domínio.

| Classe | Responsabilidade e dados conceituais |
|---|---|
| `Usuario` | Identificação de login, hash da senha, situação ativa e vínculo com perfil. |
| `Perfil` | Nome do perfil e permissões conceituais. |
| `Cliente` | Dados necessários para identificar e contatar o cliente. Usar dados fictícios nos testes. |
| `Funcionario` | Dados do funcionário e vínculo com usuário, se aprovado no modelo. |
| `Ferramenta` | Nome, descrição, código, tarifa, situação e categoria. |
| `Categoria` | Nome e descrição da categoria. |
| `Reserva` | Cliente, período pretendido, itens/quantidades conforme decisão de modelagem, estado e datas. |
| `Locacao` | Cliente, datas, estado, itens, valores e dados do contrato. |
| `ItemLocacao` | Ferramenta, quantidade e tarifa registrada no momento da locação. |
| `Pagamento` | Locação, valor, data, forma e referência. |
| `Devolucao` | Locação, data, condição observada, descrição de danos e encargos aprovados. |
| `Manutencao` | Ferramenta, abertura, descrição, estado, conclusão e técnico responsável. |

## Relações candidatas

Confirmar com o diagrama de classes. Como hipótese:
- `Usuario` está associado a um `Perfil`.
- `Funcionario` pode estar associado a `Usuario`.
- `Ferramenta` pertence a uma `Categoria`.
- `Reserva` pertence a um `Cliente`.
- `Locacao` pertence a um `Cliente` e possui um ou mais `ItemLocacao`.
- Cada `ItemLocacao` referencia uma `Ferramenta`.
- `Pagamento` referencia uma `Locacao`.
- `Devolucao` referencia uma `Locacao`.
- `Manutencao` referencia uma `Ferramenta` e o técnico responsável.

A lista atual não define se uma reserva terá uma classe de item própria. Se a necessidade aparecer, rever o limite de classes com a equipe antes de adicionar uma classe de domínio. Não representar listas de ferramentas como texto separado por vírgulas no banco.

---

# 11. Arquitetura técnica recomendada

Usar uma arquitetura em camadas simples para separar interface, regras de negócio e persistência.

## Estrutura candidata

```text
toolrent/
├── .github/
│   └── ISSUE_TEMPLATE/
├── .gitignore
├── README.md
├── pom.xml
├── docs/
│   ├── regras-do-projeto.md
│   ├── casos-de-uso.md
│   ├── backlog.md
│   ├── decisoes/
│   └── diagramas/
│       ├── casos-de-uso.puml
│       ├── casos-de-uso.png
│       ├── classes.puml
│       ├── estados-locacao.puml
│       └── ...
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/edu/toolrent/
│   │   │       ├── MainApp.java
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── util/
│   │   │       └── view/
│   │   └── resources/
│   │       ├── fxml/
│   │       ├── css/
│   │       └── database/
│   └── test/
│       └── java/
│           └── br/edu/toolrent/
└── .vscode/
```

Esta estrutura é uma proposta e pode ser ajustada conforme a implementação real. Não criar todas as pastas vazias sem necessidade.

## Responsabilidade das camadas

- `model`: entidades do domínio e tipos de valor.
- `view`/`controller`: telas JavaFX e interação com o usuário; não devem conter SQL nem concentrar regras de negócio.
- `service`: regras de negócio, validações, autorização e coordenação de operações.
- `repository`: acesso ao SQLite usando JDBC e consultas parametrizadas.
- `config`: criação e configuração de conexões, incluindo chaves estrangeiras.
- `util`: somente utilitários genéricos realmente necessários.
- `resources/fxml` e `resources/css`: arquivos de interface e estilos.
- `test`: testes automatizados.

Não introduzir Spring, Hibernate, JPA, servidor web ou banco remoto sem autorização da equipe/professor. A persistência solicitada é JDBC com SQLite.

## Regras para JDBC e SQLite

- Usar `PreparedStatement` para valores fornecidos pelo usuário.
- Fechar `Connection`, `PreparedStatement` e `ResultSet` usando `try-with-resources`.
- Não concatenar entrada do usuário em SQL.
- Usar transações para operações que alteram várias tabelas e precisam ser atômicas.
- Habilitar `PRAGMA foreign_keys = ON` em cada conexão.
- Criar as tabelas na primeira execução por meio de inicialização idempotente do banco.
- Não apagar ou recriar o banco a cada inicialização.
- Manter SQL de inicialização/migração organizado e documentado.
- Não executar consultas demoradas na thread da interface JavaFX.
- Exibir mensagens compreensíveis ao usuário e registrar detalhes técnicos sem expor senhas ou dados sensíveis.
- Evitar DAO genérico excessivamente complexo; prefira repositórios pequenos e claros.

## Regras para JavaFX

- Manter a interface responsiva.
- Separar layout de lógica quando FXML for usado.
- Validar os campos na camada de serviço, além de validações visuais.
- Não confiar apenas em botões desabilitados para garantir regras.
- Não abrir múltiplas janelas duplicadas sem necessidade.
- Tratar falhas de persistência com mensagens adequadas.
- Usar CSS externo para estilos quando isso melhorar a organização.
- Evitar criar uma interface elaborada antes de o fluxo principal estar funcionando.

---

# 12. Padrões de código

1. Usar nomes de classes em `PascalCase`, métodos e variáveis em `camelCase`, constantes em `UPPER_SNAKE_CASE`.
2. Usar nomes e mensagens em português de forma consistente, preservando termos técnicos padrão quando fizer sentido.
3. Uma classe deve ter uma responsabilidade principal.
4. Evitar métodos muito grandes e duplicação de regras.
5. Não colocar regra de cálculo em FXML/controller nem SQL dentro da entidade.
6. Não capturar exceções silenciosamente. Tratar, registrar ou propagar de forma adequada.
7. Não usar `System.out.println` como estratégia definitiva de logging da aplicação.
8. Usar `BigDecimal` para dinheiro.
9. Usar enumerações para estados e formas fixas quando apropriado.
10. Não codificar permissões ou taxas em vários lugares. Centralizar regras aprovadas.
11. Não adicionar dependências Maven sem necessidade; explique o motivo e atualize a documentação.
12. Não incluir arquivos gerados de build (`target/`) nem o banco local de dados reais no Git.
13. Usar dados de demonstração somente quando identificados claramente como fictícios.
14. Evitar comentários que apenas repetem o código; documentar decisões e regras não óbvias.
15. Não usar `catch (Exception)` para esconder falhas ou retornar sucesso falso.

---

# 13. Testes e critérios de aceitação

Cada funcionalidade deve ter critérios verificáveis. Priorizar testes de unidade para serviços e regras de negócio e testes de integração para repositórios quando viável.

## Casos importantes para testar

- Login correto, senha incorreta, usuário inativo e perfil sem permissão.
- Datas inválidas e períodos sobrepostos.
- Ferramenta em manutenção não pode ser locada.
- Reserva concorrente para o mesmo período.
- Transições válidas e inválidas de `Locacao`.
- Cálculo de aluguel em diferentes durações e quantidades.
- Cálculo de atraso, devolução no prazo e arredondamento.
- Pagamento parcial e total pendente.
- Integridade entre locação, itens, pagamentos, devolução e ferramentas.
- Falha de banco sem deixar operação parcialmente gravada.

Não marcar teste como aprovado sem executá-lo. Informar quais comandos foram executados e o resultado.

---

# 14. Estratégia de entregas semanais

O calendário oficial e o conteúdo exato de cada semana devem vir do professor. O roteiro abaixo é uma sugestão de organização, não substitui o cronograma oficial.

## Fase 0 — Preparação
- Confirmar integrantes e acesso ao repositório.
- Criar repositório privado ou público conforme orientação do professor.
- Adicionar os quatro integrantes e o professor como colaboradores.
- Confirmar o usuário GitHub do professor.
- Criar Maven com Java 21, JavaFX e `sqlite-jdbc`.
- Garantir que `mvn javafx:run` abre uma tela vazia.
- Criar README, `.gitignore`, `docs/` e este arquivo.

## Semana 1 — Casos de uso
- Confirmar tema, atores, histórias e prioridades.
- Criar Issues iniciais no GitHub.
- Escrever `docs/casos-de-uso.md` no modelo pedido em aula.
- Criar diagrama de casos de uso com todos os atores e todos os casos documentados.
- Guardar fonte e imagem do diagrama em `docs/diagramas/`.
- Verificar rastreabilidade de cada caso de uso até as histórias de origem.
- Fazer revisão da equipe antes da entrega.

## Fases seguintes — ordem técnica sugerida
1. Refinar requisitos e regras de negócio.
2. Diagrama de classes de domínio e relacionamentos.
3. Modelo de dados SQLite e criação inicial do banco.
4. Autenticação, perfil e autorização.
5. Catálogo e clientes.
6. Consulta de disponibilidade e reservas.
7. Locação, retirada e ciclo de estados.
8. Devolução, cálculo de valores e pagamentos.
9. Manutenção e indisponibilidade.
10. Relatórios e histórico.
11. Testes de integração, revisão dos diagramas e documentação.
12. Verificação final da execução limpa e preparação da entrega.

A ordem real deve acompanhar o diagrama e a orientação semanal do professor. Não adiantar funcionalidades em código sem que o escopo e o diagrama da entrega tenham sido acordados.

## Checklist antes de cada entrega

- [ ] Issue da funcionalidade identificada.
- [ ] História e critérios de aceitação revisados.
- [ ] Diagrama correspondente atualizado.
- [ ] Código implementa o diagrama aprovado.
- [ ] Testes relevantes executados.
- [ ] Compilação Maven verificada.
- [ ] Documento e README atualizados se necessário.
- [ ] Nenhum segredo ou arquivo local indevido foi adicionado.
- [ ] Pull request revisado por pelo menos um colega, quando o fluxo da equipe permitir.
- [ ] Commit/PR/Issue/diagrama possuem rastreabilidade.
- [ ] Evidência de execução preparada se solicitada pelo professor.

---

# 15. Git e colaboração em equipe

## Fluxo recomendado

- `main`: versão estável e pronta para entrega.
- Uma branch curta por Issue, por exemplo `feat/US07-solicitar-reserva` ou `fix/UC11-devolucao`.
- Abrir Pull Request para integrar na `main`.
- Referenciar Issue no PR, por exemplo `Closes #12`, quando aplicável.
- Pelo menos um colega deve revisar alterações de impacto em regras, banco ou arquitetura.
- Resolver conflitos manualmente após entender as alterações; nunca escolher “accept all” sem revisar.
- Fazer commits pequenos, com mensagens claras.

## Exemplos de mensagens de commit

- `docs: documenta atores e casos de uso`
- `feat: adiciona consulta de disponibilidade`
- `fix: impede reserva com período conflitante`
- `test: cobre cálculo de multa por atraso`
- `refactor: separa regra de locação do controller`

Não afirmar que o commit está ligado a um diagrama se essa ligação não estiver registrada no PR, Issue ou documentação.

## Cuidados de segurança do Git

Antes de commit:
- verificar `git status`;
- revisar `git diff`;
- verificar se não há `.db` pessoal, senha, token, configuração local ou arquivo gerado;
- nunca usar `git push --force` em branch compartilhada sem coordenação explícita;
- não executar `git reset --hard`, remoção de branches ou limpeza destrutiva sem autorização;
- não sobrescrever alterações de colegas.

---

# 16. Repositório inicial e comandos

## Comandos de verificação

No terminal integrado do VS Code, na raiz que contém `pom.xml`:

```bash
java -version
mvn -version
mvn clean test
mvn javafx:run
```

A equipe deve verificar se `java -version` aponta para Java 21 e se o Maven utiliza o mesmo JDK esperado. Se o projeto não compilar, corrigir a causa antes de continuar.

## Extensões recomendadas no VS Code

Instalar o **Extension Pack for Java**, que inclui suporte à linguagem, depuração, testes, Maven e gerenciamento de projetos Java. Para JavaFX, o VS Code também documenta a execução de projetos Maven pelo objetivo `javafx:run`.

## Requisitos do `pom.xml`

O projeto deve:
- definir Java 21 para compilação;
- declarar dependências JavaFX compatíveis entre si;
- declarar `org.xerial:sqlite-jdbc`;
- configurar o plugin JavaFX Maven para executar `mvn javafx:run`;
- definir a classe principal corretamente;
- usar versões explícitas e compatíveis de plugins e dependências;
- evitar dependências desnecessárias.

Não inventar uma combinação de versões sem verificar compatibilidade. Se for editar o `pom.xml`, testar o comando de execução no ambiente disponível e registrar qualquer limitação do sistema operacional.

---

# 17. Definição de pronto (Definition of Done)

Uma história só pode ser considerada pronta quando:
1. Os critérios de aceitação estão satisfeitos.
2. O código está integrado sem quebrar o projeto.
3. A compilação foi verificada.
4. Os testes relevantes foram executados e passaram, ou as limitações estão documentadas.
5. As regras de negócio estão na camada correta.
6. Os diagramas e documentos afetados foram atualizados.
7. A rastreabilidade entre Issue, história, caso de uso, diagrama e código está registrada.
8. Não foram introduzidas dependências ou decisões não aprovadas.
9. Não há credenciais, dados sensíveis ou arquivos de build indevidos no commit.
10. Outro integrante consegue entender como executar e validar a funcionalidade.

---

# 18. Como a IA deve responder ao trabalhar no projeto

Ao receber uma tarefa, use esta estrutura de resposta:

1. **Entendimento:** explique brevemente o que será feito.
2. **Rastreabilidade:** informe US, UC, Issue e diagrama relacionados, se conhecidos.
3. **Arquivos envolvidos:** liste os caminhos exatos.
4. **Plano curto:** apresente a sequência de mudanças.
5. **Implementação:** faça somente o escopo solicitado.
6. **Verificação:** informe os comandos executados e os resultados reais.
7. **Documentação:** indique quais diagramas/documentos foram atualizados ou precisam ser atualizados.
8. **Pendências:** liste decisões que dependem da equipe.

Se a IA tiver acesso ao terminal e ferramentas de edição, poderá executar verificações não destrutivas. Antes de ações destrutivas, mudanças amplas, alterações de banco que possam apagar dados ou modificações de história/arquitetura já aprovada, deve pedir autorização.

**Quando um integrante pedir apenas análise, não altere arquivos.** Quando pedir implementação, faça mudanças concretas e limitadas ao escopo. Quando não conseguir executar os testes, diga explicitamente que não foram executados.

---

# 19. Decisões pendentes para a equipe

Não tratar os pontos abaixo como resolvidos até serem confirmados:

1. Usuário GitHub do professor e confirmação de que ele foi adicionado ao repositório.
2. Se o repositório será público ou privado.
3. Modelo exato de especificação de casos de uso do livro/slides.
4. Se o cliente usará o próprio login no desktop ou se o atendente registrará solicitações em seu nome.
5. Critério de cobrança: diária, período de 24 horas ou outro.
6. Regra exata de multa por atraso e política de danos.
7. Como reservas pendentes afetam a disponibilidade.
8. Se `ATRASADA` será estado persistido ou estado derivado.
9. Quais campos cadastrais são realmente necessários para clientes e funcionários.
10. Calendário oficial das entregas semanais.
11. Versões compatíveis de JavaFX, SQLite JDBC e plugins Maven para o ambiente da equipe.

Quando uma dessas decisões se tornar necessária para implementar a tarefa atual, pergunte à equipe de forma objetiva e ofereça uma recomendação com prós e contras. Não bloqueie trabalho que não dependa da decisão.

---

# 20. Instrução de início para uma IA nova

Ao abrir este projeto pela primeira vez:

1. Leia este documento completo.
2. Inspecione `README.md`, `pom.xml`, `docs/`, código e Git status.
3. Informe em poucas linhas o estado real do projeto e as diferenças em relação a este plano.
4. Não presuma que o roteiro foi implementado apenas porque está descrito aqui.
5. Se o repositório estiver vazio ou não existir, comece pela estrutura Maven mínima exigida e pela entrega da semana 1.
6. Se já houver implementação, preserve o que está funcionando e faça um plano incremental.
7. Antes de codificar uma funcionalidade, confirme a Issue e o diagrama correspondente.
8. Para qualquer entrega, verifique a compilação e, quando possível, execute os testes e `mvn javafx:run`.
9. Termine informando arquivos alterados, verificações feitas, limitações e próximo passo recomendado.

**Objetivo final:** entregar um sistema desktop coerente e executável, com documentação acadêmica, diagramas UML fiéis ao código, regras de negócio testáveis, persistência local e histórico de desenvolvimento rastreável pelo GitHub.
