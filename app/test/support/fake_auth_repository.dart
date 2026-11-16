import 'package:toolrent_app/data/repositories/auth_repository.dart';
import 'package:toolrent_app/domain/entities/sessao_usuario.dart';

/// AuthRepository controlável para os testes de widget.
///
/// Não há rede nem Supabase nos testes: o comportamento é programado aqui.
class FakeAuthRepository implements AuthRepository {
  FakeAuthRepository({required this.aoEntrar});

  /// Decide o resultado de [entrar] — devolve a sessão ou lança a exceção.
  final Future<SessaoUsuario> Function(String email, String senha) aoEntrar;

  int chamadas = 0;
  String? emailRecebido;
  String? senhaRecebida;

  @override
  Future<SessaoUsuario> entrar(String email, String senha) async {
    chamadas++;
    emailRecebido = email;
    senhaRecebida = senha;
    return aoEntrar(email, senha);
  }
}
