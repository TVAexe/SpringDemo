package demo.backend.tuto.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import demo.backend.tuto.demo.domain.Job;
import demo.backend.tuto.demo.domain.Resume;
import demo.backend.tuto.demo.domain.User;
import demo.backend.tuto.demo.domain.response.ResultPaginationDTO;
import demo.backend.tuto.demo.domain.response.resume.ResCreateResumeDTO;
import demo.backend.tuto.demo.domain.response.resume.ResGetResumeDTO;
import demo.backend.tuto.demo.domain.response.resume.ResUpdateResumeDTO;
import demo.backend.tuto.demo.repository.JobRepository;
import demo.backend.tuto.demo.repository.ResumeRepository;
import demo.backend.tuto.demo.repository.UserRepository;
import demo.backend.tuto.demo.utils.SecurityUtils;

@Service
public class ResumeService {
    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if (resume.getUser() == null || resume.getJob() == null) {
            return false;
        }
        Optional<User> user = this.userRepository.findById(resume.getUser().getId());
        Optional<Job> job = this.jobRepository.findById(resume.getJob().getId());
        if (user.isEmpty()) {
            return false;
        }

        if (job.isEmpty()) {
            return false;
        }

        return true;
    }

    public ResCreateResumeDTO create(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resume.getId());
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resume.getCreatedBy());
        return resCreateResumeDTO;
    }

    public Optional<Resume> fetchById(Long id) {
        return this.resumeRepository.findById(id);
    }

    public ResUpdateResumeDTO update(Resume reqResume) {
        Resume resume = this.resumeRepository.save(reqResume);
        ResUpdateResumeDTO resUpdateResumeDTO = new ResUpdateResumeDTO();
        resUpdateResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resUpdateResumeDTO.setUpdatedBy(resume.getUpdatedBy());
        return resUpdateResumeDTO;
    }

    public void delete(Long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResGetResumeDTO getResume(Resume resume) {
        ResGetResumeDTO resGetResumeDTO = new ResGetResumeDTO();
        resGetResumeDTO.setId(resume.getId());
        resGetResumeDTO.setEmail(resume.getEmail());
        resGetResumeDTO.setUrl(resume.getUrl());
        resGetResumeDTO.setStatus(resume.getStatus());
        resGetResumeDTO.setCreatedAt(resume.getCreatedAt());
        resGetResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resGetResumeDTO.setCreatedBy(resume.getCreatedBy());
        resGetResumeDTO.setUpdatedBy(resume.getUpdatedBy());
        if (resume.getJob() != null) {
            resGetResumeDTO.setCompanyName(resume.getJob().getCompany().getName());
        }
        resGetResumeDTO.setUser(new ResGetResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        resGetResumeDTO.setJob(new ResGetResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return resGetResumeDTO;
    }

    public ResultPaginationDTO fetchAll(Specification<Resume> spec, Pageable pageable) {
        Page <Resume> resumes = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());
        result.setMeta(meta);

        List<ResGetResumeDTO> listResume = resumes.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());
        result.setResult(listResume);
        return result;
    }

    public ResultPaginationDTO fetchByUser(Pageable pageable) {
        // TODO Auto-generated method stub
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page <Resume> resumes = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumes.getTotalPages());
        meta.setTotal(resumes.getTotalElements());
        result.setMeta(meta);
        List<ResGetResumeDTO> listResume = resumes.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());
        result.setResult(listResume);
        return result;

    }

}
