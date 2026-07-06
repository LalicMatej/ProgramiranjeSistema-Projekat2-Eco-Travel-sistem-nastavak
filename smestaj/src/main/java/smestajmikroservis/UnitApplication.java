package smestajmikroservis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;
import smestajmikroservis.config.RateLimiterRefreshListener;

@SpringBootApplication
@EnableFeignClients
public class UnitApplication {

    @Autowired
    private RateLimiterRefreshListener rateLimiterRefreshListener;

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        rateLimiterRefreshListener.apply();
    }

    public static void main(String[] args) {
        SpringApplication.run(UnitApplication.class, args);
    }
}