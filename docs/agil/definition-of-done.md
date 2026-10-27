# Definition of Done — ToolRent

> **Uma história não está concluída porque o código foi escrito.**
> Esta é a lista que separa "terminei de programar" de "está pronto".

---

## A checklist

Copie e cole no Pull Request. Todos os itens aplicáveis precisam estar marcados antes do merge.

```markdown
### Definition of Done

**Funcionalidade**
- [ ] Todos os critérios de aceite da história estão satisfeitos
- [ ] Os fluxos alternativos e de exceção do caso de uso foram implementados
- [ ] As regras de negócio envolvidas estão na camada de serviço da API

**Qualidade**
- [ ] O projeto compila sem erro (`mvn clean install` e `flutter build`)
- [ ] Os testes relevantes foram executados e passaram
- [ ] Nenhum teste existente foi quebrado
- [ ] Não há número mágico, regra duplicada ou `System.out.println` de depuração

**Documentação**
- [ ] O diagrama afetado foi atualizado e corresponde ao código entregue
- [ ] A documentação afetada (casos de uso, regras, README) foi atualizada
- [ ] Decisões não óbvias foram registradas em `docs/decisoes/`

**Rastreabilidade**
- [ ] O PR referencia a Issue (`Closes #N`)
- [ ] O PR informa US, UC, RN e diagrama relacionados
- [ ] A branch segue a convenção de nome

**Segurança**
- [ ] Nenhuma credencial, token, `.env` ou arquivo de build foi commitado
- [ ] `git status` e `git diff` foram revisados antes do commit
- [ ] Dados de demonstração estão identificados como fictícios

**Revisão**
- [ ] Pelo menos um colega revisou e aprovou
- [ ] Outro integrante consegue executar e validar seguindo o README
```

---

## Por que cada bloco existe

### Funcionalidade

O enunciado exige que **o código implemente exatamente o que o diagrama especifica**. Entregar só o fluxo principal e deixar os alternativos para depois quebra essa correspondência: o diagrama promete um comportamento que o código não tem.

A regra na camada certa não é preciosismo. Regra no controller não pode ser testada sem subir HTTP. Regra no app Flutter pode ser burlada pelo usuário.

### Qualidade

"Compila na minha máquina" não conta se ninguém verificou. **Se os testes não foram executados, diga que não foram** — nunca marque como aprovado o que não rodou.

Número mágico é dívida garantida: a taxa de multa escrita direto no meio de um método vai ser esquecida quando o valor mudar.

### Documentação

A disciplina avalia a coerência entre diagrama e código. Um diagrama desatualizado é pior que nenhum — ele mente sobre o sistema.

### Rastreabilidade

Exigência explícita: **cada commit de entrega deve ser rastreável até um elemento de diagrama.** Sem o `Closes #N` e a menção ao UC, essa linha se perde e não há como provar a origem do trabalho.

### Segurança

Um segredo commitado **permanece no histórico do Git** mesmo depois de removido do arquivo. Remover o arquivo num commit seguinte não resolve — a chave precisa ser revogada.

### Revisão

Quem escreveu o código é a pessoa menos capaz de enxergar o que ficou confuso nele.

---

## O que **não** conta como pronto

| Situação | Por que não conta |
|---|---|
| "O código está lá, só falta testar" | Não testado é não verificado |
| "Funciona, mas o diagrama está desatualizado" | A disciplina avalia a correspondência |
| "Deixei um TODO para a validação" | Validação pendente é regra ausente |
| "Está num branch, não deu tempo de abrir PR" | Trabalho não integrado não é entregue |
| "Os testes falham, mas é problema do ambiente" | Então o problema do ambiente é parte da história |
| "Commitei o `.env` mas depois removi" | A chave vazou. Revogue. |

---

## Exceções

Uma história pode ser concluída com uma limitação conhecida **se, e somente se**:

1. A limitação estiver **escrita no PR**, não combinada verbalmente
2. Uma Issue nova for aberta para resolvê-la
3. A limitação não comprometer um critério de aceite da história

Exemplo aceitável: *"Os testes de integração do repositório não rodaram porque o ambiente local não tem Maven instalado. Testes de unidade do service passaram. Issue #42 aberta."*

Exemplo **não** aceitável: *"A validação de período ficou para depois."* Isso é a história incompleta, não uma limitação.
