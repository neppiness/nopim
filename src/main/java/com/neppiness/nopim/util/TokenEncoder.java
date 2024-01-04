package com.neppiness.nopim.util;

import com.neppiness.nopim.dto.PrincipalDto;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenEncoder {

    @Value("${spring.config.jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    public String encodePrincipalDto(PrincipalDto principalDto) {
        long tokenDurationInMinute = 30L;
        long tokenDurationInMillisecond = tokenDurationInMinute * 60 * 1000;

        Date now = new Date();
        long expirationAsMillisecond = now.getTime() + tokenDurationInMillisecond;
        Date expiration = new Date(expirationAsMillisecond);

        String name = principalDto.getName();
        String authority = principalDto.getAuthority().toString();
        return Jwts.builder()
                .header()
                    .type("jwt")
                    .and()
                .issuedAt(now)
                .expiration(expiration)
                .subject(name)
                .claim("authority", authority)
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        if (this.secretKey != null) {
            return this.secretKey;
        }
        byte[] secretAsBytes = secret.getBytes(StandardCharsets.UTF_8);
        final String secretAlgorithm = "HmacSHA256";
        return this.secretKey = new SecretKeySpec(secretAsBytes, secretAlgorithm);
    }

}
