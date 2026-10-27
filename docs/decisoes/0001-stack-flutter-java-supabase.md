# ADR 0001 — Stack: Flutter + Java/Spring Boot + Supabase

- **Data:** 27/10/2026
- **Status:** ✅ Aceita — **aprovada pelo professor em 27/10/2026**
- **Decidido por:** equipe, com aprovação do professor Anthony Irlan Marques Luz

---

## Contexto

O planejamento inicial do projeto previa uma aplicação **desktop em Java 21 com JavaFX, Maven e SQLite via JDBC**, conforme o guia mestre do projeto e a versão 1.0 do documento de casos de uso.

A equipe optou por mudar a stack para uma aplicação **Flutter consumindo uma API Java, com Supabase como banco**, pelos seguintes motivos:

- Interface mais moderna e apresentável, com um código servindo web, desktop e mobile
- Banco gerenciado, sem necessidade de cada integrante manter um arquivo SQLite local
- Autenticação pronta (Supabase Auth), eliminando a implementação própria de hash de senha
- Caminho natural para publicação na web

## Decisão

Adotar três componentes:

| Componente | Tecnologia | Papel |
|---|---|---|
| `app/` | Flutter 3.44 / Dart 3.12 | Interface multiplataforma |
| `api/` | Java 21 / Spring Boot 3.3 / Maven | Regras de negócio, autorização e cálculos |
| `supabase/` | PostgreSQL 15 + Supabase Auth | Persistência e identidade |

**Java permanece no projeto** como camada de negócio. Isso preserva a exigência de Java 21 e Maven do enunciado, e mantém as regras num lugar confiável — fora do dispositivo do usuário.

## Alternativas consideradas

### A — Manter JavaFX desktop com SQLite

Era o plano original e o que o enunciado descreve literalmente.

- ✅ Aderência total ao enunciado, sem necessidade de aprovação
- ✅ Menos partes móveis: um único processo, sem rede
- ❌ Interface datada e difícil de apresentar
- ❌ Sem caminho para a web

### B — Flutter falando direto com o Supabase, sem API Java

- ✅ Mais simples: uma linguagem, sem backend próprio
- ✅ Menos código para escrever
- ❌ **Elimina o Java do projeto**, contrariando o enunciado
- ❌ Regra de negócio no dispositivo do usuário, onde pode ser alterada

### C — Flutter + API Java + Supabase ← **escolhida**

- ✅ Mantém Java 21 e Maven
- ✅ Regra de negócio em servidor confiável
- ✅ Interface moderna e publicável na web
- ❌ Mais complexo: dois runtimes, deploy da API, CORS
- ❌ Depende de serviço externo (Supabase)

## Consequências

**Positivas**

- Regras de negócio testáveis isoladamente, sem subir interface
- A mesma API pode servir outros clientes no futuro
- Supabase Auth cuida de RN10 (hash de senha) sem código próprio

**Negativas**

- Mais trabalho de infraestrutura que a opção desktop
- Requer conexão com a internet para desenvolver e demonstrar
- A conta gratuita do Supabase pausa projetos inativos por uma semana — é preciso reativar antes de uma apresentação

**Riscos**

- A divergência com o texto original do enunciado (JavaFX, SQLite, `mvn javafx:run`) deixou de ser risco após a aprovação registrada abaixo.
- Permanece a dependência de um serviço externo: sem internet, não há como desenvolver nem demonstrar.

## ✅ Aprovação do professor

O guia mestre do projeto é explícito: *"Não presumir que haverá aplicativo web, aplicativo móvel, nuvem, API remota... Essas integrações só entram se a equipe e o professor aprovarem explicitamente."*

A aprovação foi obtida.

| Campo | Valor |
|---|---|
| Apresentado ao professor em | 27/10/2026 |
| Resposta | Aprovado |
| Registrado por | Kassio de Sousa Dias (@kassiods) |

Com isso, Flutter, Spring Boot e Supabase passam a ser a stack oficial do projeto. O Java 21 e o Maven, exigidos pelo enunciado, permanecem no módulo `api/`.

> Se o professor pedir evidência escrita da aprovação (mensagem, e-mail ou ata de aula), anexe a referência nesta tabela. Aprovação verbal não registrada é difícil de sustentar numa avaliação.
