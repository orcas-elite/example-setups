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
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-request-id", xreq);
		headers.set("x-b3-traceid", xtraceid);
		headers.set("x-b3-spanid", xspanid);
		headers.set("x-b3-parentspanid", xparentspanid);
		headers.set("x-b3-sampled", xsampled);
		headers.set("x-b3-flags", xflags);
		headers.set("x-ot-span-context", xotspan);
		HttpEntity<String> request = new HttpEntity<String>("parameters", headers);

		ResponseEntity<String> responseD1, responseE1;
		String responseD1Str, responseE1Str;
		try {
			responseD1 = restTemplate.exchange("http://d:8080/d1", HttpMethod.GET, request, String.class);
			responseD1Str = responseD1.getBody();
		} catch (Exception e) {
			responseD1Str = "Operation d1 failed";
		}
		try {
			responseE1 = restTemplate.exchange("http://e:8080/e1", HttpMethod.GET, request, String.class);
			responseE1Str = responseE1.getBody();
		} catch (Exception e) {
			responseE1Str = "Operation e1 failed";
		}
		return new ResponseEntity<String>(responseD1Str + "<br>" + responseE1Str + "<br>Operation b1 executed successfully.", HttpStatus.OK);
	}
	@RequestMapping(value = "/b2", method = GET)
	public ResponseEntity<String> b2(@RequestHeader(value="x-request-id", required=false) String xreq,
			@RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
            @RequestHeader(value="x-b3-spanid", required=false) String xspanid,
            @RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
            @RequestHeader(value="x-b3-sampled", required=false) String xsampled,
            @RequestHeader(value="x-b3-flags", required=false) String xflags,
            @RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		
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

		ResponseEntity<String> responseE1;
		String responseE1Str;

		try {
			responseE1 = restTemplate.exchange("http://e:8080/e1", HttpMethod.GET, request, String.class);
			responseE1Str = responseE1.getBody();
		} catch (Exception e) {
			responseE1Str = "Operation e1 failed";
		}
		return new ResponseEntity<String>(responseE1Str + "<br>Operation b2 executed successfully.", HttpStatus.OK);
	}
}
