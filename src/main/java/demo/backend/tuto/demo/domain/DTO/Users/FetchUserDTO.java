package demo.backend.tuto.demo.domain.DTO.Users;

import java.time.Instant;

import demo.backend.tuto.demo.utils.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FetchUserDTO {
    private long id;
    private String username;
    private String email;
    private String address;
    private int age;
    private GenderEnum gender;
    private Instant updatedAt;
    private Instant createdAt;
}
