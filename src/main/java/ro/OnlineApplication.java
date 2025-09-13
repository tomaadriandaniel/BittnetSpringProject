package ro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ro.ctrln")
public class OnlineApplication {
    public static void main(String[] args) {

        SpringApplication.run(OnlineApplication.class, args);
    }
}
