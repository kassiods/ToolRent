import 'package:flutter/material.dart';

import '../../core/app_theme.dart';
import 'em_construcao_screen.dart';
import '../widgets/cartao_acao.dart';

class HomeAdministradorScreen extends StatelessWidget {
  const HomeAdministradorScreen({super.key});

  void _abrirCasoDeUso(
    BuildContext context, {
    required String codigoUc,
    required String nomeUc,
    required String sprintPrevista,
  }) {
    Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => EmConstrucaoScreen(
          codigoUc: codigoUc,
          nomeUc: nomeUc,
          sprintPrevista: sprintPrevista,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Administrador')),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(AppTheme.espacoLg),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 720),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Área do Administrador',
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
                const SizedBox(height: AppTheme.espacoSm),
                Text(
                  'Escolha uma ação para continuar.',
                  style: Theme.of(context).textTheme.bodyLarge,
                ),
                const SizedBox(height: AppTheme.espacoLg),
                CartaoAcao(
                  rotulo: 'Usuários e perfis',
                  icone: Icons.manage_accounts_outlined,
                  aoTocar: () => _abrirCasoDeUso(
                    context,
                    codigoUc: 'UC02',
                    nomeUc: 'Gerenciar usuários e perfis',
                    sprintPrevista: 'Sprint 3',
                  ),
                ),
                CartaoAcao(
                  rotulo: 'Relatórios',
                  icone: Icons.bar_chart_outlined,
                  aoTocar: () => _abrirCasoDeUso(
                    context,
                    codigoUc: 'UC15',
                    nomeUc: 'Consultar relatórios',
                    sprintPrevista: 'Sprint 7',
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
