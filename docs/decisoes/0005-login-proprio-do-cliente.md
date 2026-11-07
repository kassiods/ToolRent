# ADR 0005 — Cliente com login próprio

- **Data:** 27/10/2026
- **Status:** 🟡 Proposta
- **Decidido por:** — *aguardando aprovação da equipe*

> ⚠️ **Esta decisão ainda não vale.** Corresponde à decisão pendente **#1** de
> [`casos-de-uso.md § 6`](../casos-de-uso.md#6-decisões-pendentes). Enquanto o status for
> 🟡 Proposta, não implemente como se estivesse aprovada — ver `CLAUDE.md`, regra 2.

---

## Contexto

O escopo prevê que o Cliente consulte o catálogo (UC06) e solicite reservas (UC07). Falta definir **como** ele faz isso:

1. **Com login próprio** — o Cliente autentica no sistema e opera sozinho
2. **Sem login** — o Cliente liga ou vai à loja, e o Atendente lança as solicitações em seu nome

A escolha não é de interface: ela muda o modelo de dados, a autorização e o volume de trabalho das Sprints 3 e 4.

### O que os artefatos já entregues dizem

O diagrama de casos de uso — **gerado, versionado e parte da entrega aprovada pelo professor** ([ADR 0001](0001-stack-flutter-java-supabase.md)) — declara três atores:

```
actor "Administrador" as ADM
actor "Atendente"     as ATD
actor "Cliente"       as CLI
```

A tabela de atores do [`README.md`](../../README.md#2-atores-e-perfis) atribui **UC06 e UC07 ao Cliente**, não ao Atendente. O enum `perfil_usuario` da migration inicial já inclui `CLIENTE`.

Ou seja: a opção 1 é a que **os documentos já aprovados descrevem**. A opção 2 exigiria redesenhar o diagrama e remover um ator — e a equipe já removeu um ator uma vez ([ADR 0002](0002-remocao-do-ator-tecnico.md)); remover o segundo reduziria o escopo a dois atores.

## Decisão proposta

**O Cliente tem login próprio.** Autentica pelo Supabase Auth com perfil `CLIENTE` e opera UC06 e UC07 por conta própria.

O Atendente **também** pode registrar uma solicitação em nome de um cliente — isso é atendimento de balcão, não substituto do autoatendimento. As duas portas existem; a regra de negócio é a mesma nas duas (RN01, RN02, RN03).

Um `cliente` sem conta de acesso continua válido: o cadastro (UC03) não exige login. O vínculo `cliente.auth_user_id` é **opcional**.

## Alternativas consideradas

### A — Cliente com login próprio ← **proposta**

- ✅ Coerente com o diagrama aprovado e com a tabela de atores do README
- ✅ Não exige redesenhar artefato já entregue
- ✅ UC06 e UC07 ficam com o ator que a especificação lhes atribui
- ✅ Demonstração mais rica na apresentação: três perfis com menus distintos
- ❌ Exige RLS para o Cliente ver somente os próprios dados
- ❌ Mais telas e mais superfície de autenticação na Sprint 3

### B — Sem login; o Atendente registra tudo

- ✅ Bem menos trabalho: nenhuma tela de cliente, RLS trivial
- ✅ Um perfil a menos para testar
- ❌ **Contradiz o diagrama aprovado pelo professor** — exigiria removê-lo e justificar
- ❌ O ator Cliente deixaria de existir, reduzindo o escopo a dois atores
- ❌ UC06 e UC07 perderiam o ator que a especificação lhes dá

## Consequências

**Positivas**

- Diagrama, README, migration e código passam a dizer a mesma coisa
- A autorização por perfil (RN09) é exercitada de verdade, com três perfis reais

**Negativas**

- A Sprint 3 cresce: além do login, precisa de RLS por cliente
- `cliente.auth_user_id` nulo é um caso que precisa de teste — cliente de balcão sem conta

**Mitigação**

- RLS é a última linha de defesa, não a primeira: a autorização é validada na API (RN09, [ADR 0004](0004-regra-de-negocio-na-api.md))
- O vínculo opcional cliente↔conta deve ser coberto por teste nos dois caminhos

## Se a equipe decidir o contrário

Marcar este ADR como ❌ Rejeitada, escrever o ADR que registra a opção B e **atualizar o diagrama de casos de uso no mesmo PR** — o ator `CLI` e os vínculos com UC06 e UC07 saem.
