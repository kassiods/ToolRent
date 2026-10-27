import 'package:flutter/material.dart';

import '../../core/app_theme.dart';

/// Tela inicial da Sprint 0.
///
/// Serve para confirmar que o projeto Flutter compila e executa, e para
/// mostrar o estado do roadmap à equipe. **Não contém regra de negócio** —
/// ver docs/decisoes/0004-regra-de-negocio-na-api.md.
///
/// Na Sprint 3 esta tela é substituída pela tela de login (US01).
class BoasVindasScreen extends StatelessWidget {
  const BoasVindasScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(AppTheme.espacoLg),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 560),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const _Marca(),
                const SizedBox(height: AppTheme.espacoLg),
                Text(
                  'Sistema de Locação de Ferramentas',
                  style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                        fontWeight: FontWeight.bold,
                        color: AppTheme.cinzaTexto,
                      ),
                ),
                const SizedBox(height: AppTheme.espacoSm),
                Text(
                  'Catálogo, disponibilidade, reservas, locações, devoluções '
                  'e pagamentos — com as regras garantidas no servidor.',
                  style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                        color: AppTheme.cinzaSuave,
                        height: 1.5,
                      ),
                ),
                const SizedBox(height: AppTheme.espacoXl),
                const _CartaoStatus(),
                const SizedBox(height: AppTheme.espacoLg),
                Text(
                  'Projeto acadêmico · Análise e Projeto de Sistemas · 2026',
                  style: Theme.of(context).textTheme.bodySmall?.copyWith(
                        color: AppTheme.cinzaSuave,
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

class _Marca extends StatelessWidget {
  const _Marca();

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Container(
          padding: const EdgeInsets.all(AppTheme.espacoMd),
          decoration: BoxDecoration(
            color: AppTheme.azulPrincipal,
            borderRadius: BorderRadius.circular(AppTheme.raioBorda),
          ),
          child: const Icon(
            Icons.handyman_outlined,
            color: Colors.white,
            size: 32,
          ),
        ),
        const SizedBox(width: AppTheme.espacoMd),
        Text(
          'ToolRent',
          style: Theme.of(context).textTheme.displaySmall?.copyWith(
                fontWeight: FontWeight.bold,
                color: AppTheme.azulEscuro,
                letterSpacing: -0.5,
              ),
        ),
      ],
    );
  }
}

/// Mostra em que ponto do roadmap o projeto está.
/// Atualize conforme as sprints avançam — ver docs/agil/sprints.md.
class _CartaoStatus extends StatelessWidget {
  const _CartaoStatus();

  static const _etapas = <({String rotulo, String sprint, bool concluida})>[
    (rotulo: 'Repositório e documentação', sprint: 'Sprint 0', concluida: true),
    (rotulo: 'Casos de uso e backlog', sprint: 'Sprint 1', concluida: false),
    (rotulo: 'Modelo de domínio e banco', sprint: 'Sprint 2', concluida: false),
    (rotulo: 'Autenticação e perfis', sprint: 'Sprint 3', concluida: false),
  ];

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(AppTheme.espacoLg),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                const Icon(
                  Icons.flag_outlined,
                  size: 18,
                  color: AppTheme.ambarEscuro,
                ),
                const SizedBox(width: AppTheme.espacoSm),
                Text(
                  'Estado do projeto',
                  style: Theme.of(context).textTheme.titleMedium?.copyWith(
                        fontWeight: FontWeight.bold,
                      ),
                ),
              ],
            ),
            const SizedBox(height: AppTheme.espacoMd),
            for (final etapa in _etapas) _LinhaEtapa(etapa: etapa),
          ],
        ),
      ),
    );
  }
}

class _LinhaEtapa extends StatelessWidget {
  const _LinhaEtapa({required this.etapa});

  final ({String rotulo, String sprint, bool concluida}) etapa;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: AppTheme.espacoSm),
      child: Row(
        children: [
          Icon(
            etapa.concluida
                ? Icons.check_circle
                : Icons.radio_button_unchecked,
            size: 20,
            color: etapa.concluida
                ? AppTheme.verdeSucesso
                : AppTheme.cinzaSuave.withValues(alpha: 0.5),
          ),
          const SizedBox(width: AppTheme.espacoMd),
          Expanded(
            child: Text(
              etapa.rotulo,
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                    color: etapa.concluida
                        ? AppTheme.cinzaTexto
                        : AppTheme.cinzaSuave,
                    fontWeight:
                        etapa.concluida ? FontWeight.w600 : FontWeight.normal,
                  ),
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(
              horizontal: AppTheme.espacoSm,
              vertical: AppTheme.espacoXs,
            ),
            decoration: BoxDecoration(
              color: AppTheme.azulClaro,
              borderRadius: BorderRadius.circular(AppTheme.espacoXs),
            ),
            child: Text(
              etapa.sprint,
              style: const TextStyle(
                fontSize: 11,
                fontWeight: FontWeight.w600,
                color: AppTheme.azulEscuro,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
