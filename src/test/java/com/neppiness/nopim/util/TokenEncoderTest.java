package com.neppiness.nopim.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.neppiness.nopim.domain.Authority;
import com.neppiness.nopim.dto.PrincipalDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenEncoderTest {

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private TokenEncoder tokenEncoder;

    @Autowired
    private TokenDecoder tokenDecoder;

    @DisplayName(value = "principalDto 인코드 테스트")
    @Test
    void encodePrincipalDtoTest() throws JsonProcessingException {
        String name = "scsc3204";
        Authority authority = Authority.ADMIN;
        PrincipalDto principalDto = PrincipalDto.builder()
                .name(name)
                .authority(authority)
                .build();

        String token = tokenEncoder.encodePrincipalDto(principalDto);
        System.out.println(token);

        PrincipalDto decodedToken = tokenDecoder.decodePrincipalDto(token);
        String decodedTokenAsString = objectWriter.writeValueAsString(decodedToken);
        System.out.println(decodedTokenAsString);

        Assertions
                .assertThat(decodedToken.getName())
                .isEqualTo(name);
        Assertions
                .assertThat(decodedToken.getAuthority())
                .isEqualTo(authority);
    }

}