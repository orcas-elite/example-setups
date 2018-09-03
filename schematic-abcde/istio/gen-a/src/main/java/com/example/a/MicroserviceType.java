package com.example.a;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

public abstract class MicroserviceType {
	protected String type = "a";
	protected String version = "1.0.0";
	protected static String uuid = java.util.UUID.randomUUID().toString();
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
		return this.type;
	}

	@RequestMapping(value = "/a1", method = GET)
	@HystrixCommand(fallbackMethod = "fallbackA1")
	public ResponseEntity<String> a1(@RequestHeader(value="x-request-id", required=false) String xreq,
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

			ResponseEntity<String> responseb = restTemplate.exchange("http://b:8080/b1", HttpMethod.GET, request, String.class);
			ResponseEntity<String> responsec = restTemplate.exchange("http://c:8080/c1", HttpMethod.GET, request, String.class);

			String returnStr = "Operation a/a1 executed successfully.";
			return new ResponseEntity<String>(returnStr, HttpStatus.OK);
	}
	@RequestMapping(value = "/a2", method = GET)
	@HystrixCommand(fallbackMethod = "fallbackA2")
	public ResponseEntity<String> a2(@RequestHeader(value="x-request-id", required=false) String xreq,
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

			ResponseEntity<String> responsec = restTemplate.exchange("http://c:8080/c2", HttpMethod.GET, request, String.class);

			String returnStr = "Operation a/a2 executed successfully.";
			return new ResponseEntity<String>(returnStr, HttpStatus.OK);
	}

	public ResponseEntity<String> fallbackA1(@RequestHeader(value="x-request-id", required=false) String xreq,
			@RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
			@RequestHeader(value="x-b3-spanid", required=false) String xspanid,
			@RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
			@RequestHeader(value="x-b3-sampled", required=false) String xsampled,
			@RequestHeader(value="x-b3-flags", required=false) String xflags,
			@RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		ResponseEntity<String> response = new ResponseEntity<String>("Fallback a/a1", HttpStatus.OK);
		return response;
	}

	public ResponseEntity<String> fallbackA2(@RequestHeader(value="x-request-id", required=false) String xreq,
			@RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
			@RequestHeader(value="x-b3-spanid", required=false) String xspanid,
			@RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
			@RequestHeader(value="x-b3-sampled", required=false) String xsampled,
			@RequestHeader(value="x-b3-flags", required=false) String xflags,
			@RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		ResponseEntity<String> response = new ResponseEntity<String>("Fallback a/a2", HttpStatus.OK);
		return response;
	}
}
