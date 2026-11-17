package com.senai.cloud_io;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import com.senai.cloud_io.config.AppProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CtiProvedorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CtiProvedorApplication.class, args);
    }
}
