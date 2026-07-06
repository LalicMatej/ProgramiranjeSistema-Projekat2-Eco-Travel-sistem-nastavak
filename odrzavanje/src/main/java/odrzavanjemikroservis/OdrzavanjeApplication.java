package odrzavanjemikroservis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;
import odrzavanjemikroservis.config.RateLimiterRefreshListener;

@EnableFeignClients
@SpringBootApplication
public class OdrzavanjeApplication {

    @Autowired
    private RateLimiterRefreshListener rateLimiterRefreshListener;

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        rateLimiterRefreshListener.apply();
    }

    public static void main(String[] args) {
        SpringApplication.run(OdrzavanjeApplication.class, args);
    }
}
