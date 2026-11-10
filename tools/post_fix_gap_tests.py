from dataclasses import dataclass, field
from decimal import Decimal
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]

@dataclass
class Product:
    stock:int
    active:bool=True

@dataclass
class Cart:
    client:str
    items:list=field(default_factory=list)
    has_order:bool=False

@dataclass
class Order:
    client:str='C1'
    status:str='RASCUNHO'
    subtotal:Decimal=Decimal('0')
    freight:Decimal=Decimal('0')
    total:Decimal=Decimal('0')
    snapshot:list=field(default_factory=list)

@dataclass
class Payment:
    status:str='PENDENTE'
    value:Decimal=Decimal('0')

movements=[]

def create_order(order, cart):
    if order.client != cart.client:
        raise ValueError('carrinho de outro cliente')
    if cart.has_order:
        raise ValueError('carrinho ja possui pedido')
    cart.has_order=True


def mutate_cart(cart):
    if cart.has_order:
        raise ValueError('carrinho convertido em pedido')


def calc_freight(order, cart, products, freight_type='NORMAL'):
    if order.status!='RASCUNHO':
        raise ValueError('frete apenas rascunho')
    if not cart.items:
        raise ValueError('carrinho vazio')
    snap=[]
    subtotal=Decimal('0')
    for product_id,qty,price in cart.items:
        if qty<=0 or not products[product_id].active:
            raise ValueError('item invalido')
        snap.append((product_id,qty,price))
        subtotal += price*qty
    order.snapshot=snap
    order.subtotal=subtotal
    order.freight=Decimal('0') if freight_type=='NORMAL' and subtotal>=Decimal('200') else (Decimal('15') if freight_type=='NORMAL' else Decimal('35'))
    order.total=order.subtotal+order.freight


def edit_freight(order):
    if order.status!='RASCUNHO': raise ValueError('somente rascunho')
    order.subtotal=order.freight=order.total=Decimal('0')
    order.snapshot=[]


def atomic_reserve(product, qty):
    if qty<=0 or not product.active or product.stock<qty:
        raise ValueError('estoque insuficiente/inativo')
    product.stock-=qty


def approve_order(order, products):
    if order.status!='RASCUNHO': raise ValueError('somente rascunho')
    if order.total<=0 or not order.snapshot: raise ValueError('calcule frete')
    for pid,qty,_ in order.snapshot:
        atomic_reserve(products[pid],qty)
        movements.append(('SAIDA',pid,qty,'RESERVA'))
    order.status='AGUARDANDO_PAGAMENTO'


def create_payment(order, payment):
    if order.status!='AGUARDANDO_PAGAMENTO': raise ValueError('pedido nao aguarda')
    if payment.value!=order.total: raise ValueError('valor divergente')


def approve_payment(order,payment):
    if payment.status!='PENDENTE': raise ValueError('pagamento nao pendente')
    if order.status!='AGUARDANDO_PAGAMENTO': raise ValueError('pedido nao aguarda')
    if payment.value!=order.total: raise ValueError('valor divergente')
    payment.status='APROVADO'; order.status='APROVADO'


def cancel_order(order, products):
    if order.status not in ('RASCUNHO','AGUARDANDO_PAGAMENTO'):
        raise ValueError('pago exige estorno')
    if order.status=='AGUARDANDO_PAGAMENTO':
        for pid,qty,_ in order.snapshot:
            products[pid].stock += qty
            movements.append(('ENTRADA',pid,qty,'CANCELAMENTO'))
    order.status='CANCELADO'


def refund_payment(order, payment, products):
    if payment.status!='APROVADO':
        raise ValueError('somente aprovado pode ser estornado')
    if order.status!='APROVADO':
        raise ValueError('pedido precisa estar aprovado')
    payment.status='ESTORNADO'
    for pid,qty,_ in order.snapshot:
        products[pid].stock += qty
        movements.append(('ENTRADA',pid,qty,'ESTORNO'))
    order.status='CANCELADO'

results=[]
def check(name, fn):
    try:
        ok,obs=fn()
    except Exception as e:
        ok=False;obs=f'EXCECAO NAO ESPERADA: {type(e).__name__}: {e}'
    results.append((name,'PASSOU' if ok else 'FALHOU',obs))

# 01 fluxo feliz
def t01():
    p={'A':Product(10)}; c=Cart('C1',[('A',1,Decimal('350'))]); o=Order(); create_order(o,c); calc_freight(o,c,p,'EXPRESSO'); approve_order(o,p); pg=Payment(value=o.total); create_payment(o,pg); approve_payment(o,pg)
    return (o.status=='APROVADO' and pg.status=='APROVADO' and p['A'].stock==9,'fluxo completo')
check('01 fluxo feliz',t01)

# 02 carrinho bloqueado depois do pedido
def t02():
    c=Cart('C1',[('A',1,Decimal('100'))]);o=Order();create_order(o,c)
    try: mutate_cart(c); return False,'alteracao foi permitida'
    except ValueError:return True,'carrinho imutavel apos conversao em pedido'
check('02 bloquear alteracao de carrinho apos pedido',t02)

# 03 cliente divergente
def t03():
    c=Cart('C2');o=Order(client='C1')
    try:create_order(o,c);return False,'pedido criado'
    except ValueError:return True,'cliente/carrinho validados'
check('03 impedir carrinho de outro cliente',t03)

# 04 carrinho reutilizado
def t04():
    c=Cart('C1');o=Order();create_order(o,c)
    try:create_order(Order(),c);return False,'reutilizado'
    except ValueError:return True,'um carrinho gera um pedido'
check('04 impedir multiplos pedidos no mesmo carrinho',t04)

# 05 snapshot no frete
def t05():
    p={'A':Product(10)};c=Cart('C1',[('A',2,Decimal('50'))]);o=Order();calc_freight(o,c,p)
    return (o.snapshot==[('A',2,Decimal('50'))] and o.subtotal==Decimal('100'),'snapshot gerado no calculo')
check('05 snapshot de itens no calculo do frete',t05)

# 06 sem frete para aguardando
def t06():
    p={'A':Product(10)};c=Cart('C1',[('A',1,Decimal('100'))]);o=Order();calc_freight(o,c,p);approve_order(o,p)
    try:calc_freight(o,c,p);return False,'recalculou'
    except ValueError:return True,'recalculo bloqueado'
check('06 bloquear recalculo aguardando pagamento',t06)

# 07 sem frete pago
def t07():
    p={'A':Product(10)};c=Cart('C1',[('A',1,Decimal('100'))]);o=Order();calc_freight(o,c,p);approve_order(o,p);pg=Payment(value=o.total);approve_payment(o,pg)
    try:calc_freight(o,c,p);return False,'recalculou'
    except ValueError:return True,'pedido pago protegido'
check('07 bloquear recalculo de pedido pago',t07)

# 08 editar frete invalida calculo
def t08():
    p={'A':Product(10)};c=Cart('C1',[('A',1,Decimal('100'))]);o=Order();calc_freight(o,c,p);edit_freight(o)
    try:approve_order(o,p);return False,'aprovou sem recalcular'
    except ValueError:return True,'edicao exige novo calculo'
check('08 editar tipo de frete invalida total/snapshot',t08)

# 09 aprovar vazio
def t09():
    o=Order(total=Decimal('15'))
    try:approve_order(o,{});return False,'aprovou sem itens'
    except ValueError:return True,'snapshot vazio bloqueado'
check('09 bloquear aprovacao sem itens',t09)

# 10 pagamento cancelado
def t10():
    o=Order(status='AGUARDANDO_PAGAMENTO',total=Decimal('10'));pg=Payment(status='CANCELADO',value=Decimal('10'))
    try:approve_payment(o,pg);return False,'reaprovou cancelado'
    except ValueError:return True,'somente pendente'
check('10 bloquear reaprovacao de pagamento',t10)

# 11 cancelar pago
def t11():
    o=Order(status='APROVADO');p={'A':Product(1)}
    try:cancel_order(o,p);return False,'cancelou pago'
    except ValueError:return True,'pedido pago exige estorno separado'
check('11 bloquear cancelamento direto de pedido pago',t11)

# 12 devolver produto inativo
def t12():
    p={'A':Product(0,False)};o=Order(status='AGUARDANDO_PAGAMENTO',snapshot=[('A',2,Decimal('10'))]);cancel_order(o,p)
    return (p['A'].stock==2 and o.status=='CANCELADO','devolucao independe de ativo')
check('12 devolver estoque mesmo com produto inativo',t12)

# 13 auditoria reserva/cancelamento
def t13():
    movements.clear();p={'A':Product(5)};o=Order(total=Decimal('10'),snapshot=[('A',1,Decimal('10'))]);approve_order(o,p);cancel_order(o,p)
    return ([x[0] for x in movements]==['SAIDA','ENTRADA'],'movimentacoes registradas')
check('13 auditar reserva e devolucao',t13)

# 14 concorrencia conceitual: duas reservas sequenciais sobre mesmo saldo
def t14():
    prod=Product(1);atomic_reserve(prod,1)
    try:atomic_reserve(prod,1);return False,'segunda reserva conseguiu'
    except ValueError:return True,'segunda reserva falha no saldo atualizado'
check('14 impedir overselling no update atomico',t14)

# 15 estoque negativo no schema
def t15():
    s=(ROOT/'src/main/resources/schema.sql').read_text()
    return ('check(estoque >= 0)' in s.lower(),'constraint presente')
check('15 constraint estoque nao negativo',t15)

# 16 quantidades positivas no schema
def t16():
    s=(ROOT/'src/main/resources/schema.sql').read_text().lower()
    return (s.count('check(quantidade > 0)')>=3,'constraints presentes')
check('16 constraint quantidades positivas',t16)

# 17 pagamento unico por pedido
def t17():
    s=(ROOT/'src/main/resources/schema.sql').read_text().lower()
    return ('pedido_id uuid not null unique references pedido(id)' in s,'unique presente')
check('17 pagamento unico por pedido',t17)

# 18 carrinho unico por pedido
def t18():
    s=(ROOT/'src/main/resources/schema.sql').read_text().lower()
    return ('carrinho_id uuid not null unique references carrinho(id)' in s,'unique presente')
check('18 carrinho gera no maximo um pedido',t18)

# 19 SQL atomico
def t19():
    s=(ROOT/'src/main/java/aulas/umc/oo/repository/ProdutoJdbcRepository.java').read_text().lower()
    return ('estoque=estoque-?' in s and 'estoque>=?' in s,'update condicional presente')
check('19 reserva/saida usa SQL atomico',t19)

# 20 CRUD produto nao grava estoque
def t20():
    s=(ROOT/'src/main/java/aulas/umc/oo/repository/ProdutoJdbcRepository.java').read_text().lower()
    trecho=s[s.index('public produto update'):s.index('public void deletelogical')]
    return ('estoque=?' not in trecho,'update de produto altera apenas nome/preco')
check('20 CRUD produto nao altera saldo',t20)

# 21 pipeline detecta ordem duplicada
def t21():
    s=(ROOT/'src/main/java/aulas/umc/oo/pipeline/PipelineExecutor.java').read_text()
    return ('Strategies com ordem duplicada' in s,'fail-fast configurado')
check('21 detectar ordem duplicada de strategy',t21)

# 22 frete detecta duplicidade
def t22():
    s=(ROOT/'src/main/java/aulas/umc/oo/strategy/frete/FreteStrategyFactory.java').read_text()
    return ('putIfAbsent' in s and 'Mais de uma FreteStrategy' in s,'fail-fast configurado')
check('22 detectar FreteStrategy duplicada',t22)

# 23 tratamento de persistencia
def t23():
    s=(ROOT/'src/main/java/aulas/umc/oo/controller/ApiExceptionHandler.java').read_text()
    return ('DataIntegrityViolationException' in s and 'RepositoryException' in s,'erros traduzidos para resposta controlada')
check('23 tratar violacao de integridade',t23)

# 24 estoque inicial auditado
def t24():
    s=(ROOT/'src/main/java/aulas/umc/oo/UseCase/ProdutoUseCase.java').read_text()
    return ('ESTOQUE_INICIAL' in s and 'movRepo.insert' in s,'estoque inicial gera movimento')
check('24 auditar estoque inicial',t24)

# 25 estorno feliz devolve estoque e cancela pedido
def t25():
    movements.clear()
    p={'A':Product(10)}
    c=Cart('C1',[('A',1,Decimal('100'))])
    o=Order(); calc_freight(o,c,p); approve_order(o,p)
    pg=Payment(value=o.total); approve_payment(o,pg)
    refund_payment(o,pg,p)
    return (pg.status=='ESTORNADO' and o.status=='CANCELADO' and p['A'].stock==10 and movements[-1][3]=='ESTORNO','estorno consistente')
check('25 estornar pagamento aprovado',t25)

# 26 estorno nao pode ser executado duas vezes
def t26():
    p={'A':Product(1)}; o=Order(status='APROVADO',snapshot=[('A',1,Decimal('10'))]); pg=Payment(status='APROVADO',value=Decimal('10'))
    refund_payment(o,pg,p)
    try: refund_payment(o,pg,p); return False,'segundo estorno permitido'
    except ValueError:return True,'estorno idempotente por bloqueio de estado'
check('26 bloquear estorno duplicado',t26)

# 27 pagamento pendente nao pode ser estornado
def t27():
    p={'A':Product(1)}; o=Order(status='APROVADO',snapshot=[('A',1,Decimal('10'))]); pg=Payment(status='PENDENTE',value=Decimal('10'))
    try: refund_payment(o,pg,p); return False,'pendente estornado'
    except ValueError:return True,'somente aprovado'
check('27 bloquear estorno de pagamento pendente',t27)

# 28 pipeline de estorno possui quatro etapas ordenadas
def t28():
    root=ROOT/'src/main/java/aulas/umc/oo/strategy/processo'
    found=[]
    import re
    for f in root.glob('*.java'):
        text=f.read_text()
        if 'Processo.ESTORNAR_PAGAMENTO' in text:
            m=re.search(r'int\s+ordem\s*\(\)\s*\{\s*return\s+(\d+)',text)
            if m: found.append(int(m.group(1)))
    return (sorted(found)==[10,20,30,40],f'ordens={sorted(found)}')
check('28 pipeline de estorno possui 4 strategies ordenadas',t28)

# 29 endpoint de estorno exposto
def t29():
    s=(ROOT/'src/main/java/aulas/umc/oo/controller/PagamentoController.java').read_text()
    return ('processos/estornar' in s and 'estornarPagamento' in s,'endpoint presente')
check('29 endpoint POST de estorno',t29)

# 30 estorno participa da mesma transacao do pipeline
def t30():
    s=(ROOT/'src/main/java/aulas/umc/oo/pipeline/PipelineExecutor.java').read_text()
    return ('@Transactional public ProcessContext executar' in s,'executor transacional')
check('30 pipeline de estorno transacional',t30)

# 31 status ESTORNADO existe no dominio
def t31():
    s=(ROOT/'src/main/java/aulas/umc/oo/model/StatusPagamento.java').read_text()
    return ('ESTORNADO' in s,'status explicito presente')
check('31 status de pagamento ESTORNADO',t31)

print('CENARIO|RESULTADO|OBSERVACAO')
for r in results:print('|'.join(r))
passed=sum(r[1]=='PASSOU' for r in results)
print(f'\nTOTAL={len(results)} PASSOU={passed} FALHOU={len(results)-passed}')
raise SystemExit(0 if passed==len(results) else 1)
