package com.example.learnenglish;

import com.example.learnenglish.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class LearnenglishApplication {
	public static void main(String[] args) {
		SpringApplication.run(LearnenglishApplication.class, args);
	}
}
