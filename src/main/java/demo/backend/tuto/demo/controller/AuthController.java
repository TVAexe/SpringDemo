package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.domain.DTO.LoginDTO;
import demo.backend.tuto.demo.domain.DTO.RestLoginDTO;
import demo.backend.tuto.demo.domain.DTO.RestLoginDTO.UserLogin;
import demo.backend.tuto.demo.service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.backend.tuto.demo.utils.SecurityUtils;
import demo.backend.tuto.demo.utils.annotation.ApiMessage;
import demo.backend.tuto.demo.utils.exception.IdInvalidException;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;
    @Value("${demo.jwt.refresh-token-validity-in-seconds}")
    private Long jwtRefreshTokenValidity;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtils securityUtils,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        RestLoginDTO restLoginDTO = new RestLoginDTO();
        User userDB = userService.findUserByUsername(loginDTO.getUsername());
        RestLoginDTO.UserLogin userLogin = new UserLogin(userDB.getId(), userDB.getEmail(), userDB.getUsername());
        restLoginDTO.setUserLogin(userLogin);
        String accessToken = this.securityUtils.createAccessToken(authentication.getName(), restLoginDTO.getUserLogin());
        restLoginDTO.setAccessToken(accessToken);
        String refreshToken = this.securityUtils.createRefreshToken(userDB.getEmail(), restLoginDTO);
        this.userService.updateUserToken(refreshToken, userDB.getEmail());
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true).secure(true)
                .path("/").maxAge(jwtRefreshTokenValidity).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(restLoginDTO);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch account information")
    public ResponseEntity<RestLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User userDB = userService.findUserByUsername(email);
        RestLoginDTO.UserLogin userLogin = new UserLogin(userDB.getId(), userDB.getEmail(), userDB.getUsername());
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<RestLoginDTO> getRefreshToken(@CookieValue(name = "refreshToken", defaultValue = "abc") String refreshToken) throws IdInvalidException {
        if (refreshToken.equals("abc")) {
            throw new IdInvalidException("Ban da khong co refresh token o cookies");
        }
        Jwt decodedToken = this.securityUtils.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh Token khong hop le");
        }
        RestLoginDTO restLoginDTO = new RestLoginDTO();
        User userDB = userService.findUserByUsername(email);
        RestLoginDTO.UserLogin userLogin = new UserLogin(userDB.getId(), userDB.getEmail(), userDB.getUsername());
        restLoginDTO.setUserLogin(userLogin);
        String accessToken = this.securityUtils.createAccessToken(email, restLoginDTO.getUserLogin());
        restLoginDTO.setAccessToken(accessToken);
        String newRefreshToken = this.securityUtils.createRefreshToken(email, restLoginDTO);
        this.userService.updateUserToken(newRefreshToken, email);
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken).httpOnly(true).secure(true)
                .path("/").maxAge(jwtRefreshTokenValidity).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(restLoginDTO);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout user")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access Token khong hop le");
        }
        this.userService.updateUserToken("", email);
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", null).httpOnly(true).secure(true)
                .path("/").maxAge(0).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).build();
    }
    


}
