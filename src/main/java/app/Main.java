package app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableConfigurationProperties
public class Main {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(Main::logUncaughtException);

        SpringApplication.run(Main.class, args);
        log.info("Logger is ready");

    }

    private static void logUncaughtException(Thread thread, Throwable error) {
        log.error("Unhandled exception caught in {}. Message: {}", thread.getName(), error.getMessage(), error);
    }

}
