import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:supabase_flutter/supabase_flutter.dart';

import '../../domain/entities/usuario.dart';
import '../../domain/enums/perfil_usuario.dart';
import 'auth_repository.dart';

/// Única porta de saída de rede do CRUD de usuários (UC02).
///
/// O widget nunca chama HTTP direto: ele só conhece esta interface, o que
/// permite testar a tela com um fake. Toda regra (RN09, RN10, RN11) é da
/// API — aqui só se traduz status em exceção para a tela mostrar.
abstract class UsuarioRepository {
  /// Lista todos os usuários, inclusive inativos.
  Future<List<Usuario>> listar();

  /// Cadastra usuário + login no Supabase Auth.
  ///
  /// Lança [EmailJaCadastradoException], [SemPermissaoException],
  /// [ValidacaoRejeitadaException] ou [FalhaDeConexaoException].
  Future<Usuario> cadastrar({
    required String nome,
    required String email,
    required PerfilUsuario perfil,
    required String senha,
  });

  /// Edita nome e perfil — e-mail e senha não mudam por aqui (RN10).
  Future<Usuario> editar({
    required String id,
    required String nome,
    required PerfilUsuario perfil,
  });

  /// Inativa o usuário — nunca exclui, histórico preservado (RN11).
  Future<Usuario> inativar(String id);
}

/// A API negou por perfil (RN09) ou sessão expirou — 403/401.
class SemPermissaoException implements Exception {
  const SemPermissaoException(this.mensagem);

  final String mensagem;
}

/// E-mail já existe na tabela ou no Supabase Auth — 409.
class EmailJaCadastradoException implements Exception {
  const EmailJaCadastradoException(this.mensagem);

  final String mensagem;
}

/// Corpo recusado pela validação da API — 400.
class ValidacaoRejeitadaException implements Exception {
  const ValidacaoRejeitadaException(this.mensagem);

  final String mensagem;
}

/// Nenhum usuário com o id pedido — 404.
class UsuarioNaoEncontradoException implements Exception {
  const UsuarioNaoEncontradoException(this.mensagem);

  final String mensagem;
}

/// CRUD real contra a API ToolRent, com o JWT da sessão Supabase atual.
class ApiUsuarioRepository implements UsuarioRepository {
  ApiUsuarioRepository({
    http.Client? cliente,
    String? Function()? tokenAtual,
    String? baseUrl,
  }) : _cliente = cliente ?? http.Client(),
       _tokenAtual = tokenAtual ?? _tokenDaSessao,
       _baseUrlFixa = baseUrl;

  final http.Client _cliente;
  final String? Function() _tokenAtual;
  final String? _baseUrlFixa;

  /// Padrão em produção: token vivo da sessão Supabase do dispositivo.
  static String? _tokenDaSessao() =>
      Supabase.instance.client.auth.currentSession?.accessToken;

  @override
  Future<List<Usuario>> listar() {
    return _executar((base, cabecalhos) async {
      final resposta = await _cliente.get(
        Uri.parse('$base/api/usuarios'),
        headers: cabecalhos,
      );
      if (resposta.statusCode == 200) {
        final lista = jsonDecode(resposta.body) as List<dynamic>;
        return lista
            .map((item) => _lerUsuario(item as Map<String, dynamic>))
            .toList();
      }
      throw _erro(resposta);
    });
  }

  @override
  Future<Usuario> cadastrar({
    required String nome,
    required String email,
    required PerfilUsuario perfil,
    required String senha,
  }) {
    return _executar((base, cabecalhos) async {
      final resposta = await _cliente.post(
        Uri.parse('$base/api/usuarios'),
        headers: cabecalhos,
        body: jsonEncode({
          'nome': nome,
          'email': email,
          'perfil': perfil.codigo,
          'senha': senha,
        }),
      );
      if (resposta.statusCode == 201) {
        return _lerUsuario(
          jsonDecode(resposta.body) as Map<String, dynamic>,
        );
      }
      throw _erro(resposta);
    });
  }

  @override
  Future<Usuario> editar({
    required String id,
    required String nome,
    required PerfilUsuario perfil,
  }) {
    return _executar((base, cabecalhos) async {
      final resposta = await _cliente.put(
        Uri.parse('$base/api/usuarios/$id'),
        headers: cabecalhos,
        body: jsonEncode({'nome': nome, 'perfil': perfil.codigo}),
      );
      if (resposta.statusCode == 200) {
        return _lerUsuario(
          jsonDecode(resposta.body) as Map<String, dynamic>,
        );
      }
      throw _erro(resposta);
    });
  }

  @override
  Future<Usuario> inativar(String id) {
    return _executar((base, cabecalhos) async {
      final resposta = await _cliente.delete(
        Uri.parse('$base/api/usuarios/$id'),
        headers: cabecalhos,
      );
      if (resposta.statusCode == 200) {
        return _lerUsuario(
          jsonDecode(resposta.body) as Map<String, dynamic>,
        );
      }
      throw _erro(resposta);
    });
  }

  /// Ponto único de chamada: resolve base + token e converte qualquer
  /// falha de transporte em [FalhaDeConexaoException], preservando as
  /// exceções de contrato.
  Future<T> _executar<T>(
    Future<T> Function(String base, Map<String, String> cabecalhos) acao,
  ) async {
    try {
      final base = _baseUrlFixa ?? _carregaBaseUrl();
      if (base == null) {
        if (kDebugMode) {
          debugPrint('API_BASE_URL ausente do .env — chamada abortada.');
        }
        throw const FalhaDeConexaoException();
      }
      final token = _tokenAtual();
      if (token == null) {
        throw const SemPermissaoException(
          'Sessão expirada. Entre novamente.',
        );
      }
      return await acao(base, {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      });
    } on FalhaDeConexaoException {
      rethrow;
    } on SemPermissaoException {
      rethrow;
    } on EmailJaCadastradoException {
      rethrow;
    } on UsuarioNaoEncontradoException {
      rethrow;
    } on ValidacaoRejeitadaException {
      rethrow;
    } catch (erro) {
      if (kDebugMode) {
        debugPrint('API usuários: ${erro.runtimeType}: $erro');
      }
      throw const FalhaDeConexaoException();
    }
  }

  String? _carregaBaseUrl() {
    try {
      return dotenv.maybeGet('API_BASE_URL');
    } catch (erro) {
      if (kDebugMode) {
        debugPrint('.env não carregado — sem API_BASE_URL.');
      }
      return null;
    }
  }

  Usuario _lerUsuario(Map<String, dynamic> dados) {
    return Usuario(
      id: dados['id'] as String,
      nome: dados['nome'] as String,
      email: dados['email'] as String,
      perfil: PerfilUsuario.fromApi(dados['perfil'] as String),
      ativo: dados['ativo'] as bool,
    );
  }

  /// Traduz o corpo `{"mensagem": "..."}` da API em exceção de contrato.
  Never _erro(http.Response resposta) {
    if (kDebugMode) {
      debugPrint(
        'API usuários respondeu ${resposta.statusCode}: ${resposta.body}',
      );
    }
    final mensagem = _lerMensagem(resposta.body);
    switch (resposta.statusCode) {
      case 400:
        throw ValidacaoRejeitadaException(mensagem ?? 'Dados inválidos.');
      case 401:
        throw const SemPermissaoException('Sessão expirada. Entre novamente.');
      case 403:
        throw SemPermissaoException(mensagem ?? 'Acesso negado.');
      case 404:
        throw UsuarioNaoEncontradoException(
          mensagem ?? 'Usuário não encontrado.',
        );
      case 409:
        throw EmailJaCadastradoException(
          mensagem ?? 'E-mail já cadastrado no sistema.',
        );
      default:
        throw const FalhaDeConexaoException();
    }
  }

  String? _lerMensagem(String corpo) {
    try {
      final dados = jsonDecode(corpo) as Map<String, dynamic>;
      return dados['mensagem'] as String?;
    } on FormatException {
      return null;
    }
  }
}
