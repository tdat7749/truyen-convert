package truyenconvert.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApplication.class);
    public static void main(String[] args) {
        LOGGER.info("App running...");
        SpringApplication.run(ServerApplication.class, args);
    }

}
