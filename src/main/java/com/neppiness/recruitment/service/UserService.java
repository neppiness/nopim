package com.neppiness.recruitment.service;

import com.neppiness.recruitment.domain.Authority;
import com.neppiness.recruitment.domain.User;
import com.neppiness.recruitment.dto.UserRequest;
import com.neppiness.recruitment.exception.ResourceAlreadyExist;
import com.neppiness.recruitment.exception.ResourceNotFound;
import com.neppiness.recruitment.repository.UserRepository;
import com.neppiness.recruitment.util.PasswordValidator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signUp(UserRequest userRequest) {
        PasswordValidator.validatePassword(userRequest.getPassword());
        Optional<User> mayBeFoundUser = userRepository.findByName(userRequest.getName());
        if (mayBeFoundUser.isPresent()) {
            throw new ResourceAlreadyExist(ResourceAlreadyExist.USER_ALREADY_EXIST);
        }
        User user = User.builder()
                .name(userRequest.getName())
                .password(userRequest.getPassword())
                .authority(Authority.MEMBER)
                .build();
        return userRepository.save(user);
    }

    public User login(UserRequest userRequest) {
        // TODO: jwt를 반환하도록 수정
        String name = userRequest.getName();
        String password = userRequest.getPassword();
        Optional<User> mayBeFoundUser = userRepository.findByNameAndPassword(name, password);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        return mayBeFoundUser.get();
    }

    @Transactional
    public User promote(Long userId) {
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();
        User userToBeUpdated = User.builder()
                .id(userId)
                .name(foundUser.getName())
                .password(foundUser.getPassword())
                .authority(Authority.MANAGER)
                .build();
        return userRepository.save(userToBeUpdated);
    }

}
