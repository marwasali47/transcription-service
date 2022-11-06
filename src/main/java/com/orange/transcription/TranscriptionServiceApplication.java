package com.orange.transcription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class TranscriptionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscriptionServiceApplication.class, args);
	}

}
