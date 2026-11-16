import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:toolrent_app/core/app_theme.dart';
import 'package:toolrent_app/data/repositories/auth_repository.dart'
    show FalhaDeConexaoException;
import 'package:toolrent_app/data/repositories/usuario_repository.dart';
import 'package:toolrent_app/domain/entities/usuario.dart';
import 'package:toolrent_app/domain/enums/perfil_usuario.dart';
import 'package:toolrent_app/ui/screens/usuarios_screen.dart';

import 'support/fake_usuario_repository.dart';

void main() {
  List<Usuario> base() => [
    const Usuario(
      id: 'id-victor',
      nome: 'Victor',
      email: 'administrador@toolrent.dev',
      perfil: PerfilUsuario.administrador,
      ativo: true,
    ),
    const Usuario(
      id: 'id-nailan',
      nome: 'Nailan',
      email: 'cliente@teste.com',
      perfil: PerfilUsuario.cliente,
      ativo: false,
    ),
  ];

  Future<void> abrirTela(
    WidgetTester tester,
    FakeUsuarioRepository repositorio,
  ) async {
    await tester.pumpWidget(
      MaterialApp(
        theme: AppTheme.claro,
        home: UsuariosScreen(repositorio: repositorio),
      ),
    );
    await tester.pumpAndSettle();
  }

  group('UsuariosScreen — listagem', () {
    testWidgets('exibe os usuários devolvidos pela API, com situação', (
      tester,
    ) async {
      await abrirTela(tester, FakeUsuarioRepository(usuarios: base()));

      expect(find.text('Usuários e perfis'), findsOneWidget);
      expect(find.text('Victor'), findsOneWidget);
      expect(find.text('Nailan'), findsOneWidget);
      expect(
        find.text('administrador@toolrent.dev · ADMINISTRADOR'),
        findsOneWidget,
      );
      expect(find.text('Ativo'), findsOneWidget);
      expect(find.text('Inativo'), findsOneWidget);
      expect(find.byKey(const Key('usuarios-erro')), findsNothing);
    });

    testWidgets('estado vazio explica que não há usuários', (tester) async {
      await abrirTela(tester, FakeUsuarioRepository());

      expect(find.text('Nenhum usuário cadastrado.'), findsOneWidget);
    });

    testWidgets('falha de rede mostra o erro e permite tentar de novo', (
      tester,
    ) async {
      final repositorio = FakeUsuarioRepository();
      repositorio.erroAoListar = const FalhaDeConexaoException();
      await abrirTela(tester, repositorio);

      expect(find.byKey(const Key('usuarios-erro')), findsOneWidget);
      expect(find.textContaining('conectar ao servidor'), findsOneWidget);

      repositorio.erroAoListar = null;
      await tester.tap(find.byKey(const Key('usuarios-tentar-novamente')));
      await tester.pumpAndSettle();

      expect(find.byKey(const Key('usuarios-erro')), findsNothing);
      expect(repositorio.listagens, 2);
    });
  });

  group('UsuariosScreen — cadastro (UC02)', () {
    Future<void> preencherNovoUsuario(WidgetTester tester) async {
      await tester.tap(find.byKey(const Key('usuarios-novo')));
      await tester.pumpAndSettle();

      await tester.enterText(find.byKey(const Key('novo-nome')), 'Lincoln');
      await tester.enterText(
        find.byKey(const Key('novo-email')),
        'lincoln@toolrent.dev',
      );
      await tester.tap(find.byKey(const Key('novo-perfil')));
      await tester.pumpAndSettle();
      await tester.tap(find.text('ATENDENTE'));
      await tester.pumpAndSettle();
      await tester.enterText(find.byKey(const Key('novo-senha')), 'senha123');
    }

    testWidgets('valida os campos antes de enviar', (tester) async {
      final repositorio = FakeUsuarioRepository();
      await abrirTela(tester, repositorio);

      await tester.tap(find.byKey(const Key('usuarios-novo')));
      await tester.pumpAndSettle();
      await tester.tap(find.byKey(const Key('novo-salvar')));
      await tester.pumpAndSettle();

      expect(find.text('Informe o nome.'), findsOneWidget);
      expect(find.text('Informe o e-mail.'), findsOneWidget);
      expect(find.text('Informe o perfil.'), findsOneWidget);
      expect(find.text('Informe a senha provisória.'), findsOneWidget);
      expect(repositorio.ultimoCadastro, isNull);
    });

    testWidgets('envia os dados e recarrega a lista', (tester) async {
      final repositorio = FakeUsuarioRepository(usuarios: base());
      await abrirTela(tester, repositorio);

      await preencherNovoUsuario(tester);
      await tester.tap(find.byKey(const Key('novo-salvar')));
      await tester.pumpAndSettle();

      expect(repositorio.ultimoCadastro, isNotNull);
      expect(repositorio.ultimoCadastro!.nome, 'Lincoln');
      expect(repositorio.ultimoCadastro!.perfil, PerfilUsuario.atendente);
      expect(repositorio.ultimoCadastro!.senha, 'senha123');
      expect(find.text('Usuário criado com sucesso.'), findsOneWidget);
      expect(find.text('Lincoln'), findsOneWidget);
    });

    testWidgets('e-mail duplicado mostra a mensagem da API (409)', (
      tester,
    ) async {
      final repositorio = FakeUsuarioRepository(usuarios: base());
      repositorio.erroAoCadastrar = const EmailJaCadastradoException(
        'E-mail já cadastrado no sistema',
      );
      await abrirTela(tester, repositorio);

      await preencherNovoUsuario(tester);
      await tester.tap(find.byKey(const Key('novo-salvar')));
      await tester.pumpAndSettle();

      expect(
        find.text('E-mail já cadastrado no sistema'),
        findsOneWidget,
      );
      expect(find.text('Lincoln'), findsNothing);
    });
  });

  group('UsuariosScreen — edição', () {
    testWidgets('editar muda nome e perfil pela API', (tester) async {
      final repositorio = FakeUsuarioRepository(usuarios: base());
      await abrirTela(tester, repositorio);

      await tester.tap(find.byTooltip('Ações de Victor'));
      await tester.pumpAndSettle();
      await tester.tap(find.text('Editar'));
      await tester.pumpAndSettle();

      await tester.enterText(find.byKey(const Key('editar-nome')), 'Victor L.');
      await tester.tap(find.byKey(const Key('editar-salvar')));
      await tester.pumpAndSettle();

      expect(repositorio.ultimaEdicao, isNotNull);
      expect(repositorio.ultimaEdicao!.nome, 'Victor L.');
      expect(repositorio.ultimaEdicao!.id, 'id-victor');
      expect(find.text('Usuário atualizado.'), findsOneWidget);
      expect(find.text('Victor L.'), findsOneWidget);
    });
  });

  group('UsuariosScreen — inativação (RN11)', () {
    testWidgets('confirma e envia inativação, mantendo o usuário na lista', (
      tester,
    ) async {
      final repositorio = FakeUsuarioRepository(usuarios: base());
      await abrirTela(tester, repositorio);

      await tester.tap(find.byTooltip('Ações de Victor'));
      await tester.pumpAndSettle();
      await tester.tap(find.text('Inativar'));
      await tester.pumpAndSettle();

      expect(find.textContaining('histórico de locações'), findsOneWidget);

      await tester.tap(find.byKey(const Key('confirmar-inativar')));
      await tester.pumpAndSettle();

      expect(repositorio.ultimoInativado, 'id-victor');
      expect(find.text('Usuário inativado.'), findsOneWidget);
      expect(find.text('Victor'), findsOneWidget);
      expect(find.text('Inativo'), findsNWidgets(2));
    });

    testWidgets('cancelar não chama a API', (tester) async {
      final repositorio = FakeUsuarioRepository(usuarios: base());
      await abrirTela(tester, repositorio);

      await tester.tap(find.byTooltip('Ações de Victor'));
      await tester.pumpAndSettle();
      await tester.tap(find.text('Inativar'));
      await tester.pumpAndSettle();
      await tester.tap(find.text('Cancelar'));
      await tester.pumpAndSettle();

      expect(repositorio.ultimoInativado, isNull);
      expect(find.text('Victor'), findsOneWidget);
    });

    testWidgets('usuário inativo não oferece a ação Inativar', (
      tester,
    ) async {
      final repositorio = FakeUsuarioRepository(usuarios: base());
      await abrirTela(tester, repositorio);

      await tester.tap(find.byTooltip('Ações de Nailan'));
      await tester.pumpAndSettle();

      expect(find.text('Editar'), findsOneWidget);
      expect(find.text('Inativar'), findsNothing);
    });
  });

  group('UsuariosScreen — permissão (RN09)', () {
    testWidgets('403 da API vira aviso de permissão na tela', (tester) async {
      final repositorio = FakeUsuarioRepository();
      repositorio.erroAoListar = const SemPermissaoException(
        'Apenas administradores podem gerenciar usuários',
      );
      await abrirTela(tester, repositorio);

      expect(
        find.text('Apenas administradores podem gerenciar usuários'),
        findsOneWidget,
      );
    });
  });
}
