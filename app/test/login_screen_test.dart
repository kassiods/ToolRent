import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:toolrent_app/data/repositories/auth_repository.dart';
import 'package:toolrent_app/domain/entities/sessao_usuario.dart';
import 'package:toolrent_app/domain/enums/perfil_usuario.dart';
import 'package:toolrent_app/ui/screens/home_administrador_screen.dart';
import 'package:toolrent_app/ui/screens/home_atendente_screen.dart';
import 'package:toolrent_app/ui/screens/home_cliente_screen.dart';
import 'package:toolrent_app/ui/screens/login_screen.dart';

import 'support/fake_auth_repository.dart';

SessaoUsuario _sessao(PerfilUsuario perfil) => SessaoUsuario(
  id: '11111111-1111-1111-1111-111111111111',
  nome: 'Teste',
  email: 'teste@toolrent.com',
  perfil: perfil,
);

void main() {
  group('LoginScreen', () {
    Future<void> pumpLogin(
      WidgetTester tester,
      FakeAuthRepository repositorio,
    ) {
      return tester.pumpWidget(
        MaterialApp(
          home: LoginScreen(authRepository: repositorio),
        ),
      );
    }

    Future<void> preencherEEntrar(WidgetTester tester) async {
      await tester.enterText(find.byType(TextFormField).first, 'ana@toolrent.com');
      await tester.enterText(find.byType(TextFormField).last, 'senha-secreta');
      await tester.tap(find.byKey(const Key('login-entrar')));
      await tester.pumpAndSettle();
    }

    testWidgets('exibe campos de e-mail e senha com o botão Entrar', (
      tester,
    ) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => _sessao(PerfilUsuario.cliente),
      );
      await pumpLogin(tester, repositorio);

      expect(find.widgetWithText(FilledButton, 'Entrar'), findsOneWidget);
      expect(find.text('E-mail'), findsOneWidget);
      expect(find.text('Senha'), findsOneWidget);
    });

    testWidgets('credenciais inválidas mostram mensagem genérica', (
      tester,
    ) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => throw const CredencialInvalidaException(),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);

      expect(find.byKey(const Key('login-erro')), findsOneWidget);
      expect(find.text('E-mail ou senha inválidos.'), findsOneWidget);
      expect(find.byType(HomeClienteScreen), findsNothing);
      expect(find.byType(HomeAdministradorScreen), findsNothing);
    });

    testWidgets('conta inativa mostra a mensagem específica', (tester) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => throw const ContaInativaException(),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);

      expect(
        find.text('Conta inativa: contate o administrador do sistema.'),
        findsOneWidget,
      );
    });

    testWidgets('usuário sem vínculo mostra a mensagem de acesso', (
      tester,
    ) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => throw const UsuarioSemAcessoException(),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);

      expect(find.text('Usuário sem acesso ao sistema.'), findsOneWidget);
    });

    testWidgets('credenciais válidas abrem a tela do ADMINISTRADOR', (
      tester,
    ) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => _sessao(PerfilUsuario.administrador),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);

      expect(find.byType(HomeAdministradorScreen), findsOneWidget);
      expect(find.byType(HomeAtendenteScreen), findsNothing);
      expect(find.byType(HomeClienteScreen), findsNothing);
      expect(find.text('Entrar'), findsNothing);
    });

    testWidgets('credenciais válidas abrem a tela do ATENDENTE', (
      tester,
    ) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => _sessao(PerfilUsuario.atendente),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);

      expect(find.byType(HomeAtendenteScreen), findsOneWidget);
      expect(find.byType(HomeAdministradorScreen), findsNothing);
      expect(find.byType(HomeClienteScreen), findsNothing);
    });

    testWidgets('credenciais válidas abrem a tela do CLIENTE', (tester) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => _sessao(PerfilUsuario.cliente),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);

      expect(find.byType(HomeClienteScreen), findsOneWidget);
      expect(find.byType(HomeAdministradorScreen), findsNothing);
      expect(find.byType(HomeAtendenteScreen), findsNothing);
    });

    testWidgets('envia o e-mail e a senha informados ao repositório', (
      tester,
    ) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => _sessao(PerfilUsuario.cliente),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);

      expect(repositorio.chamadas, 1);
      expect(repositorio.emailRecebido, 'ana@toolrent.com');
      expect(repositorio.senhaRecebida, 'senha-secreta');
    });

    testWidgets('campos vazios não chamam o repositório', (tester) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (_, __) async => _sessao(PerfilUsuario.cliente),
      );
      await pumpLogin(tester, repositorio);

      await tester.tap(find.byKey(const Key('login-entrar')));
      await tester.pumpAndSettle();

      expect(repositorio.chamadas, 0);
      expect(find.text('Informe o e-mail.'), findsOneWidget);
      expect(find.text('Informe a senha.'), findsOneWidget);
    });

    testWidgets('sair volta ao login e permite entrar com outro perfil', (
      tester,
    ) async {
      final repositorio = FakeAuthRepository(
        aoEntrar: (email, _) async => email == 'ana@toolrent.com'
            ? _sessao(PerfilUsuario.atendente)
            : _sessao(PerfilUsuario.administrador),
      );
      await pumpLogin(tester, repositorio);

      await preencherEEntrar(tester);
      expect(find.byType(HomeAtendenteScreen), findsOneWidget);

      await tester.tap(find.byTooltip('Sair'));
      await tester.pumpAndSettle();

      expect(repositorio.chamadasSair, 1);
      expect(find.byType(HomeAtendenteScreen), findsNothing);
      expect(find.byType(LoginScreen), findsOneWidget);
      expect(find.widgetWithText(FilledButton, 'Entrar'), findsOneWidget);

      await tester.enterText(
        find.byType(TextFormField).first,
        'outra@toolrent.com',
      );
      await tester.enterText(find.byType(TextFormField).last, 'outra-senha');
      await tester.tap(find.byKey(const Key('login-entrar')));
      await tester.pumpAndSettle();

      expect(find.byType(HomeAdministradorScreen), findsOneWidget);
      expect(find.byType(HomeAtendenteScreen), findsNothing);
      expect(repositorio.chamadas, 2);
    });
  });
}
