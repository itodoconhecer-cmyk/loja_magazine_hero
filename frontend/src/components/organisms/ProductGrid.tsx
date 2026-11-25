import {Add,CheckCircleOutline} from '@mui/icons-material';
import {Box,Button,Card,Chip,Grid,Stack,Typography} from '@mui/material';
import {VerifiedProductImage} from '../molecules/VerifiedProductImage';
import {moeda,type Produto} from '../../services/api';

export function ProductGrid({products,onAdd,disabled}:{products:Produto[];onAdd:(p:Produto)=>void;disabled?:boolean}){
 return <Grid container spacing={{xs:2,md:2.5}}>
 {products.map(p=><Grid key={p.id} size={{xs:12,sm:6,md:4,lg:3}}>
   <Card elevation={0} sx={{height:'100%',display:'flex',flexDirection:'column',overflow:'hidden',border:'1px solid #edf0f6',boxShadow:'0 12px 32px rgba(33,31,76,.055)',transition:'transform .22s,box-shadow .22s','&:hover':{transform:'translateY(-5px)',boxShadow:'0 20px 42px rgba(33,31,76,.13)'}}}>
    <Box sx={{position:'relative'}}>
      <VerifiedProductImage produto={p} height={205}/>
      <Chip size="small" label={p.departamento||'Destaques'} sx={{position:'absolute',top:14,left:14,bgcolor:'rgba(255,255,255,.9)',fontWeight:800}}/>
    </Box>
    <Stack spacing={1} sx={{p:2.4,flex:1}}>
      <Stack direction="row" spacing={.6} alignItems="center"><CheckCircleOutline sx={{fontSize:15,color:p.ativo?'#008f77':'#a4a9b9'}}/><Typography variant="caption" color="text.secondary">{p.ativo?'Disponível':'Indisponível'}</Typography></Stack>
      <Typography fontWeight={800} sx={{minHeight:52,fontSize:'1.05rem',lineHeight:1.42}}>{p.nome}</Typography>
      <Typography variant="h6" sx={{fontWeight:900,color:'#28213f'}}>{moeda(Number(p.preco))}</Typography>
      <Typography variant="caption" color="text.secondary">Preço sujeito a confirmação no checkout</Typography>
      <Box sx={{flex:1}}/>
      <Button variant="contained" size="large" fullWidth startIcon={<Add/>} disabled={!p.ativo||disabled} onClick={()=>onAdd(p)} sx={{mt:1,borderRadius:3,py:1.2}}>
        {p.ativo?'Adicionar ao carrinho':'Indisponível'}
      </Button>
    </Stack>
   </Card>
 </Grid>)}
 </Grid>;
}
