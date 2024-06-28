package truyenconvert.server.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "truyencv")
@Getter
@Setter
public class AppPropertiesConfiguration {
    private String accessTokenTime;
    private String refreshTokenTime;
    private String secretKey;
    private String apiPrefix;
    private String accessKeyAwsUser;
    private String secretKeyAwsUser;
    private String awsRegion;
    private String defaultAvatar;
}
