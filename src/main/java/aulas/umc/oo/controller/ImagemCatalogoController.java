package aulas.umc.oo.controller;

import aulas.umc.oo.security.MediaSha256Verifier;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves only verified bytes. Storage path is an allowlisted classpath resource; untrusted or modified bytes are never
 * returned to the browser.
 */
@RestController
@RequestMapping("/api/catalogo")
public class ImagemCatalogoController {
    private static final Logger log = LoggerFactory.getLogger(ImagemCatalogoController.class);
    private static final int MAX_FILE_SIZE = 2 * 1024 * 1024;
    private final JdbcTemplate jdbc;

    public ImagemCatalogoController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private record MediaRecord(String path, String expectedSha256) {}

    @GetMapping("/produtos/{id}/imagem")
    public ResponseEntity<byte[]> image(@PathVariable UUID id) {
        List<MediaRecord> rows = jdbc.query(
                "select imagem_storage_path, imagem_sha256 from produto where id = ? and ativo = true",
                (rs, rowNum) -> new MediaRecord(rs.getString("imagem_storage_path"), rs.getString("imagem_sha256")),
                id);
        if (rows.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MediaRecord record = rows.getFirst();
        if (record.path() == null || record.expectedSha256() == null) {
            return ResponseEntity.notFound().build();
        }
        if (!record.path().matches("classpath:catalog-images/[a-z0-9-]+\\.svg")) {
            log.error("MEDIA_INTEGRITY_VIOLATION productId={} reason=untrusted_storage_path", id);
            return ResponseEntity.notFound().build();
        }

        String relative = record.path().substring("classpath:".length());
        try (InputStream stream = new ClassPathResource(relative).getInputStream()) {
            byte[] file = stream.readNBytes(MAX_FILE_SIZE + 1);
            if (file.length > MAX_FILE_SIZE || !MediaSha256Verifier.matches(file, record.expectedSha256())) {
                log.error("MEDIA_INTEGRITY_VIOLATION productId={} reason=sha256_mismatch_or_size", id);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/svg+xml"))
                    .cacheControl(CacheControl.noStore())
                    .header("X-Content-Type-Options", "nosniff")
                    .header("Content-Security-Policy", "sandbox; default-src 'none'")
                    .body(file);
        } catch (IOException ex) {
            log.error("MEDIA_INTEGRITY_VIOLATION productId={} reason=storage_unavailable", id, ex);
            return ResponseEntity.notFound().build();
        }
    }
}
