package com.example.ecommerce.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

/**
 * Distributed rate limiter using Redis INCR with expiry. Allows 'limit' requests per window (seconds) per IP.
 * Simpler than Bucket4j but distributed and production-ready; can be adapted to Bucket4j+Redis.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RateLimitFilter extends HttpFilter {

    private final StringRedisTemplate redisTemplate;
    private final int limit = 100;
    private final long windowSec = 60L; // 60 seconds

    public RateLimitFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String ip = req.getRemoteAddr();
        String key = "rl:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1L) {
            redisTemplate.expire(key, Duration.ofSeconds(windowSec));
        }
        if (count != null && count > limit) {
            res.setStatus(429);
            res.getWriter().write("Rate limit exceeded");
            return;
        }
        chain.doFilter(req, res);
    }
}