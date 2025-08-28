package demo.backend.tuto.demo.controller;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.domain.DTO.ResultPaginationDTO;
import demo.backend.tuto.demo.service.UserService;
import demo.backend.tuto.demo.utils.exception.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User requestUser) {
        String hashedPassword = this.passwordEncoder.encode(requestUser.getPassword());
        requestUser.setPassword(hashedPassword);
        User newUser = this.userService.handleCreateUser(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id>=1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getAnUser(@PathVariable("id") long id) {
        User fetchUser = this.userService.handleGetUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> filter, Pageable pageable ) {
        return ResponseEntity.ok(this.userService.handleGetAllUsers(filter, pageable));
    }

    @PutMapping("users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser =  this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    


}
