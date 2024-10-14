package truyenconvert.server.modules.storage.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.common.service.UtilitiesService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Qualifier("s3Storage")
public class S3FileStorageServiceImpl implements S3FileStorageService{

    private final S3Client s3Client;
    private final UtilitiesService utilitiesService;
    private final MessageService messageService;

    public S3FileStorageServiceImpl(
            S3Client s3Client,
            UtilitiesService utilitiesService,
            MessageService messageService
    ){
        this.s3Client = s3Client;
        this.utilitiesService = utilitiesService;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public String saveFile(MultipartFile file,String folderName, String bucket) {
        String fullKey = folderName + "/" + "default." + Objects.requireNonNull(file.getContentType()).split("/")[1];

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fullKey)
                .build();

        File newFile = this.convertMultipartFileToFile(file);

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(newFile));

        return this.getFileUrl(bucket,fullKey);
    }

    @Override
    public boolean deleteFile(String key, String bucket) {
        if (!key.endsWith("/")) {
            key = key + "/";
        }

        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(key)
                .build();

        ListObjectsV2Response listObjectsResponse;
        List<ObjectIdentifier> objectsToDelete = new ArrayList<>();

        do {
            listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);
            for (S3Object s3Object : listObjectsResponse.contents()) {
                objectsToDelete.add(ObjectIdentifier.builder().key(s3Object.key()).build());
            }
            listObjectsRequest = listObjectsRequest.toBuilder().continuationToken(listObjectsResponse.nextContinuationToken()).build();
        } while (listObjectsResponse.isTruncated());

        if (!objectsToDelete.isEmpty()) {
            DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(d -> d.objects(objectsToDelete))
                    .build();

            DeleteObjectsResponse deleteObjectsResponse = s3Client.deleteObjects(deleteObjectsRequest);
            return deleteObjectsResponse.hasDeleted() && deleteObjectsResponse.deleted().size() == objectsToDelete.size();
        }

        return true;
    }

    private String getFileUrl(String bucketName, String fullKey) {
        URL url = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fullKey));
        return url.toString();
    }

    private File convertMultipartFileToFile(MultipartFile file){
        File convert = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convert)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convert;
    }

    private void copyDirectory(String sourceBucket, String destinationBucket, String sourcePrefix, String destinationPrefix) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(sourceBucket)
                .prefix(sourcePrefix)
                .build();

        ListObjectsV2Response listObjectsV2Response;

        do {
            listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

            for (S3Object s3Object : listObjectsV2Response.contents()) {
                String sourceKey = s3Object.key();
                String destinationKey = destinationPrefix + sourceKey.substring(sourcePrefix.length());

                CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                        .sourceBucket(sourceBucket)
                        .sourceKey(sourceKey)
                        .destinationBucket(destinationBucket)
                        .destinationKey(destinationKey)
                        .build();

                s3Client.copyObject(copyObjectRequest);
            }

            listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(sourceBucket)
                    .prefix(sourcePrefix)
                    .continuationToken(listObjectsV2Response.nextContinuationToken())
                    .build();

        } while (listObjectsV2Response.isTruncated());
    }

    private boolean doesObjectExist(String bucketName, String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }
}
