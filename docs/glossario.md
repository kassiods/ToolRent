# Glossário — ToolRent

> Vocabulário comum da equipe. Usar estes termos de forma consistente em código, documentação, Issues e conversa evita ambiguidade cara.

---

## Termos do domínio

| Termo | Definição | Não confundir com |
|---|---|---|
| **Ferramenta** | Item do catálogo que pode ser alugado. Tem código único, categoria, tarifa diária e situação operacional. | *Item de locação* — a ferramenta é o cadastro; o item é a ferramenta dentro de um contrato. |
| **Categoria** | Agrupamento de ferramentas por tipo (elétricas, medição, corte). Serve para filtrar o catálogo. | — |
| **Cliente** | Pessoa que aluga. Tem cadastro próprio e histórico. | *Usuário* — cliente é quem aluga; usuário é quem acessa o sistema. |
| **Usuário** | Conta de acesso ao sistema, com e-mail, senha e exatamente um perfil. | *Cliente* |
| **Perfil** | Conjunto de permissões: Administrador, Atendente ou Cliente. | *Usuário* — o usuário **tem** um perfil. |
| **Reserva** | Pedido do cliente para alugar uma ferramenta num período. **Ainda não é compromisso firmado.** Precisa ser aprovada. | *Locação* |
| **Locação** | Contrato formalizado entre a empresa e o cliente. Tem itens, período, valores e ciclo de vida de 5 estados. | *Reserva* — a reserva é o pedido; a locação é o acordo. |
| **Item de locação** | Uma ferramenta dentro de uma locação, com quantidade e **a tarifa congelada no momento da formalização**. | *Ferramenta* |
| **Retirada** | Momento em que o cliente leva fisicamente as ferramentas. Inicia a locação de fato. | *Formalização* — formalizar é assinar; retirar é levar. |
| **Devolução** | Momento em que as ferramentas voltam. Inclui avaliação da condição física. | — |
| **Manutenção** | Registro de que uma ferramenta está em reparo. Enquanto aberta, a ferramenta fica indisponível. | — |
| **Pagamento** | Registro de um valor recebido, com data e forma. Pode ser parcial. | — |

---

## Termos de cálculo

| Termo | Definição |
|---|---|
| **Tarifa diária** | Valor cobrado por um dia de uso de uma unidade da ferramenta. Fica no cadastro da ferramenta. |
| **Tarifa registrada** | A tarifa diária **copiada para o item** no momento da formalização. É ela que vale para aquele contrato — mudar o catálogo depois não altera locações existentes. |
| **Diária faturável** | Unidade de cobrança. O critério exato (24h corridas vs. diária iniciada) é a decisão pendente #3. |
| **Valor-base** | Soma dos subtotais dos itens, sem encargos. `Σ (tarifa × quantidade × diárias)` |
| **Encargo** | Valor adicional ao valor-base: multa por atraso ou cobrança por dano. |
| **Multa por atraso** | Encargo aplicado **apenas** quando a devolução ocorre após o prazo acordado. |
| **Cobrança por dano** | Valor por avaria, que só compõe o total **após aprovação** por uma pessoa. |
| **Total devido** | `valor-base + multa por atraso + danos aprovados` |
| **Saldo** | `total devido − total pago`. Zero significa quitado. |
| **Memória de cálculo** | Detalhamento linha a linha de como o total foi obtido. Obrigatória — número sem explicação não é auditável. |

---

## Termos de estado

### Reserva

| Estado | Significado |
|---|---|
| `SOLICITADA` | Pedida pelo cliente, aguardando análise do atendente |
| `APROVADA` | Aceita; pode virar locação |
| `RECUSADA` | Negada, com justificativa registrada |
| `CANCELADA` | Desistência, conforme política |

### Locação

| Estado | Significado |
|---|---|
| `APROVADA` | Formalizada, aguardando retirada |
| `EM_ANDAMENTO` | Ferramentas retiradas, dentro do prazo |
| `ATRASADA` | Prazo ultrapassado e ainda não devolvida. **Estado derivado** — calculado comparando a data prevista com a data atual, não armazenado no banco (ver [ADR 0003](decisoes/0003-estado-atrasada-derivado.md)) |
| `DEVOLVIDA` | Encerrada, com devolução registrada |
| `CANCELADA` | Cancelada antes da retirada |

---

## Termos técnicos

| Termo | Significado |
|---|---|
| **US** | *User Story* — história de usuário. Ex.: US07 |
| **UC** | *Use Case* — caso de uso. Ex.: UC09 |
| **RN** | Regra de negócio. Ex.: RN04 |
| **ADR** | *Architecture Decision Record* — registro de decisão arquitetural |
| **DoR** | *Definition of Ready* — quando uma história pode entrar numa sprint |
| **DoD** | *Definition of Done* — quando uma história está realmente concluída |
| **JWT** | Token assinado que prova a identidade do usuário em cada requisição |
| **RLS** | *Row Level Security* — controle de acesso por linha, no PostgreSQL |
| **Migration** | Arquivo SQL numerado que altera o schema. Nunca se edita uma já aplicada |
| **DTO** | Objeto de transferência entre a API e o app, separado da entidade de domínio |
| **Estado derivado** | Estado calculado a partir de outros dados, não armazenado |

---

## Convenções de escrita

- **Nomes de classe, método e variável:** inglês apenas quando for termo técnico consagrado (`repository`, `service`, `controller`); o domínio é em português (`Locacao`, `calcularValorBase`, `tarifaDiaria`)
- **Mensagens ao usuário:** sempre em português, sem jargão técnico
- **Estados:** `UPPER_SNAKE_CASE` em código e documentação
- **Identificadores:** `US01`, `UC09`, `RN04` — sempre com dois dígitos
