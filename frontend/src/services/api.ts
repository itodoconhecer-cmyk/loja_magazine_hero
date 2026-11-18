export type Produto={id:string;nome:string;preco:number;ativo:boolean};
export type Cliente={id:string;nome:string;email:string};
export type ItemCarrinho={id:string;produtoId:string;quantidade:number};
export type Carrinho={id:string;clienteId:string;ativo:boolean;itens:ItemCarrinho[]};
export type Pedido={id:string;clienteId:string;carrinhoId:string;status:string;tipoFrete:'NORMAL'|'EXPRESSO';subtotal:number;valorFrete:number;total:number;ativo:boolean};
export type Pagamento={id:string;pedidoId:string;valor:number;status:string;meio:string};
let accessToken='';
let testUser='';
let testPassword='';
export function setAccessToken(value:string){
  accessToken=value.trim();testUser='';testPassword='';
}
export function setTestCredentials(username:string,password:string){
  testUser=username.trim();testPassword=password;accessToken='';
}
export function clearCredentials(){accessToken='';testUser='';testPassword='';}
export function hasAccessToken(){return !!accessToken||!!(testUser&&testPassword);}
export function isTestLogin(){return !!(testUser&&testPassword);}
function basicAuthorization(username:string,password:string){
  // TextEncoder evita corrupcao de caracteres Unicode antes do Base64.
  const bytes=new TextEncoder().encode(`${username}:${password}`);
  let binary='';
  for(const byte of bytes)binary+=String.fromCharCode(byte);
  return `Basic ${btoa(binary)}`;
}
const base=(import.meta.env.VITE_API_BASE_URL||'').replace(/\/$/,'');
export async function request<T>(path:string,init:RequestInit={}):Promise<T>{
  const headers=new Headers(init.headers);
  if(init.body)headers.set('Content-Type','application/json');
  if(accessToken)headers.set('Authorization',`Bearer ${accessToken}`);
  else if(testUser&&testPassword)headers.set('Authorization',basicAuthorization(testUser,testPassword));
  let response:Response;
  try{response=await fetch(`${base}${path}`,{...init,headers});}
  catch{throw new Error('Falha de conexão com a API. Verifique o backend e o proxy.');}
  if(!response.ok){const data:unknown=await response.json().catch(()=>null);const info=(data&&typeof data==='object'?data:{} ) as Record<string,unknown>;
    const msg=String(info.message||info.mensagem||info.detail||response.statusText||'Erro');
    throw new Error(`HTTP ${response.status}: ${msg}`);}
  if(response.status===204)return undefined as T;
  return response.json() as Promise<T>;
}
const post=<T>(url:string,data:object)=>request<T>(url,{method:'POST',body:JSON.stringify(data)});
const action=(url:string)=>request<unknown>(url,{method:'POST'});
const id=(value:string)=>encodeURIComponent(value);
export const api={
  produtos:()=>request<Produto[]>('/api/produtos'),criarProduto:(nome:string,preco:number)=>post<Produto>('/api/produtos',{nome,preco}),
  clientes:()=>request<Cliente[]>('/api/clientes'),criarCliente:(nome:string,email:string)=>post<Cliente>('/api/clientes',{nome,email}),
  carrinhos:()=>request<Carrinho[]>('/api/carrinhos'),criarCarrinho:(clienteId:string)=>post<Carrinho>('/api/carrinhos',{clienteId}),
  adicionarItem:(cartId:string,produtoId:string,quantidade:number)=>post<Carrinho>(`/api/carrinhos/${id(cartId)}/itens`,{produtoId,quantidade}),
  esvaziarCarrinho:(cartId:string)=>action(`/api/carrinhos/${id(cartId)}/processos/esvaziar`),
  pedidos:()=>request<Pedido[]>('/api/pedidos'),criarPedido:(clienteId:string,carrinhoId:string,tipoFrete:'NORMAL'|'EXPRESSO')=>post<Pedido>('/api/pedidos',{clienteId,carrinhoId,tipoFrete}),
  calcularFrete:(orderId:string)=>action(`/api/pedidos/${id(orderId)}/processos/calcular-frete`),aprovarPedido:(orderId:string)=>action(`/api/pedidos/${id(orderId)}/processos/aprovar`),cancelarPedido:(orderId:string)=>action(`/api/pedidos/${id(orderId)}/processos/cancelar`),
  pagamentos:()=>request<Pagamento[]>('/api/pagamentos'),criarPagamento:(pedidoId:string,valor:number,meio:string)=>post<Pagamento>('/api/pagamentos',{pedidoId,valor,meio}),aprovarPagamento:(paymentId:string)=>action(`/api/pagamentos/${id(paymentId)}/processos/aprovar`),estornarPagamento:(paymentId:string)=>action(`/api/pagamentos/${id(paymentId)}/processos/estornar`)
};
export const moeda=(value:number)=>new Intl.NumberFormat('pt-BR',{style:'currency',currency:'BRL'}).format(value);
