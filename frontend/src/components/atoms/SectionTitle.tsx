import {Box,Typography} from '@mui/material';
export function SectionTitle({title,subtitle}:{title:string;subtitle?:string}){return <Box sx={{mb:3}}><Typography variant="h5">{title}</Typography>{subtitle&&<Typography color="text.secondary">{subtitle}</Typography>}</Box>}
