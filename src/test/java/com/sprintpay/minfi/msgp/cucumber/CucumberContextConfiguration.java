package com.sprintpay.minfi.msgp.cucumber;

import io.cucumber.java.Before;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sprintpay.minfi.msgp.SpminfimsgpApp;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = SpminfimsgpApp.class)
public class CucumberContextConfiguration {

    @Before
    public void setup_cucumber_spring_context() {
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }

}
