import {Alert,Button} from '@mui/material';
export function StatusMessage({error,onRetry}:{error:unknown;onRetry?:()=>void}){return <Alert severity="error" action={onRetry?<Button color="inherit" size="small" onClick={onRetry}>Tentar novamente</Button>:undefined}>{error instanceof Error?error.message:'Erro inesperado'}</Alert>}
