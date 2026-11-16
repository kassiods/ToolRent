# `api/` — API Java

Camada de negócio do ToolRent. **Aqui moram todas as regras do sistema.**

---

## Executar

**Não é necessário instalar o Maven** — o repositório traz o Maven Wrapper, que baixa a versão correta na primeira execução.

```bash
cp .env.example .env     # preencha com as credenciais do Supabase
```

O Spring **não lê o `.env` sozinho** — carregue as variáveis no terminal antes de rodar:

```powershell
# PowerShell (Windows)
Get-Content .env | ForEach-Object { $p = $_.Split('=', 2); if ($p.Count -eq 2) { Set-Item "env:$($p[0])" $p[1] } }
./mvnw spring-boot:run
```

```bash
# bash (Linux/macOS)
set -a; . ./.env; set +a
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Sem `SUPABASE_URL` a API **não sobe de propósito**: o
JWKS do Supabase Auth é a origem da validação do JWT e não existe JWKS sem URL do projeto.

```bash
curl http://localhost:8080/api/health
```

Verificar antes de abrir um PR:

```bash
./mvnw clean test
```

> Requer **JDK 21**. Se `java -version` não reportar 21, instale o [Temurin 21](https://adoptium.net/temurin/releases/?version=21) e aponte o `JAVA_HOME` para ele.

---

## Estrutura

```
src/main/java/br/edu/toolrent/api/
├── ToolRentApiApplication.java
├── config/        configuração, segurança e parâmetros de negócio
├── domain/        entidades e enums do domínio
├── repository/    acesso a dados
├── service/       REGRAS DE NEGÓCIO — o coração
└── web/           controllers REST e DTOs
```

Cada pacote tem um `package-info.java` com suas regras específicas.

---

## Fronteiras

| Camada | Faz | Não faz |
|---|---|---|
| `web/` | Recebe, valida formato, delega, responde | Conter regra de negócio |
| `service/` | Valida, autoriza, calcula, controla estado | Conhecer HTTP |
| `repository/` | Persiste | Conter regra de negócio |

**O teste da separação:** se um método de `service/` não pode ser testado sem subir um servidor, a fronteira foi violada.

---

## Regras de implementação

- Dinheiro em `BigDecimal`, nunca `double` (RN12)
- Estado em `enum`, com transição validada no service (RN08)
- Autorização verificada no service, não só na interface (RN09)
- Consultas parametrizadas — nunca concatenar entrada do usuário em SQL
- Operação que altera várias tabelas roda em transação (RN11)
- Nenhuma taxa ou percentual literal no código — vai para `application.yml`
- A identidade do usuário sai do JWT, **nunca** do corpo da requisição

---

## Parâmetros de negócio

Ficam em `src/main/resources/application.yml`, sob `toolrent.calculo`.

> ⚠️ Os valores atuais são **provisórios**. Dependem das decisões pendentes #3 (contagem de diárias) e #4 (fórmula da multa). Confirmar com a equipe antes da Sprint 6. Ver [regras de negócio](../docs/regras-de-negocio.md).

---

## Estado atual

Sprint 3, US01 em revisão: OAuth2 Resource Server ativo — o `JwtDecoder` valida a assinatura do
JWT pelo JWKS público do Supabase (`<SUPABASE_URL>/auth/v1/.well-known/jwks.json`, ES256). A API
**não usa chave nem secret do Supabase**: identidade vem do token; papel/perfil saem da tabela
`usuario`.

## Verificação executada

Em 15/11/2026, com JDK 21 (Temurin):

| Comando | Resultado |
|---|---|
| `./mvnw --batch-mode clean verify` | ✅ BUILD SUCCESS · 13 testes, 0 falhas |
