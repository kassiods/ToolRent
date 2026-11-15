-- ============================================================
-- US01 — usuário de teste para o login (EM BRANCO de propósito)
--
-- Este arquivo NÃO é migration: não entra em migrations/ e não
-- altera o schema. Rode só no SQL Editor do projeto, para ter
-- uma conta com que testar o login de ponta a ponta.
--
-- Passo a passo:
-- 1. Painel → Authentication → Users → Add user
--    (e-mail e senha que você digitará no app; marque Auto Confirm)
-- 2. Copie o UUID do usuário criado para <AUTH_USER_ID> abaixo
-- 3. Preencha os demais campos e execute o INSERT
-- 4. Para testar o bloqueio de conta inativa, repita com ativo = false
--
-- Nunca commite este arquivo preenchido — o padrão é o que vale.
-- ============================================================

INSERT INTO public.usuario (auth_user_id, nome, email, perfil, ativo)
VALUES (
  '<AUTH_USER_ID>',
  '<Nome do usuário de teste>',
  '<email-de-teste@exemplo.com>',
  'CLIENTE',   -- CLIENTE | ATENDENTE | ADMINISTRADOR
  true        -- false = conta inativa (login deve ser bloqueado)
);
