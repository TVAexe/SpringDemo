package demo.backend.tuto.demo.domain.response.job;

import java.time.Instant;
import java.util.List;

import demo.backend.tuto.demo.utils.constant.LevelEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdatedJobDTO {
    private Long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant updatedAt;
    private String updatedBy;
    private List<String> skills;

    // Getters and Setters
}
