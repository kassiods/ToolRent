import '../../domain/entities/sessao_usuario.dart';

/// Única porta de saída de rede do fluxo de autenticação (UC01).
///
/// O widget nunca chama HTTP direto: ele só conhece esta interface, o que
/// permite testar a tela com um fake e manter o Supabase fora dos testes.
abstract class AuthRepository {
  /// Autentica no Supabase Auth e carrega o perfil na API ToolRent.
  ///
  /// Lança [CredencialInvalidaException], [ContaInativaException],
  /// [UsuarioSemAcessoException] ou [FalhaDeConexaoException].
  Future<SessaoUsuario> entrar(String email, String senha);

  /// Encerra a sessão atual no Supabase Auth (logout — diagrama de atividades
  /// "toca em trocar de perfil"). A sessão local é sempre removida, mesmo
  /// quando a rede falha; por isso não propaga exceção.
  Future<void> sair();
}

/// Credencial rejeitada pelo Supabase Auth.
///
/// Mensagem genérica de propósito: não revela se o e-mail existe (critério
/// da US01).
class CredencialInvalidaException implements Exception {
  const CredencialInvalidaException();

  String get mensagem => 'E-mail ou senha inválidos.';
}

/// Usuário autenticado, mas com a conta inativa (bloqueio da API).
class ContaInativaException implements Exception {
  const ContaInativaException();

  String get mensagem => 'Conta inativa: contate o administrador do sistema.';
}

/// Autenticado, mas sem linha de usuário vinculada — sem acesso ao sistema.
class UsuarioSemAcessoException implements Exception {
  const UsuarioSemAcessoException();

  String get mensagem => 'Usuário sem acesso ao sistema.';
}

/// Rede indisponível ou resposta inesperada da API.
class FalhaDeConexaoException implements Exception {
  const FalhaDeConexaoException();

  String get mensagem => 'Não foi possível conectar ao servidor. Tente novamente.';
}
