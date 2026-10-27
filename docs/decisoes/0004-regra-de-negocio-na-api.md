# ADR 0004 — Regra de negócio exclusivamente na API

- **Data:** 27/10/2026
- **Status:** ✅ Aceita
- **Decidido por:** equipe

---

## Contexto

Com o app Flutter separado da API Java, surge a pergunta: onde colocar a validação e o cálculo?

É tentador calcular no Dart. A resposta fica instantânea, não gasta viagem de rede e o código é mais curto. Vários tutoriais de Flutter + Supabase fazem exatamente isso.

## Decisão

**Toda regra de negócio (RN01 a RN12) é implementada na camada de serviço da API.** O app Flutter não decide nada.

O app **pode** repetir uma validação de formato para dar retorno imediato — avisar que a data final é anterior à inicial antes de enviar a requisição é boa experiência. Essa repetição é **cortesia visual, não garantia**. A API revalida sempre, sem exceção.

## Por quê

O app Flutter roda **no dispositivo do usuário**. Tudo que roda ali pode ser inspecionado e alterado por quem o controla.

| Se a regra estivesse no Dart | Consequência |
|---|---|
| RN05 — cálculo da multa | O cliente zera a própria multa |
| RN09 — checagem de permissão | Remover um `if` dá acesso à área administrativa |
| RN02 — conflito de período | Reservas conflitantes entram no banco |
| RN07 — valor de pagamento | Pagamento negativo é aceito |

Esconder um botão **não é** controle de acesso. Uma requisição HTTP pode ser montada à mão, sem passar pela interface.

## Regras concretas

| Regra | Onde |
|---|---|
| Validação de formato (campo vazio, e-mail malformado) | App **e** API |
| Validação de regra de negócio | **Só** API |
| Cálculo de valores | **Só** API |
| Decisão de permissão | **Só** API |
| Transição de estado | **Só** API |
| Identidade do usuário | **Só** API, extraída do JWT |

### A identidade nunca vem do corpo da requisição

```java
// ❌ ERRADO — o app pode mandar qualquer id
public void formalizar(ComandoLocacao cmd) {
    Cliente c = repo.buscar(cmd.getClienteId());
}

// ✅ CERTO — a identidade sai do token
public void formalizar(ComandoLocacao cmd, UsuarioAutenticado usuario) {
    autorizacao.exigirPerfil(usuario, Perfil.ATENDENTE);
}
```

### Separação dentro da própria API

- `web/` recebe, valida formato, delega e responde. **Não contém regra.**
- `service/` contém a regra. **Não conhece HTTP** — não recebe `HttpServletRequest` nem devolve `ResponseEntity`.
- `repository/` persiste. **Não contém regra.**

O teste: se um método de `service/` não pode ser testado sem subir um servidor, a separação falhou.

## Alternativas consideradas

### A — Regra no Flutter, API como CRUD

- ✅ Resposta instantânea, menos código
- ❌ Regra burlável pelo usuário
- ❌ Reescrever tudo se outro cliente consumir a API

### B — Regra duplicada nos dois lados

- ✅ Validação instantânea com garantia no servidor
- ❌ **Duas implementações divergem.** Alguém corrige a fórmula da multa num lado e esquece o outro
- ❌ Dobra o custo de manutenção e de teste

### C — Regra só na API ← **escolhida**

- ✅ Uma implementação, uma fonte da verdade
- ✅ Testável sem interface
- ✅ A API serve qualquer cliente futuro
- ❌ Toda validação de negócio custa uma viagem de rede

## Consequências

**Positivas**

- Regras testáveis com teste de unidade puro, sem subir Flutter nem servidor
- Uma mudança de regra acontece em um lugar só
- Atende diretamente RN09 do documento de regras

**Negativas**

- Validação de negócio tem latência de rede
- Mais código que calcular direto na tela
- Exige disciplina: a tentação de "só esse cálculo rapidinho no Dart" vai aparecer

**Como manter**

Na revisão de PR, procure por: cálculo de valor no Dart, condicional de permissão no Dart, decisão de transição de estado no Dart. Qualquer um desses é motivo para pedir mudanças.
