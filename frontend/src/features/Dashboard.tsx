import {useQuery} from '@tanstack/react-query';
import {Alert,Box,Card,CardContent,CircularProgress,Grid,Typography} from '@mui/material';
import {api,moeda} from '../services/api';
import {SectionTitle} from '../components/atoms/SectionTitle';
export function Dashboard(){
 const products=useQuery({queryKey:['produtos'],queryFn:api.produtos});const clients=useQuery({queryKey:['clientes'],queryFn:api.clientes});
 const orders=useQuery({queryKey:['pedidos'],queryFn:api.pedidos});const payments=useQuery({queryKey:['pagamentos'],queryFn:api.pagamentos});
 const entries=[['Produtos',products],['Clientes',clients],['Pedidos',orders],['Pagamentos',payments]] as const;
 return <><Box sx={{background:'linear-gradient(110deg,#651fff,#ff3d71)',color:'white',p:4,borderRadius:5,mb:4}}><Typography variant="overline">PAINEL OPERACIONAL</Typography><Typography variant="h4">Sua operação em um só lugar.</Typography><Typography>Indicadores exclusivamente da API, sem números fictícios.</Typography></Box><SectionTitle title="Visão geral"/>
 <Grid container spacing={2}>{entries.map(([name,q])=><Grid key={name} size={{xs:12,sm:6,md:3}}><Card><CardContent><Typography color="text.secondary">{name}</Typography><Typography variant="h4">{q.isPending?<CircularProgress size={24}/>:q.error?'—':q.data?.length??0}</Typography>{q.error&&<Typography color="error" variant="caption">API indisponível / sem permissão</Typography>}</CardContent></Card></Grid>)}</Grid>
 <Box sx={{mt:4}}><SectionTitle title="Pedidos e pagamentos"/></Box>
 {orders.error?<Alert severity="warning">Pedidos indisponíveis. Verifique a API/JWT.</Alert>:<Grid container spacing={2}><Grid size={{xs:12,md:6}}><Card><CardContent><Typography color="text.secondary">Aguardando pagamento</Typography><Typography variant="h4">{orders.data?.filter(p=>p.status==='AGUARDANDO_PAGAMENTO').length??'—'}</Typography></CardContent></Card></Grid><Grid size={{xs:12,md:6}}><Card><CardContent><Typography color="text.secondary">Total dos pedidos consultados</Typography><Typography variant="h4">{orders.data?moeda(orders.data.reduce((sum,p)=>sum+Number(p.total),0)):'—'}</Typography><Typography variant="caption">Todos os status; não representa faturamento recebido.</Typography></CardContent></Card></Grid></Grid>}
 </>;
}
