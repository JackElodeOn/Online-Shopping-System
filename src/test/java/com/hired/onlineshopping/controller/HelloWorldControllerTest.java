package com.hired.onlineshopping.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class HelloWorldControllerTest {

    @Mock
    FakeDDBRecord fakeDDBRecord;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Resource
    HelloWorldController helloWorldController;

    @Test
    void addPlug2() {
        int result = helloWorldController.addPlug2(1, 3);
        assertEquals(6, result);
    }

    @Test
    void mockAddPlug2() {
        helloWorldController = new HelloWorldController(fakeDDBRecord);
        when(fakeDDBRecord.add(anyInt(), anyInt())).thenReturn(10);
        int result = helloWorldController.addPlug2(1, 3);
        assertEquals(6, result);
    }
}