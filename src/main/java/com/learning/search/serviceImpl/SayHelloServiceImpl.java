package com.learning.search.serviceImpl;

import com.learning.search.service.SayHelloService;
import org.springframework.stereotype.Service;

@Service
public class SayHelloServiceImpl implements SayHelloService {
    @Override
    public String sayHello() {
        return "Hello!";
    }
}
