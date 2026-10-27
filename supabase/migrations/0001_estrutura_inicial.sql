-- =====================================================================
-- ToolRent — Migration 0001: estrutura inicial
--
-- STATUS: RASCUNHO. Este schema antecipa o modelo descrito em
-- docs/casos-de-uso.md e docs/regras-de-negocio.md, mas DEVE ser
-- confirmado contra o diagrama de classes da Sprint 2 antes de ser
-- considerado definitivo.
--
-- Regras obrigatórias (docs/regras-de-negocio.md):
--   RN11 — integridade referencial; registros com histórico são
--          inativados, nunca excluídos
--   RN12 — dinheiro em NUMERIC(12,2); datas em timestamptz (UTC)
--   RN08 — estados em tipo enumerado; ATRASADA NÃO existe aqui,
--          é derivado em tempo de execução (ver ADR 0003)
--
-- NUNCA edite uma migration já aplicada. Crie a próxima.
-- =====================================================================

-- ---------------------------------------------------------------------
-- Tipos enumerados
-- ---------------------------------------------------------------------

create type perfil_usuario as enum ('ADMINISTRADOR', 'ATENDENTE', 'CLIENTE');

create type estado_reserva as enum ('SOLICITADA', 'APROVADA', 'RECUSADA', 'CANCELADA');

-- ATENÇÃO: 'ATRASADA' não consta aqui de propósito.
-- É um estado DERIVADO, calculado comparando a data prevista de
-- devolução com a data atual.
-- Ver docs/decisoes/0003-estado-atrasada-derivado.md
create type estado_locacao as enum ('APROVADA', 'EM_ANDAMENTO', 'DEVOLVIDA', 'CANCELADA');

create type situacao_ferramenta as enum ('DISPONIVEL', 'EM_MANUTENCAO', 'INATIVA');

create type estado_manutencao as enum ('ABERTA', 'CONCLUIDA');

-- ---------------------------------------------------------------------
-- Usuários e perfis
--
-- A senha NÃO fica aqui (RN10). A autenticação é delegada ao
-- Supabase Auth; esta tabela apenas associa o usuário ao seu perfil.
-- ---------------------------------------------------------------------

create table usuario (
    id            uuid primary key default gen_random_uuid(),
    auth_user_id  uuid unique not null,           -- referencia auth.users do Supabase
    nome          text not null,
    email         text unique not null,
    perfil        perfil_usuario not null,
    ativo         boolean not null default true,  -- inativação, nunca exclusão (RN11)
    criado_em     timestamptz not null default now()
);

-- ---------------------------------------------------------------------
-- Clientes
-- ---------------------------------------------------------------------

create table cliente (
    id         uuid primary key default gen_random_uuid(),
    nome       text not null,
    documento  text unique not null,
    telefone   text,
    email      text,
    endereco   text,
    ativo      boolean not null default true,
    criado_em  timestamptz not null default now()
);

-- ---------------------------------------------------------------------
-- Catálogo
-- ---------------------------------------------------------------------

create table categoria (
    id         uuid primary key default gen_random_uuid(),
    nome       text unique not null,
    descricao  text,
    criado_em  timestamptz not null default now()
);

create table ferramenta (
    id             uuid primary key default gen_random_uuid(),
    codigo         text unique not null,
    nome           text not null,
    descricao      text,
    categoria_id   uuid not null references categoria (id),
    tarifa_diaria  numeric(12, 2) not null,        -- RN12: nunca float
    quantidade     integer not null default 1,
    situacao       situacao_ferramenta not null default 'DISPONIVEL',
    ativo          boolean not null default true,
    criado_em      timestamptz not null default now(),

    constraint tarifa_positiva      check (tarifa_diaria > 0),   -- UC04 / A2
    constraint quantidade_positiva  check (quantidade > 0)
);

-- ---------------------------------------------------------------------
-- Reservas
-- ---------------------------------------------------------------------

create table reserva (
    id             uuid primary key default gen_random_uuid(),
    cliente_id     uuid not null references cliente (id),
    ferramenta_id  uuid not null references ferramenta (id),
    quantidade     integer not null default 1,
    data_inicio    timestamptz not null,
    data_fim       timestamptz not null,
    estado         estado_reserva not null default 'SOLICITADA',
    justificativa  text,                           -- obrigatória na recusa (UC08 / A3)
    decidido_por   uuid references usuario (id),
    decidido_em    timestamptz,
    criado_em      timestamptz not null default now(),

    constraint periodo_valido_reserva check (data_inicio < data_fim)   -- RN01
);

-- ---------------------------------------------------------------------
-- Locações
-- ---------------------------------------------------------------------

create table locacao (
    id                       uuid primary key default gen_random_uuid(),
    cliente_id               uuid not null references cliente (id),
    reserva_id               uuid references reserva (id),     -- nulo em atendimento direto
    data_inicio              timestamptz not null,
    data_prevista_devolucao  timestamptz not null,
    data_retirada            timestamptz,
    estado                   estado_locacao not null default 'APROVADA',
    valor_previsto           numeric(12, 2),
    valor_total              numeric(12, 2),
    criado_por               uuid not null references usuario (id),
    criado_em                timestamptz not null default now(),

    constraint periodo_valido_locacao check (data_inicio < data_prevista_devolucao)  -- RN01
);

-- A tarifa é COPIADA para o item no momento da formalização.
-- Alterar o catálogo depois não muda locações já fechadas (RN04).
create table item_locacao (
    id                 uuid primary key default gen_random_uuid(),
    locacao_id         uuid not null references locacao (id) on delete cascade,
    ferramenta_id      uuid not null references ferramenta (id),
    quantidade         integer not null default 1,
    tarifa_registrada  numeric(12, 2) not null,
    condicao_retirada  text,

    constraint quantidade_item_positiva    check (quantidade > 0),
    constraint tarifa_registrada_positiva  check (tarifa_registrada > 0)
);

-- ---------------------------------------------------------------------
-- Devolução
-- ---------------------------------------------------------------------

create table devolucao (
    id               uuid primary key default gen_random_uuid(),
    locacao_id       uuid not null unique references locacao (id),
    data_devolucao   timestamptz not null,
    condicao         text,
    descricao_danos  text,
    valor_danos      numeric(12, 2) default 0,
    danos_aprovados  boolean not null default false,   -- RN06
    valor_multa      numeric(12, 2) default 0,         -- RN05
    registrado_por   uuid not null references usuario (id),
    criado_em        timestamptz not null default now(),

    constraint valores_nao_negativos check (valor_danos >= 0 and valor_multa >= 0)
);

-- ---------------------------------------------------------------------
-- Pagamentos
-- ---------------------------------------------------------------------

create table pagamento (
    id              uuid primary key default gen_random_uuid(),
    locacao_id      uuid not null references locacao (id),
    valor           numeric(12, 2) not null,
    forma           text not null,
    data_pagamento  timestamptz not null default now(),
    registrado_por  uuid not null references usuario (id),

    constraint valor_pagamento_positivo check (valor > 0)   -- RN07
);

-- ---------------------------------------------------------------------
-- Manutenção
-- ---------------------------------------------------------------------

create table manutencao (
    id              uuid primary key default gen_random_uuid(),
    ferramenta_id   uuid not null references ferramenta (id),
    motivo          text not null,
    descricao       text,
    estado          estado_manutencao not null default 'ABERTA',
    resultado       text,
    aberta_em       timestamptz not null default now(),
    concluida_em    timestamptz,
    responsavel_id  uuid not null references usuario (id)
);

-- ---------------------------------------------------------------------
-- Índices
--
-- A consulta de disponibilidade (UC05) filtra por ferramenta e período.
-- Sem estes índices, RN02 fica lenta conforme a base cresce.
-- ---------------------------------------------------------------------

create index idx_reserva_ferramenta_periodo on reserva (ferramenta_id, data_inicio, data_fim);
create index idx_reserva_estado             on reserva (estado);
create index idx_locacao_cliente            on locacao (cliente_id);
create index idx_locacao_estado             on locacao (estado);
create index idx_locacao_previsao           on locacao (data_prevista_devolucao);
create index idx_item_locacao               on item_locacao (locacao_id);
create index idx_item_ferramenta            on item_locacao (ferramenta_id);
create index idx_pagamento_locacao          on pagamento (locacao_id);
create index idx_manutencao_ferramenta      on manutencao (ferramenta_id, estado);
