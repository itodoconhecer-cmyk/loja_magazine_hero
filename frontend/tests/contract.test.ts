import {describe,expect,it} from 'vitest';
import {moeda} from '../src/services/api';
describe('Moeda BRL',()=>{it('formata valor decimal',()=>{expect(moeda(12.5)).toContain('12,50');});});
