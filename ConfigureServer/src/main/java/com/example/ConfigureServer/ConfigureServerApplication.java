package com.example.ConfigureServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigureServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigureServerApplication.class, args);
	}

}
