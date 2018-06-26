package com.example.b;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
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
	
	@RequestMapping(value = "/b1", method = GET)
	public ResponseEntity<String> b1(@RequestHeader(value="x-request-id", required=false) String xreq,
			@RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
            @RequestHeader(value="x-b3-spanid", required=false) String xspanid,
            @RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
            @RequestHeader(value="x-b3-sampled", required=false) String xsampled,
            @RequestHeader(value="x-b3-flags", required=false) String xflags,
            @RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		System.out.println("b1 rx");
		System.out.println("x-request-id=" + xreq);
		System.out.println("x-b3-traceid=" + xtraceid);
		System.out.println("x-b3-spanid=" + xspanid);
		System.out.println("x-b3-parentspanid=" + xparentspanid);
		System.out.println("x-b3-sampled=" + xsampled);
		System.out.println("x-b3-flags=" + xflags);
		System.out.println("x-ot-span-context=" + xotspan);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-request-id", xreq);
		headers.set("x-b3-traceid", xtraceid);
		headers.set("x-b3-spanid", xspanid);
		headers.set("x-b3-parentspanid", xparentspanid);
		headers.set("x-b3-sampled", xsampled);
		headers.set("x-b3-flags", xflags);
		headers.set("x-ot-span-context", xotspan);
		
		HttpEntity<String> request = new HttpEntity<String>("parameters", headers);
		restTemplate.exchange("http://d:8080/d1", HttpMethod.GET, request, String.class);
		restTemplate.exchange("http://e:8080/e1", HttpMethod.GET, request, String.class);
		return new ResponseEntity<String>("Operation b1 executed successfully.", HttpStatus.OK);
	}
	@RequestMapping(value = "/b2", method = GET)
	public ResponseEntity<String> b2(@RequestHeader(value="x-request-id", required=false) String xreq,
			@RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
            @RequestHeader(value="x-b3-spanid", required=false) String xspanid,
            @RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
            @RequestHeader(value="x-b3-sampled", required=false) String xsampled,
            @RequestHeader(value="x-b3-flags", required=false) String xflags,
            @RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		System.out.println("b2 rx");
		System.out.println("x-request-id=" + xreq);
		System.out.println("x-b3-traceid=" + xtraceid);
		System.out.println("x-b3-spanid=" + xspanid);
		System.out.println("x-b3-parentspanid=" + xparentspanid);
		System.out.println("x-b3-sampled=" + xsampled);
		System.out.println("x-b3-flags=" + xflags);
		System.out.println("x-ot-span-context=" + xotspan);
		
		HttpHeaders headers = new HttpHeaders();
		if(xreq!=null)
			headers.set("x-request-id", xreq);
		if(xtraceid!=null)
			headers.set("x-b3-traceid", xtraceid);
		if(xspanid!=null)
			headers.set("x-b3-spanid", xspanid);
		if(xparentspanid!=null)
			headers.set("x-b3-parentspanid", xparentspanid);
		if(xsampled!=null)
			headers.set("x-b3-sampled", xsampled);
		if(xflags!=null)
			headers.set("x-b3-flags", xflags);
		if(xotspan!=null)
			headers.set("x-ot-span-context", xotspan);
		
		HttpEntity<String> request = new HttpEntity<String>("parameters", headers);
		restTemplate.exchange("http://e:8080/e1", HttpMethod.GET, request, String.class);
		return new ResponseEntity<String>("Operation b2 executed successfully.", HttpStatus.OK);
	}
}
