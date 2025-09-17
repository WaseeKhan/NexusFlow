package com.nexusflow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.nexusflow.services.impl.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {

  
    // login user using in memory authentication (only for testing purpose)
    // @Bean
    // public UserDetailsService userDetailsService(){
    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("admin")
    // .password("admin")
    // .roles("ADMIN", "USER")
    // .build();

    // UserDetails user2= User
    // .withDefaultPasswordEncoder()
    // .username("ram")
    // .password("ram")
    // // .roles(null)
    // .build();
    // InMemoryUserDetailsManager inMemoryUserDetailsManager = new
    // InMemoryUserDetailsManager(user1, user2);
    // return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailsService securityCustomUserDetailsService;

    @Autowired
    private OAuthAuthenticationSucessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register").permitAll();

            authorize.requestMatchers("/user/**").authenticated(); // is url pattern per login ya signup krna hoga
            authorize.anyRequest().permitAll(); // rest url sab open hoga

        });
        // form defualt login
        // form login se related changes yaha pr krna hoga
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/do-login");
            formLogin.defaultSuccessUrl("/user/profile");
            formLogin.failureUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            formLogin.failureHandler(authFailureHandler);

            

        });
        httpSecurity.csrf().disable();
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.defaultSuccessUrl("/user/dashboard");
            oauth.failureUrl("/login?error=true");
            oauth.successHandler(handler);

        });
        return httpSecurity.build();
    }

}
