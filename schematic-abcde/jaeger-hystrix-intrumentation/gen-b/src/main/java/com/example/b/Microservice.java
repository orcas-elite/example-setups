package com.example.b;


import java.util.List;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableHystrix
public class Microservice extends MicroserviceType {

    @Override
    public String getInfo() {
        String type = super.getInfo();
        return type + ":" + version + ":" + uuid.toString();
    }
}
