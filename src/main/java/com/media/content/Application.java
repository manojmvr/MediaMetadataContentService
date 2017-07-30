package com.media.content;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author Manoj Paramasivam
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableAutoConfiguration
@Configuration
@EnableSwagger2
@Slf4j
public class Application extends SpringApplication {

    @Value("${info.app-name}")
    private String appName;

    @Value("${info.description}")
    private String appDescription;

    @Value("${info.version}")
    private String version;


    public static void main(String[] args) {

        log.debug("Application starting...");
        Application.run(Application.class, args);
        log.debug("Application started !");

    }


    @Bean
    public Docket api() {

        log.debug("Intializing swagger configurations");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Media Content Api Documentation")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.media.content.controller"))
                .paths(PathSelectors.ant("/media/*"))
                .build()
                .apiInfo(new ApiInfo(appName, appDescription, version, "", "", null, null))
                .enableUrlTemplating(true);

    }


    private ApiInfo apiInfo() {

        return new ApiInfo(appName, appDescription, version, "", "", null, null);

    }
}
