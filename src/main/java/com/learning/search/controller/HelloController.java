package com.learning.search.controller;

import com.learning.search.service.SayHelloService;
import com.learning.search.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/hello")
@EnableAutoConfiguration
@RestController
public class HelloController {

    @Autowired
    private SayHelloService sayHelloService;

    @RequestMapping(value = "greeting", method = RequestMethod.GET)
    public Response greeting(){
        return Response.successMessage(sayHelloService.sayHello());
    }

}
