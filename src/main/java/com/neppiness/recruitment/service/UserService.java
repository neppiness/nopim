package com.neppiness.recruitment.service;

import com.neppiness.recruitment.domain.Authority;
import com.neppiness.recruitment.domain.User;
import com.neppiness.recruitment.dto.PrincipalDto;
import com.neppiness.recruitment.dto.TokenResponse;
import com.neppiness.recruitment.dto.UserRequest;
import com.neppiness.recruitment.dto.UserResponse;
import com.neppiness.recruitment.exception.ResourceAlreadyExistException;
import com.neppiness.recruitment.exception.ResourceNotFoundException;
import com.neppiness.recruitment.repository.UserRepository;
import com.neppiness.recruitment.util.PasswordValidator;
import com.neppiness.recruitment.util.TokenEncoder;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final TokenEncoder tokenEncoder;

    @Transactional
    public UserResponse signUp(UserRequest userRequest) {
        PasswordValidator.validatePassword(userRequest.getPassword());
        Optional<User> mayBeFoundUser = userRepository.findByName(userRequest.getName());
        if (mayBeFoundUser.isPresent()) {
            throw new ResourceAlreadyExistException(ResourceAlreadyExistException.USER_ALREADY_EXIST);
        }
        User user = User.builder()
                .name(userRequest.getName())
                .password(userRequest.getPassword())
                .authority(Authority.MEMBER)
                .build();
        return userRepository
                .save(user)
                .toResponse();
    }

    public TokenResponse login(UserRequest userRequest) {
        String name = userRequest.getName();
        String password = userRequest.getPassword();
        Optional<User> mayBeFoundUser = userRepository.findByNameAndPassword(name, password);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();
        PrincipalDto principalDto = PrincipalDto.builder()
                .name(foundUser.getName())
                .authority(foundUser.getAuthority())
                .build();
        String token = tokenEncoder.encodePrincipalDto(principalDto);
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    @Transactional
    public UserResponse promote(Long userId) {
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFoundException(ResourceNotFoundException.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();
        User userToBeUpdated = User.builder()
                .id(userId)
                .name(foundUser.getName())
                .password(foundUser.getPassword())
                .authority(Authority.MANAGER)
                .build();
        return userRepository
                .save(userToBeUpdated)
                .toResponse();
    }

}
