import 'package:flutter/material.dart';

import '../../core/app_theme.dart';
import '../../data/repositories/auth_repository.dart';
import '../../domain/enums/perfil_usuario.dart';
import 'home_administrador_screen.dart';
import 'home_atendente_screen.dart';
import 'home_cliente_screen.dart';

/// Tela de login — substitui o seletor de perfil provisório da Sprint 2.
///
/// O app não decide nada: autentica pelo [authRepository] e abre a tela
/// inicial que a API indicou para o perfil. Ver sequencia-uc01.md (UC01).
class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key, required this.authRepository});

  final AuthRepository authRepository;

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _senhaController = TextEditingController();

  bool _carregando = false;
  bool _senhaVisivel = false;
  String? _erro;

  @override
  void dispose() {
    _emailController.dispose();
    _senhaController.dispose();
    super.dispose();
  }

  Future<void> _entrar() async {
    if (!(_formKey.currentState?.validate() ?? false)) return;

    setState(() {
      _carregando = true;
      _erro = null;
    });

    try {
      final sessao = await widget.authRepository.entrar(
        _emailController.text.trim(),
        _senhaController.text,
      );
      if (!mounted) return;
      _abrirHome(sessao.perfil);
    } on Exception catch (erro) {
      if (!mounted) return;
      setState(() {
        _carregando = false;
        _erro = _mensagemDe(erro);
      });
    }
  }

  String _mensagemDe(Exception erro) {
    if (erro is CredencialInvalidaException) return erro.mensagem;
    if (erro is ContaInativaException) return erro.mensagem;
    if (erro is UsuarioSemAcessoException) return erro.mensagem;
    if (erro is FalhaDeConexaoException) return erro.mensagem;
    return 'Não foi possível entrar. Tente novamente.';
  }

  void _abrirHome(PerfilUsuario perfil) {
    final Widget tela = switch (perfil) {
      PerfilUsuario.administrador => HomeAdministradorScreen(
        authRepository: widget.authRepository,
      ),
      PerfilUsuario.atendente => HomeAtendenteScreen(
        authRepository: widget.authRepository,
      ),
      PerfilUsuario.cliente => HomeClienteScreen(
        authRepository: widget.authRepository,
      ),
    };

    Navigator.of(context).pushReplacement(
      MaterialPageRoute<void>(builder: (_) => tela),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('ToolRent')),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(AppTheme.espacoLg),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 400),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Text(
                  'Entrar',
                  textAlign: TextAlign.center,
                  style: Theme.of(context).textTheme.headlineSmall,
                ),
                const SizedBox(height: AppTheme.espacoSm),
                Text(
                  'Use seu e-mail e senha para acessar o sistema.',
                  textAlign: TextAlign.center,
                  style: Theme.of(context).textTheme.bodyLarge,
                ),
                const SizedBox(height: AppTheme.espacoLg),
                Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      TextFormField(
                        controller: _emailController,
                        keyboardType: TextInputType.emailAddress,
                        autofillHints: const [AutofillHints.email],
                        decoration: const InputDecoration(labelText: 'E-mail'),
                        validator: (valor) {
                          if (valor == null || valor.trim().isEmpty) {
                            return 'Informe o e-mail.';
                          }
                          if (!valor.contains('@')) {
                            return 'Informe um e-mail válido.';
                          }
                          return null;
                        },
                      ),
                      const SizedBox(height: AppTheme.espacoMd),
                      TextFormField(
                        controller: _senhaController,
                        obscureText: !_senhaVisivel,
                        autofillHints: const [AutofillHints.password],
                        decoration: InputDecoration(
                          labelText: 'Senha',
                          suffixIcon: IconButton(
                            icon: Icon(
                              _senhaVisivel
                                  ? Icons.visibility_off_outlined
                                  : Icons.visibility_outlined,
                            ),
                            onPressed: () {
                              setState(() => _senhaVisivel = !_senhaVisivel);
                            },
                          ),
                        ),
                        validator: (valor) {
                          if (valor == null || valor.isEmpty) {
                            return 'Informe a senha.';
                          }
                          return null;
                        },
                        onFieldSubmitted: (_) => _entrar(),
                      ),
                    ],
                  ),
                ),
                if (_erro != null) ...[
                  const SizedBox(height: AppTheme.espacoMd),
                  Card(
                    color: Theme.of(context).colorScheme.errorContainer,
                    child: Padding(
                      padding: const EdgeInsets.all(AppTheme.espacoMd),
                      child: Text(
                        _erro!,
                        key: const Key('login-erro'),
                        style: TextStyle(
                          color:
                              Theme.of(context).colorScheme.onErrorContainer,
                        ),
                      ),
                    ),
                  ),
                ],
                const SizedBox(height: AppTheme.espacoLg),
                FilledButton(
                  key: const Key('login-entrar'),
                  onPressed: _carregando ? null : _entrar,
                  child: _carregando
                      ? const SizedBox(
                          width: 20,
                          height: 20,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Text('Entrar'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
