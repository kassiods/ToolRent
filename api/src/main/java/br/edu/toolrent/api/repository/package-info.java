/**
 * Acesso a dados — PostgreSQL hospedado no Supabase.
 *
 * <p>Regras deste pacote:
 * <ul>
 *   <li>Usar consultas parametrizadas; <b>nunca</b> concatenar entrada do
 *       usuario em SQL</li>
 *   <li>Nao conter regra de negocio — isso e responsabilidade de
 *       {@code service}</li>
 *   <li>Operacoes que alteram varias tabelas rodam em transacao (RN11)</li>
 *   <li>Preferir repositorios pequenos e claros a um DAO generico complexo</li>
 * </ul>
 */
package br.edu.toolrent.api.repository;
