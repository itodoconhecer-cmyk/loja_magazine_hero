insert into cliente(id,nome,email,ativo)
values ('11111111-1111-1111-1111-111111111111','Cliente Demo','cliente@demo.com',true)
on conflict do nothing;

insert into produto(id,nome,preco,ativo)
values ('22222222-2222-2222-2222-222222222221','Notebook Hero',3500.00,true)
on conflict do nothing;

insert into produto(id,nome,preco,ativo)
values ('22222222-2222-2222-2222-222222222222','Mouse Hero',120.00,true)
on conflict do nothing;

insert into movimentacao_estoque(id,produto_id,tipo,quantidade,motivo,data_hora)
values (
    '44444444-4444-4444-4444-444444444441',
    '22222222-2222-2222-2222-222222222221',
    'ENTRADA',
    10,
    'ESTOQUE_INICIAL',
    current_timestamp
)
on conflict do nothing;

insert into movimentacao_estoque(id,produto_id,tipo,quantidade,motivo,data_hora)
values (
    '44444444-4444-4444-4444-444444444442',
    '22222222-2222-2222-2222-222222222222',
    'ENTRADA',
    30,
    'ESTOQUE_INICIAL',
    current_timestamp
)
on conflict do nothing;

insert into carrinho(id,cliente_id,ativo)
values (
    '33333333-3333-3333-3333-333333333333',
    '11111111-1111-1111-1111-111111111111',
    true
)
on conflict do nothing;
