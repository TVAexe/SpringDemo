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
import demo.backend.tuto.demo.domain.DTO.Users.CreatedUserDTO;
import demo.backend.tuto.demo.domain.DTO.Users.FetchUserDTO;
import demo.backend.tuto.demo.domain.DTO.Users.UpdateUserDTO;
import demo.backend.tuto.demo.service.UserService;
import demo.backend.tuto.demo.utils.annotation.ApiMessage;
import demo.backend.tuto.demo.utils.exception.IdInvalidException;
import jakarta.validation.Valid;

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
    @ApiMessage("Created new User")
    public ResponseEntity<CreatedUserDTO> createNewUser(@Valid @RequestBody User requestUser)
            throws IdInvalidException {
        boolean isEmailExisted = this.userService.checkEmailExist(requestUser.getEmail());
        if (isEmailExisted) {
            throw new IdInvalidException("Email " + requestUser.getEmail() + " already exists");
        }

        String hashedPassword = this.passwordEncoder.encode(requestUser.getPassword());
        requestUser.setPassword(hashedPassword);
        User newUser = this.userService.handleCreateUser(requestUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Deleted User")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.handleGetUser(id);
        if (currentUser == null) {
            throw new IdInvalidException("User voi id: " + id + " khong ton tai");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<FetchUserDTO> getAnUser(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.handleGetUser(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User voi id: " + id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertFetchUserDTO(fetchUser));
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> filter, Pageable pageable) {
        return ResponseEntity.ok(this.userService.handleGetAllUsers(filter, pageable));
    }

    @PutMapping("users")
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User updatedUser = this.userService.handleUpdateUser(user);
        if (updatedUser == null) {
            throw new IdInvalidException("User voi id: " + user.getId() + " khong ton tai");
        }
        return ResponseEntity.ok(this.userService.convertUpdateUserDTO(updatedUser));
    }

}
