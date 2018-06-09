package com.example.d;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

public abstract class MicroserviceType {
	protected String type = "d";
	protected String version = "1.0.0";
	protected static String uuid = java.util.UUID.randomUUID().toString();
//	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/info", method = GET)
	@HystrixCommand(fallbackMethod = "fallback")
	public String getInfo() {
	    return this.type;
	}
	
	@RequestMapping(value = "/g", method = GET)
	@HystrixCommand(fallbackMethod = "fallback")
	public ResponseEntity<String> g() {
		
		return new ResponseEntity<String>("Operation g executed successfully.", HttpStatus.OK);
	}

	public ResponseEntity<String> fallback() {
		return new ResponseEntity<String>("fallback", HttpStatus.OK);
	}
}
