import 'package:flutter/material.dart';

import '../../core/app_theme.dart';
import '../widgets/cartao_acao.dart';
import 'em_construcao_screen.dart';

/// TT06 / Issue #27 — menu especificado pelo diagrama de atividades
/// em docs/diagramas/sprint-2/atividades-tela-inicial.md.
///
/// Nesta sprint, as ações apenas abrem a tela de espera do respectivo UC.
/// UC12 faz parte de UC09 e UC11 e não possui ação independente.
class HomeAtendenteScreen extends StatelessWidget {
  const HomeAtendenteScreen({super.key});

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

  Widget _acao(
    BuildContext context, {
    required String rotulo,
    required IconData icone,
    required String codigoUc,
    required String nomeUc,
    required String sprintPrevista,
  }) {
    return CartaoAcao(
      rotulo: rotulo,
      icone: icone,
      aoTocar: () => _abrirCasoDeUso(
        context,
        codigoUc: codigoUc,
        nomeUc: nomeUc,
        sprintPrevista: sprintPrevista,
      ),
    );
  }

  Widget _secao(
    BuildContext context, {
    required String titulo,
    required List<Widget> acoes,
  }) {
    return Padding(
      padding: const EdgeInsets.only(top: AppTheme.espacoLg),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Semantics(
            header: true,
            child: Text(titulo, style: Theme.of(context).textTheme.titleLarge),
          ),
          const SizedBox(height: AppTheme.espacoSm),
          LayoutBuilder(
            builder: (context, constraints) {
              // A largura disponível e a escala de texto determinam as colunas.
              // A altura fica livre para os rótulos quebrarem linha.
              final larguraMinima = MediaQuery.textScalerOf(
                context,
              ).scale(AppTheme.espacoXl * 8);
              final colunas =
                  constraints.maxWidth >= larguraMinima * 2 + AppTheme.espacoMd
                  ? 2
                  : 1;
              final largura =
                  (constraints.maxWidth - (colunas - 1) * AppTheme.espacoMd) /
                  colunas;

              return Wrap(
                spacing: AppTheme.espacoMd,
                runSpacing: AppTheme.espacoSm,
                children: [
                  for (final acao in acoes)
                    SizedBox(width: largura, child: acao),
                ],
              );
            },
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Atendente'),
        actions: [
          IconButton(
            tooltip: 'Trocar de perfil',
            icon: const Icon(Icons.switch_account_outlined),
            onPressed: () => Navigator.of(context).pop(),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(AppTheme.espacoLg),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Área do Atendente',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            const SizedBox(height: AppTheme.espacoSm),
            Text(
              'Escolha uma ação para continuar.',
              style: Theme.of(context).textTheme.bodyLarge,
            ),
            _secao(
              context,
              titulo: 'Cadastros',
              acoes: [
                _acao(
                  context,
                  rotulo: 'Clientes',
                  icone: Icons.people_outline,
                  codigoUc: 'UC03',
                  nomeUc: 'Gerenciar clientes',
                  sprintPrevista: 'Sprint 4',
                ),
                _acao(
                  context,
                  rotulo: 'Ferramentas e categorias',
                  icone: Icons.handyman_outlined,
                  codigoUc: 'UC04',
                  nomeUc: 'Gerenciar ferramentas e categorias',
                  sprintPrevista: 'Sprint 4',
                ),
              ],
            ),
            _secao(
              context,
              titulo: 'Disponibilidade e reservas',
              acoes: [
                _acao(
                  context,
                  rotulo: 'Consultar disponibilidade',
                  icone: Icons.event_available_outlined,
                  codigoUc: 'UC05',
                  nomeUc: 'Consultar disponibilidade por período',
                  sprintPrevista: 'Sprint 4',
                ),
                _acao(
                  context,
                  rotulo: 'Analisar reservas',
                  icone: Icons.fact_check_outlined,
                  codigoUc: 'UC08',
                  nomeUc: 'Analisar solicitação de reserva',
                  sprintPrevista: 'Sprint 5',
                ),
              ],
            ),
            _secao(
              context,
              titulo: 'Locação',
              acoes: [
                _acao(
                  context,
                  rotulo: 'Formalizar locação',
                  icone: Icons.assignment_turned_in_outlined,
                  codigoUc: 'UC09',
                  nomeUc: 'Formalizar locação',
                  sprintPrevista: 'Sprint 5',
                ),
                _acao(
                  context,
                  rotulo: 'Registrar retirada',
                  icone: Icons.outbox_outlined,
                  codigoUc: 'UC10',
                  nomeUc: 'Registrar retirada',
                  sprintPrevista: 'Sprint 5',
                ),
                _acao(
                  context,
                  rotulo: 'Registrar devolução',
                  icone: Icons.assignment_return_outlined,
                  codigoUc: 'UC11',
                  nomeUc: 'Registrar devolução e avaliar condição',
                  sprintPrevista: 'Sprint 6',
                ),
                _acao(
                  context,
                  rotulo: 'Registrar pagamento',
                  icone: Icons.payments_outlined,
                  codigoUc: 'UC13',
                  nomeUc: 'Registrar pagamento',
                  sprintPrevista: 'Sprint 6',
                ),
              ],
            ),
            _secao(
              context,
              titulo: 'Apoio',
              acoes: [
                _acao(
                  context,
                  rotulo: 'Manutenções',
                  icone: Icons.build_circle_outlined,
                  codigoUc: 'UC14',
                  nomeUc: 'Gerenciar manutenção',
                  sprintPrevista: 'Sprint 7',
                ),
                _acao(
                  context,
                  rotulo: 'Histórico do cliente',
                  icone: Icons.history,
                  codigoUc: 'UC16',
                  nomeUc: 'Consultar histórico de locações',
                  sprintPrevista: 'Sprint 7',
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
