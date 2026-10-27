/**
 * Regras de negocio — o coracao do ToolRent.
 *
 * <p>Todas as regras RN01 a RN12 sao implementadas aqui. Ver
 * {@code docs/regras-de-negocio.md}.
 *
 * <p>Regras deste pacote:
 * <ul>
 *   <li><b>Nao conhece HTTP.</b> Nenhuma classe recebe HttpServletRequest
 *       nem devolve ResponseEntity</li>
 *   <li>Toda autorizacao e verificada aqui, nao apenas na interface (RN09)</li>
 *   <li>Toda transicao de estado e validada aqui (RN08)</li>
 *   <li>Todo calculo monetario usa BigDecimal (RN12)</li>
 * </ul>
 *
 * <p>O teste da separacao: se um metodo deste pacote nao puder ser testado
 * sem subir um servidor, a fronteira foi violada.
 */
package br.edu.toolrent.api.service;
