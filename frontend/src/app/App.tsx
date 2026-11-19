import {useState} from 'react';
import {useQueryClient} from '@tanstack/react-query';
import {NavLink,Navigate,Route,Routes} from 'react-router-dom';
import {ShoppingBagOutlined,Key,Login,Logout} from '@mui/icons-material';
import {Alert,AppBar,Box,Button,Container,Dialog,DialogActions,DialogContent,DialogTitle,Stack,TextField,Toolbar,Typography} from '@mui/material';
import {api,clearCredentials,hasAccessToken,request,setAccessToken,setTestCredentials} from '../services/api';
import {Catalog} from '../features/Catalog';
import {Dashboard} from '../features/Dashboard';
import {Management} from '../features/Management';
const links=[['/','Loja'],['/dashboard','Dashboard'],['/produtos','Produtos'],['/clientes','Clientes'],['/carrinhos','Carrinhos'],['/pedidos','Pedidos'],['/pagamentos','Pagamentos']] as const;
export function App(){
 const [open,setOpen]=useState(false);
 const [jwtOpen,setJwtOpen]=useState(false);
 const [draft,setDraft]=useState('');
 const [username,setUsername]=useState('');
 const [password,setPassword]=useState('');
 const [loginError,setLoginError]=useState('');
 const [pending,setPending]=useState(false);
 const [ready,setReady]=useState(hasAccessToken());
 const client=useQueryClient();
 function logout(){clearCredentials();setReady(false);client.clear();}
 async function login(){
   setLoginError('');setPending(true);
   setTestCredentials(username,password);
   try{
     await request<unknown[]>('/api/produtos');
     setReady(true);setPassword('');setOpen(false);client.clear();
   }catch(error){
     clearCredentials();setReady(false);
     setLoginError(error instanceof Error?error.message:'Nao foi possivel autenticar');
   }finally{setPending(false);}
 }
 function applyJwt(){setAccessToken(draft);setReady(hasAccessToken());setDraft('');setJwtOpen(false);client.clear();}
 return <><AppBar position="sticky" elevation={0}><Toolbar sx={{gap:2}}>
   <ShoppingBagOutlined/><Typography variant="h6" fontWeight={900} sx={{flex:1}}>Magazine Hero</Typography>
   {ready?<Button color="inherit" startIcon={<Logout/>} onClick={logout}>Sair</Button>:
    <Button color="inherit" startIcon={<Login/>} onClick={()=>setOpen(true)}>Entrar</Button>}
   <Button color="inherit" size="small" startIcon={<Key/>} onClick={()=>setJwtOpen(true)}>JWT</Button>
 </Toolbar></AppBar>
 <Container maxWidth="xl" sx={{py:3}}><Stack direction="row" flexWrap="wrap" gap={1} sx={{mb:3}}>{links.map(([path,label])=><Button key={path} component={NavLink} to={path} variant="outlined" sx={{'&.active':{bgcolor:'primary.main',color:'#fff'}}}>{label}</Button>)}</Stack>
 {!ready&&<Alert severity="warning" sx={{mb:3}}>Entre com a conta de teste para consultar e administrar os dados da loja. Operações de escrita podem modificar o banco de dados.</Alert>}
 <Routes><Route path="/" element={<Catalog/>}/><Route path="/dashboard" element={<Dashboard/>}/>{links.slice(2).map(([path])=><Route key={path} path={path} element={<Management section={path.slice(1)}/>}/>)}<Route path="*" element={<Navigate to="/" replace/>}/></Routes></Container>
 <Dialog open={open} onClose={()=>{if(!pending)setOpen(false)}} fullWidth maxWidth="xs">
   <DialogTitle>Entrar na Loja Magazine Hero</DialogTitle>
   <DialogContent><Stack spacing={2} sx={{pt:1}}>
     <Typography color="text.secondary" variant="body2">Conta administrativa de laboratório: permissões completas para testar os fluxos. Use apenas com dados de teste. As credenciais permanecem na memória da aba e são enviadas por HTTPS.</Typography>
     {loginError&&<Alert severity="error">{loginError}</Alert>}
     <TextField fullWidth label="Usuário" autoComplete="username" value={username} onChange={e=>setUsername(e.target.value)} />
     <TextField fullWidth label="Senha" type="password" autoComplete="current-password" value={password} onChange={e=>setPassword(e.target.value)} onKeyDown={e=>{if(e.key==='Enter'&&!pending&&username&&password)void login();}}/>
   </Stack></DialogContent>
   <DialogActions><Button onClick={()=>setOpen(false)} disabled={pending}>Cancelar</Button><Button variant="contained" onClick={()=>void login()} disabled={pending||!username.trim()||!password}>{pending?'Validando...':'Entrar'}</Button></DialogActions>
 </Dialog>
 <Dialog open={jwtOpen} onClose={()=>setJwtOpen(false)} fullWidth>
   <DialogTitle>Token JWT (integração OIDC)</DialogTitle>
   <DialogContent><Typography color="text.secondary" variant="body2" sx={{mb:2}}>Token de acesso temporário armazenado apenas em memória. O login OIDC com PKCE ainda está pendente.</Typography>
   <TextField fullWidth multiline minRows={3} autoComplete="off" label="Access token JWT" value={draft} onChange={e=>setDraft(e.target.value)}/></DialogContent>
   <DialogActions><Button onClick={()=>setJwtOpen(false)}>Cancelar</Button><Button variant="contained" onClick={applyJwt}>Aplicar</Button></DialogActions>
 </Dialog></>;
}
