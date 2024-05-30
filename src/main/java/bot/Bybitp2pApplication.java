package bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;



@SpringBootApplication
@EnableAsync
public class Bybitp2pApplication {

    public static void main(String[] args) {
        SpringApplication.run(Bybitp2pApplication.class, args);
    }

}
