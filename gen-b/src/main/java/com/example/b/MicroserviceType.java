package com.example.b;

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
	protected String type = "b";
	protected String version = "1.0.0";
	protected static String uuid = java.util.UUID.randomUUID().toString();
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
	    return this.type;
	}
	
	@RequestMapping(value = "/c", method = GET)
	public ResponseEntity<String> c() {
		
	restTemplate.getForObject("http://d:8080/g", String.class);
	restTemplate.getForObject("http://e:8080/h", String.class);
		return new ResponseEntity<String>("Operation c executed successfully.", HttpStatus.OK);
	}
	@RequestMapping(value = "/d", method = GET)
	public ResponseEntity<String> d() {
		
	restTemplate.getForObject("http://e:8080/h", String.class);
		return new ResponseEntity<String>("Operation d executed successfully.", HttpStatus.OK);
	}
}
