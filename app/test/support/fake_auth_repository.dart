import 'package:toolrent_app/data/repositories/auth_repository.dart';
import 'package:toolrent_app/domain/entities/sessao_usuario.dart';

/// AuthRepository controlável para os testes de widget.
///
/// Não há rede nem Supabase nos testes: o comportamento é programado aqui.
class FakeAuthRepository implements AuthRepository {
  FakeAuthRepository({this.aoEntrar});

  /// Decide o resultado de [entrar] — devolve a sessão ou lança a exceção.
  /// Opcional: testes de logout não programam o login.
  final Future<SessaoUsuario> Function(String email, String senha)? aoEntrar;

  int chamadas = 0;
  int chamadasSair = 0;
  String? emailRecebido;
  String? senhaRecebida;

  @override
  Future<SessaoUsuario> entrar(String email, String senha) async {
    chamadas++;
    emailRecebido = email;
    senhaRecebida = senha;
    final programa = aoEntrar;
    if (programa == null) {
      throw StateError('FakeAuthRepository: entrar() não foi programado.');
    }
    return programa(email, senha);
  }

  @override
  Future<void> sair() async {
    chamadasSair++;
  }
}
