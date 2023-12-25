package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.User;
import recruitment.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping(path = "/add")
    public ResponseEntity<User> addUser(@RequestParam String name) {
        User user = new User();
        user.setName(name);
        userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable long userId) {
        Optional<User> mayBeFoundUser = userRepository.findById(userId);
        if (mayBeFoundUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mayBeFoundUser.get());
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userRepository.findAll());
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        userRepository.deleteById(userId);
        String message = "The user is deleted (userId: " + userId + ")";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

    @DeleteMapping(path = "/all")
    public ResponseEntity<String> deleteAllUsers() {
        userRepository.deleteAll();
        String message = "All user data deleted";
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

}
