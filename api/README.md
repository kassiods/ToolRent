# `api/` — API Java

Camada de negócio do ToolRent. **Aqui moram todas as regras do sistema.**

---

## Executar

```bash
cp .env.example .env     # preencha com as credenciais do Supabase
mvn clean install
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

```bash
curl http://localhost:8080/api/health
```

Verificar antes de abrir um PR:

```bash
mvn clean test
```

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

## ⚠️ Verificação pendente

| Comando | Resultado |
|---|---|
| `mvn clean test` | ⚠️ **Não executado** — Maven ausente no ambiente onde o esqueleto foi criado |

**O primeiro integrante com Maven instalado deve rodar `mvn clean test` e reportar o resultado.** O `pom.xml`, a classe principal, o controller e o teste foram escritos, mas nunca compilados.

Requisitos: **JDK 21** e Maven 3.9+.
