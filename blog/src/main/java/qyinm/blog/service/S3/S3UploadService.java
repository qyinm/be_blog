package qyinm.blog.service.S3;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class S3UploadService {
    
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) {
        try {
            String orgFilename = multipartFile.getOriginalFilename();   // 원본 파일명
            if (orgFilename == null) {
                return "";
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
            String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
            String saveFilename = uuid + "." + extension;
            
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());
            
            amazonS3.putObject(bucket, saveFilename, multipartFile.getInputStream(), metadata);
            return amazonS3.getUrl(bucket, saveFilename).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
