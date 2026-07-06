package org.raflab.gatewayservice.config;

import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class RouteSecConfig {

    // Ordered list: most specific first
    private static final List<RouteRule> RULES = List.of(
            // Smestaj - units
            new RouteRule("GET",    "/api/units/",     List.of("ROLE_ADMIN", "ROLE_GUEST", "ROLE_HOST")),
            new RouteRule("POST",   "/api/units/",     List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("PUT",    "/api/units/",     List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("DELETE", "/api/units/",     List.of("ROLE_ADMIN", "ROLE_HOST")),

            new RouteRule("DELETE", "/api/smestaj/",   List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("PUT",    "/api/smestaj/",   List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("POST",   "/api/smestaj/",   List.of("ROLE_ADMIN", "ROLE_HOST")),

            // Odrzavanje - work orders
            new RouteRule("GET",    "/api/workOrder/", List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("POST",   "/api/workOrder/", List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("PATCH",  "/api/workOrder/", List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("DELETE", "/api/workOrder/", List.of("ROLE_ADMIN")),

            new RouteRule("POST", "/api/maintenanceTask/", List.of("ROLE_ADMIN")),

            // Odrzavanje - workers
            new RouteRule("GET",    "/api/worker/",    List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("POST",   "/api/worker/",    List.of("ROLE_ADMIN")),
            new RouteRule("DELETE", "/api/worker/",    List.of("ROLE_ADMIN")),

            new RouteRule("DELETE", "/api/bookings/",  List.of("ROLE_ADMIN")),
            new RouteRule("POST",   "/api/bookings/",  List.of("ROLE_ADMIN", "ROLE_HOST", "ROLE_GUEST")),
            new RouteRule("GET",    "/api/bookings/",  List.of("ROLE_ADMIN", "ROLE_HOST", "ROLE_GUEST")),
            new RouteRule("PUT",    "/api/bookings/",  List.of("ROLE_ADMIN", "ROLE_HOST")),

            new RouteRule("GET",    "/api/system/status",         List.of("ROLE_ADMIN", "ROLE_HOST", "ROLE_GUEST")),

            new RouteRule("POST",   "/api/cancellation-policies/",   List.of("ROLE_ADMIN")),
            new RouteRule("GET",    "/api/cancellation-policies/",   List.of("ROLE_ADMIN", "ROLE_HOST", "ROLE_GUEST")),

            new RouteRule("POST",   "/api/guests/",    List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("GET",    "/api/guests/",    List.of("ROLE_ADMIN", "ROLE_HOST")),

            
            // Finances - transactions
            new RouteRule("POST",   "/api/invoices/",    List.of("ROLE_ADMIN", "ROLE_HOST")),

            // Finances - invoices
            new RouteRule("POST",   "/api/invoices/",        List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("GET",    "/api/invoices/",        List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("PATCH",  "/api/invoices/",        List.of("ROLE_ADMIN", "ROLE_HOST")),

            // Finances - payment methods
            new RouteRule("POST",   "/api/payment-methods/", List.of("ROLE_ADMIN")),
            new RouteRule("GET",    "/api/payment-methods/", List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("PATCH",  "/api/payment-methods/", List.of("ROLE_ADMIN")),

            // Finances - tax rates
            new RouteRule("POST",   "/api/tax-rates/",       List.of("ROLE_ADMIN")),
            new RouteRule("GET",    "/api/tax-rates/",       List.of("ROLE_ADMIN", "ROLE_HOST")),

            new RouteRule("POST",   "/api/adventures/",List.of("ROLE_ADMIN", "ROLE_GUIDE")),
            new RouteRule(null,     "/",               List.of("ROLE_ADMIN","ROLE_GUEST","ROLE_GUIDE","ROLE_HOST","ROLE_SUPPORT")),

            //Advenures service - avanture
            new RouteRule("GET",    "/api/avanture/",     List.of("ROLE_ADMIN", "ROLE_GUEST", "ROLE_HOST")),
            new RouteRule("POST",   "/api/avanture/",     List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("PUT",    "/api/avanture/",     List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("DELETE", "/api/avanture/",     List.of("ROLE_ADMIN", "ROLE_HOST")),

            //Vodici service - vodici
            new RouteRule("GET",    "/api/vodici/",     List.of("ROLE_ADMIN", "ROLE_GUEST", "ROLE_HOST")),
            new RouteRule("POST",   "/api/vodici/",     List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("PUT",    "/api/vodici/",     List.of("ROLE_ADMIN", "ROLE_HOST")),
            new RouteRule("DELETE", "/api/vodici/",     List.of("ROLE_ADMIN", "ROLE_HOST"))
    );

    public boolean isAllowed(String method, String path, List<String> userRoles) {
        for (RouteRule rule : RULES) {
            if (rule.matches(method, path)) {
                return rule.getAllowedRoles().stream().anyMatch(userRoles::contains);
            }
        }
        return false;
    }

    record RouteRule(String method, String pathPrefix, List<String> allowedRoles) {
        boolean matches(String m, String p) {
            boolean methodMatch = method == null || method.equals(m);
            boolean pathMatch = p.startsWith(pathPrefix);
            return methodMatch && pathMatch;
        }
        List<String> getAllowedRoles() { return allowedRoles; }
    }
}
