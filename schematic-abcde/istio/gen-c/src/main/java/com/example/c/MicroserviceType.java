package com.example.c;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

public abstract class MicroserviceType {
	protected String type = "c";
	protected String version = "1.0.0";
	protected static String uuid = java.util.UUID.randomUUID().toString();
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/info", method = GET)
	public String getInfo() {
		return this.type;
	}

	@RequestMapping(value = "/c1", method = GET)
	@HystrixCommand(fallbackMethod = "fallbackC1")
	public ResponseEntity<String> c1(@RequestHeader(value="x-request-id", required=false) String xreq,
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

		ResponseEntity<String> responsee = restTemplate.exchange("http://e:8080/e2", HttpMethod.GET, request, String.class);
		String returnStr = "Operation c/c1 executed successfully.";
		return new ResponseEntity<String>(returnStr, HttpStatus.OK);
	}
	@RequestMapping(value = "/c2", method = GET)
	@HystrixCommand(fallbackMethod = "fallbackC2")
	public ResponseEntity<String> c2(@RequestHeader(value="x-request-id", required=false) String xreq,
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

		String returnStr = "Operation c/c2 executed successfully.";
		return new ResponseEntity<String>(returnStr, HttpStatus.OK);
	}

	public ResponseEntity<String> fallbackC1(@RequestHeader(value="x-request-id", required=false) String xreq,
											 @RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
											 @RequestHeader(value="x-b3-spanid", required=false) String xspanid,
											 @RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
											 @RequestHeader(value="x-b3-sampled", required=false) String xsampled,
											 @RequestHeader(value="x-b3-flags", required=false) String xflags,
											 @RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		ResponseEntity<String> response = new ResponseEntity<String>("Fallback c/c1", HttpStatus.OK);
		return response;
	}

	public ResponseEntity<String> fallbackC2(@RequestHeader(value="x-request-id", required=false) String xreq,
											 @RequestHeader(value="x-b3-traceid", required=false) String xtraceid,
											 @RequestHeader(value="x-b3-spanid", required=false) String xspanid,
											 @RequestHeader(value="x-b3-parentspanid", required=false) String xparentspanid,
											 @RequestHeader(value="x-b3-sampled", required=false) String xsampled,
											 @RequestHeader(value="x-b3-flags", required=false) String xflags,
											 @RequestHeader(value="x-ot-span-context", required=false) String xotspan) {
		ResponseEntity<String> response = new ResponseEntity<String>("Fallback c/c2", HttpStatus.OK);
		return response;
	}

}
