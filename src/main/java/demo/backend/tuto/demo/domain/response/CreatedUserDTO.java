package demo.backend.tuto.demo.domain.response;

import java.time.Instant;

import demo.backend.tuto.demo.utils.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CreatedUserDTO {
    private long id;
    private String name;
    private String email;
    private String address;
    private int age;
    private GenderEnum gender;
    private Instant createdAt;
    private CompanyUser company;
    private RoleUser role;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }
}
