# Vitrine publica e carrinho anonimo

## Regra de negocio (24/11/2026)

- Visitantes podem consultar o catalogo, pesquisar, filtrar, ordenar e manter um
  carrinho local sem autenticar.
- O frontend React acessa `GET /api/catalogo/produtos` sem credenciais.
  O endpoint responde apenas produtos ativos e nao expoe dados de clientes.
- As quantidades ficam em `localStorage` (`magazine-hero:guest-cart:v1`),
  com validacao de estrutura e limites; nao representam reserva de estoque.
- Os valores sao apenas estimativas e serao validados no servidor no checkout.
- O cliente precisa se autenticar para transformar o carrinho em pedido,
  vincular seus dados pessoais e iniciar o pagamento.
- Operacoes de gerenciamento e endpoints administrativos continuam protegidos.
- A autenticacao de comprador e a vinculacao segura do carrinho a uma conta
  ainda estao pendentes: **nao existe checkout de pagamento operacional**.
  A tela de checkout informa essa restricao e nao simula a criacao de pedido.
- A identidade do comprador deve ser derivada da sessao validada pelo backend,
  nunca de um clienteId fornecido livremente pelo navegador.

## Arquivos

- `src/main/java/aulas/umc/oo/controller/CatalogoPublicoController.java`
- `src/main/java/aulas/umc/oo/security/SecurityConfig.java`
- `frontend/src/features/Catalog.tsx`
- `frontend/src/features/Checkout.tsx`
- `frontend/src/services/guestCart.ts`
- `frontend/src/components/organisms/ProductGrid.tsx`
- `frontend/tests/cart.test.ts`

## Testes minimos

1. `GET /api/catalogo/produtos` sem token: sucesso, apenas ativos.
2. `POST /api/produtos` sem token: 401, sem gravacao.
3. Carrinho local: adicionar, aumentar, reduzir, remover, fechar e reabrir aba.
4. Atualizar pagina: itens continuam salvos; credenciais nao sao salvas.
5. Checkout: mostra etapa de identificacao, nao cria pedidos anonimos.
6. Backend: validar `mvn spotless:check` e `mvn verify`.
7. Frontend: validar `npm run build` e `npm test`.

## Pendencias de producao

- Cadastro de comprador, login JWT/OIDC e refresh seguro com cookie HttpOnly;
- Migracao de carrinho local para carrinho no servidor com controle de
  propriedade, expiracao e protecao contra manipulacao;
- Checkout transacional, calculo de frete e validacao de preco/estoque no servidor;
- Gateway de pagamento homologado, webhooks e idempotencia;
- Imagens licenciadas dos produtos: o modelo atual contem id, nome, preco e ativo,
  mas ainda nao guarda URL de imagem;
- Testes de ponta a ponta e politicas de rate limit / disponibilidade.

Nao executar operacoes reais de pagamento ate esses controles estarem concluidos.
