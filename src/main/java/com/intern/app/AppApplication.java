package com.intern.app;

import com.intern.app.repository.CustomRepository.AppRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@Configuration
@EnableJpaRepositories(repositoryBaseClass = AppRepositoryImpl.class)
@SpringBootApplication
@EnableAspectJAutoProxy
public class AppApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
