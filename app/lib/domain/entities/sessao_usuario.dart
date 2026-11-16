import '../enums/perfil_usuario.dart';

/// Dados da sessão devolvidos por `GET /api/usuarios/me`.
///
/// Objeto de dados puro: sem regra de negócio, sem senha.
class SessaoUsuario {
  const SessaoUsuario({
    required this.id,
    required this.nome,
    required this.email,
    required this.perfil,
  });

  final String id;
  final String nome;
  final String email;
  final PerfilUsuario perfil;
}
