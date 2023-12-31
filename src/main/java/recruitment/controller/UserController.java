package recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.User;
import recruitment.dto.UserRequest;
import recruitment.service.UserService;

@RequestMapping(path = "/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "")
    public ResponseEntity<User> signUp(@ModelAttribute UserRequest userRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signUp(userRequest));
    }

    @GetMapping(path = "")
    public ResponseEntity<User> login(@ModelAttribute UserRequest userRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.login(userRequest));
    }

    @PostMapping("/promote")
    public ResponseEntity<User> promote(@RequestParam Long id) {
        // TODO: API 접근 권한을 관리자(ADMIN)로 설정
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.promote(id));
    }

}
