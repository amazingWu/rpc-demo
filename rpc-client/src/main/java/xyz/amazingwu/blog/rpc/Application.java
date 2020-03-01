package xyz.amazingwu.blog.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 11:41
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
