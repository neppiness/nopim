package recruitment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recruitment.domain.User;
import recruitment.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody User addUser(
            @RequestParam String name
    ) {
        User user = new User();
        user.setName(name);
        userRepository.save(user);
        return user;
    }

    @GetMapping(path="/{userId}")
    public @ResponseBody User findUserById(
        @PathVariable long userId
    ) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) throw new NoSuchElementException();
        return foundUser.get();
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping(path="/{userId}")
    public @ResponseBody String deleteUserById(
            @PathVariable long userId
    ) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) throw new NoSuchElementException();
        userRepository.deleteById(userId);
        return "The user is deleted (userId: " + userId + ")";
    }

    @DeleteMapping(path="/all")
    public @ResponseBody String deleteAllUsers() {
        userRepository.deleteAll();
        return "All user data deleted";
    }
}
