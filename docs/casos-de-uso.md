# Documento de Casos de Uso — ToolRent

> **Projeto:** ToolRent — Sistema de Locação de Ferramentas
> **Disciplina:** Análise e Projeto de Sistemas
> **Professor:** Anthony Irlan Marques Luz
> **Equipe:** Kassio de Sousa Dias, Lincoln Gustavo de Brito Soares, Victor Rennan Alves Sátiro, Nailan Lucas dos Santos Nobre
> **Versão:** 2.0 · 27/10/2026

---

## Índice

1. [Visão geral do sistema](#1-visão-geral-do-sistema)
2. [Atores do sistema](#2-atores-do-sistema)
3. [Casos de uso identificados](#3-casos-de-uso-identificados)
4. [Especificação dos casos de uso](#4-especificação-dos-casos-de-uso)
5. [Rastreabilidade](#5-rastreabilidade)
6. [Decisões pendentes](#6-decisões-pendentes)

---

## 1. Visão geral do sistema

O ToolRent é um sistema para apoiar uma empresa de locação de ferramentas no gerenciamento de clientes, usuários, catálogo de ferramentas, reservas, locações, pagamentos e manutenções.

O sistema permite consultar a disponibilidade das ferramentas por período, registrar solicitações de reserva, formalizar locações, controlar retiradas e devoluções, calcular valores e encargos e gerar relatórios operacionais e financeiros. O acesso às funcionalidades é controlado por autenticação e perfis de usuário.

**Tecnologia:** aplicação Flutter (Dart) consumindo uma API REST em Java 21 com Spring Boot, tendo o Supabase (PostgreSQL) como banco de dados e provedor de autenticação. Ver [`arquitetura.md`](arquitetura.md) e [`decisoes/0001-stack-flutter-java-supabase.md`](decisoes/0001-stack-flutter-java-supabase.md).

### Fronteira do sistema

Está **dentro** do escopo: cadastro de usuários, clientes, ferramentas e categorias; consulta de disponibilidade; reservas; locações e seu ciclo de vida; devoluções e avaliação de condição; cálculo de valores e encargos; registro de pagamentos; manutenções; relatórios e histórico.

Está **fora** do escopo: gateway de pagamento real, envio de e-mail ou SMS, integração fiscal (NF-e), rastreamento por GPS e aplicativo público para o consumidor final.

---

## 2. Atores do sistema

| Ator | Descrição |
|---|---|
| **Administrador** | Gerencia usuários e perfis de acesso e consulta relatórios administrativos. |
| **Atendente** | Gerencia clientes, ferramentas e categorias; consulta disponibilidade; analisa reservas; registra locações, retiradas, devoluções, pagamentos e manutenções. |
| **Cliente** | Consulta o catálogo e os preços e solicita reservas. |

> **Mudança em relação à versão 1.0 deste documento:** o ator *Técnico de manutenção* foi removido. O caso de uso UC14 (Gerenciar manutenção) passou a ter o **Atendente** como ator principal. Com isso, o documento passa a corresponder exatamente ao diagrama de casos de uso, conforme exige o checklist de entrega. Justificativa completa em [`decisoes/0002-remocao-do-ator-tecnico.md`](decisoes/0002-remocao-do-ator-tecnico.md).

**Sistemas externos:** o Supabase Auth atua como provedor de identidade, participando do UC01. Nenhum outro sistema externo faz parte do escopo.

---

## 3. Casos de uso identificados

| ID | Caso de uso | Ator principal | Histórias | Tipo |
|---|---|---|---|---|
| UC01 | Autenticar usuário | Administrador, Atendente | US01 | Regra |
| UC02 | Gerenciar usuários e perfis | Administrador | US02 | CRUD |
| UC03 | Gerenciar clientes | Atendente | US03 | CRUD |
| UC04 | Gerenciar ferramentas e categorias | Atendente | US04 | CRUD |
| UC05 | Consultar disponibilidade por período | Atendente | US05 | Regra |
| UC06 | Consultar catálogo e preços | Cliente | US06 | Consulta |
| UC07 | Solicitar reserva | Cliente | US07 | Regra |
| UC08 | Analisar solicitação de reserva | Atendente | US08 | Regra |
| UC09 | Formalizar locação | Atendente | US09, US12 | Regra |
| UC10 | Registrar retirada | Atendente | US10 | Regra |
| UC11 | Registrar devolução e avaliar condição | Atendente | US11, US12 | Regra |
| UC12 | Calcular valores e encargos | Atendente | US12 | Regra |
| UC13 | Registrar pagamento | Atendente | US13 | Regra |
| UC14 | Gerenciar manutenção | Atendente | US14 | Regra |
| UC15 | Consultar relatórios | Administrador | US15 | Consulta |
| UC16 | Consultar histórico de locações | Atendente | US16 | Consulta |

**Composição:** 3 CRUD puros (UC02, UC03, UC04) de 16 casos de uso — dentro do limite de "no máximo metade pode ser CRUD puro" exigido pelo enunciado.

### Relações entre casos de uso

Apenas relações reais são declaradas:

| Relação | Origem | Destino | Por quê |
|---|---|---|---|
| `«include»` | UC07 Solicitar reserva | UC05 Consultar disponibilidade | Não existe reserva sem verificar se o período está livre |
| `«include»` | UC09 Formalizar locação | UC05 Consultar disponibilidade | A disponibilidade é revalidada no momento de formalizar |
| `«include»` | UC09 Formalizar locação | UC12 Calcular valores | O valor previsto é apresentado antes da confirmação |
| `«include»` | UC11 Registrar devolução | UC12 Calcular valores | Encargos finais dependem do período efetivo e da condição |

Não há relações `«extend»` no escopo atual.

---

## 4. Especificação dos casos de uso

---

### UC01 — Autenticar usuário

| | |
|---|---|
| **Objetivo** | Permitir que um usuário acesse as funcionalidades autorizadas ao seu perfil. |
| **Ator principal** | Administrador, Atendente |
| **Ator secundário** | Supabase Auth (provedor de identidade) |
| **Rastreabilidade** | US01 |
| **Pré-condições** | O usuário possui cadastro ativo no sistema. |
| **Gatilho** | O usuário abre o aplicativo e informa suas credenciais. |

**Fluxo principal**

1. O usuário informa e-mail e senha.
2. O sistema envia as credenciais ao provedor de identidade para validação.
3. O provedor valida e devolve um token de sessão.
4. O sistema identifica o perfil associado ao usuário.
5. O sistema libera apenas as funcionalidades autorizadas para aquele perfil.

**Fluxos alternativos e exceções**

- **A1 — Credenciais incorretas:** o sistema informa que e-mail ou senha estão inválidos, sem revelar qual dos dois falhou, e permite nova tentativa.
- **A2 — Usuário inativo:** o acesso é bloqueado com mensagem indicando que o cadastro está inativo.
- **A3 — Falha de comunicação:** o sistema informa indisponibilidade temporária e não concede acesso.

**Regras de negócio:** RN09, RN10
**Pós-condição (sucesso):** usuário autenticado, com sessão válida e perfil identificado.
**Garantia mínima (falha):** nenhum acesso é concedido e nenhuma informação sobre a existência da conta é revelada.

---

### UC02 — Gerenciar usuários e perfis

| | |
|---|---|
| **Objetivo** | Controlar quem acessa o sistema e com qual nível de permissão. |
| **Ator principal** | Administrador |
| **Rastreabilidade** | US02 |
| **Pré-condições** | Administrador autenticado. |
| **Gatilho** | O administrador acessa a gestão de usuários. |

**Fluxo principal**

1. O administrador acessa a gestão de usuários; o sistema apresenta os usuários cadastrados.
2. O administrador escolhe cadastrar um novo usuário ou editar um existente.
3. O administrador informa os dados e seleciona o perfil.
4. O sistema valida os dados e persiste as alterações.
5. O sistema exibe confirmação da operação.

**Fluxos alternativos e exceções**

- **A1 — Dados inválidos:** o sistema indica os campos com problema e solicita correção.
- **A2 — E-mail já cadastrado:** o sistema impede a criação e informa a duplicidade.
- **A3 — Inativação de usuário:** o administrador inativa em vez de excluir, preservando o histórico (RN11).
- **A4 — Autoinativação:** o sistema impede que o administrador inative a própria conta.

**Regras de negócio:** RN09, RN10, RN11
**Pós-condição (sucesso):** usuário cadastrado, atualizado ou inativado.
**Garantia mínima (falha):** nenhum dado parcial é gravado.

---

### UC03 — Gerenciar clientes

| | |
|---|---|
| **Objetivo** | Manter os dados dos clientes que utilizam o serviço de locação. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US03 |
| **Pré-condições** | Atendente autenticado. |
| **Gatilho** | O atendente acessa o cadastro de clientes. |

**Fluxo principal**

1. O atendente acessa o cadastro e pesquisa um cliente ou inicia um novo registro.
2. O atendente informa ou atualiza os dados cadastrais.
3. O sistema valida os dados.
4. O sistema persiste e confirma a operação.

**Fluxos alternativos e exceções**

- **A1 — Cliente já cadastrado:** o sistema alerta sobre o registro existente e oferece abrir o cadastro encontrado.
- **A2 — Dados inválidos:** o sistema solicita correção dos campos com problema.
- **A3 — Exclusão de cliente com histórico:** o sistema impede a exclusão e oferece inativação (RN11).

**Regras de negócio:** RN09, RN11
**Pós-condição (sucesso):** cliente cadastrado ou atualizado.
**Garantia mínima (falha):** o cadastro anterior permanece íntegro.

---

### UC04 — Gerenciar ferramentas e categorias

> ⚠️ **Este caso de uso não possuía especificação na versão 1.0 do documento.** A seção anunciava "UC01 – UC04" mas detalhava apenas UC01, UC02 e UC03. A especificação abaixo preenche a lacuna.

| | |
|---|---|
| **Objetivo** | Manter o catálogo de ferramentas e suas categorias atualizado, incluindo tarifas e situação operacional. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US04 |
| **Pré-condições** | Atendente autenticado. |
| **Gatilho** | O atendente acessa a gestão do catálogo. |

**Fluxo principal**

1. O atendente acessa a gestão de ferramentas; o sistema apresenta o catálogo com nome, código, categoria, tarifa diária e situação.
2. O atendente escolhe cadastrar uma nova ferramenta ou editar uma existente.
3. O atendente informa nome, descrição, código, categoria, tarifa diária e quantidade disponível.
4. O sistema valida os dados, verificando que a tarifa é maior que zero e que o código não se repete.
5. O sistema persiste a ferramenta e confirma a operação.

**Fluxo alternativo — gestão de categorias**

1. O atendente acessa a gestão de categorias.
2. O atendente cadastra ou edita uma categoria informando nome e descrição.
3. O sistema valida e persiste.

**Fluxos alternativos e exceções**

- **A1 — Código de ferramenta duplicado:** o sistema impede o cadastro e informa qual ferramenta já usa o código.
- **A2 — Tarifa inválida:** tarifa igual ou menor que zero é rejeitada com solicitação de correção.
- **A3 — Exclusão de ferramenta com histórico:** se a ferramenta estiver referenciada por locações, reservas ou manutenções, o sistema impede a exclusão e oferece marcá-la como inativa (RN11).
- **A4 — Exclusão de categoria em uso:** o sistema impede a exclusão de categoria que possua ferramentas associadas.
- **A5 — Alteração de tarifa:** a nova tarifa vale apenas para locações futuras; locações já formalizadas preservam a tarifa registrada no momento da formalização (RN04).

**Regras de negócio:** RN04, RN09, RN11, RN12
**Pós-condição (sucesso):** ferramenta ou categoria cadastrada, atualizada ou inativada, e o catálogo reflete a mudança.
**Garantia mínima (falha):** o catálogo permanece no estado anterior, sem registro parcial.

---

### UC05 — Consultar disponibilidade por período

| | |
|---|---|
| **Objetivo** | Informar se uma ferramenta está disponível em um período, evitando conflitos. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US05 |
| **Pré-condições** | Usuário autenticado e ferramenta cadastrada. |
| **Gatilho** | Necessidade de verificar disponibilidade, por ação direta ou por inclusão em UC07 e UC09. |

**Fluxo principal**

1. O ator seleciona a ferramenta e informa data inicial e data final.
2. O sistema valida o período.
3. O sistema verifica locações ativas, reservas bloqueantes e manutenções que se sobreponham ao período.
4. O sistema apresenta o resultado, indicando a quantidade disponível.

**Fluxos alternativos e exceções**

- **A1 — Data inicial maior ou igual à final:** o sistema rejeita e solicita correção (RN01).
- **A2 — Conflito detectado:** o sistema informa a indisponibilidade e o motivo (locação, reserva ou manutenção).
- **A3 — Ferramenta em manutenção:** indisponível durante todo o período da manutenção (RN03).

**Regras de negócio:** RN01, RN02, RN03
**Pós-condição (sucesso):** disponibilidade exibida, sem alterar nenhum dado.
**Garantia mínima (falha):** nenhuma alteração é feita; esta é uma consulta.

---

### UC06 — Consultar catálogo e preços

| | |
|---|---|
| **Objetivo** | Permitir que o cliente conheça as ferramentas disponíveis e seus preços. |
| **Ator principal** | Cliente |
| **Rastreabilidade** | US06 |
| **Pré-condições** | Catálogo cadastrado. |
| **Gatilho** | O cliente acessa o catálogo. |

**Fluxo principal**

1. O cliente acessa o catálogo de ferramentas.
2. O sistema apresenta as ferramentas ativas com descrição, categoria e tarifa diária.
3. O cliente filtra por categoria ou pesquisa por nome.
4. O cliente seleciona um item para ver os detalhes completos.

**Fluxos alternativos e exceções**

- **A1 — Busca sem resultados:** o sistema informa que nenhuma ferramenta foi encontrada e sugere limpar os filtros.
- **A2 — Ferramenta inativa:** não aparece no catálogo do cliente.

**Regras de negócio:** RN09
**Pós-condição (sucesso):** o cliente visualiza as informações do catálogo.
**Garantia mínima (falha):** nenhuma alteração é feita.

---

### UC07 — Solicitar reserva

| | |
|---|---|
| **Objetivo** | Permitir que o cliente demonstre interesse formal em alugar uma ferramenta em um período. |
| **Ator principal** | Cliente |
| **Rastreabilidade** | US07 |
| **Inclui** | UC05 — Consultar disponibilidade por período |
| **Pré-condições** | Cliente identificado e catálogo disponível. |
| **Gatilho** | O cliente escolhe uma ferramenta e decide reservar. |

**Fluxo principal**

1. O cliente seleciona a ferramenta e informa o período desejado e a quantidade.
2. O sistema executa UC05 e verifica a disponibilidade.
3. O sistema apresenta o resumo com período, quantidade e valor estimado.
4. O cliente confirma.
5. O sistema registra a reserva com estado `SOLICITADA`.

**Fluxos alternativos e exceções**

- **A1 — Período inválido:** o sistema rejeita e solicita correção (RN01).
- **A2 — Indisponível no período:** o sistema informa o conflito e não registra a reserva.
- **A3 — Cliente desiste:** nenhuma reserva é registrada.

**Regras de negócio:** RN01, RN02, RN03, RN08
**Pós-condição (sucesso):** reserva registrada como `SOLICITADA`, aguardando análise.
**Garantia mínima (falha):** nenhuma reserva parcial é criada.

---

### UC08 — Analisar solicitação de reserva

| | |
|---|---|
| **Objetivo** | Decidir se uma reserva solicitada é aprovada ou recusada. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US08 |
| **Pré-condições** | Atendente autenticado e existir ao menos uma reserva `SOLICITADA`. |
| **Gatilho** | O atendente acessa a fila de reservas pendentes. |

**Fluxo principal**

1. O atendente consulta as reservas solicitadas e seleciona uma.
2. O sistema exibe cliente, ferramenta, período, quantidade e valor estimado.
3. O sistema revalida a disponibilidade no momento da análise.
4. O atendente aprova ou recusa, podendo registrar uma justificativa.
5. O sistema salva a decisão e atualiza o estado para `APROVADA` ou `RECUSADA`.

**Fluxos alternativos e exceções**

- **A1 — Ferramenta deixou de estar disponível:** o sistema impede a aprovação e informa o conflito surgido.
- **A2 — Reserva já processada:** o sistema impede nova decisão sobre a mesma reserva (RN08).
- **A3 — Recusa sem justificativa:** o sistema exige o motivo antes de concluir.

**Regras de negócio:** RN02, RN03, RN08, RN09
**Pós-condição (sucesso):** reserva em estado `APROVADA` ou `RECUSADA`, com autor e data da decisão registrados.
**Garantia mínima (falha):** a reserva permanece `SOLICITADA`.

---

### UC09 — Formalizar locação

| | |
|---|---|
| **Objetivo** | Registrar formalmente o acordo de locação entre a empresa e o cliente. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US09, US12 |
| **Inclui** | UC05 — Consultar disponibilidade · UC12 — Calcular valores e encargos |
| **Pré-condições** | Atendente autenticado; cliente e ferramentas válidos. |
| **Gatilho** | Uma reserva aprovada avança ou um cliente chega sem reserva prévia. |

**Fluxo principal**

1. O atendente seleciona uma reserva aprovada ou inicia um atendimento direto.
2. O atendente confirma cliente, ferramentas, quantidades e período.
3. O sistema executa UC05 e revalida a disponibilidade.
4. O sistema executa UC12 e calcula o valor previsto.
5. O sistema apresenta o resumo da locação com a memória de cálculo.
6. O atendente confirma.
7. O sistema registra a locação no estado `APROVADA` e cria seus itens, gravando a tarifa vigente em cada item.

**Fluxos alternativos e exceções**

- **A1 — Indisponibilidade na revalidação:** o sistema bloqueia a operação e informa o conflito.
- **A2 — Dados inválidos:** o sistema exige correção antes de prosseguir.
- **A3 — Ferramenta entrou em manutenção:** o item é bloqueado (RN03).
- **A4 — Falha ao persistir:** a transação é revertida por completo (RN11).

**Regras de negócio:** RN01, RN02, RN03, RN04, RN08, RN11, RN12
**Pós-condição (sucesso):** locação registrada no estado `APROVADA`, com itens e valor previsto.
**Garantia mínima (falha):** nenhuma locação parcial é gravada.

---

### UC10 — Registrar retirada

| | |
|---|---|
| **Objetivo** | Documentar a entrega física das ferramentas ao cliente e iniciar a locação. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US10 |
| **Pré-condições** | Locação no estado `APROVADA`. |
| **Gatilho** | O cliente comparece para retirar as ferramentas. |

**Fluxo principal**

1. O atendente localiza a locação e confere cliente e itens.
2. O atendente registra a data da retirada e a condição inicial de cada ferramenta.
3. O sistema valida que a locação está apta à retirada.
4. O sistema registra a retirada e altera o estado para `EM_ANDAMENTO`.

**Fluxos alternativos e exceções**

- **A1 — Locação em estado inapto:** o sistema impede a retirada e informa o estado atual (RN08).
- **A2 — Divergência nos itens:** o atendente corrige a locação antes de confirmar, ou a operação é cancelada.
- **A3 — Cliente não comparece:** a locação permanece `APROVADA` até cancelamento conforme política.

**Regras de negócio:** RN03, RN08, RN09
**Pós-condição (sucesso):** retirada documentada, locação `EM_ANDAMENTO` e ferramentas em posse do cliente.
**Garantia mínima (falha):** a locação permanece `APROVADA`.

---

### UC11 — Registrar devolução e avaliar condição

| | |
|---|---|
| **Objetivo** | Encerrar a locação, avaliar o estado físico das ferramentas e apurar os encargos finais. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US11, US12 |
| **Inclui** | UC12 — Calcular valores e encargos |
| **Pré-condições** | Locação `EM_ANDAMENTO` ou `ATRASADA`. |
| **Gatilho** | O cliente devolve as ferramentas. |

**Fluxo principal**

1. O atendente localiza a locação e confere os itens devolvidos.
2. O atendente registra a data efetiva da devolução e a condição física de cada item.
3. O atendente registra eventuais danos ou ocorrências com descrição.
4. O sistema executa UC12, calcula o período efetivo e apura os encargos.
5. O sistema apresenta a memória de cálculo com valor-base, atraso e danos.
6. O atendente confirma.
7. O sistema registra a devolução, altera o estado para `DEVOLVIDA` e libera as ferramentas para novas locações.

**Fluxos alternativos e exceções**

- **A1 — Dano verificado:** o sistema registra a ocorrência; a cobrança só compõe o total após aprovação (RN06).
- **A2 — Devolução parcial:** aplica-se a política definida; a locação permanece aberta para os itens pendentes.
- **A3 — Ferramenta inutilizável:** o item é encaminhado para manutenção (UC14) e não volta ao catálogo.
- **A4 — Devolução de locação já devolvida:** o sistema impede a operação (RN08).

**Regras de negócio:** RN04, RN05, RN06, RN08, RN11, RN12
**Pós-condição (sucesso):** devolução e ocorrências registradas, encargos apurados e disponibilidade atualizada.
**Garantia mínima (falha):** a locação permanece no estado anterior.

---

### UC12 — Calcular valores e encargos

| | |
|---|---|
| **Objetivo** | Determinar o valor total devido de forma auditável. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US12 |
| **Pré-condições** | Tarifas, período e quantidades válidos. |
| **Gatilho** | Invocado por UC09 (valor previsto) e UC11 (valor final). |

**Fluxo principal**

1. O sistema obtém, para cada item, a tarifa diária registrada, a quantidade e o número de diárias faturáveis.
2. O sistema calcula o subtotal de cada item e soma os subtotais, obtendo o valor-base (RN04).
3. O sistema verifica se houve atraso e, havendo, aplica os encargos conforme a regra vigente (RN05).
4. O sistema inclui as cobranças por dano já aprovadas (RN06).
5. O sistema apresenta a memória de cálculo detalhada, linha a linha.

**Fluxos alternativos e exceções**

- **A1 — Tarifa ausente:** o sistema impede a confirmação e sinaliza o item sem preço.
- **A2 — Danos não aprovados:** permanecem registrados, mas não entram no total definitivo.
- **A3 — Devolução no prazo:** nenhum encargo de atraso é aplicado.

**Regras de negócio:** RN04, RN05, RN06, RN12
**Pós-condição (sucesso):** memória de cálculo gerada e pronta para registro.
**Garantia mínima (falha):** nenhum valor é gravado sem memória de cálculo correspondente.

---

### UC13 — Registrar pagamento

| | |
|---|---|
| **Objetivo** | Registrar os valores recebidos e acompanhar o saldo devedor. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US13 |
| **Pré-condições** | Locação identificada com valor devido apurado. |
| **Gatilho** | O cliente efetua um pagamento. |

**Fluxo principal**

1. O atendente seleciona a locação; o sistema apresenta total devido, total pago e saldo.
2. O atendente informa o valor recebido e a forma de pagamento.
3. O sistema valida o valor.
4. O sistema registra o pagamento e atualiza o saldo.

**Fluxos alternativos e exceções**

- **A1 — Valor menor ou igual a zero:** rejeitado (RN07).
- **A2 — Pagamento acima do saldo:** aplica-se a política de excedente definida pela equipe.
- **A3 — Pagamento parcial:** registrado normalmente; o saldo permanece em aberto.

**Regras de negócio:** RN07, RN09, RN11, RN12
**Pós-condição (sucesso):** pagamento registrado e saldo atualizado.
**Garantia mínima (falha):** o saldo anterior permanece íntegro.

---

### UC14 — Gerenciar manutenção

| | |
|---|---|
| **Objetivo** | Controlar ferramentas que precisam de reparo e devolvê-las ao catálogo apenas quando aptas. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US14 |
| **Pré-condições** | Atendente autenticado e ferramenta cadastrada. |
| **Gatilho** | Identificação de defeito, seja na devolução ou em inspeção. |

**Fluxo principal**

1. O atendente seleciona a ferramenta e registra o motivo e a descrição da manutenção.
2. O sistema marca a ferramenta como indisponível e cria o registro de manutenção.
3. Concluído o serviço, o atendente registra a conclusão e o resultado da avaliação.
4. O sistema atualiza a situação operacional da ferramenta.

**Fluxos alternativos e exceções**

- **A1 — Ferramenta não apta após o serviço:** permanece indisponível e a manutenção segue aberta.
- **A2 — Conflito com locação ativa:** o sistema alerta que a ferramenta está em posse de um cliente e exige a devolução antes de abrir a manutenção.
- **A3 — Ferramenta irrecuperável:** é marcada como inativa e sai do catálogo, preservando o histórico (RN11).

**Regras de negócio:** RN03, RN08, RN09, RN11
**Pós-condição (sucesso):** manutenção registrada e situação da ferramenta atualizada.
**Garantia mínima (falha):** a situação anterior da ferramenta permanece.

---

### UC15 — Consultar relatórios

| | |
|---|---|
| **Objetivo** | Fornecer visão consolidada da operação e das receitas. |
| **Ator principal** | Administrador |
| **Rastreabilidade** | US15 |
| **Pré-condições** | Administrador autenticado. |
| **Gatilho** | Necessidade de acompanhamento gerencial. |

**Fluxo principal**

1. O administrador seleciona o tipo de relatório e informa os filtros e o período.
2. O sistema valida os parâmetros.
3. O sistema consolida os dados e exibe o resultado.

**Fluxos alternativos e exceções**

- **A1 — Período inválido:** o sistema solicita correção (RN01).
- **A2 — Sem dados no período:** o sistema informa que não há registros.

**Regras de negócio:** RN01, RN09, RN12
**Pós-condição (sucesso):** relatório exibido, sem alterar dados.
**Garantia mínima (falha):** nenhuma alteração é feita.

---

### UC16 — Consultar histórico de locações

| | |
|---|---|
| **Objetivo** | Permitir que o atendente conheça o histórico do cliente antes de uma nova locação. |
| **Ator principal** | Atendente |
| **Rastreabilidade** | US16 |
| **Pré-condições** | Atendente autenticado e cliente cadastrado. |
| **Gatilho** | Atendimento a um cliente. |

**Fluxo principal**

1. O atendente pesquisa o cliente e seleciona a opção de histórico.
2. O sistema exibe as locações do cliente com itens, datas, estados e pagamentos.
3. O atendente seleciona uma locação para ver os detalhes.

**Fluxos alternativos e exceções**

- **A1 — Cliente sem histórico:** o sistema informa que não há locações registradas.
- **A2 — Ocorrências anteriores:** danos e atrasos passados são destacados.

**Regras de negócio:** RN09
**Pós-condição (sucesso):** histórico consultado, sem alterar dados.
**Garantia mínima (falha):** nenhuma alteração é feita.

---

## 5. Rastreabilidade

Cada caso de uso tem origem em pelo menos uma história de usuário. A matriz completa, incluindo Issues, branches e diagramas, está em [`agil/rastreabilidade.md`](agil/rastreabilidade.md).

| História | Caso de uso |
|---|---|
| US01 | UC01 |
| US02 | UC02 |
| US03 | UC03 |
| US04 | UC04 |
| US05 | UC05 |
| US06 | UC06 |
| US07 | UC07 |
| US08 | UC08 |
| US09 | UC09 |
| US10 | UC10 |
| US11 | UC11 |
| US12 | UC09, UC11, UC12 |
| US13 | UC13 |
| US14 | UC14 |
| US15 | UC15 |
| US16 | UC16 |

> **Correção em relação à versão 1.0:** a história **US08** não constava nas tabelas do documento original — a listagem saltava de US07 para US09 — embora o UC08 já a referenciasse. A história foi restaurada e está em [`backlog.md`](backlog.md).

---

## 6. Decisões pendentes

Estes pontos ainda precisam de validação da equipe e, quando aplicável, do professor. Nenhum deles bloqueia a entrega atual, mas todos afetam a implementação.

| # | Questão | Afeta |
|---|---|---|
| 1 | O Cliente terá login próprio ou o Atendente registra as solicitações em seu nome? | UC01, UC06, UC07 |
| 2 | Uma reserva `SOLICITADA` bloqueia a disponibilidade ou só após aprovação? | RN02, UC05, UC07 |
| 3 | Como contar diárias: períodos exatos de 24h ou diárias iniciadas? | RN04, UC12 |
| 4 | Qual a fórmula exata do atraso e quem aprova cobranças por dano? | RN05, RN06, UC11, UC12 |
| 5 | Locação presencial direta, sem reserva prévia, é permitida? | UC09 |
| 6 | Devolução parcial de itens do mesmo contrato é permitida? | UC11 |
| 7 | Quais relatórios são obrigatórios na primeira versão? | UC15 |
| 8 | O professor exige um modelo específico de especificação textual visto em aula? | Este documento |

Ver também [`decisoes/`](decisoes/) para as decisões já tomadas.
