import 'package:flutter/material.dart';

import 'core/app_theme.dart';
import 'ui/screens/boas_vindas_screen.dart';

/// Ponto de entrada do ToolRent.
///
/// Sprint 0: apenas a estrutura e a tela de boas-vindas.
/// A autenticação (US01) entra na Sprint 3 e substitui a tela inicial
/// pela tela de login.
void main() {
  runApp(const ToolRentApp());
}

class ToolRentApp extends StatelessWidget {
  const ToolRentApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ToolRent',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.claro,
      darkTheme: AppTheme.escuro,
      home: const BoasVindasScreen(),
    );
  }
}
