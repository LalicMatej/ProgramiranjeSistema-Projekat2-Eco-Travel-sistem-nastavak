package odrzavanjemikroservis.config;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterRefreshListener {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @Value("${resilience4j.ratelimiter.instances.createWorkOrder.limit-for-period:5}")
    private int createWorkOrderLimit;

    @Value("${resilience4j.ratelimiter.instances.updateWorkOrderStatus.limit-for-period:10}")
    private int updateWorkOrderStatusLimit;

    @Value("${resilience4j.ratelimiter.instances.createMaintenanceTask.limit-for-period:3}")
    private int createMaintenanceTaskLimit;

    @Value("${resilience4j.ratelimiter.instances.createWorker.limit-for-period:3}")
    private int createWorkerLimit;

    @PostConstruct
    public void apply() {
        rateLimiterRegistry.find("createWorkOrder")
                .ifPresent(rl -> rl.changeLimitForPeriod(createWorkOrderLimit));
        rateLimiterRegistry.find("updateWorkOrderStatus")
                .ifPresent(rl -> rl.changeLimitForPeriod(updateWorkOrderStatusLimit));
        rateLimiterRegistry.find("createMaintenanceTask")
                .ifPresent(rl -> rl.changeLimitForPeriod(createMaintenanceTaskLimit));
        rateLimiterRegistry.find("createWorker")
                .ifPresent(rl -> rl.changeLimitForPeriod(createWorkerLimit));
    }
}
