package com.example.c;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

public abstract class MicroserviceType {
	protected String type = "c";
	protected String version = "1.0.0";
	protected static String uuid = java.util.UUID.randomUUID().toString();
//	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Tracer jaegerTracer;

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
	    return this.type;
	}
	
	@RequestMapping(value = "/c1", method = GET)
	@HystrixCommand(fallbackMethod = "fallback")
	public ResponseEntity<String> c1() {
		jaegerTracer.activeSpan().setTag("pattern.circuitBreaker", true);
		jaegerTracer.activeSpan().setTag("pattern.circuitBreaker.fallback", true);
		restTemplate.getForObject("http://lbe:80/e2", String.class);
		jaegerTracer.activeSpan().setTag("patter.circuitBreaker.fallback", false);
		return new ResponseEntity<String>("Operation e executed successfully.", HttpStatus.OK);
	}

	@RequestMapping(value = "/c2", method = GET)
	public ResponseEntity<String> c2() {
		return new ResponseEntity<String>("Operation f executed successfully.", HttpStatus.OK);
	}

	public ResponseEntity<String> fallback() {
		return new ResponseEntity<String>("fallback", HttpStatus.OK);
	}
}
