package tech.investbuddy.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tech.investbuddy.apigateway.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

       @Autowired
   private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Check if the route is secured
            if (validator.isSecured.test(request)) {
                String authHeader = null;

                // Check for Authorization header
                if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        authHeader = authHeader.substring(7);
                    }
                } else if (request.getQueryParams().containsKey("Bearer")) {
                    // Check for token in query parameters as fallback
                    authHeader = request.getQueryParams().getFirst("Bearer");
                }

                if (authHeader == null) {
                    throw new RuntimeException("Missing authorization token");
                }

                try {
                    jwtUtil.validateToken(authHeader);

                    // Pass user details to downstream services
                    String username = jwtUtil.extractUsername(authHeader);
                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", username)
                            .build();

                } catch (Exception e) {
                    throw new RuntimeException("Unauthorized access: " + e.getMessage());
                }
            }

            return chain.filter(exchange.mutate().request(request).build());
        };
    }


    public static class Config {

    }
}
