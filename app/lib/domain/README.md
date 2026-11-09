# Domínio do app

Este pacote contém apenas estruturas de dados do aplicativo Flutter, espelhadas
dos contratos da API. Entidades ficam em `entities/` e enums em `enums/`.

Não coloque regras de negócio neste pacote. As regras RN01–RN12 pertencem à
camada `service/` da API, conforme o ADR 0004.
