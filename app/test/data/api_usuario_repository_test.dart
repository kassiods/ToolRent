import 'dart:convert';

import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:toolrent_app/data/repositories/auth_repository.dart';
import 'package:toolrent_app/data/repositories/usuario_repository.dart';
import 'package:toolrent_app/domain/enums/perfil_usuario.dart';

void main() {
  const base = 'https://api.teste';
  const token = 'jwt-de-teste';

  /// Resposta de contrato da API: corpo {"mensagem": ...} nos erros.
  http.Response corpo(int status, Object dados) => http.Response(
        jsonEncode(dados),
        status,
        headers: {'content-type': 'application/json'},
      );

  ApiUsuarioRepository repositorioCom(MockClient cliente) =>
      ApiUsuarioRepository(
        cliente: cliente,
        tokenAtual: () => token,
        baseUrl: base,
      );

  group('ApiUsuarioRepository — contrato de sucesso', () {
    test('listar envia o Bearer token e devolve a lista', () async {
      late http.Request capturada;
      final cliente = MockClient((requisicao) async {
        capturada = requisicao;
        return corpo(200, [
          {
            'id': 'id-1',
            'nome': 'Victor',
            'email': 'admin@toolrent.dev',
            'perfil': 'ADMINISTRADOR',
            'ativo': true,
          },
          {
            'id': 'id-2',
            'nome': 'Nailan',
            'email': 'cliente@teste.com',
            'perfil': 'CLIENTE',
            'ativo': false,
          },
        ]);
      });

      final usuarios = await repositorioCom(cliente).listar();

      expect(capturada.url.toString(), '$base/api/usuarios');
      expect(capturada.method, 'GET');
      expect(capturada.headers['Authorization'], 'Bearer $token');
      expect(usuarios, hasLength(2));
      expect(usuarios.first.perfil, PerfilUsuario.administrador);
      expect(usuarios.last.ativo, isFalse);
      // RN10: nada de senha no contrato, nem vindo nem indo
      expect(capturada.body, isEmpty);
    });

    test('cadastrar envia os campos e lê o 201', () async {
      late http.Request capturada;
      final cliente = MockClient((requisicao) async {
        capturada = requisicao;
        return corpo(201, {
          'id': 'id-novo',
          'nome': 'Lincoln',
          'email': 'lincoln@toolrent.dev',
          'perfil': 'ATENDENTE',
          'ativo': true,
        });
      });

      final criado = await repositorioCom(cliente).cadastrar(
        nome: 'Lincoln',
        email: 'lincoln@toolrent.dev',
        perfil: PerfilUsuario.atendente,
        senha: 'senha123',
      );

      expect(capturada.method, 'POST');
      final enviado = jsonDecode(capturada.body) as Map<String, dynamic>;
      expect(enviado['nome'], 'Lincoln');
      expect(enviado['perfil'], 'ATENDENTE');
      expect(enviado['senha'], 'senha123');
      // resposta nunca devolve senha (RN10)
      expect(criado.id, 'id-novo');
      expect(criado.email, 'lincoln@toolrent.dev');
    });

    test('inativar usa DELETE e devolve o usuário inativo', () async {
      late http.Request capturada;
      final cliente = MockClient((requisicao) async {
        capturada = requisicao;
        return corpo(200, {
          'id': 'id-1',
          'nome': 'Cliente',
          'email': 'cliente@teste.com',
          'perfil': 'CLIENTE',
          'ativo': false,
        });
      });

      final inativado = await repositorioCom(cliente).inativar('id-1');

      expect(capturada.method, 'DELETE');
      expect(capturada.url.toString(), '$base/api/usuarios/id-1');
      expect(inativado.ativo, isFalse);
    });
  });

  group('ApiUsuarioRepository — erros traduzidos', () {
    test('409 vira EmailJaCadastradoException com a mensagem da API', () async {
      final cliente = MockClient(
        (_) async => corpo(409, {'mensagem': 'E-mail já cadastrado no sistema'}),
      );

      await expectLater(() => repositorioCom(cliente).cadastrar(
          nome: 'Duplicado',
          email: 'duplicado@toolrent.dev',
          perfil: PerfilUsuario.cliente,
          senha: 'senha123',
        ),
        throwsA(
          isA<EmailJaCadastradoException>().having(
            (erro) => erro.mensagem,
            'mensagem',
            'E-mail já cadastrado no sistema',
          ),
        ),
      );
    });

    test('403 vira SemPermissaoException com a mensagem da API (RN09)', () async {
      final cliente = MockClient(
        (_) async => corpo(
          403,
          {'mensagem': 'Apenas administradores podem gerenciar usuários'},
        ),
      );

      await expectLater(() => repositorioCom(cliente).listar(),
        throwsA(isA<SemPermissaoException>()),
      );
    });

    test('400 vira ValidacaoRejeitadaException', () async {
      final cliente = MockClient(
        (_) async => corpo(400, {'mensagem': 'Informe o nome.'}),
      );

      await expectLater(() => repositorioCom(cliente).cadastrar(
          nome: '',
          email: 'novo@toolrent.dev',
          perfil: PerfilUsuario.cliente,
          senha: 'senha123',
        ),
        throwsA(isA<ValidacaoRejeitadaException>()),
      );
    });

    test('500 vira FalhaDeConexaoException, sem vazar detalhe', () async {
      final cliente = MockClient((_) async => http.Response('erro interno', 500));

      await expectLater(() => repositorioCom(cliente).listar(),
        throwsA(isA<FalhaDeConexaoException>()),
      );
    });

    test('queda de rede vira FalhaDeConexaoException', () async {
      final cliente = MockClient((_) => throw http.ClientException('sem rede'));

      await expectLater(() => repositorioCom(cliente).listar(),
        throwsA(isA<FalhaDeConexaoException>()),
      );
    });

    test('sem token de sessão não chama a API (401 evitado)', () async {
      var chamouRede = false;
      final cliente = MockClient((_) async {
        chamouRede = true;
        return corpo(200, <dynamic>[]);
      });
      final semSessao = ApiUsuarioRepository(
        cliente: cliente,
        tokenAtual: () => null,
        baseUrl: base,
      );

      await expectLater(() => semSessao.listar(),
        throwsA(
          isA<SemPermissaoException>().having(
            (erro) => erro.mensagem,
            'mensagem',
            contains('Sessão expirada'),
          ),
        ),
      );
      await Future<void>.delayed(Duration.zero);
      expect(chamouRede, isFalse);
    });
  });
}
