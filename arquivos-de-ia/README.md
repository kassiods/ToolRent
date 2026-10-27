# Arquivos de IA

Registro do uso de inteligência artificial no desenvolvimento do ToolRent.

---

## Por que esta pasta existe

Duas razões práticas:

**Transparência.** A equipe usa IA como ferramenta de apoio. Registrar como ela foi usada — o que foi pedido, o que foi aceito, o que foi rejeitado — deixa o processo auditável para o professor e honesto entre os integrantes.

**Continuidade.** Uma sessão de IA começa do zero. Os guias desta pasta carregam o contexto do projeto de uma vez, em vez de cada integrante reexplicar tudo toda vez.

---

## Estrutura

```
arquivos-de-ia/
├── guias/        instruções e prompts reutilizáveis
│   ├── guia-mestre.md      contexto completo do projeto
│   └── prompts.md          prompts prontos para tarefas comuns
└── sessoes/      transcrições, uma pasta por integrante
    ├── kassio/
    ├── lincoln/
    ├── victor/
    └── nailan/
```

---

## Como registrar uma sessão

Crie um arquivo em `sessoes/<seu-nome>/` com o nome `AAAA-MM-DD-assunto.md`:

```
sessoes/kassio/2026-11-03-implementacao-us05.md
```

Use o modelo de [`sessoes/README.md`](sessoes/README.md).

### O que vale registrar

| Registre | Não registre |
|---|---|
| Decisão de projeto tomada com apoio da IA | Conversa de tira-dúvida trivial |
| Código gerado que foi para o repositório | Rascunho descartado |
| Sugestão da IA que foi **rejeitada**, e por quê | Transcrição bruta de horas de conversa |
| Erro que a IA cometeu e que a equipe pegou | — |

A última linha importa: **registrar o que a IA errou** é mais valioso que registrar o que ela acertou. Mostra que houve revisão humana.

---

## ⛔ Nunca coloque aqui

| Item | Por quê |
|---|---|
| Chave de API, token, senha | Vazamento permanente no histórico do Git |
| Conteúdo de `.env` | Mesmo motivo |
| String de conexão com credencial | Mesmo motivo |
| Dados pessoais reais | Use dados fictícios |
| E-mail pessoal dos integrantes | O guia mestre já veda isso |

**Revise a transcrição antes de commitar.** É comum colar uma mensagem de erro que inclui a string de conexão inteira.

---

## Regras de uso da IA no projeto

Derivadas do [guia mestre](guias/guia-mestre.md):

1. **A IA não decide pela equipe.** Onde o guia diz "proposta", é hipótese. Decisão que afeta requisitos, domínio, estados, persistência ou arquitetura precisa de confirmação humana.
2. **Código gerado é revisado antes de commitar.** Ninguém commita o que não entende.
3. **A IA não marca teste como aprovado sem executá-lo.** Se não rodou, isso tem que estar dito.
4. **A IA não reescreve o projeto inteiro.** Menor alteração coerente, preservando o que funciona.
5. **Decisão tomada com apoio da IA vira ADR** em `docs/decisoes/`, como qualquer outra.

---

## Conteúdo dos guias

| Arquivo | Para quê |
|---|---|
| [`guias/guia-mestre.md`](guias/guia-mestre.md) | Contexto completo do projeto. Carregue no início de uma sessão nova. |
| [`guias/prompts.md`](guias/prompts.md) | Prompts prontos para tarefas recorrentes. |

> ⚠️ **O guia mestre descreve a stack original** (JavaFX desktop + SQLite). A equipe mudou para Flutter + API Java + Supabase — ver [ADR 0001](../docs/decisoes/0001-stack-flutter-java-supabase.md). O guia continua válido para requisitos, regras de negócio, processo e critérios de qualidade; **a parte de tecnologia está superada.**
