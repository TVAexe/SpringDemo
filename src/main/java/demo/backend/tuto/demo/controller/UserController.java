package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.service.UserSevice;

@RestController
public class UserController {

    private final UserSevice userService;

    public UserController(UserSevice userService) {
        this.userService = userService;
    }

    @GetMapping("/users/create")
    public String createNewUser() {

        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        this.userService.handleCreateUser(user);
        return "User created successfully";
    }
}
