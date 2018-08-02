package com.example.a;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.opentracing.Tracer;
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

    @Autowired
    private Tracer jaegerTracer;

    @RequestMapping(value = "/info", method = GET)
    public String getInfo() {
        return this.type;
    }

    @RequestMapping(value = "/a1", method = GET)
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<String> a1() {
        jaegerTracer.activeSpan().setTag("pattern.circuitBreaker", true);
        jaegerTracer.activeSpan().setTag("pattern.circuitBreaker.fallback", true);
        restTemplate.getForObject("http://lbb:80/b1", String.class);
        restTemplate.getForObject("http://lbc:80/c1", String.class);
        jaegerTracer.activeSpan().setTag("pattern.circuitBreaker.fallback", false);
        return new ResponseEntity<String>("Operation a executed successfully.", HttpStatus.OK);
    }

    @RequestMapping(value = "/a2", method = GET)
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<String> a2() {
        jaegerTracer.activeSpan().setTag("pattern.circuitBreaker", true);
        jaegerTracer.activeSpan().setTag("pattern.circuitBreaker.fallback", true);
        restTemplate.getForObject("http://lbc:80/c2", String.class);
        jaegerTracer.activeSpan().setTag("pattern.circuitBreaker.fallback", false);
        return new ResponseEntity<String>("Operation b executed successfully.", HttpStatus.OK);
    }

    public ResponseEntity<String> fallback() {
        ResponseEntity<String> response = new ResponseEntity<String>("Fallback", HttpStatus.OK);
        return response;
    }
}