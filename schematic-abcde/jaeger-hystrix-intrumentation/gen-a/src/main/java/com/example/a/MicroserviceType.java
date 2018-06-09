package com.example.a;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

public abstract class MicroserviceType {
    protected String type = "a";
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

    @RequestMapping(value = "/a", method = GET)
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<String> a() {
        restTemplate.getForObject("http://b:8080/c", String.class);
        restTemplate.getForObject("http://c:8080/e", String.class);
        return new ResponseEntity<String>("Operation a executed successfully.", HttpStatus.OK);
    }

    @RequestMapping(value = "/b", method = GET)
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<String> b() {
        restTemplate.getForObject("http://c:8080/f", String.class);
        return new ResponseEntity<String>("Operation b executed successfully.", HttpStatus.OK);
    }

    public ResponseEntity<String> fallback() {
        return new ResponseEntity<String>("Fallback", HttpStatus.OK);
    }
}
