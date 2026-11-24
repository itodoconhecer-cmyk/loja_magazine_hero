import type {Produto} from './api';

export type CartEntry={produtoId:string;quantidade:number};
const KEY='magazine-hero:guest-cart:v1';
const MAX_ITEMS=60;
const MAX_QUANTITY=20;

export function sanitizeCart(input:unknown):CartEntry[]{
  if(!Array.isArray(input))return [];
  const unique=new Map<string,number>();
  for(const row of input.slice(0,MAX_ITEMS)){
    if(!row||typeof row!=='object')continue;
    const item=row as Partial<CartEntry>;
    if(typeof item.produtoId!=='string'||! /^[0-9a-f-]{36}$/i.test(item.produtoId))continue;
    if(!Number.isInteger(item.quantidade)||Number(item.quantidade)<1)continue;
    unique.set(item.produtoId,Math.min(MAX_QUANTITY,(unique.get(item.produtoId)||0)+Number(item.quantidade)));
  }
  return [...unique].map(([produtoId,quantidade])=>({produtoId,quantidade}));
}
export function readCart():CartEntry[]{
  try{return sanitizeCart(JSON.parse(localStorage.getItem(KEY)||'[]'));}
  catch{return [];}
}
export function saveCart(items:CartEntry[]):void{
  try{localStorage.setItem(KEY,JSON.stringify(sanitizeCart(items)));}
  catch{/* Storage may be blocked; cart still works in the active page. */}
}
export function addToCart(items:CartEntry[],produtoId:string):CartEntry[]{
  const current=items.find(i=>i.produtoId===produtoId);
  if(!current)return sanitizeCart([...items,{produtoId,quantidade:1}]);
  return items.map(i=>i.produtoId===produtoId?{...i,quantidade:Math.min(MAX_QUANTITY,i.quantidade+1)}:i);
}
export function changeQuantity(items:CartEntry[],produtoId:string,quantidade:number):CartEntry[]{
  if(quantidade<=0)return items.filter(i=>i.produtoId!==produtoId);
  return items.map(i=>i.produtoId===produtoId?{...i,quantidade:Math.min(MAX_QUANTITY,Math.max(1,Math.trunc(quantidade)))}:i);
}
export function cartTotal(items:CartEntry[],products:Produto[]):number{
  return items.reduce((total,item)=>{
    const product=products.find(p=>p.id===item.produtoId&&p.ativo);
    return total+(product?Number(product.preco)*item.quantidade:0);
  },0);
}
