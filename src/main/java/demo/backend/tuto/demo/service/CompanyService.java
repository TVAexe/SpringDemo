package demo.backend.tuto.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.Company;
import demo.backend.tuto.demo.domain.DTO.Meta;
import demo.backend.tuto.demo.domain.DTO.ResultPaginationDTO;
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

    public ResultPaginationDTO handleGetAllCompanies(Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageCompany.getNumber() + 1);
        meta.setPageSize(pageCompany.getSize());
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());
        result.setMeta(meta);
        result.setResult(pageCompany.getContent());
        return result;
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
