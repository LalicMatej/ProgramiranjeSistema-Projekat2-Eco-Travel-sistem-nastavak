package smestajmikroservis.config;

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

    @Value("${resilience4j.ratelimiter.instances.createUnit.limit-for-period:5}")
    private int createUnitLimit;

    @Value("${resilience4j.ratelimiter.instances.createReservation.limit-for-period:10}")
    private int createReservationLimit;

    @PostConstruct
    public void apply() {
        rateLimiterRegistry.find("createUnit")
                .ifPresent(rl -> rl.changeLimitForPeriod(createUnitLimit));
        rateLimiterRegistry.find("createReservation")
                .ifPresent(rl -> rl.changeLimitForPeriod(createReservationLimit));
    }
}
