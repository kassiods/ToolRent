import 'package:flutter/material.dart';

import '../../core/app_theme.dart';

class CartaoAcao extends StatelessWidget {
  const CartaoAcao({
    super.key,
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
