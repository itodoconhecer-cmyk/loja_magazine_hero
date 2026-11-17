# Publicacao gratuita do frontend na Vercel

Frontend React/Vite estatico no subdiretorio `frontend/` do mesmo repositorio.
- Projeto Vercel: `loja-magazine-hero-frontend`; `rootDirectory=frontend`; framework `vite`; plano Hobby gratuito, apenas uso didatico/pessoal.
- Git Integration: repo `itodoconhecer-cmyk/loja_magazine_hero`, branch `main`.
- `frontend/vercel.json` reescreve chamadas `/api/**` para o backend Render por HTTPS, evitando CORS em desenvolvimento de laboratorio.
- Rota de SPA faz fallback para `index.html`, sem interceptar `/api/**`.
- Nao publique credenciais ou JWT nas variaveis `VITE_*` (sao incorporadas no bundle).
- O frontend permite informar manualmente JWT em memoria. **Nao existe login OIDC real ainda**; funcionalidades protegidas exigem token valido com issuer/audience/permissoes adequadas.
- Backend Render Free pode entrar em suspensao, causando atraso no primeiro request.
- Testes: `cd frontend && npm install && npm run build && npm test`.
- Seguranca futura: implementar OIDC Authorization Code com PKCE, protecao contra XSS, revisao de CORS, rate limit e testes e2e.
- Este laboratorio nao deve ser usado para transacoes comerciais reais.
