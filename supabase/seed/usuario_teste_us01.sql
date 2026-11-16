-- ============================================================
-- US01 — 3 usuarios de teste para o login (EM BRANCO de senha)
--
-- Este arquivo NAO e migration: nao entra em migrations/ e nao
-- altera o schema. Rode so no SQL Editor do projeto, para ter
-- contas com que testar o login de ponta a ponta.
--
-- Passo a passo:
-- 1. Painel > Authentication > Users > Add user, 3 vezes, com auto confirm:
--      administrador@teste.com   atendente@teste.com   cliente@teste.com
--    (a senha e a que voce digitara no app — NUNCA coloque senha aqui)
-- 2. Copie o UUID de cada usuario criado para os <UUID_...> abaixo
-- 3. Confira o que ja existe antes de inserir:
--      SELECT auth_user_id, nome, email, perfil, ativo FROM public.usuario;
-- 4. Execute o INSERT (idempotente: rodar de novo so atualiza perfil/ativo)
-- 5. Para testar o bloqueio de conta inativa:
--      UPDATE public.usuario SET ativo = false WHERE email = 'cliente@teste.com';
--
-- Nunca commite este arquivo preenchido — o padrao e o que vale.
-- ============================================================

INSERT INTO public.usuario (auth_user_id, nome, email, perfil, ativo) VALUES
  ('<UUID_ADMIN>',   'Administrador Teste', 'administrador@teste.com', 'ADMINISTRADOR'::perfil_usuario, true),
  ('<UUID_ATEND>',   'Atendente Teste',     'atendente@teste.com',     'ATENDENTE'::perfil_usuario,     true),
  ('<UUID_CLIENTE>', 'Cliente Teste',       'cliente@teste.com',       'CLIENTE'::perfil_usuario,       true)
ON CONFLICT (email) DO UPDATE
  SET perfil = EXCLUDED.perfil,
      ativo  = EXCLUDED.ativo;
