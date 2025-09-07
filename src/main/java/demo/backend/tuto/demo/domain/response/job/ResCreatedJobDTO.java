package demo.backend.tuto.demo.domain.response.job;

import java.time.Instant;
import java.util.List;

import demo.backend.tuto.demo.utils.constant.LevelEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreatedJobDTO {
    private Long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private boolean isActive;
    private List<String> skills;
    private Instant createdAt;
    private String createdBy;

}
