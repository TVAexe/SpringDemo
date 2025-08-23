package demo.backend.tuto.demo.domain.DTO;

import lombok.Getter;
import lombok.Setter;

public class RestLoginDTO {

    @Getter
    @Setter
    private String accessToken;

    public RestLoginDTO(String accessToken) {
        this.accessToken = accessToken;
    }

}
