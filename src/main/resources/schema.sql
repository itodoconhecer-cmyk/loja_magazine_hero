create table if not exists cliente(
    id uuid primary key,
    nome varchar(120) not null,
    email varchar(180) not null unique,
    ativo boolean not null
);

create table if not exists produto(
    id uuid primary key,
    nome varchar(160) not null,
    preco numeric(12,2) not null check(preco >= 0),
    ativo boolean not null
);

-- Compatibilidade com bancos criados por versoes anteriores.
-- Produto nao deve armazenar saldo de estoque.
alter table produto drop column if exists estoque;

create table if not exists carrinho(
    id uuid primary key,
    cliente_id uuid not null references cliente(id),
    ativo boolean not null
);

create table if not exists item_carrinho(
    id uuid primary key,
    carrinho_id uuid not null references carrinho(id),
    produto_id uuid not null references produto(id),
    quantidade int not null check(quantidade > 0)
);

create table if not exists pedido(
    id uuid primary key,
    cliente_id uuid not null references cliente(id),
    carrinho_id uuid not null unique references carrinho(id),
    status varchar(40) not null,
    tipo_frete varchar(40) not null,
    subtotal numeric(12,2) not null check(subtotal >= 0),
    valor_frete numeric(12,2) not null check(valor_frete >= 0),
    total numeric(12,2) not null check(total >= 0),
    ativo boolean not null
);

create table if not exists item_pedido(
    id uuid primary key,
    pedido_id uuid not null references pedido(id),
    produto_id uuid not null references produto(id),
    quantidade int not null check(quantidade > 0),
    preco_unitario numeric(12,2) not null check(preco_unitario >= 0)
);

create table if not exists pagamento(
    id uuid primary key,
    pedido_id uuid not null unique references pedido(id),
    valor numeric(12,2) not null check(valor >= 0),
    status varchar(40) not null,
    meio varchar(40) not null
);

create table if not exists movimentacao_estoque(
    id uuid primary key,
    produto_id uuid not null references produto(id),
    tipo varchar(20) not null check(tipo in ('ENTRADA', 'SAIDA')),
    quantidade int not null check(quantidade > 0),
    motivo varchar(255) not null,
    data_hora timestamp not null
);

create index if not exists idx_movimentacao_estoque_produto_data
    on movimentacao_estoque(produto_id, data_hora, id);
