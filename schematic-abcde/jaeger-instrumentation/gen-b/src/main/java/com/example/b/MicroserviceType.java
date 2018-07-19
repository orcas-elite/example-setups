package com.example.b;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
//	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
	    return this.type;
	}
	
	@RequestMapping(value = "/b1", method = GET)
	public ResponseEntity<String> b1() {
			restTemplate.getForObject("http://d:8080/d1", String.class);
			restTemplate.getForObject("http://e:8080/e1", String.class);
			return new ResponseEntity<String>("Operation c executed successfully.", HttpStatus.OK);
	}

	@RequestMapping(value = "/b2", method = GET)
	public ResponseEntity<String> b2() {
		
	restTemplate.getForObject("http://e:8080/e1", String.class);
		return new ResponseEntity<String>("Operation d executed successfully.", HttpStatus.OK);
	}
}
