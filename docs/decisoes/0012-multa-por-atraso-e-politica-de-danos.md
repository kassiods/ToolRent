# ADR 0012 — Multa por atraso e política de danos

- **Data:** 07/11/2026
- **Status:** ✅ Aceita
- **Decidido por:** Kassio de Sousa Dias, em nome da equipe

> ✅ Resolve a decisão pendente **#4**. A equipe delegou a escolha.

---

## Contexto

RN05 (atraso) e RN06 (danos) estavam sem fórmula. O guia mestre deixava três opções para o atraso
— multa fixa, percentual, tarifa por diária de atraso ou combinação — e exigia que a política de
danos fosse aprovada antes de qualquer cálculo automático.

`application.yml` trazia `percentual-multa-atraso: 0.20` marcado como **provisório**.

Sem esta decisão, US11 e US12 ficavam bloqueadas.

## Decisão

### Atraso (RN05)

**Multa por diária de atraso, proporcional à tarifa da ferramenta**, acumulativa:

```
diariasAtraso = ceil( (devolucaoReal - devolucaoPrevista) / 24h )     -- mesma contagem do ADR 0011
multaAtraso   = soma, por item:
                  tarifaRegistrada × quantidade × diariasAtraso × PERCENTUAL_MULTA
PERCENTUAL_MULTA = 0,20   (20% da diária, por diária de atraso)
```

- A multa **só existe** se `devolucaoReal > devolucaoPrevista`. Devolução no prazo: multa zero
- As diárias de atraso são contadas **a partir** do prazo, nunca recontando o período já cobrado — não há cobrança em duplicidade
- Arredondamento: `HALF_UP`, duas casas, uma única vez no total — não a cada item (RN12)

Exemplo: tarifa R$ 50,00 · 2 unidades · 2 diárias de atraso → `50 × 2 × 2 × 0,20` = **R$ 40,00**.

### Danos (RN06)

**Nenhum valor de dano é calculado automaticamente.** O fluxo é:

1. O Atendente registra, na devolução: descrição do dano e **valor estimado**
2. O dano nasce com `danosAprovados = false` e **não entra** no valor devido
3. Um usuário de perfil `ADMINISTRADOR` aprova ou rejeita o valor
4. Só depois de aprovado o valor entra no total da locação

```
valorTotal = valorBase + multaAtraso + (danosAprovados ? valorDanos : 0)
```

**Por que exigir Administrador:** cobrar dano é discricionário e envolve conflito com o cliente.
Separar quem avalia de quem autoriza é controle interno elementar, e o campo `danosAprovados` já
existia no rascunho da migration à espera desta regra.

## Alternativas consideradas

### Atraso

#### A — Multa fixa por locação
- ✅ Trivial de calcular
- ❌ Atrasar 1 dia custa o mesmo que atrasar 30: não desestimula o atraso longo

#### B — Tarifa cheia por diária de atraso
- ✅ Simples e forte
- ❌ Atraso vira locação não acordada; não distingue descuido de apropriação

#### C — Percentual da diária por diária de atraso ← **escolhida**
- ✅ Proporcional ao valor da ferramenta e ao tamanho do atraso
- ✅ Um único parâmetro configurável, em um lugar só
- ✅ Reaproveita a contagem de diárias do ADR 0011
- ❌ Exige explicar ao cliente que a multa é percentual, não a diária inteira

### Danos

#### A — Valor automático por tabela de avarias
- ✅ Sem discricionariedade
- ❌ Exige uma tabela de avarias que a equipe não tem e o escopo não comporta

#### B — Avaliação do Atendente, aprovação do Administrador ← **escolhida**
- ✅ Registro e autorização separados
- ✅ Usa o `danosAprovados` que já existe no modelo
- ❌ Depende de alguém aprovar para fechar o valor

## Consequências

**Positivas**
- RN05 e RN06 ficam calculáveis e testáveis
- Nenhum percentual literal no código: `percentual-multa-atraso` vive em `config/`
- US11 e US12 deixam de estar bloqueadas

**Negativas**
- Uma devolução com dano fica com valor **em aberto** até a aprovação. A interface precisa mostrar "aguardando aprovação de dano", não um total que muda sozinho

**Riscos**
- Dupla contagem do período de atraso. **Mitigação:** teste obrigatório para devolução no prazo, no dia exato do vencimento e após o vencimento, conferindo que o valor-base não muda
- Alguém reimplementar a fórmula em outro lugar. **Mitigação:** o cálculo vive em um único serviço, como manda o [ADR 0004](0004-regra-de-negocio-na-api.md)

## Impacto

| Onde | O que muda |
|---|---|
| `docs/regras-de-negocio.md` | RN05 e RN06 ganham fórmula e perdem o aviso de pendência |
| `application.yml` | `percentual-multa-atraso: 0.20` deixa de ser provisório |
| US11, US12 | Deixam de estar bloqueadas |
| UC11 | O fluxo de devolução passa a ter o passo de aprovação de dano pelo Administrador |
