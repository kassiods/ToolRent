# `app/` — Aplicativo Flutter

Camada de apresentação do ToolRent.

---

## Executar

```bash
flutter pub get
flutter run -d chrome     # ou: -d windows / -d android
```

Verificar antes de abrir um PR:

```bash
flutter analyze
flutter test
```

---

## Estrutura

```
lib/
├── main.dart              ponto de entrada
├── core/                  tema, configuração, cliente HTTP
├── domain/                entidades e enums espelhados da API
├── data/
│   └── repositories/      ÚNICA camada que fala com a rede
└── ui/
    ├── screens/           telas
    └── widgets/           componentes reutilizáveis
```

---

## Regras

### Sem regra de negócio

O app **não** calcula valor, **não** decide permissão e **não** valida regra de negócio. Tudo isso é da API.

Motivo: este código roda no dispositivo do usuário e pode ser modificado por ele. Ver [ADR 0004](../docs/decisoes/0004-regra-de-negocio-na-api.md).

O app **pode** validar formato — avisar que a data final é anterior à inicial antes de gastar uma viagem de rede é boa experiência. Mas a API revalida.

### Rede só nos repositórios

Nenhum widget chama HTTP diretamente. Toda comunicação passa por `data/repositories/`. Isso mantém as telas testáveis e localiza a troca de cliente HTTP num lugar só.

### Cores e espaçamentos vêm do tema

Nenhuma cor literal em widget. Tudo sai de `core/app_theme.dart`, para que um ajuste de identidade visual aconteça num lugar só.

---

## Estado atual

Sprint 0: estrutura, tema e tela de boas-vindas.

A tela inicial é substituída pela tela de login na Sprint 3 (US01).

As dependências de Supabase, HTTP e gerenciamento de estado estão listadas comentadas no `pubspec.yaml` e entram junto da história correspondente — não descomente sem a Issue aberta.

## Verificação executada

| Comando | Resultado |
|---|---|
| `flutter pub get` | ✅ 27 dependências resolvidas |
| `flutter analyze` | ✅ No issues found |
| `flutter test` | ✅ 3 testes passaram |
