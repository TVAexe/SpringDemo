package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import demo.backend.tuto.demo.domain.Resume;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.domain.response.resume.ResCreateResumeDTO;
import demo.backend.tuto.demo.domain.response.resume.ResGetResumeDTO;
import demo.backend.tuto.demo.domain.response.resume.ResUpdateResumeDTO;
import demo.backend.tuto.demo.service.ResumeService;
import demo.backend.tuto.demo.utils.annotation.ApiMessage;
import demo.backend.tuto.demo.utils.exception.IdInvalidException;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create Resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException("User or Job not exist");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update Resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
        Optional<Resume> existingResume = this.resumeService.fetchById(resume.getId());
        if (existingResume.isEmpty()) {
            throw new IdInvalidException("Resume with id " + resume.getId() + " not exist");
        }

        Resume reqResume = existingResume.get();
        reqResume.setStatus(resume.getStatus());
        return ResponseEntity.ok().body(this.resumeService.update(reqResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete Resume")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
        Optional<Resume> existingResume = this.resumeService.fetchById(id);
        if (existingResume.isEmpty()) {
            throw new IdInvalidException("Resume with id " + id + " not exist");
        }

        this.resumeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get Resume")
    public ResponseEntity<ResGetResumeDTO> get(@PathVariable("id") Long id) throws IdInvalidException {
        Optional<Resume> existingResume = this.resumeService.fetchById(id);
        if (existingResume.isEmpty()) {
            throw new IdInvalidException("Resume with id " + id + " not exist");
        }

        return ResponseEntity.ok().body(this.resumeService.getResume(existingResume.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes with pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAll (@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchAll(spec, pageable));
    }

    @PostMapping("/resumes/by-user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchByUser(pageable));
    }


}
