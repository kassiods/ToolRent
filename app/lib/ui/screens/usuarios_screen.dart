import 'package:flutter/material.dart';

import '../../core/app_theme.dart';
import '../../data/repositories/auth_repository.dart'
    show FalhaDeConexaoException;
import '../../data/repositories/usuario_repository.dart';
import '../../domain/entities/usuario.dart';
import '../../domain/enums/perfil_usuario.dart';

/// Gestão de usuários e perfis — UC02 (US02).
///
/// A tela só traduz ações em chamadas do [repositorio]: quem decide o que
/// é permitido é a API (RN09, RN10, RN11). O administrador nunca vê senha —
/// nem a que acabou de definir (RN10).
class UsuariosScreen extends StatefulWidget {
  const UsuariosScreen({super.key, required this.repositorio});

  final UsuarioRepository repositorio;

  @override
  State<UsuariosScreen> createState() => _UsuariosScreenState();
}

class _UsuariosScreenState extends State<UsuariosScreen> {
  late Future<List<Usuario>> _usuarios;

  @override
  void initState() {
    super.initState();
    _usuarios = widget.repositorio.listar();
  }

  void _recarregar() {
    // a future nasce ANTES do setState: `=> atribuicao` devolveria a
    // Future e o framework derruba o build nesse caso
    final novaConsulta = widget.repositorio.listar();
    setState(() {
      _usuarios = novaConsulta;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Usuários e perfis')),
      floatingActionButton: FloatingActionButton.extended(
        key: const Key('usuarios-novo'),
        onPressed: _criarUsuario,
        icon: const Icon(Icons.person_add_outlined),
        label: const Text('Novo usuário'),
      ),
      body: FutureBuilder<List<Usuario>>(
        future: _usuarios,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return _areaDeErro(snapshot.error!);
          }
          final usuarios = snapshot.data ?? const <Usuario>[];
          if (usuarios.isEmpty) {
            return const Center(child: Text('Nenhum usuário cadastrado.'));
          }
          return ListView.builder(
            padding: const EdgeInsets.fromLTRB(
              AppTheme.espacoMd,
              AppTheme.espacoMd,
              AppTheme.espacoMd,
              88,
            ),
            itemCount: usuarios.length,
            itemBuilder: (context, indice) =>
                _cartaoDeUsuario(usuarios[indice]),
          );
        },
      ),
    );
  }

  Widget _areaDeErro(Object erro) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(AppTheme.espacoLg),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              _mensagemDe(erro),
              textAlign: TextAlign.center,
              key: const Key('usuarios-erro'),
            ),
            const SizedBox(height: AppTheme.espacoMd),
            OutlinedButton(
              key: const Key('usuarios-tentar-novamente'),
              onPressed: _recarregar,
              child: const Text('Tentar novamente'),
            ),
          ],
        ),
      ),
    );
  }

  Widget _cartaoDeUsuario(Usuario usuario) {
    return Card(
      margin: const EdgeInsets.only(bottom: AppTheme.espacoSm),
      child: ListTile(
        title: Text(usuario.nome, key: Key('usuario-${usuario.id}')),
        subtitle: Text('${usuario.email} · ${usuario.perfil.codigo}'),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Chip(label: Text(usuario.ativo ? 'Ativo' : 'Inativo')),
            PopupMenuButton<_AcaoUsuario>(
              tooltip: 'Ações de ${usuario.nome}',
              onSelected: (acao) {
                if (acao == _AcaoUsuario.editar) {
                  _editarUsuario(usuario);
                } else {
                  _inativarUsuario(usuario);
                }
              },
              itemBuilder: (context) => [
                const PopupMenuItem(
                  value: _AcaoUsuario.editar,
                  child: Text('Editar'),
                ),
                if (usuario.ativo)
                  const PopupMenuItem(
                    value: _AcaoUsuario.inativar,
                    child: Text('Inativar'),
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  // ---------------------------------------------------------------
  // Ações — cada uma delega ao repositório e só cuida do retorno
  // ---------------------------------------------------------------

  Future<void> _criarUsuario() async {
    final dados = await showDialog<({String nome, String email, PerfilUsuario perfil, String senha})>(
      context: context,
      builder: (_) => const _DialogoNovoUsuario(),
    );
    if (dados == null || !mounted) return;

    try {
      await widget.repositorio.cadastrar(
        nome: dados.nome,
        email: dados.email,
        perfil: dados.perfil,
        senha: dados.senha,
      );
      if (!mounted) return;
      _notificar('Usuário criado com sucesso.');
      _recarregar();
    } on Exception catch (erro) {
      if (!mounted) return;
      _notificar(_mensagemDe(erro));
    }
  }

  Future<void> _editarUsuario(Usuario usuario) async {
    final dados = await showDialog<({String nome, PerfilUsuario perfil})>(
      context: context,
      builder: (_) => _DialogoEdicao(usuario: usuario),
    );
    if (dados == null || !mounted) return;

    try {
      await widget.repositorio.editar(
        id: usuario.id,
        nome: dados.nome,
        perfil: dados.perfil,
      );
      if (!mounted) return;
      _notificar('Usuário atualizado.');
      _recarregar();
    } on Exception catch (erro) {
      if (!mounted) return;
      _notificar(_mensagemDe(erro));
    }
  }

  Future<void> _inativarUsuario(Usuario usuario) async {
    final confirmado = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Inativar usuário'),
        content: Text(
          'Inativar ${usuario.nome}? O histórico de locações é '
          'preservado e a conta deixa de ter acesso ao sistema.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(false),
            child: const Text('Cancelar'),
          ),
          FilledButton(
            key: const Key('confirmar-inativar'),
            onPressed: () => Navigator.of(context).pop(true),
            child: const Text('Inativar'),
          ),
        ],
      ),
    );
    if (confirmado != true || !mounted) return;

    try {
      await widget.repositorio.inativar(usuario.id);
      if (!mounted) return;
      _notificar('Usuário inativado.');
      _recarregar();
    } on Exception catch (erro) {
      if (!mounted) return;
      _notificar(_mensagemDe(erro));
    }
  }

  void _notificar(String mensagem) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(mensagem, key: const Key('usuarios-snackbar'))),
    );
  }

  String _mensagemDe(Object erro) {
    if (erro is SemPermissaoException) return erro.mensagem;
    if (erro is EmailJaCadastradoException) return erro.mensagem;
    if (erro is ValidacaoRejeitadaException) return erro.mensagem;
    if (erro is UsuarioNaoEncontradoException) return erro.mensagem;
    if (erro is FalhaDeConexaoException) return erro.mensagem;
    return 'Não foi possível concluir a operação. Tente novamente.';
  }
}

enum _AcaoUsuario { editar, inativar }

// ---------------------------------------------------------------
// Diálogos — validação de FORMATO aqui é só cortesia visual; a API
// revalida sempre (RN e arquitetura)
// ---------------------------------------------------------------

class _DialogoNovoUsuario extends StatefulWidget {
  const _DialogoNovoUsuario();

  @override
  State<_DialogoNovoUsuario> createState() => _DialogoNovoUsuarioState();
}

class _DialogoNovoUsuarioState extends State<_DialogoNovoUsuario> {
  final _formKey = GlobalKey<FormState>();
  final _nomeController = TextEditingController();
  final _emailController = TextEditingController();
  final _senhaController = TextEditingController();
  PerfilUsuario? _perfil;
  bool _senhaVisivel = false;

  @override
  void dispose() {
    _nomeController.dispose();
    _emailController.dispose();
    _senhaController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Novo usuário'),
      content: SizedBox(
        width: 400,
        child: Form(
          key: _formKey,
          child: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextFormField(
                  key: const Key('novo-nome'),
                  controller: _nomeController,
                  decoration: const InputDecoration(labelText: 'Nome'),
                  validator: (valor) =>
                      (valor == null || valor.trim().isEmpty)
                          ? 'Informe o nome.'
                          : null,
                ),
                const SizedBox(height: AppTheme.espacoMd),
                TextFormField(
                  key: const Key('novo-email'),
                  controller: _emailController,
                  keyboardType: TextInputType.emailAddress,
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
                DropdownButtonFormField<PerfilUsuario>(
                  key: const Key('novo-perfil'),
                  initialValue: _perfil,
                  decoration: const InputDecoration(labelText: 'Perfil'),
                  hint: const Text('Selecione'),
                  items: [
                    for (final perfil in PerfilUsuario.values)
                      DropdownMenuItem(
                        value: perfil,
                        child: Text(perfil.codigo),
                      ),
                  ],
                  onChanged: (valor) => setState(() => _perfil = valor),
                  validator: (valor) =>
                      valor == null ? 'Informe o perfil.' : null,
                ),
                const SizedBox(height: AppTheme.espacoMd),
                TextFormField(
                  key: const Key('novo-senha'),
                  controller: _senhaController,
                  obscureText: !_senhaVisivel,
                  decoration: InputDecoration(
                    labelText: 'Senha provisória',
                    helperText: 'A senha é definida uma vez e nunca exibida.',
                    suffixIcon: IconButton(
                      icon: Icon(
                        _senhaVisivel
                            ? Icons.visibility_off_outlined
                            : Icons.visibility_outlined,
                      ),
                      onPressed: () =>
                          setState(() => _senhaVisivel = !_senhaVisivel),
                    ),
                  ),
                  validator: (valor) {
                    if (valor == null || valor.isEmpty) {
                      return 'Informe a senha provisória.';
                    }
                    if (valor.length < 6) {
                      return 'A senha precisa de ao menos 6 caracteres.';
                    }
                    return null;
                  },
                ),
              ],
            ),
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancelar'),
        ),
        FilledButton(
          key: const Key('novo-salvar'),
          onPressed: () {
            if (!(_formKey.currentState?.validate() ?? false)) return;
            Navigator.of(context).pop((
              nome: _nomeController.text.trim(),
              email: _emailController.text.trim(),
              perfil: _perfil!,
              senha: _senhaController.text,
            ));
          },
          child: const Text('Salvar'),
        ),
      ],
    );
  }
}

class _DialogoEdicao extends StatefulWidget {
  const _DialogoEdicao({required this.usuario});

  final Usuario usuario;

  @override
  State<_DialogoEdicao> createState() => _DialogoEdicaoState();
}

class _DialogoEdicaoState extends State<_DialogoEdicao> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _nomeController;
  late PerfilUsuario _perfil;

  @override
  void initState() {
    super.initState();
    _nomeController = TextEditingController(text: widget.usuario.nome);
    _perfil = widget.usuario.perfil;
  }

  @override
  void dispose() {
    _nomeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Editar usuário'),
      content: SizedBox(
        width: 400,
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                key: const Key('editar-nome'),
                controller: _nomeController,
                decoration: const InputDecoration(labelText: 'Nome'),
                validator: (valor) => (valor == null || valor.trim().isEmpty)
                    ? 'Informe o nome.'
                    : null,
              ),
              const SizedBox(height: AppTheme.espacoMd),
              DropdownButtonFormField<PerfilUsuario>(
                key: const Key('editar-perfil'),
                initialValue: _perfil,
                decoration: const InputDecoration(labelText: 'Perfil'),
                items: [
                  for (final perfil in PerfilUsuario.values)
                    DropdownMenuItem(value: perfil, child: Text(perfil.codigo)),
                ],
                onChanged: (valor) {
                  if (valor != null) setState(() => _perfil = valor);
                },
              ),
              const SizedBox(height: AppTheme.espacoSm),
              Text(
                'O e-mail e a senha não são alterados por aqui.',
                style: Theme.of(context).textTheme.bodySmall,
              ),
            ],
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancelar'),
        ),
        FilledButton(
          key: const Key('editar-salvar'),
          onPressed: () {
            if (!(_formKey.currentState?.validate() ?? false)) return;
            Navigator.of(context).pop((
              nome: _nomeController.text.trim(),
              perfil: _perfil,
            ));
          },
          child: const Text('Salvar'),
        ),
      ],
    );
  }
}
