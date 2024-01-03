package com.neppiness.recruitment.util;

import com.neppiness.recruitment.domain.Authority;
import com.neppiness.recruitment.dto.PrincipalDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenDecoder {

    @Value("${spring.config.jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    public PrincipalDto decodePrincipalDto(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getSecretKey())
                .build();
        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        String name = claimsJws.getPayload().get("sub").toString();
        String authorityAsString = claimsJws.getPayload().get("authority").toString();
        Authority authority = Authority.parseAuthority(authorityAsString);

        return PrincipalDto.builder()
                .name(name)
                .authority(authority)
                .build();
    }

    private SecretKey getSecretKey() {
        if (secretKey != null) {
            return this.secretKey;
        }
        byte[] secretKeyAsBytes = secret.getBytes(StandardCharsets.UTF_8);
        String secretAlgorithm = "HmacSHA256";
        return this.secretKey = new SecretKeySpec(secretKeyAsBytes, secretAlgorithm);
    }

}
