package aulas.umc.oo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aulas.umc.oo.security.MediaSha256Verifier;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class MediaSha256VerifierTest {

    @Test
    void correctDigestIsAccepted() {
        byte[] data = "abc".getBytes(StandardCharsets.UTF_8);
        assertTrue(
                MediaSha256Verifier.matches(data, "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"));
    }

    @Test
    void changedBytesAreRejected() {
        byte[] data = "abcd".getBytes(StandardCharsets.UTF_8);
        assertFalse(
                MediaSha256Verifier.matches(data, "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"));
        assertFalse(MediaSha256Verifier.matches(data, "not-a-valid-digest"));
    }

    @Test
    void bundledArtworkMatchesDatabaseSeed() throws IOException {
        byte[] data = new ClassPathResource("catalog-images/tecnologia.svg")
                .getInputStream()
                .readAllBytes();
        assertTrue(
                MediaSha256Verifier.matches(data, "9fa37ba3ef1d355969983cdbb5cb8a582b4c44002483b18d2351d49f1da265e6"));
    }
}
