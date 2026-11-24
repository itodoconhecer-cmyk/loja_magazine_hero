import {useState} from 'react';
import {useQueryClient} from '@tanstack/react-query';
import {NavLink,Navigate,Route,Routes} from 'react-router-dom';
import {ArrowForward,DashboardOutlined,Key,Login,Logout,Menu as MenuIcon,ShoppingBagOutlined,StorefrontOutlined} from '@mui/icons-material';
import {Alert,AppBar,Box,Button,Container,Dialog,DialogActions,DialogContent,DialogTitle,Divider,Stack,TextField,Toolbar,Typography} from '@mui/material';
import {clearCredentials,hasAccessToken,request,setAccessToken,setTestCredentials} from '../services/api';
import {Catalog} from '../features/Catalog';
import {Checkout} from '../features/Checkout';
import {Dashboard} from '../features/Dashboard';
import {Management} from '../features/Management';

const adminLinks=[['/dashboard','Painel'],['/produtos','Produtos'],['/clientes','Clientes'],['/carrinhos','Carrinhos'],['/pedidos','Pedidos'],['/pagamentos','Pagamentos']] as const;

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
     setLoginError(error instanceof Error?error.message:'Não foi possível autenticar');
   }finally{setPending(false);}
 }
 function applyJwt(){setAccessToken(draft);setReady(hasAccessToken());setDraft('');setJwtOpen(false);client.clear();}
 const loginPrompt=<Box sx={{maxWidth:620,mx:'auto',mt:5,mb:9,textAlign:'center',borderRadius:5,p:5,bgcolor:'white',border:'1px solid #e9e7f5'}}>
   <DashboardOutlined sx={{fontSize:58,color:'#7347d9'}}/><Typography variant="h5" fontWeight={900} mt={1}>Área restrita</Typography>
   <Typography color="text.secondary" my={2}>O painel administrativo exige uma conta com permissões específicas.</Typography>
   <Button variant="contained" onClick={()=>setOpen(true)} startIcon={<Login/>}>Entrar no back office</Button>
 </Box>;
 return <Box sx={{minHeight:'100vh',display:'flex',flexDirection:'column'}}>
  <Box sx={{bgcolor:'#27194f',color:'white',py:.7}}><Container maxWidth="xl"><Stack direction="row" alignItems="center" justifyContent="center" gap={1}><ShoppingBagOutlined sx={{fontSize:16}}/><Typography variant="caption" fontWeight={750} letterSpacing={.4}>MAGAZINE HERO · ESCOLHAS QUE COMBINAM COM VOCÊ</Typography></Stack></Container></Box>
  <AppBar position="sticky" elevation={0} color="inherit" sx={{borderBottom:'1px solid #ebe8f5',bgcolor:'rgba(255,255,255,.96)',backdropFilter:'blur(16px)'}}>
   <Container maxWidth="xl"><Toolbar disableGutters sx={{gap:{xs:1,sm:2},minHeight:76}}>
    <Box component={NavLink} to="/" sx={{display:'flex',alignItems:'center',gap:1.2,color:'inherit',textDecoration:'none',flex:1}}>
      <Box sx={{width:44,height:44,borderRadius:'14px',background:'linear-gradient(140deg,#7350ef,#fe5597)',display:'grid',placeItems:'center',color:'white'}}><ShoppingBagOutlined/></Box>
      <Box><Typography fontWeight={950} sx={{letterSpacing:'-.045em',lineHeight:1.1,fontSize:{xs:20,md:23}}}>magazine<Box component="span" sx={{color:'#7646e7'}}>hero.</Box></Typography><Typography variant="caption" color="text.secondary" sx={{fontSize:10,letterSpacing:1}}>SUA LOJA, SUAS ESCOLHAS</Typography></Box>
    </Box>
    <Button component={NavLink} to="/" color="inherit" startIcon={<StorefrontOutlined/>} sx={{display:{xs:'none',sm:'inline-flex'}}}>Vitrine</Button>
    {ready?<><Button component={NavLink} to="/dashboard" color="inherit" startIcon={<DashboardOutlined/>}>Back office</Button><Button color="inherit" onClick={logout} startIcon={<Logout/>}>Sair</Button></>:
      <Button variant="outlined" onClick={()=>setOpen(true)} startIcon={<Login/>} sx={{borderRadius:3}}>Entrar</Button>}
   </Toolbar></Container>
  </AppBar>
  <Container maxWidth="xl" sx={{pt:{xs:3,md:4},pb:5,flex:1}}>
   {ready&&<Stack direction="row" flexWrap="wrap" gap={1} sx={{mb:3,p:1.2,bgcolor:'#f0ecfc',borderRadius:3}}>{adminLinks.map(([path,label])=><Button key={path} component={NavLink} to={path} size="small" variant="text" sx={{'&.active':{bgcolor:'#6440d5',color:'white'},borderRadius:2}}>{label}</Button>)}<Button size="small" startIcon={<Key/>} onClick={()=>setJwtOpen(true)}>Configurar JWT</Button></Stack>}
   <Routes>
    <Route path="/" element={<Catalog/>}/>
    <Route path="/checkout" element={<Checkout authenticated={ready} onLogin={()=>setOpen(true)}/>}/>
    <Route path="/dashboard" element={ready?<Dashboard/>:loginPrompt}/>
    {adminLinks.slice(1).map(([path])=><Route key={path} path={path} element={ready?<Management section={path.slice(1)}/>:loginPrompt}/>)}
    <Route path="*" element={<Navigate to="/" replace/>}/>
   </Routes>
  </Container>
  <Box component="footer" sx={{bgcolor:'#231946',color:'white',mt:'auto',py:5}}>
   <Container maxWidth="xl"><Stack direction={{xs:'column',md:'row'}} justifyContent="space-between" gap={3}>
    <Box><Typography fontWeight={950} fontSize={22}>magazinehero.</Typography><Typography sx={{color:'#c8c0e4',maxWidth:370,mt:1}} variant="body2">Uma experiência de compra simples e bonita. Navegue livremente e faça login somente quando for finalizar.</Typography></Box>
    <Box><Typography fontWeight={800}>Sua experiência</Typography><Stack spacing={1} mt={1}><Typography variant="body2" sx={{color:'#c8c0e4'}}>Catálogo público</Typography><Typography variant="body2" sx={{color:'#c8c0e4'}}>Carrinho sem login</Typography><Typography variant="body2" sx={{color:'#c8c0e4'}}>Checkout protegido</Typography></Stack></Box>
    <Box><Typography fontWeight={800}>Loja Magazine Hero</Typography><Typography variant="body2" sx={{color:'#c8c0e4',mt:1}}>Projeto educacional Novos Horizontes</Typography><Typography variant="caption" sx={{color:'#a99dcf'}}>Pagamentos reais ainda não habilitados.</Typography></Box>
   </Stack><Divider sx={{borderColor:'rgba(255,255,255,.17)',my:3}}/><Typography variant="caption" sx={{color:'#a99dcf'}}>© 2026 Magazine Hero · Ambiente educacional</Typography></Container>
  </Box>
  <Dialog open={open} onClose={()=>{if(!pending)setOpen(false)}} fullWidth maxWidth="xs">
   <DialogTitle fontWeight={900}>Entrar na Magazine Hero</DialogTitle>
   <DialogContent><Stack spacing={2} sx={{pt:1}}>
     <Alert severity="info">O acesso disponível é administrativo e de laboratório. O cadastro e login de compradores ainda serão integrados.</Alert>
     {loginError&&<Alert severity="error">{loginError}</Alert>}
     <TextField fullWidth label="Usuário" autoComplete="username" value={username} onChange={e=>setUsername(e.target.value)}/>
     <TextField fullWidth label="Senha" type="password" autoComplete="current-password" value={password} onChange={e=>setPassword(e.target.value)} onKeyDown={e=>{if(e.key==='Enter'&&!pending&&username&&password)void login();}}/>
   </Stack></DialogContent>
   <DialogActions><Button onClick={()=>setOpen(false)} disabled={pending}>Cancelar</Button><Button variant="contained" onClick={()=>void login()} disabled={pending||!username.trim()||!password}>{pending?'Validando...':'Entrar'}</Button></DialogActions>
  </Dialog>
  <Dialog open={jwtOpen} onClose={()=>setJwtOpen(false)} fullWidth>
   <DialogTitle>Token JWT (integração OIDC)</DialogTitle>
   <DialogContent><Typography color="text.secondary" variant="body2" sx={{mb:2}}>Token temporário, mantido apenas na memória da aba.</Typography>
    <TextField fullWidth multiline minRows={3} autoComplete="off" label="Access token JWT" value={draft} onChange={e=>setDraft(e.target.value)}/></DialogContent>
   <DialogActions><Button onClick={()=>setJwtOpen(false)}>Cancelar</Button><Button variant="contained" onClick={applyJwt}>Aplicar</Button></DialogActions>
  </Dialog>
 </Box>;
}
