import {useEffect,useState} from 'react';
import {ImageNotSupportedOutlined,ShoppingBagOutlined} from '@mui/icons-material';
import {Box,Typography} from '@mui/material';
import type {Produto} from '../../services/api';

/** Only the backend can serve image bytes after SHA-256 verification. */
export function VerifiedProductImage({produto,height=210,eager=false}:{produto:Produto;height?:number;eager?:boolean}){
 const [failed,setFailed]=useState(false);
 useEffect(()=>setFailed(false),[produto.imagemUrl]);
 const validUrl=typeof produto.imagemUrl==='string' &&
  /^\/api\/catalogo\/produtos\/[0-9a-f-]{36}\/imagem$/i.test(produto.imagemUrl);
 return <Box sx={{height,position:'relative',overflow:'hidden',background:'linear-gradient(135deg,#eee7ff,#d9e5ff)',display:'grid',placeItems:'center'}}>
  {validUrl&&!failed?
   <Box component="img" src={produto.imagemUrl!} alt={`Imagem ilustrativa de ${produto.nome}`} loading={eager?'eager':'lazy'} onError={()=>setFailed(true)}
    sx={{width:'100%',height:'100%',objectFit:'cover',display:'block'}}/>:
   <Box sx={{textAlign:'center',color:'#6850ae'}}><ShoppingBagOutlined sx={{fontSize:60,opacity:.55}}/><Typography fontSize={12}>Imagem indisponível</Typography></Box>}
 </Box>;
}
