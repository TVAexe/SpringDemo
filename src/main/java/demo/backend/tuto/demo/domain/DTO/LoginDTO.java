package demo.backend.tuto.demo.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class LoginDTO {

    @Getter
    @Setter
    @NotBlank(message = "Username không được để trống")
    private String username;

    @Getter
    @Setter
    @NotBlank(message = "Password không được để trống")
    private String password;

}
