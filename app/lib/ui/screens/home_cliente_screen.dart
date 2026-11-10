import 'package:flutter/material.dart';

import '../../core/app_theme.dart';
import '../widgets/cartao_acao.dart';
import 'em_construcao_screen.dart';

/// TT07 / Issue #28 — menu especificado pelo diagrama de sequência
/// em docs/diagramas/sprint-2/sequencia-abrir-tela-inicial.md.
///
/// Nesta sprint, as ações apenas abrem a tela de espera do respectivo UC.
/// O Cliente tem exatamente dois casos de uso: UC06 e UC07.
class HomeClienteScreen extends StatelessWidget {
  const HomeClienteScreen({super.key});

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
      appBar: AppBar(title: const Text('Cliente')),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(AppTheme.espacoLg),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 720),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Área do Cliente',
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
                const SizedBox(height: AppTheme.espacoSm),
                Text(
                  'Escolha uma ação para continuar.',
                  style: Theme.of(context).textTheme.bodyLarge,
                ),
                const SizedBox(height: AppTheme.espacoLg),
                CartaoAcao(
                  rotulo: 'Catálogo e preços',
                  icone: Icons.storefront_outlined,
                  aoTocar: () => _abrirCasoDeUso(
                    context,
                    codigoUc: 'UC06',
                    nomeUc: 'Consultar catálogo e preços',
                    sprintPrevista: 'Sprint 4',
                  ),
                ),
                CartaoAcao(
                  rotulo: 'Solicitar reserva',
                  icone: Icons.add_shopping_cart_outlined,
                  aoTocar: () => _abrirCasoDeUso(
                    context,
                    codigoUc: 'UC07',
                    nomeUc: 'Solicitar reserva',
                    sprintPrevista: 'Sprint 5',
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
