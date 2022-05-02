package com.university.sofia.sulpharma.security.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * The Open API 3.0 config.
 * <p>
 * This class configures the open API Bearer authorization in the swagger ui
 */
@Configuration
public class OpenApi30Config {

    private final String moduleName;
    private final String apiVersion;

    /**
     * Instantiates a new Open api 3.0 config.
     *
     * @param moduleName the module name
     * @param apiVersion the api version
     */
    public OpenApi30Config(
            @Value("${module-name}") String moduleName,
            @Value("${api-version}") String apiVersion) {
        this.moduleName = moduleName;
        this.apiVersion = apiVersion;
    }

    /**
     * Custom open API.
     *
     * @return the open API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "JWT Token Authorization";
        final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info().title(apiTitle).version(apiVersion));
    }
}
