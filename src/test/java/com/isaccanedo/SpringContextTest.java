package com.isaccanedo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.isaccanedo.springbootmvc.SpringBootMvcApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootMvcApplication.class)
public class SpringContextTest {

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}