# Catalogo multidepartamental, storage de imagens e SHA-256

## Decisao de arquitetura

Cada produto possui:
- `departamento`: classificacao de vitrine;
- `imagem_storage_path`: caminho controlado do recurso de imagem;
- `imagem_sha256`: hash SHA-256 (hexadecimal, 64 caracteres) dos **bytes originais**.

Os campos foram adicionados em `schema.sql` com `ALTER TABLE ... ADD COLUMN IF NOT EXISTS`. O `data.sql` insere 32 produtos com IDs estaveis, `ON CONFLICT DO NOTHING`, em oito departamentos. Os produtos existentes sao preservados. A carga automatica ocorre apenas quando `spring.sql.init.mode=always` (perfil default); o perfil `neon` possui `mode=never` e exige **migracao executada de forma controlada**.

Os arquivos usados nesta primeira versao sao **ilustracoes SVG originais**, versionadas e armazenadas como recursos de leitura em `src/main/resources/catalog-images/` dentro da imagem Docker. O caminho no banco e `classpath:catalog-images/<departamento>.svg`. Isto nao e um bucket externo; a evolucao para object storage imutavel (S3/R2/OCI/GCS) deve manter o mesmo protocolo de verificacao por bytes. Fotos reais licenciadas ainda nao foram carregadas.

## Fluxo de verificacao no backend

1. Vitrine consulta `GET /api/catalogo/produtos` sem autenticar.
2. O catalogo retorna URL interna de imagem `/api/catalogo/produtos/{id}/imagem`, nunca o caminho do storage nem o hash.
3. O backend consulta os metadados do produto ativo no PostgreSQL.
4. A chave de storage passa por lista de permitidos (prefixo `classpath:catalog-images/`, nome seguro, extensao SVG) e o arquivo e lido com limite de 2 MiB.
5. O servidor calcula `SHA-256(bytes)` e compara com o valor confiavel armazenado no banco em tempo constante.
6. Se igual, entrega **somente os bytes verificados** em `image/svg+xml`, com `Cache-Control: no-store` e `nosniff`.
7. Se diferente, ausente, sem hash, ou com caminho invalido: retorna `404`, nao entrega o arquivo e registra evento de nivel ERROR com `MEDIA_INTEGRITY_VIOLATION` e ID do produto.
8. O frontend apresenta a imagem indisponivel em vez de acessar o storage diretamente.

Todos os produtos do mesmo departamento podem apontar ao **mesmo arquivo e ao mesmo hash**, sem fazer copias. Conteudos diferentes exigem hashes diferentes; nao recalcular automaticamente o valor esperado na operacao de leitura.

## Observabilidade e alerta

O backend gera um **sinal estruturado de erro** nos logs:
`MEDIA_INTEGRITY_VIOLATION productId=<uuid> reason=sha256_mismatch_or_size`

Esse sinal pode ser coletado pelo New Relic quando o encaminhamento de logs estiver habilitado. Regra sugerida de alerta NRQL, a configurar na conta New Relic:

```sql
SELECT count(*) FROM Log
WHERE message LIKE '%MEDIA_INTEGRITY_VIOLATION%'
```

Definir condicao acima de zero na janela de cinco minutos e rotear a politica a um canal de notificacao da equipe. **A politica de alerta New Relic nao e criada por este codigo e deve ser validada no workspace.** O log de severidade ERROR foi implementado; a entrega de notificacoes externas depende da configuracao do provedor.

## Regras importantes de seguranca

- Hash SHA-256 **nao e senha, assinatura ou prova de autoria**: valida integridade em relacao ao digest confiavel do banco. Se um invasor alterar simultaneamente a imagem e o hash esperado, essa verificacao nao detecta o ataque.
- Para proteger contra adulteracao do banco e do objeto juntos, usar manifesto assinado (Ed25519, por exemplo) ou HMAC com chave guardada separadamente em vault; restringir gravacao e versao de objetos.
- Nao exibir imagem se falhar a verificacao. O hash nao deve ser atualizado automaticamente quando houver divergencia.
- Bucket externo futuro: private, IAM minimo, chaves versionadas/imutaveis; download somente pelo backend ou CDN com verificacao criptografica previa e estrategia de invalidacao.
- SVG e ilustrativo. Antes de usar SVG de terceiros, sanitizar e revisar scripts e referencias externas, ou converter para formatos raster seguros em etapa controlada.
- Operacoes administrativas de cadastro, precos e estoque continuam protegidas.

## Como validar

- Tests: `MediaSha256VerifierTest` comprova aceite de SHA correto e rejeicao de bytes alterados.
- Endpoint valido: `GET /api/catalogo/produtos/{uuid}/imagem` devolve a imagem com 200.
- Teste de adulteracao (em ambiente de testes, **nunca no banco de producao**): configurar temporariamente hash errado, requisitar imagem e verificar `404` + evento `MEDIA_INTEGRITY_VIOLATION`. Restaurar o valor sem regravar a imagem.
- Verificar indicador/alerta New Relic antes de considerar o requisito de notificacao concluido.
