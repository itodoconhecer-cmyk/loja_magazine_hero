import {Add,ArrowOutward,CheckCircleOutline,DevicesOtherOutlined,HeadphonesOutlined,KitchenOutlined,LocalMallOutlined,SportsEsportsOutlined,WeekendOutlined} from '@mui/icons-material';
import {Box,Button,Card,Chip,Grid,Stack,Typography} from '@mui/material';
import {moeda,type Produto} from '../../services/api';

type Kind='tech'|'audio'|'home'|'games'|'style';
function kindOf(name:string):Kind{
 const s=name.toLowerCase();
 if(/fone|head|audio|som|caixa de som|bluetooth/.test(s))return 'audio';
 if(/sofá|sofa|cadeira|mesa|decor|cozinha|cafeteira|geladeira/.test(s))return 'home';
 if(/game|console|controle|playstation|xbox/.test(s))return 'games';
 if(/roupa|tenis|tênis|camisa|bolsa|mochila|vestido/.test(s))return 'style';
 return 'tech';
}
const visuals={
 tech:{name:'Tecnologia',bg:'linear-gradient(138deg,#ecedff 0%,#d6ddff 100%)',ink:'#5a4bd8',icon:DevicesOtherOutlined},
 audio:{name:'Áudio',bg:'linear-gradient(138deg,#fce6f1 0%,#ffd4e4 100%)',ink:'#d03775',icon:HeadphonesOutlined},
 home:{name:'Casa',bg:'linear-gradient(138deg,#d9f3ed 0%,#bce7dc 100%)',ink:'#197f70',icon:WeekendOutlined},
 games:{name:'Games',bg:'linear-gradient(138deg,#e5dbff 0%,#d0c3fb 100%)',ink:'#6942ce',icon:SportsEsportsOutlined},
 style:{name:'Estilo',bg:'linear-gradient(138deg,#fff0d6 0%,#ffe0ae 100%)',ink:'#b96b2a',icon:LocalMallOutlined}
};
export function ProductGrid({products,onAdd,disabled}:{products:Produto[];onAdd:(p:Produto)=>void;disabled?:boolean}){
 return <Grid container spacing={{xs:2,md:2.5}}>
 {products.map(p=>{
   const visual=visuals[kindOf(p.nome)],Icon=visual.icon;
   return <Grid key={p.id} size={{xs:12,sm:6,md:4,lg:3}}>
    <Card elevation={0} sx={{height:'100%',display:'flex',flexDirection:'column',overflow:'hidden',border:'1px solid #edf0f6',boxShadow:'0 12px 32px rgba(33,31,76,.055)',transition:'transform .22s,box-shadow .22s','&:hover':{transform:'translateY(-5px)',boxShadow:'0 20px 42px rgba(33,31,76,.13)'}}}>
      <Box sx={{height:{xs:190,md:205},background:visual.bg,display:'flex',alignItems:'center',justifyContent:'center',position:'relative'}}>
        <Chip size="small" label={visual.name} sx={{position:'absolute',top:15,left:15,bgcolor:'rgba(255,255,255,.88)',fontWeight:800}}/>
        <Icon sx={{fontSize:112,color:visual.ink,filter:'drop-shadow(0 15px 15px rgba(50,34,86,.15))',transform:'rotate(-8deg)'}}/>
        <ArrowOutward sx={{position:'absolute',bottom:16,right:18,color:visual.ink,opacity:.65}}/>
      </Box>
      <Stack spacing={1} sx={{p:2.4,flex:1}}>
        <Stack direction="row" spacing={.6} alignItems="center"><CheckCircleOutline sx={{fontSize:15,color:p.ativo?'#008f77':'#a4a9b9'}}/><Typography variant="caption" color="text.secondary">{p.ativo?'Pronta entrega':'Indisponível'}</Typography></Stack>
        <Typography fontWeight={800} sx={{minHeight:52,fontSize:'1.05rem',lineHeight:1.42}}>{p.nome}</Typography>
        <Typography variant="h6" sx={{fontWeight:900,color:'#28213f'}}>{moeda(Number(p.preco))}</Typography>
        <Typography variant="caption" color="text.secondary">Preço sujeito a confirmação no checkout</Typography>
        <Box sx={{flex:1}}/>
        <Button variant="contained" size="large" fullWidth startIcon={<Add/>} disabled={!p.ativo||disabled} onClick={()=>onAdd(p)} sx={{mt:1,borderRadius:3,py:1.2}}>
          {p.ativo?'Adicionar ao carrinho':'Indisponível'}
        </Button>
      </Stack>
    </Card>
   </Grid>;
 })}
 </Grid>;
}
