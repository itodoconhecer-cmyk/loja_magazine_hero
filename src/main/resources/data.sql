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

-- Catalogo demonstrativo multidepartamental, com IDs estaveis e cargas idempotentes.
-- Arquivos verificados no servidor: catalog-images/<departamento>.svg.
-- Um mesmo recurso pode ser usado por varios produtos (mesmo caminho e mesmo SHA-256).
insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000001', 'Notebook Ultra 14', 4299.90, true, 'Tecnologia', 'classpath:catalog-images/tecnologia.svg', '9fa37ba3ef1d355969983cdbb5cb8a582b4c44002483b18d2351d49f1da265e6')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000002', 'Monitor Full HD 27', 1099.90, true, 'Tecnologia', 'classpath:catalog-images/tecnologia.svg', '9fa37ba3ef1d355969983cdbb5cb8a582b4c44002483b18d2351d49f1da265e6')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000003', 'Teclado Mecanico RGB', 329.90, true, 'Tecnologia', 'classpath:catalog-images/tecnologia.svg', '9fa37ba3ef1d355969983cdbb5cb8a582b4c44002483b18d2351d49f1da265e6')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000004', 'Smartphone 128 GB', 1699.90, true, 'Tecnologia', 'classpath:catalog-images/tecnologia.svg', '9fa37ba3ef1d355969983cdbb5cb8a582b4c44002483b18d2351d49f1da265e6')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000005', 'Cafeteira Programavel', 289.90, true, 'Casa', 'classpath:catalog-images/casa.svg', '7583daa412f803819d0ff0a9d97b9a8947c7c1a398cafc357936e15437bea3b5')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000006', 'Poltrona de Leitura', 879.90, true, 'Casa', 'classpath:catalog-images/casa.svg', '7583daa412f803819d0ff0a9d97b9a8947c7c1a398cafc357936e15437bea3b5')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000007', 'Luminaria de Mesa', 129.90, true, 'Casa', 'classpath:catalog-images/casa.svg', '7583daa412f803819d0ff0a9d97b9a8947c7c1a398cafc357936e15437bea3b5')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000008', 'Kit Organizadores', 89.90, true, 'Casa', 'classpath:catalog-images/casa.svg', '7583daa412f803819d0ff0a9d97b9a8947c7c1a398cafc357936e15437bea3b5')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000009', 'Camiseta Premium Unissex', 79.90, true, 'Moda', 'classpath:catalog-images/moda.svg', '784ee3bb308c41e37bb10f60fc453c7152de085be787741fdf676c624edcc474')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000010', 'Tenis Casual Urbano', 249.90, true, 'Moda', 'classpath:catalog-images/moda.svg', '784ee3bb308c41e37bb10f60fc453c7152de085be787741fdf676c624edcc474')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000011', 'Mochila Essencial', 189.90, true, 'Moda', 'classpath:catalog-images/moda.svg', '784ee3bb308c41e37bb10f60fc453c7152de085be787741fdf676c624edcc474')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000012', 'Jaqueta Corta-Vento', 329.90, true, 'Moda', 'classpath:catalog-images/moda.svg', '784ee3bb308c41e37bb10f60fc453c7152de085be787741fdf676c624edcc474')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000013', 'Bola de Futebol Society', 129.90, true, 'Esportes', 'classpath:catalog-images/esportes.svg', '51904be18900756d93c10f078ded2a572ea44d2a97ec3612c4c2b8e27d45bd57')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000014', 'Garrafa Termica 1L', 99.90, true, 'Esportes', 'classpath:catalog-images/esportes.svg', '51904be18900756d93c10f078ded2a572ea44d2a97ec3612c4c2b8e27d45bd57')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000015', 'Tapete de Yoga', 119.90, true, 'Esportes', 'classpath:catalog-images/esportes.svg', '51904be18900756d93c10f078ded2a572ea44d2a97ec3612c4c2b8e27d45bd57')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000016', 'Kit Faixas Elasticas', 84.90, true, 'Esportes', 'classpath:catalog-images/esportes.svg', '51904be18900756d93c10f078ded2a572ea44d2a97ec3612c4c2b8e27d45bd57')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000017', 'Kit Cuidado Facial', 149.90, true, 'Beleza', 'classpath:catalog-images/beleza.svg', '0874351d5249c1c390c7f10cd5351fea390a7cd405b7f08aac38d5a651825220')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000018', 'Secador Compacto', 199.90, true, 'Beleza', 'classpath:catalog-images/beleza.svg', '0874351d5249c1c390c7f10cd5351fea390a7cd405b7f08aac38d5a651825220')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000019', 'Organizador de Maquiagem', 69.90, true, 'Beleza', 'classpath:catalog-images/beleza.svg', '0874351d5249c1c390c7f10cd5351fea390a7cd405b7f08aac38d5a651825220')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000020', 'Escova Modeladora', 179.90, true, 'Beleza', 'classpath:catalog-images/beleza.svg', '0874351d5249c1c390c7f10cd5351fea390a7cd405b7f08aac38d5a651825220')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000021', 'Guia de Arquitetura de Software', 119.90, true, 'Livros', 'classpath:catalog-images/livros.svg', 'e0fad4068bc64ddb887cf7e6dd67849e4be4b1cfd27cfacc07a179ff92ad2b4e')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000022', 'Caderno Universitario Premium', 39.90, true, 'Livros', 'classpath:catalog-images/livros.svg', 'e0fad4068bc64ddb887cf7e6dd67849e4be4b1cfd27cfacc07a179ff92ad2b4e')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000023', 'Livro de Design de Interfaces', 94.90, true, 'Livros', 'classpath:catalog-images/livros.svg', 'e0fad4068bc64ddb887cf7e6dd67849e4be4b1cfd27cfacc07a179ff92ad2b4e')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000024', 'Planner Semanal', 59.90, true, 'Livros', 'classpath:catalog-images/livros.svg', 'e0fad4068bc64ddb887cf7e6dd67849e4be4b1cfd27cfacc07a179ff92ad2b4e')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000025', 'Controle Sem Fio', 299.90, true, 'Games', 'classpath:catalog-images/games.svg', 'd63d4779e1455522f0272ad14a3e5b6e7fc25bd53f94d5f62a01d9a85b7ef4b9')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000026', 'Headset Gamer Stereo', 259.90, true, 'Games', 'classpath:catalog-images/games.svg', 'd63d4779e1455522f0272ad14a3e5b6e7fc25bd53f94d5f62a01d9a85b7ef4b9')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000027', 'Mousepad Gamer XL', 89.90, true, 'Games', 'classpath:catalog-images/games.svg', 'd63d4779e1455522f0272ad14a3e5b6e7fc25bd53f94d5f62a01d9a85b7ef4b9')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000028', 'Suporte para Headset', 79.90, true, 'Games', 'classpath:catalog-images/games.svg', 'd63d4779e1455522f0272ad14a3e5b6e7fc25bd53f94d5f62a01d9a85b7ef4b9')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000029', 'Blocos de Montar 120 Pecas', 159.90, true, 'Brinquedos', 'classpath:catalog-images/brinquedos.svg', 'e5eef8a28bf2b691fecf329c9aa5a4a65d929b3c7423c023514aaf96097a4079')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000030', 'Quebra-Cabeca 500 Pecas', 74.90, true, 'Brinquedos', 'classpath:catalog-images/brinquedos.svg', 'e5eef8a28bf2b691fecf329c9aa5a4a65d929b3c7423c023514aaf96097a4079')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000031', 'Jogo de Memoria Infantil', 49.90, true, 'Brinquedos', 'classpath:catalog-images/brinquedos.svg', 'e5eef8a28bf2b691fecf329c9aa5a4a65d929b3c7423c023514aaf96097a4079')
on conflict (id) do nothing;

insert into produto(id, nome, preco, ativo, departamento, imagem_storage_path, imagem_sha256)
values ('66666666-6666-4666-8666-000000000032', 'Carrinho de Friccao', 69.90, true, 'Brinquedos', 'classpath:catalog-images/brinquedos.svg', 'e5eef8a28bf2b691fecf329c9aa5a4a65d929b3c7423c023514aaf96097a4079')
on conflict (id) do nothing;

