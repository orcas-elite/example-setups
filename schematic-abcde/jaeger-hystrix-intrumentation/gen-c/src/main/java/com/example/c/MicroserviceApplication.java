package com.example.c;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class MicroserviceApplication extends WebMvcConfigurerAdapter {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	public Tracer jaegerTracer() {
		Configuration config = Configuration.fromEnv();
		Tracer tracer = config.getTracer();

		return tracer;
	}



	public static void main(String[] args) {
		SpringApplication.run(MicroserviceApplication.class, args);
	}
}
