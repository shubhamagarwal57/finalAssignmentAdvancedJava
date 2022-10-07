package com.hashedin.apigatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private ApiInfo apiInfo(){
        return new ApiInfo("Currency related rest API",
                "API documentation for the authentication and gateway services",
                "1",
                "Terms of service",
                new Contact("Shubham Agarwal","www.hashedin.com","shubhamagarwal57@deloitte.com"),
                "Liscence of Api",
                "Api Liscence Url",
                Collections.emptyList());
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
