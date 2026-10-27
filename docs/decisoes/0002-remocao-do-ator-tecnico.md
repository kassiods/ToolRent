# ADR 0002 — Remoção do ator "Técnico de manutenção"

- **Data:** 27/10/2026
- **Status:** ✅ Aceita
- **Decidido por:** equipe

---

## Contexto

Havia uma **incoerência entre o documento e o diagrama** de casos de uso:

| Artefato | Atores |
|---|---|
| Documento de casos de uso v1.0 (seção 2) | Administrador, Atendente, Cliente, **Técnico de manutenção** |
| Diagrama de casos de uso | Administrador, Atendente, Cliente |

No diagrama, o UC14 (Gerenciar manutenção) estava ligado ao **Atendente**, não a um técnico.

O próprio checklist de entrega do documento exige: *"Os atores do documento correspondem exatamente aos atores do diagrama."* Entregar os dois artefatos divergentes seria uma falha visível na avaliação.

## Decisão

**Remover o ator Técnico de manutenção.** O UC14 passa a ter o **Atendente** como ator principal.

O sistema fica com **3 atores**: Administrador, Atendente e Cliente.

## Alternativas consideradas

### A — Manter 4 atores e corrigir o diagrama

- ✅ Modela melhor uma empresa grande, com função de manutenção separada
- ✅ Mais atores dá mais complexidade ao trabalho acadêmico
- ❌ Exige refazer o diagrama já produzido
- ❌ Adiciona um quarto perfil de acesso, com telas e permissões próprias, para dois casos de uso
- ❌ Numa locadora pequena — o contexto do produto — não existe técnico dedicado; é o próprio atendente que avalia e encaminha o reparo

### B — Remover o ator e corrigir o documento ← **escolhida**

- ✅ O diagrama já produzido permanece válido
- ✅ Menos um perfil de acesso para implementar e testar
- ✅ Coerente com o porte da empresa modelada
- ✅ 3 atores continua dentro da faixa exigida (3 a 6)
- ❌ Menos separação de responsabilidade no modelo

### C — Deixar como está e decidir depois

- ❌ Entregaria documento e diagrama contradizendo um ao outro

## Consequências

**O que muda**

- O documento de casos de uso passa a listar 3 atores
- UC01 (Autenticar) tem como atores Administrador e Atendente
- UC14 (Gerenciar manutenção) tem como ator principal o Atendente
- A US14 passa a ser escrita da perspectiva do atendente
- O sistema de perfis implementa 3 perfis, não 4

**O que não muda**

- O comportamento do UC14 é idêntico — só muda quem executa
- Nenhuma regra de negócio é afetada
- Nenhum outro caso de uso é afetado

**Reversibilidade**

Alta. Se a equipe decidir reintroduzir o técnico, basta adicionar o perfil e reassociar o UC14 — nenhuma regra de negócio depende dessa separação.

## Nota de risco

O enunciado permite de 3 a 6 atores. Com 3, o projeto está no **limite inferior**. Se o professor sinalizar que espera mais complexidade de atores, a alternativa A pode ser retomada com baixo custo.
