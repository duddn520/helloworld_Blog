package com.helloworldweb.helloworld_guestbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HelloworldGuestbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloworldGuestbookApplication.class, args);
	}

}
