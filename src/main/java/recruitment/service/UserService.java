package recruitment.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recruitment.domain.Authority;
import recruitment.domain.User;
import recruitment.dto.UserRequest;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signUp(UserRequest userRequest) {
        // TODO: 중복되는 이름을 갖는 경우를 배제
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
