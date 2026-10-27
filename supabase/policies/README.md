# Row Level Security (RLS)

Políticas de acesso por linha, no PostgreSQL.

## Status

⬜ **A escrever na Sprint 3**, junto com a autenticação (US01, US02).

## O papel do RLS neste projeto

O RLS é a **última** linha de defesa, não a primeira.

A autorização principal acontece na camada de serviço da API ([ADR 0004](../../docs/decisoes/0004-regra-de-negocio-na-api.md)): é lá que a RN09 é verificada, com regra de negócio completa e mensagem de erro em português.

O RLS existe para o caso de algo escapar — uma consulta mal escrita, um endpoint novo sem checagem, um acesso direto ao banco. Ele **não substitui** a verificação na API.

## Políticas previstas

| Tabela | Política |
|---|---|
| `cliente` | O perfil CLIENTE lê apenas o próprio registro |
| `reserva` | O perfil CLIENTE lê e cria apenas reservas próprias |
| `locacao` | O perfil CLIENTE lê apenas locações próprias |
| `pagamento` | Somente ADMINISTRADOR e ATENDENTE |
| `usuario` | Somente ADMINISTRADOR escreve |
| `ferramenta`, `categoria` | Leitura liberada a autenticados; escrita por ATENDENTE |

> A chave `service_role`, usada pela API, **ignora todas as políticas de RLS**. É exatamente por isso que ela nunca pode sair do servidor.
