package crud.tech.proof;

import crud.tech.proof.web.WebTraceIdFilter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "${info.app.name}", description = "${info.app.description}", version = "${info.app.version}"))
public class Application {

    public static void main(String[] args) {
        MDC.put(WebTraceIdFilter.TRACE_ID, UUID.randomUUID().toString());
        SpringApplication.run(Application.class, args);
    }

}