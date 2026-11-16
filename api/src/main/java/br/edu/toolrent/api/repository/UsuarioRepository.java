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
