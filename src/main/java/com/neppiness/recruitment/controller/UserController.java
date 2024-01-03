package com.neppiness.recruitment.controller;

import com.neppiness.recruitment.domain.User;
import com.neppiness.recruitment.dto.Principal;
import com.neppiness.recruitment.dto.PrincipalDto;
import com.neppiness.recruitment.dto.UserRequest;
import com.neppiness.recruitment.dto.UserResponse;
import com.neppiness.recruitment.service.AuthorizationService;
import com.neppiness.recruitment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthorizationService authorizationService;

    @PostMapping(path = "")
    public ResponseEntity<UserResponse> signUp(@ModelAttribute UserRequest userRequest) {
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
    public ResponseEntity<UserResponse> promote(@Principal PrincipalDto principal, @RequestParam Long id) {
        authorizationService.checkIfAdmin(principal.getAuthority());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.promote(id));
    }

}
