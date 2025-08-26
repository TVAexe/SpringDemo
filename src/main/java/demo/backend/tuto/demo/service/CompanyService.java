package demo.backend.tuto.demo.service;

import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.Company;
import demo.backend.tuto.demo.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }
}
