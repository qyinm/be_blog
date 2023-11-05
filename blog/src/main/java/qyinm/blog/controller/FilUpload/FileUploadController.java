package qyinm.blog.controller.FilUpload;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import qyinm.blog.service.S3.S3UploadService;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class FileUploadController {

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("image") MultipartFile multi) {
        
        return null;
    }
}
