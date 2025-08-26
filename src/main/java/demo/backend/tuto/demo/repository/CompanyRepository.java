package demo.backend.tuto.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import demo.backend.tuto.demo.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
