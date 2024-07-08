package truyenconvert.server.modules.storage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.modules.storage.service.S3FileStorageService;

@RestController
@RequestMapping("/api/storages")
public class S3FileStorageController {
    private final S3FileStorageService s3FileStorageService;

    public S3FileStorageController(S3FileStorageService s3FileStorageService){
        this.s3FileStorageService = s3FileStorageService;
    }

    @PostMapping("/")
    public ResponseEntity<String> saveFile(@RequestPart MultipartFile file){
        var result = this.s3FileStorageService.saveFile(file,"thumbnails/xich-tam-tuan-thien-4","truyencv");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
