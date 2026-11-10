# Sessão — TT07: tela do Cliente e teste de referência

- **Data:** 09/11/2026
- **Integrante:** Victor Rennan Alves Sátiro
- **Ferramenta:** Assistente de IA (opencode)
- **Relacionado a:** TT07 / UC06 / UC07 / Issue #28

## Objetivo

Implementar o Pacote 3 da Sprint 2: a tela inicial do Cliente com os dois itens de
menu (UC06 e UC07) e o teste de widget que percorre a sequência do diagrama
`sequencia-abrir-tela-inicial.md`.

## O que foi feito

- Criado `app/lib/ui/screens/home_cliente_screen.dart` — exatamente dois
  `CartaoAcao`: Catálogo e preços (UC06, Sprint 4) e Solicitar reserva (UC07,
  Sprint 5), com os ícones do diagrama de atividades.
- Criado `app/test/home_cliente_screen_test.dart` — quatro testes: menu com
  dois itens (e nada inventado), toque em UC06, toque em UC07 e volta da tela
  de espera.
- Alterado `app/lib/ui/screens/seletor_perfil_screen.dart` — a opção Cliente
  passa a empilhar `HomeClienteScreen` (o diagrama de sequência começa em
  `SeletorPerfilScreen` → `push(HomeClienteScreen)`).
- Atualizada a matriz de rastreabilidade: TT07 → 🟡.

## Decisões tomadas

- Só o ramo **Cliente** do seletor foi alterado. O ramo **Atendente** continua
  apontando para a tela de espera ("Pacote 2") — essa ligação é do Pacote 2
  (TT06), não deste pacote.
- Estrutura da tela espelha `home_administrador_screen.dart` (mesmo padrão de
  2 itens), sem botão extra de troca de perfil: o diagrama de sequência fecha
  com "toca em voltar".
- Sprint prevista de cada UC sai do `docs/backlog.md` (UC06 → Sprint 4,
  UC07 → Sprint 5), igual ao padrão das telas do Administrador e do Atendente.

## O que foi rejeitado

- Acrescentar itens como "Minhas reservas" ou "Meus pagamentos": não existem
  casos de uso do Cliente para isso (diagrama de atividades e rastreabilidade).
- Editar `home_atendente_screen.dart` ou ligar o Atendente no seletor: fora da
  fronteira do Pacote 3.

## Verificação

- `flutter analyze` — **No issues found!** (Flutter 3.44.9 instalado em diretório
  temporário para esta sessão; o PATH da máquina não tinha o SDK).
- `flutter test` — **All tests passed!** (10 testes: 6 antigos + 4 novos da
  `home_cliente_screen_test.dart`).
- Revisão manual do diff: sem cor literal, sem HTTP, sem regra de negócio,
  `app/lib/domain/rules/` ausente.

## Pendências

- Testar no celular: seletor → Cliente → tocar nos dois cartões → voltar.
- Ligar `HomeAtendenteScreen` no seletor (Pacote 2 / TT06 — Lincoln).
- Revisão do PR e merge (`Closes #28`).
