# `api/` — API Java

Camada de negócio do ToolRent. **Aqui moram todas as regras do sistema.**

---

## Executar

**Não é necessário instalar o Maven** — o repositório traz o Maven Wrapper, que baixa a versão correta na primeira execução.

```bash
cp .env.example .env     # preencha com as credenciais do Supabase
./mvnw clean install     # Windows: mvnw.cmd clean install
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

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

Sprint 0: esqueleto com o endpoint de verificação.

As dependências de segurança (OAuth2 Resource Server) entram na Sprint 3, junto da US01.

## Verificação executada

Em 27/10/2026, com JDK 21.0.12 (Temurin) e Maven 3.9.9:

| Comando | Resultado |
|---|---|
| `mvn clean test` | ✅ BUILD SUCCESS · 2 testes, 0 falhas |
| `./mvnw --batch-mode test` | ✅ exit 0 — o wrapper funciona sem Maven instalado |
