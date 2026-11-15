import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:supabase_flutter/supabase_flutter.dart';

import 'core/app_theme.dart';
import 'data/repositories/auth_repository.dart';
import 'data/repositories/supabase_auth_repository.dart';
import 'ui/screens/login_screen.dart';

/// Ponto de entrada do ToolRent.
///
/// Carrega o `.env` (URLs da API e do Supabase) e inicializa o Supabase
/// Auth — quem emite o JWT da US01. A senha vive só no Supabase (RN10).
Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await dotenv.load();
  await Supabase.initialize(
    url: dotenv.env['SUPABASE_URL'] ?? '',
    publishableKey: dotenv.env['SUPABASE_ANON_KEY'] ?? '',
  );
  runApp(const ToolRentApp());
}

class ToolRentApp extends StatelessWidget {
  const ToolRentApp({super.key, this.authRepository});

  /// Injeção para testes: sem ela, usa o repositório real do Supabase.
  final AuthRepository? authRepository;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ToolRent',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.claro,
      darkTheme: AppTheme.escuro,
      home: LoginScreen(
        authRepository: authRepository ?? SupabaseAuthRepository(),
      ),
    );
  }
}
