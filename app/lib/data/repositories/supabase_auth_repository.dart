import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:supabase_flutter/supabase_flutter.dart';

import '../../domain/entities/sessao_usuario.dart';
import '../../domain/enums/perfil_usuario.dart';
import 'auth_repository.dart';

/// Autenticação real: senha no Supabase Auth (RN10), perfil na API ToolRent.
///
/// A senha nunca passa pela API do ToolRent nem fica no banco da aplicação —
/// quem valida credencial é o Supabase. O app só envia o JWT recebido para
/// `GET /api/usuarios/me`, que decide o acesso pelo perfil (RN09).
class SupabaseAuthRepository implements AuthRepository {
  @override
  Future<SessaoUsuario> entrar(String email, String senha) async {
    final session = await _autenticarNoSupabase(email, senha);
    return _carregarSessaoNaApi(session);
  }

  Future<Session> _autenticarNoSupabase(String email, String senha) async {
    try {
      final resposta = await Supabase.instance.client.auth.signInWithPassword(
        email: email,
        password: senha,
      );
      final session = resposta.session;
      if (session == null) throw const CredencialInvalidaException();
      return session;
    } on CredencialInvalidaException {
      rethrow;
    } on AuthException catch (erro) {
      _logaErroDeAuth(erro);
      // URL errada, DNS ou rede: o gotrue lanca AuthRetryableFetchException,
      // que TAMBEM e AuthException — sem este teste o app mostraria
      // "e-mail ou senha invalidos" para um problema de conexao.
      if (erro is AuthRetryableFetchException) {
        throw const FalhaDeConexaoException();
      }
      // Chave de API rejeitada pelo Supabase: e configuracao, nao credencial.
      if (erro.statusCode == '401' &&
          erro.message.toLowerCase().contains('api key')) {
        throw const FalhaDeConexaoException();
      }
      // Mensagem genérica: não revela se o e-mail existe (critério da US01).
      throw const CredencialInvalidaException();
    } catch (erro) {
      if (kDebugMode) {
        debugPrint('Login: erro inesperado ${erro.runtimeType}: $erro');
      }
      throw const FalhaDeConexaoException();
    }
  }

  /// Diagnóstico real do Supabase em modo debug — tipo da exceção,
  /// statusCode e code. Chave e token nunca aparecem aqui.
  void _logaErroDeAuth(AuthException erro) {
    if (!kDebugMode) return;
    debugPrint(
      'Supabase Auth rejeitou: ${erro.runtimeType} '
      '(statusCode=${erro.statusCode}, code=${erro.code}) — ${erro.message}',
    );
  }

  Future<SessaoUsuario> _carregarSessaoNaApi(Session session) async {
    final baseUrl = dotenv.maybeGet('API_BASE_URL');
    if (baseUrl == null) {
      if (kDebugMode) {
        debugPrint('API_BASE_URL ausente do .env — etapa da API pulada.');
      }
      throw const FalhaDeConexaoException();
    }

    try {
      final resposta = await http.get(
        Uri.parse('$baseUrl/api/usuarios/me'),
        headers: {'Authorization': 'Bearer ${session.accessToken}'},
      );

      switch (resposta.statusCode) {
        case 200:
          return _lerSessao(resposta.body);
        case 401:
          if (kDebugMode) {
            debugPrint(
              'API ToolRent rejeitou o token (401): ${resposta.body}',
            );
          }
          // Token rejeitado pela API — falha genérica, sem diagnosticar.
          throw const CredencialInvalidaException();
        case 403:
          if (kDebugMode) {
            debugPrint('API ToolRent negou o acesso (403): ${resposta.body}');
          }
          throw _erroDeAcesso(resposta.body);
        default:
          if (kDebugMode) {
            debugPrint(
              'API ToolRent respondeu ${resposta.statusCode}: '
              '${resposta.body}',
            );
          }
          throw const FalhaDeConexaoException();
      }
    } on CredencialInvalidaException {
      rethrow;
    } on ContaInativaException {
      rethrow;
    } on UsuarioSemAcessoException {
      rethrow;
    } on FalhaDeConexaoException {
      rethrow;
    } catch (erro) {
      if (kDebugMode) {
        debugPrint('API ToolRent: erro inesperado ${erro.runtimeType}: $erro');
      }
      throw const FalhaDeConexaoException();
    }
  }

  SessaoUsuario _lerSessao(String corpo) {
    final dados = jsonDecode(corpo) as Map<String, dynamic>;
    return SessaoUsuario(
      id: dados['id'] as String,
      nome: dados['nome'] as String,
      email: dados['email'] as String,
      perfil: PerfilUsuario.fromApi(dados['perfil'] as String),
    );
  }

  Exception _erroDeAcesso(String corpo) {
    String mensagem;
    try {
      final dados = jsonDecode(corpo) as Map<String, dynamic>;
      mensagem = (dados['mensagem'] as String?) ?? '';
    } on FormatException {
      return const FalhaDeConexaoException();
    }

    if (mensagem.contains('inativa')) return const ContaInativaException();
    if (mensagem.contains('acesso')) return const UsuarioSemAcessoException();
    return const FalhaDeConexaoException();
  }
}
