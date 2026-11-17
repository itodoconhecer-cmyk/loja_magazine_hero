import {useState} from 'react';
import {useQueryClient} from '@tanstack/react-query';
import {NavLink,Navigate,Route,Routes} from 'react-router-dom';
import {ShoppingBagOutlined,Key} from '@mui/icons-material';
import {Alert,AppBar,Box,Button,Container,Dialog,DialogActions,DialogContent,DialogTitle,Stack,TextField,Toolbar,Typography} from '@mui/material';
import {hasAccessToken,setAccessToken} from '../services/api';
import {Catalog} from '../features/Catalog';
import {Dashboard} from '../features/Dashboard';
import {Management} from '../features/Management';
const links=[['/','Loja'],['/dashboard','Dashboard'],['/produtos','Produtos'],['/clientes','Clientes'],['/carrinhos','Carrinhos'],['/pedidos','Pedidos'],['/pagamentos','Pagamentos']] as const;
export function App(){
 const [open,setOpen]=useState(false);const [draft,setDraft]=useState('');const [ready,setReady]=useState(hasAccessToken());const client=useQueryClient();
 function update(){setAccessToken(draft);setReady(hasAccessToken());setDraft('');setOpen(false);client.clear();}
 return <><AppBar position="sticky" elevation={0}><Toolbar sx={{gap:2}}><ShoppingBagOutlined/><Typography variant="h6" fontWeight={900} sx={{flex:1}}>Magazine Hero</Typography><Button color="inherit" startIcon={<Key/>} onClick={()=>setOpen(true)}>{ready?'Alterar JWT':'Configurar JWT'}</Button></Toolbar></AppBar>
 <Container maxWidth="xl" sx={{py:3}}><Stack direction="row" flexWrap="wrap" gap={1} sx={{mb:3}}>{links.map(([path,label])=><Button key={path} component={NavLink} to={path} variant="outlined" sx={{'&.active':{bgcolor:'primary.main',color:'#fff'}}}>{label}</Button>)}</Stack>
 {!ready&&<Alert severity="warning" sx={{mb:3}}>Endpoints protegidos: informe um JWT válido do provedor configurado no backend. Não há dados simulados ou login fictício.</Alert>}
 <Routes><Route path="/" element={<Catalog/>}/><Route path="/dashboard" element={<Dashboard/>}/>{links.slice(2).map(([path])=><Route key={path} path={path} element={<Management section={path.slice(1)}/>}/>)}<Route path="*" element={<Navigate to="/" replace/>}/></Routes></Container>
 <Dialog open={open} onClose={()=>setOpen(false)} fullWidth><DialogTitle>Credencial temporária de desenvolvimento</DialogTitle><DialogContent><Typography color="text.secondary" variant="body2" sx={{mb:2}}>JWT armazenado apenas em memória até recarregar a aba. Esta entrada manual não substitui OIDC/PKCE de produção.</Typography><TextField fullWidth multiline minRows={3} autoComplete="off" label="Access token JWT" value={draft} onChange={e=>setDraft(e.target.value)}/></DialogContent><DialogActions><Button onClick={()=>setOpen(false)}>Cancelar</Button><Button variant="contained" onClick={update}>Aplicar</Button></DialogActions></Dialog></>;
}
