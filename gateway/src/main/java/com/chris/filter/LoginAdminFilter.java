package com.chris.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Component
@Slf4j
public class LoginAdminFilter implements GatewayFilter, Ordered {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if(!path.contains("/admin")){
            return chain.filter(exchange);
        }
        if(path.contains("/system/admin/user/login")
        || path.contains("/system/admin/user/logout")
        ||path.contains("/system/admin/kaptcha")){
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("token");
        if(token == null || StringUtils.isEmpty(token)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        Object obj = redisTemplate.opsForValue().get(token);
        if(obj == null){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
