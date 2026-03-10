package br.com.paulohenrique.delivery_orders_api.filters;

import br.com.paulohenrique.delivery_orders_api.config.RateLimitProperties;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
    private final LettuceBasedProxyManager<byte[]> proxyManager;
    private final BucketConfiguration bucketConfiguration;
    private final RateLimitProperties properties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String ip = Optional.ofNullable(httpServletRequest.getHeader("X-Forwarded-For"))
                .map(h -> h.split(",")[0].trim())
                .orElse(httpServletRequest.getRemoteAddr());

        byte[] key = ("rate-limit:" + ip).getBytes(StandardCharsets.UTF_8);

        try {
            Bucket bucket = proxyManager.builder()
                    .build(key, () -> bucketConfiguration);

            if (bucket.tryConsume(1)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                httpServletResponse.setHeader("Retry-After", String.valueOf(properties.getRefillMinutes() * 60));
                httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpServletResponse.getWriter().write("""
                        {"message": "Limite de requisições excedido, tente novamente em instantes", "errors": []}
                        """);
            }
        } catch (Exception e) {
            log.warn("Serviço de rate limit indisponível, requisição liberada");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.contains("/swagger-ui") ||
                path.contains("/v3/api-docs") ||
                path.contains("/swagger-resources");
    }
}
