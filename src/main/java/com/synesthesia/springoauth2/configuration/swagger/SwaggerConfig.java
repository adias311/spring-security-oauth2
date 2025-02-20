package com.synesthesia.springoauth2.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
        .info(new Info()
            .title("Spring Security OAuth2 API")
            .version("1.0")
            .description("API documentation for OAuth2 authentication using Spring Security")
            .contact(new Contact()
                    .name("Adias Afnan")
                    .url("https://mochallate.vercel.app/")
                    .email("adiasafnanva@gmail.com")));
    }
}
