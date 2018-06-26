package com.example.e;

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
	protected String type = "e";
	protected String version = "1.0.0";
	protected static String uuid = java.util.UUID.randomUUID().toString();
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
	    return this.type;
	}
	
	@RequestMapping(value = "/h", method = GET)
	public ResponseEntity<String> h(@RequestHeader(value="x-request-id", required=false) String xreq,
			@RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
            @RequestHeader(value="x-b3-spanid", required=false) String xspanid,
            @RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
            @RequestHeader(value="x-b3-sampled", required=false) String xsampled,
            @RequestHeader(value="x-b3-flags", required=false) String xflags,
            @RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		System.out.println("h rx");
		System.out.println("x-request-id=" + xreq);
		System.out.println("x-b3-traceid=" + xtraceid);
		System.out.println("x-b3-spanid=" + xspanid);
		System.out.println("x-b3-parentspanid=" + xparentspanid);
		System.out.println("x-b3-sampled=" + xsampled);
		System.out.println("x-b3-flags=" + xflags);
		System.out.println("x-ot-span-context=" + xotspan);
		
		return new ResponseEntity<String>("Operation h executed successfully.", HttpStatus.OK);
	}
	@RequestMapping(value = "/i", method = GET)
	public ResponseEntity<String> i(@RequestHeader(value="x-request-id", required=false) String xreq,
			@RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
            @RequestHeader(value="x-b3-spanid", required=false) String xspanid,
            @RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
            @RequestHeader(value="x-b3-sampled", required=false) String xsampled,
            @RequestHeader(value="x-b3-flags", required=false) String xflags,
            @RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		System.out.println("i rx");
		System.out.println("x-request-id=" + xreq);
		System.out.println("x-b3-traceid=" + xtraceid);
		System.out.println("x-b3-spanid=" + xspanid);
		System.out.println("x-b3-parentspanid=" + xparentspanid);
		System.out.println("x-b3-sampled=" + xsampled);
		System.out.println("x-b3-flags=" + xflags);
		System.out.println("x-ot-span-context=" + xotspan);
		
		return new ResponseEntity<String>("Operation i executed successfully.", HttpStatus.OK);
	}
}
