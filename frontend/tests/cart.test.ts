import {describe,expect,it} from 'vitest';
import {addToCart,cartTotal,changeQuantity,sanitizeCart} from '../src/services/guestCart';
import type {Produto} from '../src/services/api';

const first='11111111-1111-4111-8111-111111111111';
const second='22222222-2222-4222-8222-222222222222';
const products:Produto[]=[
 {id:first,nome:'Produto de teste',preco:59.9,ativo:true},
 {id:second,nome:'Produto inativo',preco:100,ativo:false}
];

describe('Carrinho anônimo',()=>{
 it('permite adicionar e alterar quantidades sem login',()=>{
  const a=addToCart([],first);
  const b=addToCart(a,first);
  expect(b).toEqual([{produtoId:first,quantidade:2}]);
  expect(changeQuantity(b,first,1)[0].quantidade).toBe(1);
  expect(changeQuantity(b,first,0)).toEqual([]);
 });
 it('nao aceita itens arbitrarios vindos do localStorage',()=>{
  expect(sanitizeCart([{produtoId:'foo',quantidade:1},{produtoId:first,quantidade:3.5},null])).toEqual([]);
 });
 it('limita as quantidades e remove duplicados',()=>{
  const values=sanitizeCart([{produtoId:first,quantidade:19},{produtoId:first,quantidade:10}]);
  expect(values).toEqual([{produtoId:first,quantidade:20}]);
 });
 it('calcula apenas produtos ativos e existentes',()=>{
  expect(cartTotal([{produtoId:first,quantidade:2},{produtoId:second,quantidade:1}],products)).toBeCloseTo(119.8);
 });
});
