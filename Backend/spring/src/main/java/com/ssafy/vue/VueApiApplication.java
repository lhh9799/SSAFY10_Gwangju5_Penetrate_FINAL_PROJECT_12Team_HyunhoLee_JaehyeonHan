package com.ssafy.vue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class VueApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VueApiApplication.class, args);
	}

}
