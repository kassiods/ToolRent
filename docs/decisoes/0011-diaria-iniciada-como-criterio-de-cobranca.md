# ADR 0011 — Diária iniciada como critério de cobrança

- **Data:** 07/11/2026
- **Status:** ✅ Aceita
- **Decidido por:** Kassio de Sousa Dias, em nome da equipe

> ✅ Resolve a decisão pendente **#3**. A equipe delegou a escolha.

---

## Contexto

RN04 define o valor-base da locação, mas deixava em aberto **como contar as diárias**. O guia
mestre lista as opções: dias corridos, períodos de 24 horas, diárias iniciadas ou outro critério.

`api/src/main/resources/application.yml` já tinha `criterio-diaria: DIARIA_INICIADA`, marcado como
**provisório**, com o aviso de não implementar antes desta decisão.

A escolha muda o valor cobrado do cliente, então não pode ficar implícita no código.

## Decisão

**Diária iniciada.** Toda diária começada é cobrada integralmente.

```
diariasCobradas = ceil( (devolucaoPrevista - inicio) / 24h )
mínimo de 1 diária
```

| Retirada | Devolução | Duração | Diárias cobradas |
|---|---|---|---|
| seg 08:00 | seg 17:00 | 9 h | **1** |
| seg 08:00 | ter 07:00 | 23 h | **1** |
| seg 08:00 | ter 09:00 | 25 h | **2** |
| seg 08:00 | qua 08:00 | 48 h | **2** |
| seg 08:00 | qua 08:01 | 48 h 1 min | **3** |

O enum `CriterioDiaria` tem um único valor nesta versão: `DIARIA_INICIADA`. O parâmetro continua
em `config/`, nunca literal no meio de um método.

## Alternativas consideradas

### A — Períodos exatos de 24 horas, com fração proporcional
- ✅ "Justo" do ponto de vista matemático
- ❌ Exige decidir o arredondamento da fração, que é outra decisão escondida
- ❌ Cliente não consegue conferir a conta de cabeça
- ❌ Ferramenta devolvida 20 minutos atrasada geraria cobrança quebrada

### B — Dias corridos de calendário
- ✅ Simples de calcular
- ❌ Retirar 23:00 de segunda e devolver 01:00 de terça cobraria 2 diárias por 2 horas de uso
- ❌ Penaliza retirada no fim do dia

### C — Diária iniciada ← **escolhida**
- ✅ É o critério que locadoras reais usam; o cliente já conhece
- ✅ Conta conferível de cabeça
- ✅ A ferramenta fica indisponível para outros durante toda a diária, mesmo que usada por 2 horas — a cobrança reflete o custo real de oportunidade
- ❌ Uso de 25 horas custa o dobro de um uso de 23 horas: o salto no limite incomoda

## Consequências

**Positivas**
- RN04 fica calculável e testável com números inteiros
- A memória de cálculo (UC12) consegue mostrar "3 diárias × R$ 50,00 × 2 unidades"

**Negativas**
- O salto no limite das 24 h precisa estar visível na interface **antes** de o cliente confirmar

**Mitigação**
- UC07 e UC09 exibem o valor estimado e a quantidade de diárias antes da confirmação
- Teste obrigatório cobrindo as cinco linhas da tabela acima, incluindo os limites de 24 h e 48 h

## Impacto

| Onde | O que muda |
|---|---|
| `docs/regras-de-negocio.md` | RN04 deixa de ter o aviso de pendência |
| `application.yml` | `criterio-diaria: DIARIA_INICIADA` deixa de ser provisório |
| US12 / UC12 | Deixa de estar bloqueada por esta decisão |
