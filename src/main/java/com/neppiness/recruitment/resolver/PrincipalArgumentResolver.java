package com.neppiness.recruitment.resolver;

import com.neppiness.recruitment.dto.Principal;
import com.neppiness.recruitment.dto.PrincipalDto;
import com.neppiness.recruitment.exception.AuthenticationException;
import com.neppiness.recruitment.util.TokenDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class PrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenDecoder tokenDecoder;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Principal.class) &&
                parameter.getParameterType().equals(PrincipalDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader("Authorization");
        if (authorizationHeader == null) {
            throw new AuthenticationException(AuthenticationException.LOGIN_REQUIRED);
        }
        String token = authorizationHeader.split(" ")[1];
        return tokenDecoder.decodePrincipalDto(token);
    }

}
