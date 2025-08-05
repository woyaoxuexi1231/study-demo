// package org.hulei.demo.oauth2.server.sb2;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
//
// @Configuration
// class JwkSetEndpointConfiguration extends AuthorizationServerSecurityConfiguration {
//
// 	@Override
// 	protected void configure(HttpSecurity http) throws Exception {
// 		super.configure(http);
// 		http
// 			.requestMatchers()
// 				.mvcMatchers("/.well-known/jwks.json")
// 				.and()
// 			.authorizeRequests()
// 				.mvcMatchers("/.well-known/jwks.json").permitAll();
// 	}
//
// 	@Bean
// 	@Override
// 	public AuthenticationManager authenticationManagerBean() throws Exception {
// 		return super.authenticationManagerBean();
// 	}
// }