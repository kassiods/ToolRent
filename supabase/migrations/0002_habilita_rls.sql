-- =====================================================================
-- ToolRent — Migration 0002: habilita Row Level Security
--
-- RLS ligado em TODAS as tabelas, ainda SEM políticas: no PostgreSQL,
-- RLS habilitado sem política nega tudo por padrão. Isso fecha o acesso
-- anônimo pela Data API (PostgREST) do Supabase enquanto as políticas
-- por perfil não existem.
--
-- A API Java NÃO é afetada: ela conecta com credencial de servidor, que
-- ignora RLS. O RLS aqui é a última linha de defesa (RN09), não a
-- primeira — ver supabase/policies/README.md e ADR 0004.
--
-- As políticas por perfil (CLIENTE lê só o que é dele etc.) entram na
-- Sprint 3, junto da autenticação (US01/US02), como migration própria.
--
-- NUNCA edite uma migration já aplicada. Crie a próxima.
-- =====================================================================

alter table usuario       enable row level security;
alter table cliente       enable row level security;
alter table categoria     enable row level security;
alter table ferramenta    enable row level security;
alter table reserva       enable row level security;
alter table locacao       enable row level security;
alter table item_locacao  enable row level security;
alter table devolucao     enable row level security;
alter table pagamento     enable row level security;
alter table manutencao    enable row level security;
