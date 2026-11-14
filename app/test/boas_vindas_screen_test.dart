import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:toolrent_app/ui/screens/boas_vindas_screen.dart';

void main() {
  group('BoasVindasScreen', () {
    Future<void> pumpTela(WidgetTester tester) {
      return tester.pumpWidget(const MaterialApp(home: BoasVindasScreen()));
    }

    testWidgets('exibe a marca e a descrição do sistema', (tester) async {
      await pumpTela(tester);

      expect(find.text('ToolRent'), findsOneWidget);
      expect(find.text('Sistema de Locação de Ferramentas'), findsOneWidget);
    });

    testWidgets('lista as etapas do roadmap', (tester) async {
      await pumpTela(tester);

      expect(find.text('Estado do projeto'), findsOneWidget);
      expect(find.text('Repositório e documentação'), findsOneWidget);
      expect(find.text('Autenticação e perfis'), findsOneWidget);
    });

    testWidgets('marca as Sprints 0, 1 e 2 como concluídas', (tester) async {
      await pumpTela(tester);

      expect(find.byIcon(Icons.check_circle), findsNWidgets(3));
      expect(find.byIcon(Icons.radio_button_unchecked), findsOneWidget);
    });
  });
}
