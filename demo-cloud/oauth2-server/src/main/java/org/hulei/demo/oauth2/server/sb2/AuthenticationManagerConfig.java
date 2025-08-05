// package org.hulei.demo.oauth2.server.sb2;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.ProviderManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
// /**
//  * @author hulei
//  * @since 2025/8/5 21:59
//  */
//
// @Configuration
// public class AuthenticationManagerConfig {
//
//     @Bean
//     public AuthenticationManager authenticationManager(
//             UserDetailsService userDetailsService,
//             PasswordEncoder passwordEncoder
//     ) {
//         // DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//         // provider.setUserDetailsService(userDetailsService);
//         // provider.setPasswordEncoder(passwordEncoder);
//         // return new ProviderManager(provider);
//         return new ProviderManager(new DaoAuthenticationProvider());
//     }
// }
