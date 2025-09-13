package demo.backend.tuto.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import demo.backend.tuto.demo.domain.response.file.ResUploadFileDTO;
import demo.backend.tuto.demo.service.FileService;
import demo.backend.tuto.demo.utils.exception.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;

    @Value("${demo.upload-file.base-uri}")
    private String baseURI;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @PostMapping("/file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
            // validate
            if (file == null ||file.isEmpty()) {
                throw new StorageException("File is empty. Please upload a file.");
            }
            String fileName = file.getOriginalFilename();
            List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
            boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
            if (!isValid) {
                throw new StorageException("File extension is not valid. Please upload a file with one of the following extensions: " + String.join(", ", allowedExtensions));
            }
            // create directory if not exist

            this.fileService.createDirectoryIfNotExist(baseURI + folder);
            // store file
            String uploadedFiles = this.fileService.store(file, folder);
            ResUploadFileDTO res = new ResUploadFileDTO(uploadedFiles, Instant.now());
        return  ResponseEntity.ok().body(res);
    }
}
