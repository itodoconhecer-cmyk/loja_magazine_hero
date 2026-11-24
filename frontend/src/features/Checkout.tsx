import {useMemo} from 'react';
import {ArrowBack,LockOutlined,PersonOutline,ReceiptLongOutlined,ShoppingBagOutlined} from '@mui/icons-material';
import {Alert,Box,Button,Card,CardContent,Divider,Stack,Typography} from '@mui/material';
import {Link as RouterLink} from 'react-router-dom';
import {useQuery} from '@tanstack/react-query';
import {api,moeda} from '../services/api';
import {cartTotal,readCart} from '../services/guestCart';

/**
 * Checkout segura: NUNCA transformar um carrinho local em pedido sem
 * autenticacao e prova de vinculo entre identidade e clienteId.
 * O backend atual ainda nao expoe sessao de comprador com esse vinculo.
 */
export function Checkout({authenticated,onLogin}:{authenticated:boolean;onLogin:()=>void}){
 const cart=useMemo(()=>readCart(),[]);
 const products=useQuery({queryKey:['public-catalog'],queryFn:api.catalogo});
 const total=cartTotal(cart,products.data||[]);
 return <Box sx={{maxWidth:940,mx:'auto',pb:8}}>
  <Button component={RouterLink} to="/" startIcon={<ArrowBack/>} sx={{mb:2}}>Voltar à vitrine</Button>
  <Typography variant="overline" color="secondary" fontWeight={900}>CHECKOUT SEGURO</Typography>
  <Typography variant="h4" fontWeight={950} mb={1}>Quase lá! Seu pedido começa aqui.</Typography>
  <Typography color="text.secondary" mb={3}>Você pode escolher produtos sem login. A identificação é obrigatória apenas para criar o pedido e pagar.</Typography>
  <Stack direction={{xs:'column',md:'row'}} spacing={3} alignItems="flex-start">
   <Card sx={{flex:1,width:'100%'}}><CardContent sx={{p:3}}>
    <Stack direction="row" gap={1} alignItems="center" mb={2}><PersonOutline color="primary"/><Typography variant="h6" fontWeight={900}>Identificação do comprador</Typography></Stack>
    {!authenticated?<><Alert severity="info" sx={{mb:2}}>Para continuar, entre ou crie sua conta. Seus produtos continuarão salvos neste navegador.</Alert>
      <Button variant="contained" fullWidth startIcon={<LockOutlined/>} onClick={onLogin}>Entrar para finalizar</Button>
      <Typography variant="caption" color="text.secondary" display="block" mt={2}>O cadastro e o login de clientes ainda estão sendo integrados ao backend. Não efetuaremos cobrança nem criaremos pedidos sem autenticação válida.</Typography>
    </>:<Alert severity="warning">A autenticação administrativa não representa a identidade de um comprador. A criação do pedido ficará disponível após integrar contas de clientes e validar a propriedade do carrinho no servidor.</Alert>}
   </CardContent></Card>
   <Card sx={{width:{xs:'100%',md:320}}}><CardContent sx={{p:3}}>
    <Stack direction="row" gap={1} alignItems="center"><ReceiptLongOutlined color="primary"/><Typography variant="h6" fontWeight={900}>Resumo</Typography></Stack>
    <Divider sx={{my:2}}/>
    {cart.length?cart.map(item=>{const p=products.data?.find(x=>x.id===item.produtoId);return <Stack key={item.produtoId} direction="row" justifyContent="space-between" gap={1} sx={{mb:1.5}}><Typography variant="body2">{item.quantidade}× {p?.nome||'Produto indisponível'}</Typography><Typography variant="body2" fontWeight={800}>{p?moeda(Number(p.preco)*item.quantidade):'—'}</Typography></Stack>;}):<Stack alignItems="center" spacing={1} py={2}><ShoppingBagOutlined color="disabled"/><Typography variant="body2">Seu carrinho está vazio.</Typography></Stack>}
    <Divider sx={{my:2}}/><Stack direction="row" justifyContent="space-between"><Typography fontWeight={800}>Subtotal</Typography><Typography fontWeight={950}>{moeda(total)}</Typography></Stack>
    <Typography variant="caption" color="text.secondary" display="block" mt={2}>Frete e estoque serão validados no servidor antes da confirmação do pedido.</Typography>
   </CardContent></Card>
  </Stack>
 </Box>;
}
