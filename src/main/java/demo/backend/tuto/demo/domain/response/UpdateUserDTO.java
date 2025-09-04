package demo.backend.tuto.demo.domain.response;

import java.time.Instant;

import demo.backend.tuto.demo.utils.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private long id;
    private String name;
    private String email;
    private String address;
    private int age;
    private GenderEnum gender;
    private Instant updatedAt;
    private CompanyUser company;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
