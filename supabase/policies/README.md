# Row Level Security (RLS)

Políticas de acesso por linha, no PostgreSQL.

## Status

🟡 **Primeira política escrita na US02** ([`0003_politicas_usuario.sql`](../migrations/0003_politicas_usuario.sql)):
a tabela `usuario` já tem as regras — leitura para autenticados, escrita só para ADMINISTRADOR,
sem política de delete (RN11). Falta **aplicar a migration no SQL Editor** e escrever as demais
tabelas.

Desde 13/11/2026 o RLS está **habilitado em todas as tabelas**
([`0002_habilita_rls.sql`](../migrations/0002_habilita_rls.sql)): sem política, a Data API nega
tudo por padrão. A API Java não é afetada.

## O papel do RLS neste projeto

O RLS é a **última** linha de defesa, não a primeira.

A autorização principal acontece na camada de serviço da API ([ADR 0004](../../docs/decisoes/0004-regra-de-negocio-na-api.md)): é lá que a RN09 é verificada, com regra de negócio completa e mensagem de erro em português.

O RLS existe para o caso de algo escapar — uma consulta mal escrita, um endpoint novo sem checagem, um acesso direto ao banco. Ele **não substitui** a verificação na API.

## Políticas

| Tabela | Política | Status |
|---|---|---|
| `usuario` | Leitura para autenticados; escrita só ADMINISTRADOR; sem delete (RN11) | ✅ [`0003_politicas_usuario.sql`](../migrations/0003_politicas_usuario.sql) — ⬜ aplicar |
| `cliente` | O perfil CLIENTE lê apenas o próprio registro | ⬜ |
| `reserva` | O perfil CLIENTE lê e cria apenas reservas próprias | ⬜ |
| `locacao` | O perfil CLIENTE lê apenas locações próprias | ⬜ |
| `pagamento` | Somente ADMINISTRADOR e ATENDENTE | ⬜ |
| `ferramenta`, `categoria` | Leitura liberada a autenticados; escrita por ATENDENTE | ⬜ |

A checagem de perfil em `usuario` usa a função `perfil_do_usuario_logado()` (`SECURITY DEFINER`):
é ela que permite a política consultar a própria tabela sem recursão de RLS, e que devolve `NULL`
para quem não tem linha ativa — negando a escrita.

> A secret key (`sb_secret_…`, antiga `service_role`) ignora todas as políticas de RLS — e por isso
> jamais pode sair do servidor. Desde a US02 ela é usada **só pela API Java** para criar login no
> Supabase Auth (Admin API), no `.env` da API. A API valida o JWT pelo JWKS público e consulta o
> banco com as credenciais normais de conexão.
