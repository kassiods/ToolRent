package br.edu.toolrent.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.edu.toolrent.api.domain.PerfilUsuario;
import br.edu.toolrent.api.domain.Usuario;

/**
 * Persistencia da tabela {@code usuario} — Supabase (PostgreSQL).
 *
 * <p>Somente acesso a dados: nenhuma regra de negocio mora aqui (RN09, RN10).
 * Consultas parametrizadas — nunca concatenacao de entrada do usuario (RN09).
 */
@Repository
public class UsuarioRepository {

    private static final String SQL_POR_AUTH_USER_ID = """
            select id, auth_user_id, nome, email, perfil, ativo, criado_em
              from usuario
             where auth_user_id = :authUserId
            """;

    private static final String SQL_LISTAR = """
            select id, auth_user_id, nome, email, perfil, ativo, criado_em
              from usuario
             order by nome
            """;

    private static final String SQL_POR_ID = """
            select id, auth_user_id, nome, email, perfil, ativo, criado_em
              from usuario
             where id = :id
            """;

    private static final String SQL_POR_EMAIL = """
            select id, auth_user_id, nome, email, perfil, ativo, criado_em
              from usuario
             where lower(email) = lower(:email)
            """;

    private static final String SQL_INSERIR = """
            insert into usuario (auth_user_id, nome, email, perfil, ativo)
            values (:authUserId, :nome, :email, :perfil::perfil_usuario, :ativo)
            returning id, auth_user_id, nome, email, perfil, ativo, criado_em
            """;

    private static final String SQL_EDITAR = """
            update usuario
               set nome = :nome, perfil = :perfil::perfil_usuario
             where id = :id
            """;

    private static final String SQL_INATIVAR = """
            update usuario
               set ativo = false
             where id = :id
            """;

    private final NamedParameterJdbcTemplate jdbc;

    public UsuarioRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Usuario> buscarPorAuthUserId(UUID authUserId) {
        List<Usuario> encontrados = jdbc.query(
                SQL_POR_AUTH_USER_ID,
                Map.of("authUserId", authUserId),
                new UsuarioRowMapper());
        return encontrados.stream().findFirst();
    }

    public List<Usuario> listarTodos() {
        return jdbc.query(SQL_LISTAR, Map.of(), new UsuarioRowMapper());
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        List<Usuario> encontrados = jdbc.query(
                SQL_POR_ID,
                Map.of("id", id),
                new UsuarioRowMapper());
        return encontrados.stream().findFirst();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        List<Usuario> encontrados = jdbc.query(
                SQL_POR_EMAIL,
                Map.of("email", email),
                new UsuarioRowMapper());
        return encontrados.stream().findFirst();
    }

    public Usuario inserir(Usuario usuario) {
        return jdbc.queryForObject(
                SQL_INSERIR,
                Map.of(
                        "authUserId", usuario.getAuthUserId(),
                        "nome", usuario.getNome(),
                        "email", usuario.getEmail(),
                        "perfil", usuario.getPerfil().name(),
                        "ativo", usuario.isAtivo()),
                new UsuarioRowMapper());
    }

    public void editar(UUID id, String nome, PerfilUsuario perfil) {
        jdbc.update(SQL_EDITAR,
                Map.of("id", id, "nome", nome, "perfil", perfil.name()));
    }

    public void inativar(UUID id) {
        jdbc.update(SQL_INATIVAR, Map.of("id", id));
    }

    private static final class UsuarioRowMapper implements RowMapper<Usuario> {

        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Usuario(
                    rs.getObject("id", UUID.class),
                    rs.getObject("auth_user_id", UUID.class),
                    rs.getString("nome"),
                    rs.getString("email"),
                    PerfilUsuario.valueOf(rs.getString("perfil")),
                    rs.getBoolean("ativo"),
                    rs.getObject("criado_em", OffsetDateTime.class));
        }
    }
}
