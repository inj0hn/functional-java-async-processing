package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.components.JavaConcurrencyStrategy;
import com.example.demo.components.ProcessStrategy;

@SpringBootApplication
public class FunctionalJavaAsyncProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunctionalJavaAsyncProcessingApplication.class, args);
	}
	
	@Bean
	public ProcessStrategy processStrategy(@Value("${logprocessor.thread.count}") int threads) {
		return new JavaConcurrencyStrategy(threads);
	}

}
