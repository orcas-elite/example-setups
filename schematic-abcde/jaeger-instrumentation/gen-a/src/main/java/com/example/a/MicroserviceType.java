package com.example.a;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	protected String type = "a";
	protected String version = "1.0.0";
	protected static String uuid = java.util.UUID.randomUUID().toString();
//	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
	    return this.type;
	}
	
	@RequestMapping(value = "/a1", method = GET)
	private ResponseEntity<String> a1() {
		
	restTemplate.getForObject("http://b:8080/b1", String.class);
	restTemplate.getForObject("http://c:8080/c1", String.class);
		return new ResponseEntity<String>("Operation a executed successfully.", HttpStatus.OK);
	}

	@RequestMapping(value = "/a2", method = GET)
	public ResponseEntity<String> a2() {
		
	restTemplate.getForObject("http://c:8080/c2", String.class);
		return new ResponseEntity<String>("Operation b executed successfully.", HttpStatus.OK);
	}
}
