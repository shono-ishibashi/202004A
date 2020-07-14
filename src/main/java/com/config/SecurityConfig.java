package com.config;


import com.domain.User;
import com.service.LoginService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( "/css/**", "/js/**", "/img_noodle/**", "/fonts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                //指定したパターンごとに制限をかける
                .antMatchers("/js/**", "/css/**", "/img_noodle/**","/**").permitAll()//制限なし
                //アクセスの許可
                .and()
                .formLogin()
                .loginPage("/login-form")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/noodle/show-list", true)
                .permitAll();

        http.authorizeRequests()
                .antMatchers("/insert").permitAll()
                .antMatchers("/register-user").permitAll()
                .antMatchers("/register-user/insert").permitAll()
                .anyRequest().authenticated();




        http.logout()
                //  ログアウト時の遷移先URL
                .logoutSuccessUrl("/")
                //  ログアウトするとCookieのJSESSIONIDを削除
                .deleteCookies();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(loginService)
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception{

        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}