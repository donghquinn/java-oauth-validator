package org.donghyuns.oauth.validator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Oauth Validator API")
                        .version("1.0")
                        .description("Oauth Validator API"))
                .addSecurityItem(new SecurityRequirement().addList("X-Custom-Header"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("X-Custom-Header",
                                new SecurityScheme()
                                        .name("X-Custom-Header")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-Custom-Header")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
