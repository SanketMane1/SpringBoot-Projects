package com.example.CloudGateway;


import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class CloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}
	
	@Bean
	KeyResolver userKeySolver() {
		return exchange -> Mono.just("userKey");
	}
	


}
