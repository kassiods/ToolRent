# Product Backlog — ToolRent

> **Versão:** 2.0 · 27/10/2026
> As histórias abaixo são a fonte da verdade do escopo. Cada uma vira uma Issue no GitHub com o ID no título: `[US07] Solicitar reserva`.

---

## Como ler este documento

| Campo | Significado |
|---|---|
| **ID** | Identificador estável. **Nunca é renumerado** — documentação, diagramas e commits dependem dele. |
| **Prioridade** | `P0` = necessária para a primeira fatia funcional · `P1` = importante, entra depois do fluxo principal |
| **Estimativa** | Story points em escala de Fibonacci (1, 2, 3, 5, 8, 13) |
| **UC** | Caso de uso que realiza a história, especificado em [`casos-de-uso.md`](casos-de-uso.md) |

> ⚠️ **A US08 foi restaurada nesta versão.** A versão 1.0 do documento de casos de uso saltava de US07 para US09, embora o UC08 já a referenciasse como origem. A lacuna está corrigida.

---

## Visão geral

| ID | História | Prioridade | Estimativa | UC | Sprint |
|---|---|---|---|---|---|
| [US01](#us01) | Autenticar-se no sistema | P0 | 5 | UC01 | 3 |
| [US02](#us02) | Gerenciar usuários e perfis | P0 | 5 | UC02 | 3 |
| [US03](#us03) | Cadastrar e consultar clientes | P0 | 3 | UC03 | 4 |
| [US04](#us04) | Cadastrar ferramentas e categorias | P0 | 5 | UC04 | 4 |
| [US05](#us05) | Consultar disponibilidade por período | P0 | 8 | UC05 | 4 |
| [US06](#us06) | Consultar catálogo e preços | P0 | 3 | UC06 | 4 |
| [US07](#us07) | Solicitar reserva | P0 | 5 | UC07 | 5 |
| [US08](#us08) | Aprovar ou recusar reservas | P0 | 5 | UC08 | 5 |
| [US09](#us09) | Formalizar locação | P0 | 8 | UC09 | 5 |
| [US10](#us10) | Registrar retirada | P0 | 3 | UC10 | 5 |
| [US11](#us11) | Registrar devolução e condição | P0 | 8 | UC11 | 6 |
| [US12](#us12) | Calcular valores e encargos | P0 | 13 | UC09, UC11, UC12 | 6 |
| [US13](#us13) | Registrar pagamentos | P1 | 5 | UC13 | 6 |
| [US14](#us14) | Gerenciar manutenções | P1 | 5 | UC14 | 7 |
| [US15](#us15) | Consultar relatórios | P1 | 8 | UC15 | 7 |
| [US16](#us16) | Consultar histórico do cliente | P1 | 3 | UC16 | 7 |

**Total:** 16 histórias · 88 pontos · 10 em P0, 6 em P1

---

## Especificação das histórias

---

### US01

> **Como** usuário, **quero** autenticar-me **para** acessar as funcionalidades autorizadas ao meu perfil.

**Prioridade:** P0 · **Estimativa:** 5 · **UC:** UC01 · **RN:** RN09, RN10

**Critérios de aceite**

- [ ] Credenciais válidas concedem acesso e identificam o perfil do usuário
- [ ] Credenciais inválidas exibem mensagem genérica, sem revelar se o e-mail existe
- [ ] Usuário inativo tem o acesso bloqueado com mensagem específica
- [ ] A senha nunca trafega nem é armazenada em texto puro
- [ ] O menu apresenta apenas as opções permitidas ao perfil autenticado
- [ ] Uma requisição à API sem token válido é rejeitada com HTTP 401

**Tarefas técnicas** — configurar Supabase Auth · tela de login no Flutter · validação de JWT na API · filtro de autorização por perfil

---

### US02

> **Como** administrador, **quero** gerenciar usuários e perfis **para** controlar o acesso ao sistema.

**Prioridade:** P0 · **Estimativa:** 5 · **UC:** UC02 · **RN:** RN09, RN10, RN11

**Critérios de aceite**

- [ ] O administrador lista, cadastra, edita e inativa usuários
- [ ] Cada usuário tem exatamente um perfil associado
- [ ] E-mail duplicado é rejeitado com mensagem clara
- [ ] Senhas nunca são exibidas, nem mesmo ao administrador
- [ ] Usuários com histórico são inativados, não excluídos
- [ ] O administrador não consegue inativar a própria conta
- [ ] Usuário sem perfil de administrador recebe HTTP 403 nesses endpoints

---

### US03

> **Como** atendente, **quero** cadastrar e consultar clientes **para** registrar quem utiliza o serviço.

**Prioridade:** P0 · **Estimativa:** 3 · **UC:** UC03 · **RN:** RN09, RN11

**Critérios de aceite**

- [ ] Cadastro, edição e busca de clientes por nome ou documento
- [ ] Campos obrigatórios são validados antes de salvar
- [ ] Documento duplicado gera alerta e oferece abrir o cadastro existente
- [ ] Cliente com locações no histórico não pode ser excluído, apenas inativado
- [ ] Os dados de demonstração estão identificados como fictícios

---

### US04

> **Como** atendente, **quero** cadastrar ferramentas e categorias **para** manter o catálogo atualizado.

**Prioridade:** P0 · **Estimativa:** 5 · **UC:** UC04 · **RN:** RN04, RN09, RN11, RN12

**Critérios de aceite**

- [ ] Cadastro, edição e inativação de ferramentas com nome, código, descrição, categoria, tarifa diária e quantidade
- [ ] Cadastro e edição de categorias
- [ ] Código de ferramenta é único; duplicado é rejeitado informando o conflito
- [ ] Tarifa menor ou igual a zero é rejeitada
- [ ] Categoria com ferramentas associadas não pode ser excluída
- [ ] Ferramenta com histórico é inativada, não excluída
- [ ] Alterar a tarifa não altera o valor de locações já formalizadas

---

### US05

> **Como** atendente, **quero** consultar a disponibilidade por período **para** evitar conflitos de locação.

**Prioridade:** P0 · **Estimativa:** 8 · **UC:** UC05 · **RN:** RN01, RN02, RN03

**Critérios de aceite**

- [ ] A consulta recebe ferramenta, data inicial e data final e devolve a quantidade disponível
- [ ] Data inicial maior ou igual à final é rejeitada
- [ ] Locações ativas que se sobrepõem reduzem a disponibilidade
- [ ] Reservas bloqueantes que se sobrepõem reduzem a disponibilidade
- [ ] Ferramenta em manutenção aparece como indisponível no período
- [ ] O resultado informa o motivo da indisponibilidade
- [ ] Testes cobrem sobreposição parcial no início, no fim, total e sem sobreposição

> **Depende da decisão pendente #2:** se uma reserva `SOLICITADA` bloqueia ou não a disponibilidade.

---

### US06

> **Como** cliente, **quero** consultar ferramentas, categorias e preços **para** escolher o que preciso.

**Prioridade:** P0 · **Estimativa:** 3 · **UC:** UC06 · **RN:** RN09

**Critérios de aceite**

- [ ] O catálogo lista ferramentas ativas com nome, descrição, categoria e tarifa diária
- [ ] Filtro por categoria e busca por nome funcionam
- [ ] Busca sem resultados exibe mensagem orientando limpar filtros
- [ ] Ferramentas inativas não aparecem
- [ ] A tela de detalhe mostra as informações completas do item

---

### US07

> **Como** cliente, **quero** solicitar uma reserva **para** tentar garantir uma ferramenta em determinado período.

**Prioridade:** P0 · **Estimativa:** 5 · **UC:** UC07 · **RN:** RN01, RN02, RN03, RN08

**Critérios de aceite**

- [ ] A solicitação recebe ferramenta, período e quantidade
- [ ] A disponibilidade é verificada antes de registrar
- [ ] Período inválido é rejeitado com mensagem clara
- [ ] Indisponibilidade impede o registro e informa o conflito
- [ ] O resumo com valor estimado é exibido antes da confirmação
- [ ] A reserva é criada no estado `SOLICITADA`

---

### US08

> **Como** atendente, **quero** aprovar ou recusar reservas **para** controlar a demanda e a disponibilidade.

**Prioridade:** P0 · **Estimativa:** 5 · **UC:** UC08 · **RN:** RN02, RN03, RN08, RN09

**Critérios de aceite**

- [ ] A fila lista todas as reservas em estado `SOLICITADA`
- [ ] A tela exibe cliente, ferramenta, período, quantidade e valor estimado
- [ ] A disponibilidade é revalidada no momento da análise
- [ ] Aprovar muda o estado para `APROVADA`; recusar muda para `RECUSADA`
- [ ] Recusa exige justificativa
- [ ] Reserva já processada não aceita nova decisão
- [ ] Autor e data da decisão ficam registrados

---

### US09

> **Como** atendente, **quero** formalizar uma locação **para** registrar o acordo com o cliente.

**Prioridade:** P0 · **Estimativa:** 8 · **UC:** UC09 · **RN:** RN01–RN04, RN08, RN11, RN12

**Critérios de aceite**

- [ ] A locação pode partir de uma reserva aprovada ou de atendimento direto
- [ ] A disponibilidade é revalidada antes de confirmar
- [ ] O valor previsto é calculado e exibido com memória de cálculo
- [ ] Cada item grava a tarifa vigente no momento da formalização
- [ ] A locação é criada no estado `APROVADA`
- [ ] Falha ao persistir reverte a transação inteira, sem registro parcial
- [ ] Ferramenta em manutenção bloqueia a formalização

---

### US10

> **Como** atendente, **quero** registrar a retirada **para** marcar o início efetivo da locação.

**Prioridade:** P0 · **Estimativa:** 3 · **UC:** UC10 · **RN:** RN03, RN08, RN09

**Critérios de aceite**

- [ ] Apenas locações `APROVADA` aceitam retirada
- [ ] Data da retirada e condição inicial de cada item são registradas
- [ ] O estado muda para `EM_ANDAMENTO`
- [ ] Tentar retirar locação em outro estado é bloqueado com mensagem do estado atual

---

### US11

> **Como** atendente, **quero** registrar a devolução **para** encerrar a locação e atualizar a disponibilidade.

**Prioridade:** P0 · **Estimativa:** 8 · **UC:** UC11 · **RN:** RN04–RN06, RN08, RN11, RN12

**Critérios de aceite**

- [ ] Apenas locações `EM_ANDAMENTO` ou `ATRASADA` aceitam devolução
- [ ] Data efetiva e condição física de cada item são registradas
- [ ] Danos são registrados com descrição e valor estimado
- [ ] Os encargos são calculados e exibidos antes da confirmação
- [ ] O estado muda para `DEVOLVIDA`
- [ ] As ferramentas voltam a ficar disponíveis
- [ ] Devolução de locação já devolvida é bloqueada

> **Depende da decisão pendente #6:** se devolução parcial é permitida.

---

### US12

> **Como** atendente, **quero** calcular valores de aluguel, atraso e danos **conforme** regras configuradas.

**Prioridade:** P0 · **Estimativa:** 13 · **UC:** UC09, UC11, UC12 · **RN:** RN04, RN05, RN06, RN12

**Critérios de aceite**

- [ ] O valor-base usa tarifa diária, quantidade e diárias faturáveis
- [ ] O atraso só é cobrado quando a devolução ocorre após o prazo
- [ ] Um mesmo período nunca é cobrado duas vezes
- [ ] Danos só entram no total depois de aprovados
- [ ] Todos os cálculos usam `BigDecimal`, nunca `double`
- [ ] A precisão e o arredondamento são definidos em um único lugar do código
- [ ] A memória de cálculo é exibida linha a linha
- [ ] Nenhuma taxa ou percentual aparece como número mágico no código
- [ ] Testes cobrem: 1 dia, vários dias, devolução no prazo, 1 dia de atraso, vários dias de atraso e arredondamento

> **Maior história do backlog.** Depende das decisões pendentes #3 e #4. Considere quebrá-la em sub-tarefas na Sprint 6.

---

### US13

> **Como** atendente, **quero** registrar pagamentos **para** acompanhar valores pagos e pendentes.

**Prioridade:** P1 · **Estimativa:** 5 · **UC:** UC13 · **RN:** RN07, RN09, RN11, RN12

**Critérios de aceite**

- [ ] A tela exibe total devido, total pago e saldo
- [ ] Valor, data e forma de pagamento são registrados
- [ ] Valor menor ou igual a zero é rejeitado
- [ ] Pagamento parcial é aceito e o saldo permanece aberto
- [ ] Pagamento acima do saldo segue a política definida
- [ ] Todo pagamento referencia uma locação existente

---

### US14

> **Como** atendente, **quero** registrar defeitos e manutenções **para** manter os equipamentos seguros e disponíveis.

**Prioridade:** P1 · **Estimativa:** 5 · **UC:** UC14 · **RN:** RN03, RN08, RN09, RN11

**Critérios de aceite**

- [ ] Abertura de manutenção com motivo e descrição
- [ ] A ferramenta fica indisponível enquanto a manutenção estiver aberta
- [ ] Conclusão registra o resultado da avaliação
- [ ] A ferramenta só volta ao catálogo após conclusão com avaliação apta
- [ ] Ferramenta em locação ativa não aceita abertura de manutenção
- [ ] Ferramenta irrecuperável é inativada, preservando o histórico

---

### US15

> **Como** administrador, **quero** consultar relatórios de locações e receitas **para** acompanhar a operação.

**Prioridade:** P1 · **Estimativa:** 8 · **UC:** UC15 · **RN:** RN01, RN09, RN12

**Critérios de aceite**

- [ ] Relatório de locações por período
- [ ] Relatório de receitas por período
- [ ] Relatório de ferramentas mais locadas
- [ ] Filtros por período são validados
- [ ] Período sem dados exibe mensagem apropriada
- [ ] Apenas o perfil administrador acessa os relatórios

> **Depende da decisão pendente #7:** quais relatórios são obrigatórios no MVP.

---

### US16

> **Como** atendente, **quero** consultar o histórico de locações de um cliente **para** verificar ocorrências anteriores.

**Prioridade:** P1 · **Estimativa:** 3 · **UC:** UC16 · **RN:** RN09

**Critérios de aceite**

- [ ] Busca de cliente e exibição de suas locações
- [ ] Cada locação mostra itens, datas, estado e pagamentos
- [ ] Atrasos e danos anteriores aparecem destacados
- [ ] Cliente sem histórico exibe mensagem apropriada

---

## Qualidade das histórias

Antes de entrar numa sprint, toda Issue precisa conter:

1. A história no formato **Como… quero… para…**
2. Descrição e contexto
3. Critérios de aceite objetivos e verificáveis
4. Regras de negócio relacionadas
5. Caso(s) de uso relacionado(s)
6. Diagrama(s) afetado(s)
7. Tarefas técnicas
8. Testes esperados

Os critérios completos estão em [`agil/definition-of-ready.md`](agil/definition-of-ready.md).

**Uma história não está concluída porque o código foi escrito.** Ver [`agil/definition-of-done.md`](agil/definition-of-done.md).
