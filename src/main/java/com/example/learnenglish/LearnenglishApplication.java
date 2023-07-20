package com.example.learnenglish;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.example.learnenglish.model.UserContextHolder;
import com.example.learnenglish.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

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
