package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 부트스트랩 클래스
@SpringBootApplication  // 스프링 부트 애플리케이션
public class TacoCloudApplication {
    public static void main(String[] args) {
        // 애플리케이션 실행
        SpringApplication.run(TacoCloudApplication.class, args);
    }
}
