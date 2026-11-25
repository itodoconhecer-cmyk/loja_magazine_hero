package aulas.umc.oo.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Integrity check of the original file bytes against the trusted hash stored in PostgreSQL.
 * The expected hash is never rewritten when validation fails.
 */
public final class MediaSha256Verifier {
    private MediaSha256Verifier() {}

    public static boolean matches(byte[] bytes, String expectedHex) {
        if (bytes == null || expectedHex == null || !expectedHex.matches("[0-9a-fA-F]{64}")) {
            return false;
        }
        try {
            byte[] actual = MessageDigest.getInstance("SHA-256").digest(bytes);
            byte[] expected = HexFormat.of().parseHex(expectedHex);
            return MessageDigest.isEqual(actual, expected);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 unavailable", ex);
        }
    }

    public static String hash(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 unavailable", ex);
        }
    }
}
