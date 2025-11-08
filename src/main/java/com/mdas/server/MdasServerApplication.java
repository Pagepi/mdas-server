package com.mdas.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.mdas.server.entity")
public class MdasServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdasServerApplication.class, args);
	}

}
