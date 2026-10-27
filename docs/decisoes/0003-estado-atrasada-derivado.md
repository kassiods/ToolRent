# ADR 0003 — `ATRASADA` como estado derivado

- **Data:** 27/10/2026
- **Status:** ✅ Aceita
- **Decidido por:** equipe

---

## Contexto

A `Locacao` tem o estado `ATRASADA`, aplicável quando o prazo de devolução passou e as ferramentas ainda não voltaram.

Há duas formas de representá-lo:

1. **Persistido** — uma coluna no banco guarda `ATRASADA`, e algum mecanismo atualiza o registro quando o prazo vence
2. **Derivado** — o estado é calculado no momento da leitura, comparando a data prevista com a data atual

O documento de casos de uso v1.0 já sinalizava a preferência: *"O estado ATRASADA poderá ser calculado pelo sistema em tempo de execução a partir do prazo de devolução, em vez de armazenado em banco."*

O guia mestre alerta: *"a equipe deve escolher uma abordagem e documentá-la. Não misturar os dois modelos."*

## Decisão

**`ATRASADA` é um estado derivado.** Não existe coluna no banco com esse valor.

O estado persistido da locação assume apenas: `APROVADA`, `EM_ANDAMENTO`, `DEVOLVIDA`, `CANCELADA`.

A camada de serviço calcula:

```java
if (estadoPersistido == EM_ANDAMENTO && dataPrevistaDevolucao.isBefore(hoje)) {
    return ATRASADA;
}
return estadoPersistido;
```

**O cálculo fica em um único método da camada de serviço.** Nenhuma consulta SQL e nenhum trecho do app Flutter replica essa lógica.

## Alternativas consideradas

### A — Estado persistido

- ✅ Consulta direta: `WHERE estado = 'ATRASADA'`
- ✅ Estado visível ao inspecionar o banco
- ❌ **Exige um processo agendado** rodando diariamente para virar os estados
- ❌ Se o processo falhar ou o sistema ficar um fim de semana parado, o banco fica mentindo
- ❌ A "verdade" depende da última execução do processo, não dos dados

### B — Estado derivado ← **escolhida**

- ✅ Sempre correto, por construção — não existe janela de desatualização
- ✅ Sem processo agendado, sem infraestrutura adicional
- ✅ Menos código e menos modos de falha
- ❌ Consultar locações atrasadas exige comparação de data em vez de igualdade
- ❌ O estado não aparece ao inspecionar a tabela diretamente

## Consequências

**Positivas**

- Impossível o sistema exibir `EM_ANDAMENTO` para uma locação vencida
- Não há processo agendado para monitorar, configurar ou depurar
- Testar o estado é trivial: basta variar a data

**Negativas**

- Toda consulta que filtra por atraso usa `estado = 'EM_ANDAMENTO' AND data_prevista_devolucao < CURRENT_DATE`
- Risco de alguém reimplementar a comparação em outro lugar

**Mitigação do risco**

- O cálculo vive em **um** método da camada de serviço
- Nenhuma consulta nem o app Flutter duplica a lógica
- Teste cobrindo: devolução no prazo, no dia do vencimento e após o vencimento

**Transições afetadas**

`EM_ANDAMENTO → ATRASADA` **não é uma transição executada** — é uma mudança de leitura. A transição real continua sendo `EM_ANDAMENTO → DEVOLVIDA`, com os encargos de atraso aplicados na devolução conforme RN05.

## Nota sobre o diagrama de estados

O diagrama de estados **mantém** `ATRASADA` como estado, com a transição a partir de `EM_ANDAMENTO`. Isso é correto do ponto de vista de modelagem: o estado existe conceitualmente, mesmo não sendo persistido.

O diagrama deve levar a anotação: *"ATRASADA é derivado da data prevista de devolução, não persistido."*
