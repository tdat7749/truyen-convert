package truyenconvert.server.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Configuration {
    @Value("${truyencv.access-key-aws-user}")
    private String accessKey;

    @Value("${truyencv.secret-key-aws-user}")
    private String secretKey;

    @Value("${truyencv.aws-region}")
    private Region region;

    @Bean
    public S3Client s3Client(){
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
          accessKey,
          secretKey
        );

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(region)
                .build();
    }
}
