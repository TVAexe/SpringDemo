package demo.backend.tuto.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.service.UserSevice;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class UserController {

    private final UserSevice userService;

    public UserController(UserSevice userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User requestUser) {
        User newUser = this.userService.handleCreateUser(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getAnUser(@PathVariable("id") long id) {
        User fetchUser = this.userService.handleGetUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUsers());
    }

    @PutMapping("users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser =  this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    


}
