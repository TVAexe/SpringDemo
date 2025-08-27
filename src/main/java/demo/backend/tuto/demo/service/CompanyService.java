package demo.backend.tuto.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.Company;
import demo.backend.tuto.demo.repository.CompanyRepository;
import jakarta.validation.Valid;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> handleGetAllCompanies() {
        return this.companyRepository.findAll();
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> existingCompany = this.companyRepository.findById(company.getId());
        if (existingCompany.isPresent()) {
            Company updatedCompany = existingCompany.get();
            updatedCompany.setName(company.getName());
            updatedCompany.setAddress(company.getAddress());
            updatedCompany.setLogo(company.getLogo());
            updatedCompany.setDescription(company.getDescription());
            return this.companyRepository.save(updatedCompany);
        } else {
            return null;
        }
    }

    public void handleDeleteCompany(Long id) {
        this.companyRepository.deleteById(id);
    }
}
