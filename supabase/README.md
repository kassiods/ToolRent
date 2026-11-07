# Banco de dados — Supabase

PostgreSQL gerenciado, usado também como provedor de autenticação.

---

## 1. Criar o projeto

1. Acesse [supabase.com](https://supabase.com) e crie um projeto
2. Escolha a região mais próxima (South America / São Paulo)
3. **Guarde a senha do banco** — ela só é exibida uma vez

> Um único projeto Supabase é compartilhado por toda a equipe. Não crie um por integrante: os dados ficariam divergentes.

## 2. Aplicar as migrations

No painel do Supabase, abra **SQL Editor**, cole o conteúdo de `migrations/0001_estrutura_inicial.sql` e execute.

Aplique as migrations **em ordem numérica**. Pular uma deixa o banco inconsistente.

## 3. Pegar as credenciais

| Credencial | Onde | Vai para |
|---|---|---|
| URL do projeto | Settings → API | `.env` do app e da API |
| Chave `anon` | Settings → API | `.env` do **app** |
| Chave `service_role` | Settings → API | `.env` da **API**, jamais do app |
| Conexão do banco | Settings → Database | `.env` da API |
| JWT Secret | Settings → API | `.env` da API |

> ⛔ **A chave `service_role` ignora todas as políticas de segurança.** Se ela chegar ao app Flutter, qualquer pessoa que abrir o aplicativo terá acesso total ao banco. Ela fica **somente** na API.

---

## Estrutura

```
supabase/
├── migrations/     schema versionado, numerado e aplicado em ordem
└── policies/       Row Level Security
```

### Tabelas

| Tabela | Guarda | Caso de uso |
|---|---|---|
| `usuario` | contas e perfis de acesso | UC01, UC02 |
| `cliente` | quem aluga | UC03 |
| `categoria` | agrupamento de ferramentas | UC04 |
| `ferramenta` | catálogo, tarifa e situação | UC04, UC06 |
| `reserva` | pedidos de locação | UC07, UC08 |
| `locacao` | contratos formalizados | UC09, UC10 |
| `item_locacao` | ferramentas de cada contrato, com tarifa congelada | UC09 |
| `devolucao` | retorno, condição e encargos | UC11 |
| `pagamento` | valores recebidos | UC13 |
| `manutencao` | reparos e indisponibilidade | UC14 |

### Decisões refletidas no schema

| Decisão | Como aparece |
|---|---|
| Senha não fica na aplicação (RN10) | `usuario` não tem coluna de senha; `auth_user_id` aponta para o Supabase Auth |
| `ATRASADA` é derivado ([ADR 0003](../docs/decisoes/0003-estado-atrasada-derivado.md)) | O enum `estado_locacao` **não** contém `ATRASADA` |
| Tarifa congelada no contrato (RN04) | `item_locacao.tarifa_registrada` copia o valor da formalização |
| Inativação em vez de exclusão (RN11) | Coluna `ativo` em `usuario`, `cliente` e `ferramenta` |
| Precisão monetária (RN12) | Todo valor é `numeric(12,2)`, nunca `float` |
| Período válido (RN01) | Constraints `periodo_valido_reserva` e `periodo_valido_locacao` |
| Dano só após aprovação (RN06) | `devolucao.danos_aprovados` |

---

## ⚠️ Estado atual

**`0001_estrutura_inicial.sql` é um RASCUNHO e nunca foi aplicado.** Não existe projeto Supabase
do ToolRent — ver [o plano da Sprint 2](../docs/sprints/sprint-2.md).

A correspondência com o [diagrama de classes](../docs/diagramas/sprint-2/classes.md) **foi
conferida em 07/11/2026**: 10 tabelas para as 10 classes, uma a uma. Na mesma data o rascunho
recebeu `cliente.usuario_id` para acompanhar o [ADR 0005](../docs/decisoes/0005-login-proprio-do-cliente.md).
Editar o arquivo foi legítimo porque ele nunca rodou; **depois de aplicado, a correção vem como
`0002_*.sql`.**

### Pendente

- [ ] **Criar o projeto Supabase do ToolRent** — não reaproveitar projeto existente de outro sistema
- [ ] Aplicar a `0001_estrutura_inicial.sql` nesse projeto
- [ ] Escrever as políticas de RLS em `policies/` (junto da autenticação, Sprint 3)
- [ ] Criar dados de demonstração fictícios

> A decisão #2 — como reservas `SOLICITADA` afetam a disponibilidade — **foi tomada**: só a
> `APROVADA` bloqueia ([ADR 0006](../docs/decisoes/0006-reserva-bloqueia-apenas-apos-aprovacao.md)).
> É regra de serviço, não de schema: nada muda aqui.

---

## Regras de trabalho

1. **Nunca edite uma migration já aplicada.** Quem já rodou a versão antiga ficaria com um banco diferente do seu. Crie a próxima.
2. **Numere em sequência:** `0001_`, `0002_`, `0003_`.
3. **Nomeie pelo que a migration faz:** `0002_adiciona_indice_disponibilidade.sql`.
4. **Nenhuma regra de negócio em trigger.** A regra vive na camada de serviço da API ([ADR 0004](../docs/decisoes/0004-regra-de-negocio-na-api.md)). Constraint que garante integridade é bem-vinda; lógica escondida no banco, não.
5. **Dados de demonstração são fictícios** e identificados como tal.

---

## Conta gratuita

O plano gratuito **pausa projetos inativos por 7 dias**. Antes de uma apresentação, abra o painel e confirme que o projeto está ativo — reativar leva alguns minutos.
