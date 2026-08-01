package io.github.artsobol.kurkod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KurkodApplication {

  public static void main(String[] args) {
    SpringApplication.run(KurkodApplication.class, args);
  }
}
