package io.github.jfelixy.concurrentlabs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/** Bean para configuração de Swagger API **/
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("ConcurrentLabs")
                        .version("0.0.1-SNAPSHOT")
                        .description("Sistema de reserva de laboratórios em Java e Spring Boot com controle de concorrência via Semaphore e processamento em lote.")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                        .contact(new Contact().name("João Felix").email("jovibfel@gmail.com").url("https://github.com/JoaoFXs"))
                );
    }
}
