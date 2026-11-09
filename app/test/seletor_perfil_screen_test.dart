import 'package:flutter_test/flutter_test.dart';
import 'package:toolrent_app/main.dart';

void main() {
  group('SeletorPerfilScreen', () {
    testWidgets('exibe os três perfis provisórios', (tester) async {
      await tester.pumpWidget(const ToolRentApp());

      expect(find.text('Administrador'), findsOneWidget);
      expect(find.text('Atendente'), findsOneWidget);
      expect(find.text('Cliente'), findsOneWidget);
    });

    testWidgets('abre a tela do Administrador', (tester) async {
      await tester.pumpWidget(const ToolRentApp());

      await tester.tap(find.text('Administrador'));
      await tester.pumpAndSettle();

      expect(find.text('Área do Administrador'), findsOneWidget);
      expect(find.text('Usuários e perfis'), findsOneWidget);
      expect(find.text('Relatórios'), findsOneWidget);
    });

    testWidgets('abre a tela de espera de uma ação do Administrador', (
      tester,
    ) async {
      await tester.pumpWidget(const ToolRentApp());

      await tester.tap(find.text('Administrador'));
      await tester.pumpAndSettle();
      await tester.tap(find.text('Relatórios'));
      await tester.pumpAndSettle();

      expect(find.text('UC15'), findsOneWidget);
      expect(find.text('Consultar relatórios'), findsOneWidget);
    });
  });
}
