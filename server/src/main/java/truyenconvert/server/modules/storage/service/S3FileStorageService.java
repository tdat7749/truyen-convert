package truyenconvert.server.modules.storage.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface S3FileStorageService {
    String saveFile(MultipartFile file, String folderName, String bucket);
    boolean deleteFile(String key, String bucket);

    String getFileUrl(String bucketName,String folderName);
}
