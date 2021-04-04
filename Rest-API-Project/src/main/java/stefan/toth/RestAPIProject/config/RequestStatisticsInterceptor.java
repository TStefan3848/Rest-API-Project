package stefan.toth.RestAPIProject.config;

import org.hibernate.EmptyInterceptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Component
public class RequestStatisticsInterceptor extends EmptyInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(RequestStatisticsInterceptor.class);
    private final String START_TIME = "Start_Time";

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();


    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);
        memoryMXBean.gc();

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        long executionTime = System.currentTimeMillis() - (long) request.getAttribute(START_TIME);
        log.info("[" + handler + "] [Time: " + executionTime + "ms], [Heap-Memory usage: " + memoryMXBean.getHeapMemoryUsage().getUsed() + "], [Non-Heap-Memory usage: " + memoryMXBean.getNonHeapMemoryUsage().getUsed() + "]");
    }

}
