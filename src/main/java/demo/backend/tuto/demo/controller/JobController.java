package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import demo.backend.tuto.demo.domain.Job;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.domain.response.job.ResCreatedJobDTO;
import demo.backend.tuto.demo.domain.response.job.ResUpdatedJobDTO;
import demo.backend.tuto.demo.service.JobService;
import demo.backend.tuto.demo.utils.annotation.ApiMessage;
import demo.backend.tuto.demo.utils.exception.IdInvalidException;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create new job")
    public ResponseEntity<ResCreatedJobDTO> create(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.createJob(job));
    
    }

    @PutMapping("/jobs")
    public ResponseEntity<ResUpdatedJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok().body(this.jobService.update(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> delete(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.deleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") Long id) throws IdInvalidException {
        Optional<Job> job = this.jobService.fetchJobById(id);
        if (!job.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok().body(job.get());
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.getAllJobs(spec, pageable));
        
    }
    
    

}
