package aulas.umc.oo.security;

public final class Permission {
    private Permission() {}

    public static final String CLIENTE_LER = "CLIENTE_LER";
    public static final String CLIENTE_ESCREVER = "CLIENTE_ESCREVER";
    public static final String PRODUTO_LER = "PRODUTO_LER";
    public static final String PRODUTO_ESCREVER = "PRODUTO_ESCREVER";
    public static final String CARRINHO_LER = "CARRINHO_LER";
    public static final String CARRINHO_ESCREVER = "CARRINHO_ESCREVER";
    public static final String CARRINHO_PROCESSAR = "CARRINHO_PROCESSAR";
    public static final String PEDIDO_LER = "PEDIDO_LER";
    public static final String PEDIDO_ESCREVER = "PEDIDO_ESCREVER";
    public static final String PEDIDO_CALCULAR_FRETE = "PEDIDO_CALCULAR_FRETE";
    public static final String PEDIDO_APROVAR = "PEDIDO_APROVAR";
    public static final String PEDIDO_CANCELAR = "PEDIDO_CANCELAR";
    public static final String PEDIDO_EXCLUIR = "PEDIDO_EXCLUIR";
    public static final String PEDIDO_EDITAR = "PEDIDO_EDITAR";
    public static final String PAGAMENTO_LER = "PAGAMENTO_LER";
    public static final String PAGAMENTO_ESCREVER = "PAGAMENTO_ESCREVER";
    public static final String PAGAMENTO_APROVAR = "PAGAMENTO_APROVAR";
    public static final String PAGAMENTO_ESTORNAR = "PAGAMENTO_ESTORNAR";
    public static final String ESTOQUE_LER = "ESTOQUE_LER";
    public static final String ESTOQUE_MOVIMENTAR = "ESTOQUE_MOVIMENTAR";
}
