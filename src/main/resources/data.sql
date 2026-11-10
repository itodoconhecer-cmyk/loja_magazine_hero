insert into cliente(id,nome,email,ativo) values ('11111111-1111-1111-1111-111111111111','Cliente Demo','cliente@demo.com',true) on conflict do nothing;
insert into produto(id,nome,preco,estoque,ativo) values ('22222222-2222-2222-2222-222222222221','Notebook Hero',3500.00,10,true) on conflict do nothing;
insert into produto(id,nome,preco,estoque,ativo) values ('22222222-2222-2222-2222-222222222222','Mouse Hero',120.00,30,true) on conflict do nothing;
insert into carrinho(id,cliente_id,ativo) values ('33333333-3333-3333-3333-333333333333','11111111-1111-1111-1111-111111111111',true) on conflict do nothing;
