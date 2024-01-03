package com.neppiness.recruitment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.neppiness.recruitment.domain.Authority;
import com.neppiness.recruitment.dto.PrincipalDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenDecoderTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private TokenDecoder tokenDecoder;

    @DisplayName(value = "토큰 디코드 테스트")
    @Test
    void decodePrincipalDtoTest() throws JsonProcessingException {
        /*
        HEADER = { "typ": "jwt", "alg": "HS256" }
        PAYLOAD = { "iat": 1704266460, "exp": 2004268260, "sub": "scsc3204", "authority": "ADMIN" }
        */
        String token = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9"
                + ".eyJpYXQiOjE3MDQyNjY0NjAsImV4cCI6MjAwNDI2ODI2MCwic3ViIjoic2NzYzMyMDQiLCJhdXRob3JpdHkiOiJBRE1JTiJ9"
                + ".KbkWvmA_eQkGGIA_ArQs1ilWTHXhoUidg62FGghouko";
        PrincipalDto decodedToken = tokenDecoder.decodePrincipalDto(token);
        String decodedTokenAsString = objectWriter.writeValueAsString(decodedToken);
        System.out.println(decodedTokenAsString);

        String name = "scsc3204";
        Assertions
                .assertThat(decodedToken.getName())
                .isEqualTo(name);
        Assertions
                .assertThat(decodedToken.getAuthority())
                .isEqualTo(Authority.ADMIN);
    }

    @DisplayName(value = "서명이 깨진 토큰에 대한 디코드 예외 테스트")
    @Test
    void tokenWithInvalidSignatureDecodeExceptionTest() {
        String invalidToken = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9"
                + ".eyJpYXQiOjE3MDQyNjY0NjAsImV4cCI6MjAwNDI2ODI2MCwic3ViIjoic2NzYzMyMDQiLCJhdXRob3JpdHkiOiJBRE1JTiJ9"
                + ".KbkWvmA_eQkGGIA_ArQs1ilWTHXhoUidg62FGghouk0";
        Assertions
                .assertThatThrownBy(() -> tokenDecoder.decodePrincipalDto(invalidToken))
                .isInstanceOf(SignatureException.class);
    }

    @DisplayName(value = "만료된 토큰에 대한 디코드 예외 테스트")
    @Test
    void expiredTokenDecodeExceptionTest() {
        /*
        HEADER = { "typ": "jwt", "alg": "HS256" }
        PAYLOAD = { "iat": 1704266460, "exp": 1704266460, "sub": "scsc3204", "authority": "ADMIN" }
        */
        String expiredToken = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9"
                + ".eyJpYXQiOjE3MDQyNjY0NjAsImV4cCI6MTcwNDI2NjQ2MCwic3ViIjoic2NzYzMyMDQiLCJhdXRob3JpdHkiOiJBRE1JTiJ9"
                + ".Nzd_lkJekq68TwxAaWxcQUD0EHQwX96Cc2Ta_TGn1II";
        Assertions
                .assertThatThrownBy(() -> tokenDecoder.decodePrincipalDto(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

}