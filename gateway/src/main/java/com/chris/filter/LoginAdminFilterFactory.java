package com.chris.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginAdminFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Resource
    private LoginAdminFilter adminFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return adminFilter;
    }
}
