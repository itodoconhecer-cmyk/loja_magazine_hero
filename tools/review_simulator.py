from dataclasses import dataclass, field
from decimal import Decimal

@dataclass
class Product:
    stock: int
    active: bool = True

@dataclass
class Cart:
    items: list = field(default_factory=list)

@dataclass
class Order:
    status: str = 'RASCUNHO'
    subtotal: Decimal = Decimal('0')
    freight: Decimal = Decimal('0')
    total: Decimal = Decimal('0')
    snapshot: list = field(default_factory=list)

@dataclass
class Payment:
    status: str = 'PENDENTE'
    value: Decimal = Decimal('0')

# Reproduz as regras centrais das strategies atuais para testes de mesa executaveis.
def calc_freight(order, cart, products, freight_type='NORMAL'):
    if order.status in ('CANCELADO', 'EXCLUIDO'):
        raise ValueError('pedido nao permite frete')
    if not cart.items:
        raise ValueError('carrinho vazio')
    subtotal = sum((price * q for _, q, price in cart.items), Decimal('0'))
    order.subtotal = subtotal
    order.freight = Decimal('0') if freight_type == 'NORMAL' and subtotal >= Decimal('200') else (Decimal('15') if freight_type == 'NORMAL' else Decimal('35'))
    order.total = order.subtotal + order.freight

def approve_order(order, cart, products):
    if order.status != 'RASCUNHO':
        raise ValueError('somente rascunho')
    if order.total <= 0:
        raise ValueError('calcule frete')
    snap = []
    for product_id, qty, price in cart.items:
        product = products[product_id]
        if product.stock < qty:
            raise ValueError('estoque insuficiente')
        product.stock -= qty
        snap.append((product_id, qty, price))
    order.snapshot = snap
    order.status = 'AGUARDANDO_PAGAMENTO'

def approve_payment(order, payment):
    if order.status != 'AGUARDANDO_PAGAMENTO':
        raise ValueError('pedido nao aguarda pagamento')
    if payment.value != order.total:
        raise ValueError('valor divergente')
    # Regra atual nao verifica payment.status == PENDENTE.
    payment.status = 'APROVADO'
    order.status = 'APROVADO'

def cancel_order(order, products):
    if order.status in ('EXCLUIDO', 'CANCELADO'):
        raise ValueError('nao pode cancelar')
    if order.status != 'RASCUNHO':
        for product_id, qty, _ in order.snapshot:
            product = products[product_id]
            # ProdutoRepository.findById() atual so encontra produto ativo.
            if not product.active:
                raise LookupError('produto nao encontrado')
            product.stock += qty
    order.status = 'CANCELADO'

results = []
def record(name, safe, severity, observation):
    results.append((name, 'PASSOU' if safe else 'GAP', severity, observation))

# 01 Fluxo feliz
p = {'A': Product(10)}; c = Cart([('A',1,Decimal('3500'))]); o = Order(); calc_freight(o,c,p,'EXPRESSO'); approve_order(o,c,p); pg=Payment(value=o.total); approve_payment(o,pg)
record('01 fluxo feliz completo', o.status=='APROVADO' and pg.status=='APROVADO' and p['A'].stock==9, 'baseline', 'Fluxo principal funciona no modelo atual.')

# 02 carrinho muda depois do frete
p={'A':Product(10),'B':Product(10)}; c=Cart([('A',1,Decimal('100'))]); o=Order(); calc_freight(o,c,p); total_calculado=o.total; c.items=[('B',2,Decimal('500'))]; approve_order(o,c,p); snapshot_total=sum((price*q for _,q,price in o.snapshot),Decimal('0'))
record('02 carrinho alterado apos calcular frete', o.total==snapshot_total or o.status=='RASCUNHO', 'CRITICO', f'total pedido={o.total}; itens reservados={snapshot_total}; o pipeline aprovou mesmo divergente.')

# 03 carrinho esvaziado depois do frete
p={'A':Product(10)}; c=Cart([('A',1,Decimal('100'))]); o=Order(); calc_freight(o,c,p); c.items=[]; approve_order(o,c,p)
record('03 carrinho vazio na aprovacao', not (o.status=='AGUARDANDO_PAGAMENTO' and not o.snapshot), 'CRITICO', 'ReservarEstoquePedidoStrategy nao rejeita lista vazia.')

# 04 recalculo aguardando pagamento
p={'A':Product(10)}; c=Cart([('A',1,Decimal('100'))]); o=Order(); calc_freight(o,c,p); approve_order(o,c,p); before=o.total; c.items=[('A',2,Decimal('100'))]
try:
    calc_freight(o,c,p); safe=False
except ValueError:
    safe=True
record('04 recalcular frete aguardando pagamento', safe, 'CRITICO', f'status={o.status}; validacao atual bloqueia apenas CANCELADO/EXCLUIDO.')

# 05 recalculo de pedido pago
p={'A':Product(10)}; c=Cart([('A',1,Decimal('100'))]); o=Order(); calc_freight(o,c,p); approve_order(o,c,p); pg=Payment(value=o.total); approve_payment(o,pg); c.items=[('A',3,Decimal('100'))]
try:
    calc_freight(o,c,p); safe=False
except ValueError:
    safe=True
record('05 recalcular frete de pedido APROVADO', safe, 'CRITICO', 'Permite alterar total de pedido ja pago.')

# 06 cancelamento de pedido pago
p={'A':Product(10)}; c=Cart([('A',1,Decimal('100'))]); o=Order(); calc_freight(o,c,p); approve_order(o,c,p); pg=Payment(value=o.total); approve_payment(o,pg); cancel_order(o,p)
record('06 cancelar pedido pago', not (o.status=='CANCELADO' and pg.status=='APROVADO'), 'CRITICO', 'Pedido vira CANCELADO e pagamento permanece APROVADO; falta politica de estorno/cancelamento.')

# 07 produto inativo e cancelamento
p={'A':Product(10)}; c=Cart([('A',1,Decimal('100'))]); o=Order(); calc_freight(o,c,p); approve_order(o,c,p); p['A'].active=False
try:
    cancel_order(o,p); safe=True
except LookupError:
    safe=False
record('07 cancelar pedido com produto desativado', safe, 'ALTO', 'findById de Produto filtra ativo=true e impede devolucao de estoque.')

# 08 pagamento cancelado/recusado
p={'A':Product(10)}; c=Cart([('A',1,Decimal('100'))]); o=Order(); calc_freight(o,c,p); approve_order(o,c,p); pg=Payment(status='CANCELADO',value=o.total); approve_payment(o,pg)
record('08 aprovar pagamento previamente CANCELADO', pg.status!='APROVADO', 'ALTO', 'ValidarPagamentoStrategy nao exige status PENDENTE.')

# 09 alterar pagamento aprovado via CRUD
record('09 editar pagamento aprovado via CRUD', False, 'CRITICO', 'PagamentoUseCase.atualizar nao bloqueia status APROVADO nem mudanca de valor/pedido.')

# 10 excluir pagamento aprovado via CRUD
record('10 excluir pagamento aprovado via CRUD', False, 'CRITICO', 'DELETE fisico de pagamento pode deixar pedido APROVADO sem evidencia de pagamento.')

# 11 estoque alterado via CRUD produto
record('11 alterar estoque por PUT /produtos', False, 'ALTO', 'ProdutoRequest contem estoque; update persiste saldo sem MovimentacaoEstoque.')

# 12 concorrencia de estoque
record('12 concorrencia na reserva/saida', False, 'CRITICO', 'Le saldo e grava valor absoluto; sem lock/version/UPDATE condicional pode ocorrer lost update/overselling.')

# 13 pedido e carrinho de clientes diferentes
record('13 pedido usa carrinho de outro cliente', False, 'ALTO', 'PedidoUseCase valida existencia separadamente, mas nao confere carrinho.clienteId == pedido.clienteId.')

# 14 multiplos pedidos no mesmo carrinho
record('14 multiplos pedidos para o mesmo carrinho', False, 'MEDIO', 'Nao ha regra/constraint que impeça reaproveitar carrinho e reservar estoque varias vezes.')

# 15 esvaziar carrinho vinculado a pedido em andamento
record('15 esvaziar carrinho vinculado a pedido', False, 'ALTO', 'ESVAZIAR_CARRINHO valida apenas existencia do carrinho; nao verifica pedidos RASCUNHO/AGUARDANDO.')

# 16 ordem duplicada pipeline
record('16 duas ProcessStrategy com a mesma ordem', False, 'MEDIO', 'PipelineExecutor apenas ordena; nao valida unicidade de (processo, ordem).')

# 17 strategy duplicada de frete
record('17 duas FreteStrategy do mesmo tipo', False, 'MEDIO', 'EnumMap.put sobrescreve silenciosamente a implementacao anterior.')

# 18 repositorio/constraint estoque negativo
record('18 constraint de banco para estoque >= 0', False, 'ALTO', 'schema.sql nao possui CHECK(estoque >= 0) nem CHECK(quantidade > 0).')

# 19 movimentacao de reserva/cancelamento nao auditada
record('19 reserva e devolucao sem movimentacao de estoque', False, 'ALTO', 'Aprovar/cancelar altera Produto diretamente e nao insere MovimentacaoEstoque.')

# 20 tratamento de excecoes do banco
record('20 erro de integridade vira resposta controlada', False, 'MEDIO', 'ApiExceptionHandler nao trata DataIntegrityViolationException/RepositoryException; pode retornar 500 cru.')

print('CENARIO|RESULTADO|SEVERIDADE|OBSERVACAO')
for row in results:
    print('|'.join(row))
print(f'\nTOTAL={len(results)} PASSOU={sum(r[1]=="PASSOU" for r in results)} GAPS={sum(r[1]=="GAP" for r in results)}')
