# Prompts reutilizáveis

Prompts prontos para tarefas recorrentes do ToolRent. Copie, preencha o que
está entre `<>` e cole na sua ferramenta de IA.

**Sempre anexe ou cole o [guia mestre](guia-mestre.md) antes**, ou a IA não
terá o contexto do projeto.

---

## 1. Abrir uma sessão nova

```
Você vai me ajudar no ToolRent, um sistema de locação de ferramentas,
projeto da disciplina de Análise e Projeto de Sistemas.

Antes de sugerir qualquer coisa, leia nesta ordem:
- README.md
- docs/casos-de-uso.md
- docs/regras-de-negocio.md
- docs/arquitetura.md
- docs/decisoes/

Regras do projeto:
1. Regra de negócio fica SEMPRE na camada de serviço da API Java,
   nunca no app Flutter (ADR 0004).
2. Dinheiro em BigDecimal, nunca double (RN12).
3. Não invente decisão. Onde a documentação diz "pendente", pergunte.
4. Não reescreva o que já funciona. Menor alteração coerente.
5. Se não executou um teste, diga que não executou.

Me diga em poucas linhas o estado real do projeto antes de começarmos.
```

---

## 2. Implementar uma história

```
Vou implementar a <USxx>.

Contexto obrigatório:
- História e critérios de aceite: docs/backlog.md
- Caso de uso: docs/casos-de-uso.md, <UCxx>
- Regras: docs/regras-de-negocio.md, <RNxx, RNyy>

Antes de escrever código, me responda:
1. Quais arquivos precisam ser criados ou alterados?
2. Em que camada cada regra entra?
3. Quais testes provam que os critérios de aceite estão satisfeitos?
4. Alguma decisão pendente bloqueia essa história?

Só depois que eu confirmar, implemente — limitado a esse escopo.
```

---

## 3. Revisar código antes do PR

```
Revise este código como se fosse um Pull Request do ToolRent.

Verifique:
- A regra de negócio está na camada certa? (service/, nunca no Flutter)
- Há número mágico que deveria estar em config/?
- Dinheiro usa BigDecimal?
- Estado usa enum e a transição é validada?
- Os testes cobrem os casos de borda ou só o caminho feliz?
- Há credencial, token ou .env no diff?
- Exceção sendo capturada e silenciada?

Aponte problemas concretos, com o trecho e a correção. Não elogie código
que está apenas correto.

<cole o diff aqui>
```

---

## 4. Escrever testes

```
Escreva testes para <classe ou método>, cobrindo <UCxx> e <RNxx>.

Requisitos:
- JUnit 5 + AssertJ
- Nomes de teste em português, descrevendo o comportamento
- Cobrir: caminho feliz, limites, e cada fluxo de exceção do caso de uso
- Teste de unidade puro — sem subir servidor nem banco

Casos de borda que não podem faltar:
<liste>
```

---

## 5. Atualizar diagrama após mudança

```
Alterei <o que mudou> em <arquivo>.

Verifique se docs/diagramas/<diagrama>.puml continua correspondendo ao
código. Se não:
1. Aponte exatamente a divergência
2. Proponha o .puml corrigido
3. Diga que documentos em docs/ também precisam mudar

Lembre: o enunciado exige que o código implemente exatamente o que o
diagrama especifica.
```

---

## 6. Investigar um defeito

```
Comportamento esperado (conforme <UCxx> / <RNxx>):
<o que deveria acontecer>

Comportamento observado:
<o que acontece>

Como reproduzir:
<passos>

Investigue a CAUSA antes de propor correção. Não sugira mudança sem
explicar por que o problema ocorre. Se precisar de mais informação para
ter certeza, peça.
```

---

## Prompts a evitar

| ❌ Não peça | Por quê |
|---|---|
| "Implemente o sistema inteiro" | Sai genérico, ignora os casos de uso e vira retrabalho |
| "Refatore tudo" | O guia veda refatoração ampla sem motivo e confirmação |
| "Escolha a melhor opção por mim" (em decisão pendente) | Decisão de projeto é da equipe, não da IA |
| "Pode commitar e dar push" | Commit sem revisão humana é commit sem dono |
| "Esse teste passa, né?" | Teste não executado não passou |
