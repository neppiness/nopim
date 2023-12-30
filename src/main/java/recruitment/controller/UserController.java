package recruitment.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.Authority;
import recruitment.domain.User;
import recruitment.exception.ResourceNotFound;
import recruitment.repository.UserRepository;

@RequestMapping(path = "/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Transactional
    @PostMapping(path = "")
    public ResponseEntity<User> signUp(@RequestParam String name, @RequestParam String password) {
        // TODO: 중복되는 이름을 갖는 경우를 배제
        User user = User.builder()
                .name(name)
                .password(password)
                .authority(Authority.MEMBER)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRepository.save(user));
    }

    @GetMapping(path = "")
    public ResponseEntity<User> login(@RequestParam String name, @RequestParam String password) {
        Optional<User> mayBeFoundUser = userRepository.findByNameAndPassword(name, password);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mayBeFoundUser.get());
    }

    @Transactional
    @PostMapping("/promote")
    public ResponseEntity<User> promote(@RequestParam Long id) {
        // TODO: API 접근 권한을 관리자(ADMIN)로 설정
        Optional<User> mayBeFoundUser = userRepository.findById(id);
        if (mayBeFoundUser.isEmpty()) {
            throw new ResourceNotFound(ResourceNotFound.USER_NOT_FOUND);
        }
        User foundUser = mayBeFoundUser.get();
        User userToBeUpdated = User.builder()
                .id(id)
                .name(foundUser.getName())
                .password(foundUser.getPassword())
                .authority(Authority.MANAGER)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userRepository.save(userToBeUpdated));
    }

}
