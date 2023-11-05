package qyinm.blog.controller.FilUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import qyinm.blog.dto.UploadDto;
import qyinm.blog.service.S3.S3UploadService;

@RequestMapping("/api")
@RestController
public class FileUploadController {

    @Autowired
    private S3UploadService s3UploadService;

    @PostMapping("/uploadFile")
    public ResponseEntity<UploadDto> uploadFile(@RequestParam("image") MultipartFile multi) {
        UploadDto dto = new UploadDto(s3UploadService.saveFile(multi)) ;
        return ResponseEntity.ok(dto);
    }
}
