package com.neppiness.nopim.util;

import com.neppiness.nopim.exception.InvalidArgumentException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    @DisplayName("유효한 비밀번호 테스트")
    @Test
    void validPasswordTest() {
        String password = "Neppiness12!";
        PasswordValidator.validatePassword(password);
    }

    @DisplayName("길이가 만족되지 않은 비밀번호에 대한 예외 테스트")
    @Test
    void invalidPasswordTest1() {
        String password = "np";
        Assertions
                .assertThatThrownBy(() -> PasswordValidator.validatePassword(password))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage(InvalidArgumentException.INVALID_PASSWORD_LENGTH);
    }

    @DisplayName("숫자 조건이 만족되지 않은 비밀번호에 대한 예외 테스트")
    @Test
    void invalidPasswordTest2() {
        String password = "neppiness";
        Assertions
                .assertThatThrownBy(() -> PasswordValidator.validatePassword(password))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage(InvalidArgumentException.PASSWORD_REQUIRES_DIGIT);
    }

    @DisplayName("영문 대문자 조건이 만족되지 않은 비밀번호에 대한 예외 테스트")
    @Test
    void invalidPasswordTest3() {
        String password = "neppiness12";
        Assertions
                .assertThatThrownBy(() -> PasswordValidator.validatePassword(password))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage(InvalidArgumentException.PASSWORD_REQUIRES_CAPITAL_LETTER);
    }

    @DisplayName("영문 소문자 조건이 만족되지 않은 비밀번호에 대한 예외 테스트")
    @Test
    void invalidPasswordTest4() {
        String password = "NEPPINESS12";
        Assertions
                .assertThatThrownBy(() -> PasswordValidator.validatePassword(password))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage(InvalidArgumentException.PASSWORD_REQUIRES_SMALL_LETTER);
    }

    @DisplayName("특수문자 조건이 만족되지 않은 비밀번호에 대한 예외 테스트")
    @Test
    void invalidPasswordTest5() {
        String password = "Neppiness12";
        Assertions
                .assertThatThrownBy(() -> PasswordValidator.validatePassword(password))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage(InvalidArgumentException.PASSWORD_REQUIRES_SPECIAL_CHARACTER);
    }

}