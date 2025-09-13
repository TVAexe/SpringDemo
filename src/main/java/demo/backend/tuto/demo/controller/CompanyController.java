package demo.backend.tuto.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import demo.backend.tuto.demo.domain.Company;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.service.CompanyService;
import demo.backend.tuto.demo.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("Create new company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company createdCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> filter, Pageable pageable) {

        ResultPaginationDTO companies = this.companyService.handleGetAllCompanies(filter, pageable);
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/companies")
    @ApiMessage("Update company")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        Company updatedCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete company by id")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("Get company by id")
    public ResponseEntity<Company> getCompany(@PathVariable("id") Long id) {
        Company company = this.companyService.handleGetCompany(id);
        return ResponseEntity.ok(company);
    }


}
