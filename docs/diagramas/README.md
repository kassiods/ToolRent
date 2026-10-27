# Diagramas — ToolRent

> **Regra de ouro da disciplina:** o código entregue implementa exatamente o que o diagrama especifica.
> Diagrama e implementação são atualizados **no mesmo Pull Request**. Nunca um sem o outro.

---

## Diagramas do projeto

| Diagrama | Fonte | Imagem | Status | Sprint |
|---|---|---|---|---|
| Casos de uso | [`casos-de-uso.puml`](casos-de-uso.puml) | [`casos-de-uso.jpg`](casos-de-uso.jpg) | ✅ | 1 |
| Classes de domínio | `classes.puml` | — | ⬜ | 2 |
| Estados da Locação | `estados-locacao.puml` | — | ⬜ | 2 |
| Estados da Reserva | `estados-reserva.puml` | — | ⬜ | 2 |
| Sequência — Formalizar locação | `sequencia-uc09.puml` | — | ⬜ | 5 |

---

## Por que a fonte `.puml` é versionada

O checklist de entrega exige: *"O arquivo-fonte do diagrama está versionado no repositório."*

Uma imagem solta não pode ser revisada em Pull Request, não mostra o que mudou entre versões e não pode ser corrigida por quem não tem a ferramenta original. O arquivo `.puml` é texto — aparece no `git diff` como qualquer código.

**Sempre commite a fonte e a imagem juntas.**

---

## Como gerar a imagem

### Opção 1 — Servidor online (sem instalar nada)

Cole o conteúdo do `.puml` em [plantuml.com/plantuml](https://www.plantuml.com/plantuml/uml/) e baixe o PNG.

### Opção 2 — VS Code

Instale a extensão **PlantUML** (jebbs.plantuml). Com o arquivo aberto, pressione `Alt+D` para pré-visualizar e use o comando *PlantUML: Export Current Diagram* para gerar a imagem.

Requer Java instalado — que o projeto já exige.

### Opção 3 — Linha de comando

```bash
# Instale o PlantUML (requer Java)
# Windows: choco install plantuml
# macOS:   brew install plantuml

plantuml -tpng casos-de-uso.puml
```

---

## Convenções

**Nome do arquivo:** kebab-case, descrevendo o conteúdo — `casos-de-uso.puml`, `estados-locacao.puml`, `sequencia-uc09.puml`.

**Dentro do arquivo:**

- Cabeçalho em comentário indicando o documento correspondente
- Identificadores dos elementos iguais aos da documentação (`UC01`, não `uc1` nem `autenticar`)
- Apenas relações reais — `«include»` e `«extend»` não são enfeite

**`«include»` vs `«extend»`:**

| Relação | Quando usar | Exemplo no ToolRent |
|---|---|---|
| `«include»` | O comportamento é **sempre** executado | UC07 sempre consulta disponibilidade (UC05) |
| `«extend»` | O comportamento é **opcional**, sob condição | Nenhum no escopo atual |

Na dúvida, não use nenhuma das duas. Relação inventada é erro de modelagem visível na avaliação.

---

## Checklist antes de commitar um diagrama

- [ ] A fonte `.puml` está no commit
- [ ] A imagem gerada está no commit
- [ ] Os atores do diagrama batem **exatamente** com os do documento
- [ ] Todos os casos de uso do documento aparecem no diagrama
- [ ] Nenhum caso de uso no diagrama está ausente do documento
- [ ] A fronteira do sistema está desenhada
- [ ] As relações `«include»`/`«extend»` correspondem a comportamento real
- [ ] A documentação afetada foi atualizada no mesmo PR

---

## Estado atual — casos de uso

| Verificação | Resultado |
|---|---|
| Atores no documento | Administrador, Atendente, Cliente |
| Atores no diagrama | Administrador, Atendente, Cliente |
| Correspondem? | ✅ Sim |
| Casos de uso no documento | 16 (UC01–UC16) |
| Casos de uso no diagrama | 16 (UC01–UC16) |
| Correspondem? | ✅ Sim |
| Relações `«include»` | 4 — todas reais |
| Relações `«extend»` | 0 |

> **Nota histórica:** a versão 1.0 do documento listava 4 atores, incluindo *Técnico de manutenção*, enquanto o diagrama tinha 3. A divergência foi resolvida removendo o ator — ver [ADR 0002](../decisoes/0002-remocao-do-ator-tecnico.md).
>
> A imagem `casos-de-uso.jpg` é a versão produzida pela equipe. Ao regenerar a partir do `.puml`, substitua-a por um PNG e atualize as referências no [README principal](../../README.md).
