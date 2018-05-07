package com.example.c;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
	    return this.type;
	}
	
	@RequestMapping(value = "/e", method = GET)
	public ResponseEntity<String> e() {
		
	restTemplate.getForObject("http://e:8080/i", String.class);
		return new ResponseEntity<String>("Operation e executed successfully.", HttpStatus.OK);
	}
	@RequestMapping(value = "/f", method = GET)
	public ResponseEntity<String> f() {
		
		return new ResponseEntity<String>("Operation f executed successfully.", HttpStatus.OK);
	}
}
