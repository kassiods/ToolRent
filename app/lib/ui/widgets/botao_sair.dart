import 'package:flutter/material.dart';

import '../../data/repositories/auth_repository.dart';
import '../screens/login_screen.dart';

/// Botão de sair das três telas iniciais — estrutura comum "cabeçalho, menu
/// de ações, sair" do diagrama de atividades da Sprint 2
/// (`docs/diagramas/sprint-2/atividades-tela-inicial.md`, fluxo "toca em
/// trocar de perfil").
///
/// Desloga pelo [AuthRepository] (o widget nunca chama rede direto) e volta
/// para a tela de login, permitindo entrar com outra conta sem reiniciar o
/// app. A sessão local do Supabase é removida antes da chamada de rede, então
/// um erro de rede não impede a troca de perfil.
class BotaoSair extends StatefulWidget {
  const BotaoSair({super.key, required this.authRepository});

  final AuthRepository authRepository;

  @override
  State<BotaoSair> createState() => _BotaoSairState();
}

class _BotaoSairState extends State<BotaoSair> {
  bool _saindo = false;

  Future<void> _sair() async {
    if (_saindo) return;
    setState(() => _saindo = true);

    await widget.authRepository.sair();
    if (!mounted) return;

    Navigator.of(context).pushAndRemoveUntil(
      MaterialPageRoute<void>(
        builder: (_) => LoginScreen(authRepository: widget.authRepository),
      ),
      (rota) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    return IconButton(
      tooltip: 'Sair',
      icon: const Icon(Icons.logout_outlined),
      onPressed: _saindo ? null : _sair,
    );
  }
}
