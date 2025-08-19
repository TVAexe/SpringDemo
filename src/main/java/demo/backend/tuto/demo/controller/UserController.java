package demo.backend.tuto.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.service.UserSevice;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class UserController {

    private final UserSevice userService;

    public UserController(UserSevice userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User requestUser) {
        User newUser = this.userService.handleCreateUser(requestUser);
        return newUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "User deleted successfully";
    }

    @GetMapping("user/{id}")
    public User getAnUser(@PathVariable("id") long id) {
        return this.userService.handleGetUser(id);
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.handleGetAllUsers();
    }

    @PutMapping("user")
    public User updateUser(@RequestBody User user) {
        return this.userService.handleUpdateUser(user);
    }
    
    


}
