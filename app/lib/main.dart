import 'package:flutter/material.dart';

import 'core/app_theme.dart';
import 'ui/screens/seletor_perfil_screen.dart';

/// Ponto de entrada do ToolRent.
///
/// O seletor de perfil é provisório e será substituído pelo fluxo de
/// autenticação da Sprint 3.
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
      home: const SeletorPerfilScreen(),
    );
  }
}
