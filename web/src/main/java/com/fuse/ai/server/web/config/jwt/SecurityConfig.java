package com.fuse.ai.server.web.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级安全注解，如 @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    /**
     * 核心安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用CSRF（跨站请求伪造）和Session，因为JWT是无状态的
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 配置请求授权规则
                .authorizeRequests()
                // ----- 【请在此处配置您的公开接口】-----
                // 示例：登录、注册、获取验证码等接口完全公开
                .antMatchers("/api/user/login/google/callback", "/api/user/send-email-code", "/api/user/login-by-email").permitAll()
                .antMatchers("/api/common/models/tree", "/api/callback/**").permitAll()
                // 示例：允许对OPTIONS方法的预检请求（用于跨域）
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // -----------------------------------
                // 除此之外的所有请求都需要认证（即携带有效的JWT Token）
                .anyRequest().authenticated()
                .and()
                // 配置异常处理：当未认证或Token无效时，使用自定义的入口点返回401
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                // 将我们自定义的JWT过滤器添加到默认的UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}