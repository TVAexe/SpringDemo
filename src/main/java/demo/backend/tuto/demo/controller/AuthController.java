package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.domain.DTO.LoginDTO;
import demo.backend.tuto.demo.domain.DTO.RestLoginDTO;
import demo.backend.tuto.demo.domain.DTO.RestLoginDTO.UserLogin;
import demo.backend.tuto.demo.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.backend.tuto.demo.utils.SecurityUtils;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtils securityUtils, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }


    
    @PostMapping("/login")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = this.securityUtils.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RestLoginDTO restLoginDTO = new RestLoginDTO();
        User userDB = userService.findUserByUsername(loginDTO.getUsername());
        restLoginDTO.setAccessToken(accessToken);
        RestLoginDTO.UserLogin userLogin = new UserLogin(userDB.getId(), userDB.getUsername(), userDB.getEmail());
        restLoginDTO.setUserLogin(userLogin);
        return ResponseEntity.ok().body(restLoginDTO);
    }
    

}
