package com.wittypuppy.backend.config.webimage;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebImageConfig implements WebMvcConfigurer {


    // 정적 자원에 접근을 허용하게 하기 위함
    @Value("${image.add-resource-locations}")
    private String ADD_RESOURCE_LOCATION;

    @Value("${image.add-resource-handler}")
    private String ADD_RESOURCE_HANDLER;

    @Value("${spring.mvc.static-path-pattern}")
    private String ADD_STATIC_RESOURCE_HANDLER;
/*
  add-resource-locations: classpath:/static/web-images/
  add-resource-handler: /web-images/**
* */
    @Value("${file.add-resource-locations}")
    private String ADD_FILE_LOCATION;

    @Value("${file.add-resource-handler}")
    private String ADD_FILE_HANDLER;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(ADD_RESOURCE_HANDLER)
                .addResourceLocations(ADD_RESOURCE_LOCATION)
                .setCachePeriod(20);
        registry.addResourceHandler(ADD_STATIC_RESOURCE_HANDLER)
                .addResourceLocations(ADD_RESOURCE_LOCATION)
                .addResourceLocations(ADD_RESOURCE_LOCATION);
        registry.addResourceHandler(ADD_FILE_HANDLER)
                .addResourceLocations(ADD_FILE_LOCATION)
        ;
    }
}
