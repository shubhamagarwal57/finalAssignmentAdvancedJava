package com.hashedin.currencyexchangeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private ApiInfo apiInfo() {
        return new ApiInfo("Currency related rest API",
                "API documentation for Currency-exchange microservice",
                "1",
                "Terms of service",
                new Contact("Shubham Agarwal", "www.hashedin.com", "shubhamagarwal57@deloitte.com"),
                "Liscence of Api",
                "Api Liscence Url",
                Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}