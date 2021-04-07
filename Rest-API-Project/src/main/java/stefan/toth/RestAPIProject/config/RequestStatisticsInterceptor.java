package stefan.toth.RestAPIProject.config;

import org.hibernate.EmptyInterceptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestStatisticsInterceptor extends EmptyInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(RequestStatisticsInterceptor.class);
    private final String START_TIME = "Start_Time";
    private final String INITIAL_USED_MEMORY = "Initial_memory";

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        long startTime = System.currentTimeMillis();

        Runtime.getRuntime().gc();

        request.setAttribute(START_TIME, startTime);
        request.setAttribute(INITIAL_USED_MEMORY, Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        long executionTime = System.currentTimeMillis() - (long) request.getAttribute(START_TIME);

        long currentMemory = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
        log.info("[" + handler + "] [Time: " + executionTime + "ms], [Memory-Used: " + (currentMemory - (long) request.getAttribute(INITIAL_USED_MEMORY)) + " bytes]");
    }

}
