package com.epam.gym;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;


@EnableTransactionManagement
@SpringBootApplication
//@EnableDiscoveryClient
@EnableJms
public class GymManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymManagerApplication.class, args);
	}

	@Bean
	public MeterRegistry getMeterRegistry() {
		return new CompositeMeterRegistry();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
		return restTemplateBuilder.requestFactory(HttpComponentsClientHttpRequestFactory.class).build();
	}

}
