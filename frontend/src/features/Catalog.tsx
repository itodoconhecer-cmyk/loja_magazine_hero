import {useEffect,useMemo,useState} from 'react';
import {useQuery} from '@tanstack/react-query';
import {ArrowForward,AutoAwesome,Close,DeleteOutline,LocalShippingOutlined,LockOutlined,Menu as MenuIcon,Remove,Add,Search,ShoppingBagOutlined,ShoppingCartCheckout,VerifiedUserOutlined} from '@mui/icons-material';
import {Alert,Badge,Box,Button,Chip,CircularProgress,Divider,Drawer,IconButton,InputAdornment,MenuItem,Select,Stack,TextField,Typography} from '@mui/material';
import {Link as RouterLink} from 'react-router-dom';
import {api,moeda,type Produto} from '../services/api';
import {ProductGrid} from '../components/organisms/ProductGrid';
import {StatusMessage} from '../components/molecules/StatusMessage';
import {addToCart,cartTotal,changeQuantity,readCart,saveCart,type CartEntry} from '../services/guestCart';

type Sort='featured'|'price-low'|'price-high'|'name';
function categoryOf(s:string){
 const v=s.toLowerCase();
 if(/fone|head|som|caixa de som|bluetooth/.test(v))return 'Áudio';
 if(/sofá|sofa|cadeira|mesa|decor|cozinha|cafeteira|geladeira/.test(v))return 'Casa';
 if(/game|console|controle|playstation|xbox/.test(v))return 'Games';
 if(/roupa|tenis|tênis|camisa|bolsa|mochila|vestido/.test(v))return 'Estilo';
 return 'Tecnologia';
}

export function Catalog(){
 const [search,setSearch]=useState('');
 const [category,setCategory]=useState('Todos');
 const [sort,setSort]=useState<Sort>('featured');
 const [cart,setCart]=useState<CartEntry[]>(readCart);
 const [cartOpen,setCartOpen]=useState(false);
 const [notice,setNotice]=useState('');
 const products=useQuery({queryKey:['public-catalog'],queryFn:api.catalogo,staleTime:60000});
 const all=products.data||[];
 useEffect(()=>saveCart(cart),[cart]);
 const categories=useMemo(()=>['Todos',...new Set(all.map(p=>categoryOf(p.nome)))], [all]);
 const filtered=useMemo(()=>{
  const q=search.trim().toLocaleLowerCase('pt-BR');
  const list=all.filter(p=>(category==='Todos'||categoryOf(p.nome)===category)&&p.nome.toLocaleLowerCase('pt-BR').includes(q));
  if(sort==='price-low')return [...list].sort((a,b)=>Number(a.preco)-Number(b.preco));
  if(sort==='price-high')return [...list].sort((a,b)=>Number(b.preco)-Number(a.preco));
  if(sort==='name')return [...list].sort((a,b)=>a.nome.localeCompare(b.nome,'pt-BR'));
  return list;
 },[all,search,category,sort]);
 const count=cart.reduce((sum,item)=>sum+item.quantidade,0);
 const total=cartTotal(cart,all);
 function addProduct(p:Produto){setCart(items=>addToCart(items,p.id));setNotice(p.nome+' foi adicionado ao carrinho.');}
 return <Box>
  <Box sx={{borderRadius:{xs:4,md:6},background:'radial-gradient(ellipse at 76% 28%,#8c68ff 0%,transparent 35%),linear-gradient(125deg,#20164b,#5435a6 59%,#ae63c4)',color:'white',minHeight:{xs:360,md:400},p:{xs:3,md:6},display:'flex',alignItems:'center',position:'relative',overflow:'hidden',mb:4}}>
   <Box sx={{position:'absolute',right:{xs:-130,md:42},top:{xs:175,md:44},width:{xs:270,md:300},height:{xs:270,md:300},borderRadius:'35%',border:'32px solid rgba(255,255,255,.12)',transform:'rotate(-22deg)',pointerEvents:'none'}}/>
   <Box sx={{position:'absolute',right:120,bottom:-75,width:210,height:210,borderRadius:'50%',background:'rgba(255,213,142,.24)',filter:'blur(3px)',pointerEvents:'none'}}/>
   <Box sx={{maxWidth:680,zIndex:1}}>
    <Chip icon={<AutoAwesome sx={{color:'white !important'}}/>} label="SEU PRÓXIMO FAVORITO ESTÁ AQUI" sx={{bgcolor:'rgba(255,255,255,.17)',color:'white',fontWeight:800,letterSpacing:1,mb:2}}/>
    <Typography component="h1" sx={{fontSize:{xs:'2.6rem',sm:'3.6rem',md:'4.4rem'},fontWeight:950,letterSpacing:'-.055em',lineHeight:1.08,mb:2}}>Achou. Gostou.<br/><Box component="span" sx={{color:'#ffd58e'}}>É seu.</Box></Typography>
    <Typography sx={{fontSize:{xs:16,md:18},opacity:.92,maxWidth:480,mb:3}}>Descubra novidades para sua rotina. Escolha com calma, monte seu carrinho e faça login só na hora de comprar.</Typography>
    <Stack direction={{xs:'column',sm:'row'}} gap={1.5}>
      <Button variant="contained" color="secondary" size="large" endIcon={<ArrowForward/>} onClick={()=>document.getElementById('produtos-da-loja')?.scrollIntoView({behavior:'smooth'})} sx={{px:3,py:1.5,bgcolor:'#fff',color:'#3d288d','&:hover':{bgcolor:'#f5eeff'}}}>Explorar produtos</Button>
      <Button size="large" variant="outlined" startIcon={<ShoppingBagOutlined/>} onClick={()=>setCartOpen(true)} sx={{borderColor:'rgba(255,255,255,.6)',color:'white',py:1.5,'&:hover':{borderColor:'white',bgcolor:'rgba(255,255,255,.1)'}}}>Meu carrinho ({count})</Button>
    </Stack>
   </Box>
  </Box>
  <Stack direction={{xs:'column',md:'row'}} justifyContent="space-between" spacing={2} sx={{mb:5}}>
   {[['local-shipping','Entrega com transparência','Frete calculado na finalização'],['shield','Compra protegida','Login apenas no checkout'],['heart','Do seu jeito','Carrinho salvo neste navegador']].map(([key,title,subtitle])=>
    <Stack key={key} direction="row" spacing={1.5} alignItems="center" sx={{flex:1,py:1}}>
     <Box sx={{height:48,width:48,borderRadius:3,bgcolor:'#efeaff',color:'#623fe0',display:'grid',placeItems:'center'}}>{key==='local-shipping'?<LocalShippingOutlined/>:key==='shield'?<VerifiedUserOutlined/>:<ShoppingBagOutlined/>}</Box>
     <Box><Typography fontWeight={850} fontSize={14}>{title}</Typography><Typography color="text.secondary" variant="body2">{subtitle}</Typography></Box>
    </Stack>)}
  </Stack>
  <Box id="produtos-da-loja" sx={{scrollMarginTop:100}}>
   <Stack direction={{xs:'column',md:'row'}} alignItems={{md:'flex-end'}} justifyContent="space-between" gap={2} sx={{mb:3}}>
    <Box><Typography color="secondary" variant="overline" fontWeight={900} letterSpacing={2}>CATÁLOGO MAGAZINE HERO</Typography><Typography variant="h4" fontWeight={950} sx={{letterSpacing:'-.04em'}}>Escolhas que fazem a diferença.</Typography><Typography color="text.secondary">Encontre o que combina com você — sem criar conta.</Typography></Box>
    <Button startIcon={<Badge badgeContent={count} color="secondary"><ShoppingBagOutlined/></Badge>} variant="outlined" sx={{alignSelf:{xs:'flex-start',md:'flex-end'}}} onClick={()=>setCartOpen(true)}>Ver carrinho</Button>
   </Stack>
   <Stack direction={{xs:'column',md:'row'}} gap={1.5} sx={{mb:2}}>
    <TextField size="small" placeholder="O que você está procurando?" value={search} onChange={e=>setSearch(e.target.value)} sx={{flex:1,bgcolor:'white'}} InputProps={{startAdornment:<InputAdornment position="start"><Search/></InputAdornment>}}/>
    <Select size="small" value={sort} onChange={e=>setSort(e.target.value as Sort)} sx={{minWidth:200,bgcolor:'white'}} aria-label="Ordenar produtos">
      <MenuItem value="featured">Destaques</MenuItem><MenuItem value="price-low">Menor preço</MenuItem><MenuItem value="price-high">Maior preço</MenuItem><MenuItem value="name">Nome A–Z</MenuItem>
    </Select>
   </Stack>
   <Stack direction="row" gap={1} flexWrap="wrap" sx={{mb:3}}>{categories.map(name=><Chip key={name} label={name} clickable color={category===name?'primary':'default'} variant={category===name?'filled':'outlined'} onClick={()=>setCategory(name)} sx={{fontWeight:750,px:.7,py:2.1}}/>)}</Stack>
   {notice&&<Alert severity="success" sx={{mb:2}} onClose={()=>setNotice('')}>{notice} <Button size="small" onClick={()=>setCartOpen(true)}>Ver carrinho</Button></Alert>}
   <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{mb:2}}><Typography color="text.secondary" variant="body2">{filtered.length} {filtered.length===1?'produto encontrado':'produtos encontrados'}</Typography><Typography variant="caption" color="text.secondary">Preços em R$</Typography></Stack>
   {products.isPending?<Stack alignItems="center" py={7}><CircularProgress/><Typography mt={2}>Carregando produtos...</Typography></Stack>:products.error?<StatusMessage error={products.error} onRetry={()=>products.refetch()}/>:filtered.length?<ProductGrid products={filtered} onAdd={addProduct}/>:<Alert severity="info">Nenhum produto encontrado. Tente outra busca ou categoria.</Alert>}
  </Box>
  <Box sx={{mt:6,mb:2,borderRadius:5,bgcolor:'#eee9ff',p:{xs:3,md:4},display:'flex',flexDirection:{xs:'column',md:'row'},gap:2,alignItems:{md:'center'},justifyContent:'space-between'}}>
   <Box><Typography variant="h5" fontWeight={950}>Sua próxima descoberta começa aqui.</Typography><Typography color="text.secondary">Sem filas, sem pressa e sem login para explorar.</Typography></Box>
   <Button variant="contained" endIcon={<ArrowForward/>} onClick={()=>document.getElementById('produtos-da-loja')?.scrollIntoView({behavior:'smooth'})}>Ver todos os produtos</Button>
  </Box>
  <Drawer anchor="right" open={cartOpen} onClose={()=>setCartOpen(false)} PaperProps={{sx:{width:{xs:'100%',sm:440},p:3}}}>
   <Stack direction="row" alignItems="center" justifyContent="space-between"><Box><Typography variant="overline" color="secondary" fontWeight={900}>MINHAS ESCOLHAS</Typography><Typography variant="h5" fontWeight={950}>Seu carrinho ({count})</Typography></Box><IconButton aria-label="Fechar carrinho" onClick={()=>setCartOpen(false)}><Close/></IconButton></Stack>
   <Divider sx={{my:2}}/>
   {cart.length===0?<Stack alignItems="center" justifyContent="center" textAlign="center" sx={{flex:1,gap:2}}><ShoppingBagOutlined sx={{fontSize:80,color:'#b8a9e9'}}/><Typography variant="h6" fontWeight={800}>Seu carrinho está vazio</Typography><Typography color="text.secondary">Escolha produtos na vitrine para começar.</Typography><Button variant="contained" onClick={()=>setCartOpen(false)}>Continuar comprando</Button></Stack>:
    <Stack sx={{flex:1,overflow:'auto'}} spacing={2}>
     {cart.map(item=>{const p=all.find(product=>product.id===item.produtoId);return <Stack key={item.produtoId} direction="row" gap={2} sx={{borderBottom:'1px solid #f0edf8',pb:2}}>
       <Box sx={{width:74,height:74,flexShrink:0,borderRadius:3,bgcolor:'#ece5ff',display:'grid',placeItems:'center'}}><ShoppingBagOutlined sx={{color:'#714ad1',fontSize:37}}/></Box>
       <Box sx={{flex:1,minWidth:0}}><Typography fontWeight={800}>{p?.nome||'Produto indisponível'}</Typography><Typography color="primary" fontWeight={900}>{p?moeda(Number(p.preco)*item.quantidade):'Confirmar disponibilidade'}</Typography><Stack direction="row" alignItems="center" gap={.3} sx={{mt:1}}><IconButton size="small" aria-label="Diminuir quantidade" onClick={()=>setCart(items=>changeQuantity(items,item.produtoId,item.quantidade-1))}><Remove fontSize="small"/></IconButton><Typography sx={{minWidth:22,textAlign:'center'}}>{item.quantidade}</Typography><IconButton size="small" aria-label="Aumentar quantidade" onClick={()=>setCart(items=>changeQuantity(items,item.produtoId,item.quantidade+1))}><Add fontSize="small"/></IconButton><IconButton size="small" aria-label="Remover produto" sx={{ml:'auto'}} onClick={()=>setCart(items=>items.filter(i=>i.produtoId!==item.produtoId))}><DeleteOutline fontSize="small"/></IconButton></Stack></Box>
      </Stack>;})}
    </Stack>}
   <Divider sx={{my:2}}/>
   <Stack spacing={1.5}>
    <Stack direction="row" justifyContent="space-between"><Typography color="text.secondary">Subtotal estimado</Typography><Typography variant="h6" fontWeight={950}>{moeda(total)}</Typography></Stack>
    <Typography variant="caption" color="text.secondary">Valores, disponibilidade e frete serão conferidos no checkout.</Typography>
    <Button component={RouterLink} to="/checkout" variant="contained" size="large" fullWidth disabled={!cart.length||cart.some(item=>!all.some(p=>p.id===item.produtoId&&p.ativo))} startIcon={<ShoppingCartCheckout/>} onClick={()=>setCartOpen(false)}>Finalizar compra</Button>
    <Button onClick={()=>setCartOpen(false)} fullWidth>Continuar comprando</Button>
    <Stack direction="row" gap={.8} alignItems="center" justifyContent="center"><LockOutlined sx={{fontSize:14,color:'#85839a'}}/><Typography variant="caption" color="text.secondary">Login exigido apenas no checkout</Typography></Stack>
   </Stack>
  </Drawer>
 </Box>;
}
