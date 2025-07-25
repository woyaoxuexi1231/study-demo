package com.hundsun.demo.springcloud.security.authenticationprovider;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class DeviceAuthenticationDetails extends WebAuthenticationDetails {
    private final String userAgent;

    public DeviceAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.userAgent = request.getHeader("User-Agent");
    }

    public String getUserAgent() {
        return userAgent;
    }
}
