package stefan.toth.RestAPIProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class InterceptorConfig {

    @Bean
    public MappedInterceptor mappedInterceptor(HandlerInterceptor requestStatisticsInterceptor) {
        return new MappedInterceptor(new String[]{"/**"}, requestStatisticsInterceptor);
    }
}
