import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:toolrent_app/main.dart';

void main() {
  group('BoasVindasScreen', () {
    testWidgets('exibe a marca e a descrição do sistema', (tester) async {
      await tester.pumpWidget(const ToolRentApp());

      expect(find.text('ToolRent'), findsOneWidget);
      expect(find.text('Sistema de Locação de Ferramentas'), findsOneWidget);
    });

    testWidgets('lista as etapas do roadmap', (tester) async {
      await tester.pumpWidget(const ToolRentApp());

      expect(find.text('Estado do projeto'), findsOneWidget);
      expect(find.text('Repositório e documentação'), findsOneWidget);
      expect(find.text('Autenticação e perfis'), findsOneWidget);
    });

    testWidgets('marca a Sprint 0 como concluída', (tester) async {
      await tester.pumpWidget(const ToolRentApp());

      // A Sprint 0 é a única etapa concluída neste momento do projeto.
      expect(find.byIcon(Icons.check_circle), findsOneWidget);
    });
  });
}
