import {useMemo,useState} from 'react';
import {useMutation,useQuery,useQueryClient} from '@tanstack/react-query';
import {Alert,Box,Button,CircularProgress,FormControl,InputLabel,MenuItem,Select,Stack,TextField,Typography} from '@mui/material';
import {api,type Produto} from '../services/api';
import {ProductGrid} from '../components/organisms/ProductGrid';
import {SectionTitle} from '../components/atoms/SectionTitle';
import {StatusMessage} from '../components/molecules/StatusMessage';
export function Catalog(){
 const [search,setSearch]=useState('');const [customer,setCustomer]=useState('');const [cart,setCart]=useState('');const [notice,setNotice]=useState('');const qc=useQueryClient();
 const products=useQuery({queryKey:['produtos'],queryFn:api.produtos});
 const customers=useQuery({queryKey:['clientes'],queryFn:api.clientes});
 const carts=useQuery({queryKey:['carrinhos'],queryFn:api.carrinhos});
 const mutation=useMutation({mutationFn:async(p:Produto)=>{
  if(!customer)throw new Error('Selecione um cliente antes de adicionar ao carrinho.');
  let cartId=cart;
  if(!cartId){const created=await api.criarCarrinho(customer);cartId=created.id;setCart(cartId);}
  await api.adicionarItem(cartId,p.id,1);
 },onSuccess:()=>{setNotice('Produto incluído no carrinho pela API.');qc.invalidateQueries({queryKey:['carrinhos']});}});
 const filtered=useMemo(()=>(products.data||[]).filter(p=>p.nome.toLocaleLowerCase('pt-BR').includes(search.toLocaleLowerCase('pt-BR'))),[products.data,search]);
 return <><Box sx={{background:'linear-gradient(110deg,#651fff,#9239e8 60%,#ff3d71)',color:'white',borderRadius:5,p:{xs:3,md:5},mb:4}}><Typography variant="overline">MAGAZINE HERO</Typography><Typography variant="h4">Encontre o próximo favorito.</Typography><Typography>Loja com catálogo integrado à API Java e dados do PostgreSQL.</Typography></Box>
 <SectionTitle title="Produtos da loja" subtitle="Busca por nome e inclusão em carrinho real"/>
 <Stack direction={{xs:'column',md:'row'}} gap={2} sx={{mb:3}}>
 <TextField label="Buscar produto" value={search} onChange={e=>setSearch(e.target.value)} sx={{flex:2}}/>
 <FormControl sx={{flex:1,minWidth:180}}><InputLabel>Cliente</InputLabel><Select value={customer} label="Cliente" onChange={e=>{setCustomer(e.target.value);setCart('');}}>{(customers.data||[]).map(c=><MenuItem key={c.id} value={c.id}>{c.nome}</MenuItem>)}</Select></FormControl>
 <FormControl sx={{flex:1,minWidth:180}}><InputLabel>Carrinho</InputLabel><Select value={cart} label="Carrinho" onChange={e=>setCart(e.target.value)}><MenuItem value="">Criar novo ao adicionar</MenuItem>{(carts.data||[]).filter(c=>c.ativo&&c.clienteId===customer).map(c=><MenuItem key={c.id} value={c.id}>{c.id.slice(0,8)} — {c.itens.length} itens</MenuItem>)}</Select></FormControl>
 </Stack>
 {notice&&<Alert severity="success" onClose={()=>setNotice('')} sx={{mb:2}}>{notice}</Alert>}
 {mutation.error&&<StatusMessage error={mutation.error}/>}
 {(customers.error||carts.error)&&<Alert severity="warning" sx={{mb:2}}>Não foi possível carregar clientes ou carrinhos. Confira a autenticação.</Alert>}
 {products.isPending?<CircularProgress/>:products.error?<StatusMessage error={products.error} onRetry={()=>products.refetch()}/>:filtered.length?<ProductGrid products={filtered} onAdd={p=>mutation.mutate(p)} disabled={mutation.isPending}/>:<Alert severity="info">Nenhum produto encontrado.</Alert>}
 </>;
}
