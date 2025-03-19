package com.steiner.vblog.security;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfigure {
    @Resource
    @Qualifier("custom.permit")
    public String [] permitUrls;


}
