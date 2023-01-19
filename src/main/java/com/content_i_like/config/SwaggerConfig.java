package com.content_i_like.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Content I Like API Docs",
                description = "API 명세서",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

}