import {useEffect,useMemo,useState} from 'react';
import {ArrowBackIosNew,ArrowForwardIos,AutoAwesome,ShoppingBagOutlined} from '@mui/icons-material';
import {Box,Button,Card,Chip,IconButton,Stack,Typography} from '@mui/material';
import {VerifiedProductImage} from '../molecules/VerifiedProductImage';
import {moeda,type Produto} from '../../services/api';

export function FeaturedCarousel({products,onAdd}:{products:Produto[];onAdd:(product:Produto)=>void}){
 const featured=useMemo(()=>{
  const seen=new Set<string>();
  return products.filter(p=>{const k=p.departamento||'Outros';if(!p.imagemUrl||seen.has(k))return false;seen.add(k);return true;});
 },[products]);
 const [active,setActive]=useState(0);
 useEffect(()=>{if(featured.length<2)return;
  const id=window.setInterval(()=>setActive(i=>(i+1)%featured.length),7000);
  return ()=>window.clearInterval(id);
 },[featured.length]);
 if(!featured.length)return null;
 const visible=Array.from({length:Math.min(3,featured.length)},(_,offset)=>featured[(active+offset)%featured.length]);
 return <Box sx={{mb:5,borderRadius:5,p:{xs:2,md:3},background:'linear-gradient(115deg,#f6f1ff,#edf7ff)'}}>
  <Stack direction="row" alignItems="center" justifyContent="space-between" gap={1} sx={{mb:2}}>
   <Box><Stack direction="row" alignItems="center" gap={1}><AutoAwesome sx={{color:'#754be2'}}/><Typography variant="overline" color="primary" fontWeight={900}>ESCOLHAS EM DESTAQUE</Typography></Stack>
    <Typography variant="h5" fontWeight={950}>Explore nossos departamentos</Typography></Box>
   <Stack direction="row" gap={1}>
    <IconButton aria-label="Departamento anterior" onClick={()=>setActive(i=>(i-1+featured.length)%featured.length)} sx={{bgcolor:'white'}}><ArrowBackIosNew/></IconButton>
    <IconButton aria-label="Próximo departamento" onClick={()=>setActive(i=>(i+1)%featured.length)} sx={{bgcolor:'white'}}><ArrowForwardIos/></IconButton>
   </Stack>
  </Stack>
  <Stack direction="row" gap={2} sx={{overflow:'hidden'}}>
   {visible.map((p,index)=><Card key={p.id} sx={{flex:'1 0 0',minWidth:0,display:{xs:index===0?'block':'none',md:'block'},borderRadius:4,overflow:'hidden',boxShadow:'0 12px 25px rgba(27,28,72,.08)'}}>
    <Box sx={{position:'relative'}}><VerifiedProductImage produto={p} height={172} eager={index===0}/><Chip label={p.departamento||'Novidades'} size="small" sx={{position:'absolute',top:10,left:10,bgcolor:'white',fontWeight:800}}/></Box>
    <Stack sx={{p:2}} gap={.6}><Typography fontWeight={850} noWrap>{p.nome}</Typography><Typography color="primary" fontWeight={950}>{moeda(Number(p.preco))}</Typography><Button size="small" variant="contained" startIcon={<ShoppingBagOutlined/>} onClick={()=>onAdd(p)}>Adicionar</Button></Stack>
   </Card>)}
  </Stack>
  <Stack direction="row" gap={.75} justifyContent="center" sx={{mt:2}}>
   {featured.map((p,i)=><Box component="button" type="button" key={p.id} aria-label={`Ir ao destaque ${i+1}`} aria-current={active===i?'true':undefined} onClick={()=>setActive(i)} sx={{width:active===i?26:9,height:9,border:0,borderRadius:5,background:active===i?'#6846db':'#c5b9e9',cursor:'pointer',transition:'width .2s'}}/>)}
  </Stack>
 </Box>;
}
