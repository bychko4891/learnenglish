package com.example.learnenglish;

import com.example.learnenglish.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class LearnenglishApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnenglishApplication.class, args);
	}
}
