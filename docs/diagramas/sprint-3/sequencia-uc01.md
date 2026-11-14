ja # Diagrama de Sequência — Autenticar-se e autorizar por perfil (UC01)

> **Especificação da US01 e do contrato de autorização que a US02 consome.**
> Documento correspondente: [`casos-de-uso.md`](../../casos-de-uso.md) (UC01) ·
> [`backlog.md`](../../backlog.md#us01) · regras RN09 e RN10 ·
> [ADR 0004](../../decisoes/0004-regra-de-negocio-na-api.md) ·
> [ADR 0005](../../decisoes/0005-login-proprio-do-cliente.md)
>
> Nasceu na Sprint 3, para implementação na Sprint 3 — ver [plano](../../sprints/sprint-3.md).

A senha nunca passa pela API do ToolRent nem fica no banco da aplicação (RN10): quem autentica
é o **Supabase Auth**. A API só **valida o JWT** e decide a autorização pelo perfil gravado na
tabela `usuario` (RN09). O app não decide nada — exibe o que a API responder (ADR 0004).

## Parte 1 — Login e abertura da tela do perfil

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant LS as LoginScreen
    participant AR as AuthRepository
    participant SA as Supabase Auth
    participant API as API ToolRent
    participant HOME as Home do perfil

    U->>LS: informa e-mail e senha
    LS->>AR: entrar(email, senha)
    %% O widget nunca chama HTTP direto - repositorio e a unica porta de rede
    AR->>SA: signInWithPassword(email, senha)

    alt credencial valida
        SA-->>AR: sessao com JWT
        AR->>API: GET /api/usuarios/me (Authorization: Bearer JWT)
        %% A API valida a assinatura do JWT e busca o usuario por auth_user_id
        API->>API: valida JWT e carrega usuario

        alt usuario ativo
            API-->>AR: 200 perfil (ADMINISTRADOR, ATENDENTE ou CLIENTE)
            AR-->>LS: perfil autenticado
            LS->>HOME: abre a tela inicial do perfil
            %% As tres telas ja existem desde a Sprint 2 - o login so escolhe qual abrir
        else usuario inativo
            API-->>AR: 403 usuario inativo
            AR-->>LS: bloqueado
            LS-->>U: mensagem especifica de conta inativa
        end

    else credencial invalida
        SA-->>AR: erro de autenticacao
        AR-->>LS: falha
        %% Mensagem generica: nao revela se o e-mail existe (criterio da US01)
        LS-->>U: mensagem generica de credencial invalida
    end
```

## Parte 2 — Requisição protegida e autorização por perfil (RN09)

O mesmo contrato vale para **todo** endpoint protegido. A US02 é a primeira a usá-lo: os
endpoints de gerenciar usuários exigem perfil `ADMINISTRADOR`.

```mermaid
sequenceDiagram
    autonumber
    participant APP as App Flutter (repositorio)
    participant F as Filtro JWT (web)
    participant S as Service (regra RN09)
    participant R as Repository (dados)

    APP->>F: requisicao a endpoint protegido

    alt sem token ou token invalido
        F-->>APP: 401 nao autenticado
    else token valido
        F->>S: requisicao com identidade do usuario
        %% web/ nao contem regra: o filtro so estabelece QUEM e
        S->>S: verifica o perfil exigido pelo caso de uso

        alt perfil nao autorizado
            S-->>APP: 403 perfil sem permissao
            %% Esconder botao no app nao e controle de acesso - a decisao e aqui
        else perfil autorizado
            S->>R: executa a operacao
            R-->>S: resultado
            S-->>APP: 200 resposta
        end
    end
```

## Legenda

| Participante | O que é | Onde mora |
|---|---|---|
| `LoginScreen` | Tela de login — substitui o seletor de perfil provisório da Sprint 2 | `app/lib/ui/screens/` |
| `AuthRepository` | Única porta de saída de rede do fluxo de autenticação | `app/lib/data/repositories/` |
| `Supabase Auth` | Identidade, senha e emissão do JWT — a senha vive só aqui (RN10) | Supabase |
| `API ToolRent` / `Filtro JWT` / `Service` | Validação do token em `web/`, autorização (RN09) em `service/` | `api/src/main/java/br/edu/toolrent/api/` |
| `Home do perfil` | `home_administrador_screen` · `home_atendente_screen` · `home_cliente_screen` | entregues na Sprint 2 |

## Decisões que este diagrama aplica

| Ponto | Decisão |
|---|---|
| Senha nunca na API nem no banco da aplicação; `usuario.auth_user_id` referencia o Supabase Auth | RN10 · [ADR 0004](../../decisoes/0004-regra-de-negocio-na-api.md) |
| Perfil `CLIENTE` entra pelo mesmo login — vínculo `Cliente ↔ Usuario` é opcional | [ADR 0005](../../decisoes/0005-login-proprio-do-cliente.md) |
| Autorização no `service/`, nunca no app nem no filtro — `web/` não contém regra | RN09 · [arquitetura](../../arquitetura.md) |
| Mensagem genérica para credencial inválida; específica para conta inativa | critérios da [US01](../../backlog.md#us01) |
