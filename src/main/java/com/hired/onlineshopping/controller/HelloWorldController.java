package com.hired.onlineshopping.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloWorldController {

    @Resource
    FakeDDBRecord fakeDDBRecord;

    public HelloWorldController(FakeDDBRecord a) {
        this.fakeDDBRecord = a;
    }

    @PostMapping("/")
    public String helloWorld() {
        return "Post Hello World";
    }

    @GetMapping("/hello/{text}")
    public String echo(@PathVariable("text") String text) {
        return "Hello, you just input: " + text;
    }

    public int addPlug2(int a, int b) {
        return fakeDDBRecord.add(a, b) + 2;
    }
}
