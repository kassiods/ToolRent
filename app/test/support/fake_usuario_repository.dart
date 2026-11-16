import 'package:toolrent_app/data/repositories/usuario_repository.dart';
import 'package:toolrent_app/domain/entities/usuario.dart';
import 'package:toolrent_app/domain/enums/perfil_usuario.dart';

/// UsuarioRepository controlável para os testes de widget.
///
/// Não há rede nem Supabase nos testes: o comportamento é programado aqui.
class FakeUsuarioRepository implements UsuarioRepository {
  FakeUsuarioRepository({List<Usuario>? usuarios})
    : usuarios = usuarios ?? <Usuario>[];

  final List<Usuario> usuarios;

  /// Exceções que cada operação deve lançar (null = sucesso).
  Exception? erroAoListar;
  Exception? erroAoCadastrar;
  Exception? erroAoEditar;
  Exception? erroAoInativar;

  /// Últimos argumentos recebidos, para asserção.
  ({String nome, String email, PerfilUsuario perfil, String senha})?
  ultimoCadastro;
  ({String id, String nome, PerfilUsuario perfil})? ultimaEdicao;
  String? ultimoInativado;

  int listagens = 0;

  @override
  Future<List<Usuario>> listar() async {
    listagens++;
    final erro = erroAoListar;
    if (erro != null) throw erro;
    return List<Usuario>.unmodifiable(usuarios);
  }

  @override
  Future<Usuario> cadastrar({
    required String nome,
    required String email,
    required PerfilUsuario perfil,
    required String senha,
  }) async {
    ultimoCadastro = (nome: nome, email: email, perfil: perfil, senha: senha);
    final erro = erroAoCadastrar;
    if (erro != null) throw erro;
    final criado = Usuario(
      id: 'novo-${usuarios.length + 1}',
      nome: nome,
      email: email,
      perfil: perfil,
      ativo: true,
    );
    usuarios.add(criado);
    return criado;
  }

  @override
  Future<Usuario> editar({
    required String id,
    required String nome,
    required PerfilUsuario perfil,
  }) async {
    ultimaEdicao = (id: id, nome: nome, perfil: perfil);
    final erro = erroAoEditar;
    if (erro != null) throw erro;
    final indice = usuarios.indexWhere((usuario) => usuario.id == id);
    final atualizado = Usuario(
      id: id,
      nome: nome,
      email: usuarios[indice].email,
      perfil: perfil,
      ativo: usuarios[indice].ativo,
    );
    usuarios[indice] = atualizado;
    return atualizado;
  }

  @override
  Future<Usuario> inativar(String id) async {
    ultimoInativado = id;
    final erro = erroAoInativar;
    if (erro != null) throw erro;
    final indice = usuarios.indexWhere((usuario) => usuario.id == id);
    final inativado = Usuario(
      id: usuarios[indice].id,
      nome: usuarios[indice].nome,
      email: usuarios[indice].email,
      perfil: usuarios[indice].perfil,
      ativo: false,
    );
    usuarios[indice] = inativado;
    return inativado;
  }
}
