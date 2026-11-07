# Diagrama de Casos de Uso — ToolRent

> **Esta é a fonte do diagrama.** O bloco Mermaid abaixo é renderizado pelo próprio GitHub —
> não existe imagem a gerar nem ferramenta a instalar. Ao alterar o diagrama, altere este
> arquivo e atualize [`../casos-de-uso.md`](../casos-de-uso.md) no mesmo PR.
>
> Documento correspondente: [`docs/casos-de-uso.md`](../casos-de-uso.md) (versão 2.0)
> Notação: ver [ADR 0008](../decisoes/0008-mermaid-no-lugar-do-plantuml.md).

**3 atores · 16 casos de uso · 3 CRUD puro**

```mermaid
flowchart LR
    %% ================= ATORES =================
    %% O ator "Técnico de manutenção" foi removido do escopo.
    %% Justificativa: docs/decisoes/0002-remocao-do-ator-tecnico.md
    ADM["👤 Administrador"]
    ATD["👤 Atendente"]
    CLI["👤 Cliente"]

    %% ============ FRONTEIRA DO SISTEMA ============
    subgraph SISTEMA["ToolRent"]
        direction TB
        UC01("UC01<br/>Autenticar usuário")
        UC02("UC02<br/>Gerenciar usuários e perfis")
        UC03("UC03<br/>Gerenciar clientes")
        UC04("UC04<br/>Gerenciar ferramentas e categorias")
        UC05("UC05<br/>Consultar disponibilidade por período")
        UC06("UC06<br/>Consultar catálogo e preços")
        UC07("UC07<br/>Solicitar reserva")
        UC08("UC08<br/>Analisar solicitação de reserva")
        UC09("UC09<br/>Formalizar locação")
        UC10("UC10<br/>Registrar retirada")
        UC11("UC11<br/>Registrar devolução e avaliar condição")
        UC12("UC12<br/>Calcular valores e encargos")
        UC13("UC13<br/>Registrar pagamento")
        UC14("UC14<br/>Gerenciar manutenção")
        UC15("UC15<br/>Consultar relatórios")
        UC16("UC16<br/>Consultar histórico de locações")
    end

    %% ================ ASSOCIAÇÕES ================
    ADM --- UC01
    ADM --- UC02
    ADM --- UC15

    ATD --- UC01
    ATD --- UC03
    ATD --- UC04
    ATD --- UC05
    ATD --- UC08
    ATD --- UC09
    ATD --- UC10
    ATD --- UC11
    ATD --- UC12
    ATD --- UC13
    ATD --- UC14
    ATD --- UC16

    CLI --- UC06
    CLI --- UC07

    %% ================== RELAÇÕES ==================
    %% Apenas relações reais. Não há <<extend>> no escopo.
    UC07 -.->|"«include»"| UC05
    UC09 -.->|"«include»"| UC05
    UC09 -.->|"«include»"| UC12
    UC11 -.->|"«include»"| UC12

    %% =================== ESTILO ===================
    classDef ator fill:#DBEAFE,stroke:#1E3A8A,stroke-width:2px,color:#1E3A8A
    classDef caso fill:#FFFFFF,stroke:#1E40AF,stroke-width:1px,color:#0F172A
    classDef incluido fill:#FEF3C7,stroke:#B45309,stroke-width:2px,color:#7C2D12

    class ADM,ATD,CLI ator
    class UC01,UC02,UC03,UC04,UC06,UC07,UC08,UC09,UC10,UC11,UC13,UC14,UC15,UC16 caso
    class UC05,UC12 incluido
```

## Legenda

| Elemento | Significado |
|---|---|
| Retângulo azul-claro | Ator — perfil de usuário que interage com o sistema |
| Caixa arredondada branca | Caso de uso |
| Caixa arredondada âmbar | Caso de uso **incluído** por outros (UC05 e UC12) |
| Linha contínua | Associação ator ↔ caso de uso |
| Seta tracejada `«include»` | Comportamento sempre executado pelo caso de uso de origem |
| Moldura **ToolRent** | Fronteira do sistema |

## Correspondência com os atores

| Ator | Casos de uso |
|---|---|
| **Administrador** | UC01, UC02, UC15 |
| **Atendente** | UC01, UC03, UC04, UC05, UC08–UC14, UC16 |
| **Cliente** | UC06, UC07 |

## Relações `«include»` declaradas

| Origem | Destino | Por quê |
|---|---|---|
| UC07 Solicitar reserva | UC05 Consultar disponibilidade | Não se solicita reserva sem antes verificar o período |
| UC09 Formalizar locação | UC05 Consultar disponibilidade | A formalização revalida a disponibilidade no momento do contrato |
| UC09 Formalizar locação | UC12 Calcular valores | O contrato nasce com o valor base calculado |
| UC11 Registrar devolução | UC12 Calcular valores | A devolução recalcula atraso e danos |

Não há `«extend»` no escopo: nenhum caso de uso atual é extensão opcional de outro.
