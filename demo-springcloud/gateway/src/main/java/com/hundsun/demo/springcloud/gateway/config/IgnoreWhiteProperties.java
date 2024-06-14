// package com.hundsun.demo.springcloud.gateway.config;
//
// import org.apache.commons.lang.StringUtils;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.cloud.context.config.annotation.RefreshScope;
// import org.springframework.context.annotation.Configuration;
//
// import java.util.HashSet;
// import java.util.Set;
//
// /**
//  * @projectName: weibo
//  * @package: org.weibo.hl.gateway.config
//  * @className: IgnoreWhiteProperties
//  * @description:
//  * @author: hl
//  * @createDate: 2023/5/21 17:11
//  */
//
// @Configuration
// @RefreshScope
// @ConfigurationProperties(prefix = "ignore")
// public class IgnoreWhiteProperties {
//
//     /**
//      * 放行白名单配置，网关不校验此处的白名单
//      */
//     private Set<String> whites = new HashSet<>();
//
//     private String loginUrl;
//
//     private String logoutUrl;
//
//     public Set<String> getWhites() {
//         return whites;
//     }
//
//     public void setWhites(Set<String> whites) {
//         this.whites = whites;
//     }
//
//     public String getLoginUrl() {
//         return StringUtils.isNotBlank(loginUrl) ? loginUrl : "/security/login";
//     }
//
//     public void setLoginUrl(String loginUrl) {
//         this.loginUrl = loginUrl;
//     }
//
//     public String getLogoutUrl() {
//         return StringUtils.isNotBlank(logoutUrl) ? logoutUrl : "/security/logout";
//     }
//
//     public void setLogoutUrl(String logoutUrl) {
//         this.logoutUrl = logoutUrl;
//     }
// }
