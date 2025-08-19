package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.service.UserSevice;

@RestController
public class UserController {

    private final UserSevice userService;

    public UserController(UserSevice userService) {
        this.userService = userService;
    }

    @PostMapping("/users/create")
    public User createNewUser(@RequestBody User requestUser) {
        User newUser = this.userService.handleCreateUser(requestUser);
        return newUser;
    }
}
