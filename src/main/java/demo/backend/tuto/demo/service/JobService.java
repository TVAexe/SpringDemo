package demo.backend.tuto.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import demo.backend.tuto.demo.domain.Job;
import demo.backend.tuto.demo.domain.Skill;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.domain.response.job.ResCreatedJobDTO;
import demo.backend.tuto.demo.domain.response.job.ResUpdatedJobDTO;
import demo.backend.tuto.demo.repository.JobRepository;
import demo.backend.tuto.demo.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreatedJobDTO createJob(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(x->x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        Job createdJob = this.jobRepository.save(job);
        ResCreatedJobDTO res = new ResCreatedJobDTO();
        res.setId(createdJob.getId());
        res.setName(createdJob.getName());
        res.setLocation(createdJob.getLocation());
        res.setSalary(createdJob.getSalary());
        res.setQuantity(createdJob.getQuantity());
        res.setLevel(createdJob.getLevel());
        res.setStartDate(createdJob.getStartDate());
        res.setEndDate(createdJob.getEndDate());
        res.setActive(createdJob.isActive());
        res.setCreatedAt(createdJob.getCreatedAt());
        res.setCreatedBy(createdJob.getCreatedBy());

        if (createdJob.getSkills() != null) {
            List<String> skillNames = createdJob.getSkills().stream().map(x->x.getName()).collect(Collectors.toList());
            res.setSkills(skillNames);
        }

        return res;
    }

    public ResUpdatedJobDTO update(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(x->x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        Job updatedJob = this.jobRepository.save(job);
        ResUpdatedJobDTO res = new ResUpdatedJobDTO();
        res.setId(updatedJob.getId());
        res.setName(updatedJob.getName());
        res.setLocation(updatedJob.getLocation());
        res.setSalary(updatedJob.getSalary());
        res.setQuantity(updatedJob.getQuantity());
        res.setLevel(updatedJob.getLevel());
        res.setStartDate(updatedJob.getStartDate());
        res.setEndDate(updatedJob.getEndDate());
        res.setActive(updatedJob.isActive());
        res.setUpdatedAt(updatedJob.getUpdatedAt());
        res.setUpdatedBy(updatedJob.getUpdatedBy());

        if (updatedJob.getSkills() != null) {
            List<String> skillNames = updatedJob.getSkills().stream().map(x->x.getName()).collect(Collectors.toList());
            res.setSkills(skillNames);
        }

        return res;
    }

    public Optional<Job> fetchJobById(Long id) {
        return this.jobRepository.findById(id);
    }

    public void deleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());
        result.setMeta(meta);
        result.setResult(pageJob.getContent());
        return result;
    }
}
