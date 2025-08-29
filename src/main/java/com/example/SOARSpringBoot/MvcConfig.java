package com.example.SOARSpringBoot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        //registry.addViewController("/req-approval").setViewName("req-approval");
        //registry.addViewController("/emp-profile").setViewName("emp-profile");
        //registry.addViewController("/invt-maintenanace").setViewName("invt-maintenanace");
       // registry.addViewController("/invt-request").setViewName("invt-request");
    }
}
