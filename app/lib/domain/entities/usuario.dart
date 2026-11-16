import '../enums/perfil_usuario.dart';

/// Usuário do sistema devolvido pelo CRUD da API (UC02).
///
/// Objeto de dados puro: sem regra de negócio e sem senha — a senha nasce
/// e fica no Supabase Auth (RN10) e nem este objeto sabe que ela existe.
class Usuario {
  const Usuario({
    required this.id,
    required this.nome,
    required this.email,
    required this.perfil,
    required this.ativo,
  });

  final String id;
  final String nome;
  final String email;
  final PerfilUsuario perfil;

  /// [false] significa inativado, nunca excluído (RN11).
  final bool ativo;
}
