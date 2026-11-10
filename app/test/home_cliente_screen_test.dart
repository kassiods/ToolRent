import 'package:flutter_test/flutter_test.dart';
import 'package:toolrent_app/main.dart';
import 'package:toolrent_app/ui/screens/em_construcao_screen.dart';
import 'package:toolrent_app/ui/screens/home_cliente_screen.dart';
import 'package:toolrent_app/ui/widgets/cartao_acao.dart';

void main() {
  group('HomeClienteScreen', () {
    Future<void> abrirTelaDoCliente(WidgetTester tester) async {
      await tester.pumpWidget(const ToolRentApp());

      await tester.tap(find.text('Cliente'));
      await tester.pumpAndSettle();
    }

    testWidgets('exibe os dois itens do menu do Cliente', (tester) async {
      await abrirTelaDoCliente(tester);

      expect(find.byType(HomeClienteScreen), findsOneWidget);
      expect(find.text('Área do Cliente'), findsOneWidget);
      expect(find.byType(CartaoAcao), findsNWidgets(2));
      expect(find.text('Catálogo e preços'), findsOneWidget);
      expect(find.text('Solicitar reserva'), findsOneWidget);
      expect(find.text('Minhas reservas'), findsNothing);
      expect(find.text('Meus pagamentos'), findsNothing);
    });

    testWidgets('sequência: tocar em Catálogo e preços abre UC06', (
      tester,
    ) async {
      await abrirTelaDoCliente(tester);

      await tester.tap(find.text('Catálogo e preços'));
      await tester.pumpAndSettle();

      expect(find.byType(EmConstrucaoScreen), findsOneWidget);
      expect(find.text('UC06'), findsOneWidget);
      expect(find.text('Consultar catálogo e preços'), findsOneWidget);
      expect(find.textContaining('Sprint 4'), findsOneWidget);
    });

    testWidgets('sequência: tocar em Solicitar reserva abre UC07', (
      tester,
    ) async {
      await abrirTelaDoCliente(tester);

      await tester.tap(find.text('Solicitar reserva'));
      await tester.pumpAndSettle();

      expect(find.byType(EmConstrucaoScreen), findsOneWidget);
      expect(find.text('UC07'), findsOneWidget);
      expect(find.textContaining('Sprint 5'), findsOneWidget);
    });

    testWidgets('voltar da tela de espera retorna à tela inicial', (
      tester,
    ) async {
      await abrirTelaDoCliente(tester);

      await tester.tap(find.text('Catálogo e preços'));
      await tester.pumpAndSettle();

      await tester.pageBack();
      await tester.pumpAndSettle();

      expect(find.byType(HomeClienteScreen), findsOneWidget);
      expect(find.text('Área do Cliente'), findsOneWidget);
    });
  });
}
