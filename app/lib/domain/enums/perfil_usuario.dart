/// Perfis de acesso do ToolRent — espelha `PerfilUsuario` da API.
///
/// A API é quem decide o que cada perfil pode fazer (RN09). Aqui o enum
/// apenas traduz o valor vindo do contrato JSON.
enum PerfilUsuario {
  administrador('ADMINISTRADOR'),
  atendente('ATENDENTE'),
  cliente('CLIENTE');

  const PerfilUsuario(this.codigo);

  /// Valor exatamente como a API serializa o perfil.
  final String codigo;

  /// Converte o valor do contrato JSON em perfil.
  ///
  /// Lança [FormatException] se a API responder com valor desconhecido —
  /// contrato quebrado deve falhar em voz alta, não escolher perfil por conta.
  static PerfilUsuario fromApi(String valor) {
    for (final perfil in values) {
      if (perfil.codigo == valor) return perfil;
    }
    throw FormatException('Perfil desconhecido: $valor');
  }
}
