package com.fuse.ai.server.web.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 自动注入 final 字段
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 自动注入你的 JWT 过滤器
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // 建议添加：认证失败处理器
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF（前后端分离项目标准做法）
                .csrf().disable()
                // 2. 启用 CORS 并应用配置（允许前端跨域请求）
                .cors().configurationSource(corsConfigurationSource()).and()
                // 3. 配置会话为无状态（JWT 不需要 Session）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 4. 配置异常处理：认证/授权失败时返回 JSON，而不是重定向到登录页
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler) // 处理未认证访问
                // .accessDeniedHandler(accessDeniedHandler) // 可选的：处理权限不足
                .and()
                // 5. 配置授权规则
                .authorizeRequests()
                // 公开接口（不需要认证）
                .antMatchers("/api/user/**"
                ).permitAll()
                // 所有其他接口都需要认证（但不一定需要特定角色）
                .anyRequest().authenticated()
                .and()
                // 6. 【核心】将你的 JWT 过滤器添加到 UsernamePasswordAuthenticationFilter 之前
                // 这样每个请求都会先经过 JWT 解析，再走 Spring Security 的标准流程
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置 CORS（跨域资源共享）
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080")); // 允许的前端地址
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization")); // 允许前端获取的响应头
        configuration.setAllowCredentials(true); // 允许携带 Cookie 等凭证
        configuration.setMaxAge(3600L); // 预检请求缓存时间（秒）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对所有路径生效
        return source;
    }

    /**
     * 暴露 AuthenticationManager Bean，供登录认证服务使用
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}