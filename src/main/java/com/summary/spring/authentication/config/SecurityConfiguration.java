package com.summary.spring.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder password;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.userDetailsService(myUserDetailsService).passwordEncoder(password);
    }

    private AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(password);
        return authenticationProvider;
    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        String password = this.password.encode("123456");
//
//        InMemoryUserDetailsManager memoryUserDetailsManager = new InMemoryUserDetailsManager();
//        UserDetails normalUser = User.builder().username("user1").password(password).authorities(Roles.NORMAL_USER.toString()).roles(Roles.NORMAL_USER.toString()).build();
//        UserDetails adminUser = User.builder().username("admin").password(password).authorities(Roles.ADMIN.toString()).roles(Roles.ADMIN.toString()).build();
//        memoryUserDetailsManager.createUser(normalUser);
//        memoryUserDetailsManager.createUser(adminUser);
//        return memoryUserDetailsManager;
//    }

    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .requestMatchers().anyRequest()
            .and()
            .authorizeRequests()
            .antMatchers("/oauth/**")
            .permitAll();
    }
}
