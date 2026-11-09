import 'package:flutter/material.dart';

import '../../core/app_theme.dart';
import 'em_construcao_screen.dart';
import 'home_administrador_screen.dart';

class SeletorPerfilScreen extends StatelessWidget {
  const SeletorPerfilScreen({super.key});

  void _abrirAdministrador(BuildContext context) {
    Navigator.of(context).push(
      MaterialPageRoute<void>(builder: (_) => const HomeAdministradorScreen()),
    );
  }

  void _abrirTelaPrevista(
    BuildContext context, {
    required String perfil,
    required String sprintPrevista,
  }) {
    Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => EmConstrucaoScreen(
          codigoUc: 'Sprint 2',
          nomeUc: 'Tela inicial do $perfil',
          sprintPrevista: sprintPrevista,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Selecionar perfil')),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(AppTheme.espacoLg),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 560),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Text(
                  'Como deseja acessar o app?',
                  textAlign: TextAlign.center,
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
                const SizedBox(height: AppTheme.espacoSm),
                Text(
                  'Seleção provisória para a Sprint 2.',
                  textAlign: TextAlign.center,
                  style: Theme.of(context).textTheme.bodyLarge,
                ),
                const SizedBox(height: AppTheme.espacoLg),
                _PerfilCard(
                  rotulo: 'Administrador',
                  icone: Icons.admin_panel_settings_outlined,
                  aoTocar: () => _abrirAdministrador(context),
                ),
                _PerfilCard(
                  rotulo: 'Atendente',
                  icone: Icons.support_agent_outlined,
                  aoTocar: () => _abrirTelaPrevista(
                    context,
                    perfil: 'Atendente',
                    sprintPrevista: 'Pacote 2',
                  ),
                ),
                _PerfilCard(
                  rotulo: 'Cliente',
                  icone: Icons.person_outline,
                  aoTocar: () => _abrirTelaPrevista(
                    context,
                    perfil: 'Cliente',
                    sprintPrevista: 'Pacote 3',
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

class _PerfilCard extends StatelessWidget {
  const _PerfilCard({
    required this.rotulo,
    required this.icone,
    required this.aoTocar,
  });

  final String rotulo;
  final IconData icone;
  final VoidCallback aoTocar;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: InkWell(
        borderRadius: BorderRadius.circular(AppTheme.raioBorda),
        onTap: aoTocar,
        child: Padding(
          padding: const EdgeInsets.all(AppTheme.espacoLg),
          child: Row(
            children: [
              Icon(
                icone,
                size: 32,
                color: Theme.of(context).colorScheme.primary,
              ),
              const SizedBox(width: AppTheme.espacoMd),
              Expanded(
                child: Text(
                  rotulo,
                  style: Theme.of(context).textTheme.titleMedium,
                ),
              ),
              Icon(
                Icons.chevron_right,
                color: Theme.of(context).colorScheme.onSurfaceVariant,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
