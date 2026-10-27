/**
 * Entidades e enums do dominio.
 *
 * <p>O enunciado da disciplina limita o dominio a 9-13 classes. As classes
 * previstas (a confirmar no diagrama de classes da Sprint 2) sao: Usuario,
 * Perfil, Cliente, Categoria, Ferramenta, Reserva, Locacao, ItemLocacao,
 * Devolucao, Pagamento e Manutencao.
 *
 * <p>Classes de configuracao, DTO, service e repository <b>nao</b> contam
 * como classes de dominio.
 *
 * <p>Regras deste pacote:
 * <ul>
 *   <li>Entidade nao contem SQL</li>
 *   <li>Valor monetario usa {@link java.math.BigDecimal}, nunca double (RN12)</li>
 *   <li>Estado usa enum, nunca String solta (RN08)</li>
 * </ul>
 */
package br.edu.toolrent.api.domain;
