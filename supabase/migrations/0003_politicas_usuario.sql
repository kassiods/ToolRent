-- =====================================================================
-- ToolRent — Migration 0003: políticas RLS da tabela usuario (US02)
--
-- Primeira política do projeto, conforme supabase/policies/README.md:
-- "usuario — Somente ADMINISTRADOR escreve".
--
-- Como funciona:
--   * leitura liberada a qualquer usuário autenticado (o papel de ler a
--     lista inteira é da API Java, com RN09 checada no service);
--   * escrita (insert/update) exige perfil ADMINISTRADOR;
--   * delete NÃO recebe política: a tabela não pode ser excluída pela
--     Data API — RN11 manda inativar, nunca apagar;
--   * a checagem de perfil lê a MESMA tabela e usa a função
--     perfil_do_usuario_logado(), que é SECURITY DEFINER e portanto
--     enxerga as linhas sem passar pela política (evita recursão de RLS).
--
-- A API Java NÃO é afetada: ela conecta com credencial de servidor, que
-- ignora RLS. Aqui é a última linha de defesa (ADR 0004), não a primeira.
--
-- Ainda faltam as políticas das demais tabelas (cliente, reserva etc.),
-- previstas em supabase/policies/README.md.
--
-- NUNCA edite uma migration já aplicada. Crie a próxima.
-- =====================================================================

-- Perfil do usuário dono do JWT atual.
-- SECURITY DEFINER: roda com o papel de quem criou (postgres), que não
-- sofre RLS na propria tabela — assim a politica pode consultar usuario
-- sem recursao. auth.uid() vem do token, não é forjavel pelo cliente.
create or replace function public.perfil_do_usuario_logado()
returns perfil_usuario
language sql
security definer
set search_path = public
stable
as $$
    select (
        select u.perfil
          from public.usuario u
         where u.auth_user_id = auth.uid()
           and u.ativo
         limit 1
    );
$$;

revoke execute on function public.perfil_do_usuario_logado() from public, anon;
grant execute on function public.perfil_do_usuario_logado() to authenticated;

-- Leitura: autenticados enxergam a tabela (a regra de quem pode listar
-- de verdade é a API, com mensagem e status proprio).
create policy "usuario_leitura_autenticados"
    on public.usuario
    for select
    to authenticated
    using (true);

-- Escrita: somente ADMINISTRADOR (RN09 como ultima linha de defesa).
create policy "usuario_escrita_administrador"
    on public.usuario
    for insert
    to authenticated
    with check (public.perfil_do_usuario_logado() = 'ADMINISTRADOR');

create policy "usuario_atualizacao_administrador"
    on public.usuario
    for update
    to authenticated
    using (public.perfil_do_usuario_logado() = 'ADMINISTRADOR')
    with check (public.perfil_do_usuario_logado() = 'ADMINISTRADOR');

-- Sem politica de delete: RN11 — usuario referenciado por locacao,
-- pagamento ou manutencao preserva o historico. Inativacao e o caminho.
