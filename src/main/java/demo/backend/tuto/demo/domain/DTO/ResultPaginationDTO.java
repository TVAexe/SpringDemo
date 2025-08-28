package demo.backend.tuto.demo.domain.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;
}
