package com.JIMS.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // Access token — 15 minutes
    private static final long ACCESS_EXPIRY  = 1 * 60 * 1000L;

    // Refresh token — 8 hours
    private static final long REFRESH_EXPIRY = 8 * 60 * 60 * 1000L;

    // ── Generate Access Token ────────────────────────
    public String generateAccessToken(String userId) {
        return generateToken(userId, "access", ACCESS_EXPIRY);
    }

    // ── Generate Refresh Token ───────────────────────
    public String generateRefreshToken(String userId) {
        return generateToken(userId, "refresh", REFRESH_EXPIRY);
    }

    // ── Validate Token ───────────────────────────────
    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            // Verify signature
            String expectedSig = sign(parts[0] + "." + parts[1]);
            if (!expectedSig.equals(parts[2])) return false;

            // Check expiry
            String payload = decode(parts[1]);
            long exp = extractExp(payload);
            return System.currentTimeMillis() < exp;

        } catch (Exception e) {
            return false;
        }
    }

    // ── Get UserId from Token ────────────────────────
    public String getUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String payload = decode(parts[1]);
            return extractField(payload, "sub");
        } catch (Exception e) {
            return null;
        }
    }

    // ── Access token expiry time for frontend ────────
    public long getAccessTokenExpiryTime() {
        return System.currentTimeMillis() + ACCESS_EXPIRY;
    }

    // ════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ════════════════════════════════════════════════

    private String generateToken(String userId,
                                  String type,
                                  long expiry) {
        long now = System.currentTimeMillis();
        long exp = now + expiry;

        // Header
        String header = encode(
            "{\"alg\":\"HS256\",\"typ\":\"JWT\"}"
        );

        // Payload
        String payloadJson = "{"
            + "\"sub\":\"" + userId + "\","
            + "\"type\":\"" + type + "\","
            + "\"iat\":" + now + ","
            + "\"exp\":" + exp
            + "}";
        String payload = encode(payloadJson);

        // Signature
        String sig = sign(header + "." + payload);

        return header + "." + payload + "." + sig;
    }

    private String encode(String data) {
        return Base64.getUrlEncoder()
                     .withoutPadding()
                     .encodeToString(
                         data.getBytes(StandardCharsets.UTF_8)
                     );
    }

    private String decode(String data) {
        byte[] bytes = Base64.getUrlDecoder().decode(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
            );
            mac.init(keySpec);
            byte[] hash = mac.doFinal(
                data.getBytes(StandardCharsets.UTF_8)
            );
            return Base64.getUrlEncoder()
                         .withoutPadding()
                         .encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Signing failed", e);
        }
    }

    private long extractExp(String payload) {
        String val = extractField(payload, "exp");
        return val != null ? Long.parseLong(val) : 0L;
    }

    private String extractField(String payload, String field) {
        try {
            String search = "\"" + field + "\":";
            int idx = payload.indexOf(search);
            if (idx == -1) return null;
            idx += search.length();
            // Check if value is string (starts with ")
            if (payload.charAt(idx) == '"') {
                int end = payload.indexOf("\"", idx + 1);
                return payload.substring(idx + 1, end);
            } else {
                // Numeric value
                int end = idx;
                while (end < payload.length() &&
                       (Character.isDigit(payload.charAt(end))
                        || payload.charAt(end) == '-')) {
                    end++;
                }
                return payload.substring(idx, end);
            }
        } catch (Exception e) {
            return null;
        }
    }
}