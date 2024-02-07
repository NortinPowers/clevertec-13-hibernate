package by.clevertec.config;

import by.clevertec.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "global.exception.handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GlobalExceptionHandlerAutoConfiguration {

    @Bean
    public GlobalExceptionHandler controllerAdvice() {
        return new GlobalExceptionHandler();
    }
}
