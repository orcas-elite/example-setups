package com.example.b;


import java.util.List;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class Microservice extends MicroserviceType {

    @Override
    public String getInfo() {
        String type = super.getInfo();
        return type + ":" + version + ":" + uuid.toString();
    }
}
