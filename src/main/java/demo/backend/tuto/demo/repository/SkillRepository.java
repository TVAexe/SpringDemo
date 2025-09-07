package demo.backend.tuto.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import demo.backend.tuto.demo.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    boolean existsByName(String name);

    List<Skill> findByIdIn(List<Long> reqSkills);

}
