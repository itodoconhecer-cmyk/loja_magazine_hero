from pathlib import Path
import re, collections
root=Path('.')
proc_text=(root/'src/main/java/aulas/umc/oo/pipeline/Processo.java').read_text()
body=re.search(r'enum\s+Processo\s*\{([^}]*)\}',proc_text,re.S).group(1)
processes=[x.strip() for x in body.split(',') if x.strip()]
rows=[]
for f in (root/'src/main/java/aulas/umc/oo/strategy/processo').glob('*.java'):
    t=f.read_text()
    p=re.search(r'return\s+Processo\.([A-Z_]+)',t)
    o=re.search(r'int\s+ordem\s*\(\)\s*\{\s*return\s+(\d+)',t)
    n=re.search(r'String\s+nome\s*\(\)\s*\{\s*return\s+"([^"]+)"',t)
    if p and o: rows.append((p.group(1),int(o.group(1)),n.group(1) if n else f.stem,f.name))
by=collections.defaultdict(list)
for r in rows: by[r[0]].append(r)
print('PROCESSOS DECLARADOS:',len(processes))
print('STRATEGIES DE PROCESSO:',len(rows))
missing=[p for p in processes if not by[p]]
print('PROCESSOS SEM STRATEGY:',missing or 'nenhum')
for p in processes:
    arr=sorted(by[p],key=lambda x:x[1])
    orders=[x[1] for x in arr]
    dup=[x for x,c in collections.Counter(orders).items() if c>1]
    print(f'{p}: {[(x[1],x[2]) for x in arr]} duplicadas={dup or "nenhuma"}')
# freight coverage
frete=(root/'src/main/java/aulas/umc/oo/model/TipoFrete.java').read_text()
fbody=re.search(r'enum\s+TipoFrete\s*\{([^}]*)\}',frete,re.S).group(1)
tipos=[x.strip() for x in fbody.split(',') if x.strip()]
impl=[]
for f in (root/'src/main/java/aulas/umc/oo/strategy/frete').glob('*Strategy.java'):
    if f.name=='FreteStrategy.java': continue
    t=f.read_text(); m=re.search(r'return\s+TipoFrete\.([A-Z_]+)',t)
    if m: impl.append((m.group(1),f.name))
print('TIPOS FRETE:',tipos)
print('IMPLEMENTACOES FRETE:',impl)
missing_f=[t for t in tipos if t not in [x[0] for x in impl]]
dup_f=[t for t,c in collections.Counter(x[0] for x in impl).items() if c>1]
print('FRETES SEM STRATEGY:',missing_f or 'nenhum')
print('FRETES DUPLICADOS:',dup_f or 'nenhum')
