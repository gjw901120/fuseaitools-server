package com.fuse.ai.server.web.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级安全注解，如 @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Environment environment;


    /**
     * 核心安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置 CORS
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("https://www.fuseaitools.com");
        
        // 如果是开发环境，允许本地测试域名
        if (isDevProfile()) {
            corsConfig.addAllowedOrigin("http://localhost:3000");
            corsConfig.addAllowedOrigin("http://127.0.0.1:3000");
        }
        
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedHeader("authorization");
        corsConfig.addAllowedHeader("content-type");
        corsConfig.addAllowedHeader("accept");
        corsConfig.addAllowedHeader("x-requested-with");
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
            
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/api/**", corsConfig);
            
        // 禁用 CSRF（跨站请求伪造）和 Session，因为 JWT 是无状态的
        http.cors().configurationSource(corsSource).and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 配置请求授权规则
                .authorizeRequests()
                // ----- 【请在此处配置您的公开接口】-----
                // 示例：登录、注册、获取验证码等接口完全公开
                .antMatchers("/api/user/login/google/callback", "/api/user/send-email-code", "/api/user/login-by-email").permitAll()
                .antMatchers("/api/common/models/tree", "/api/callback/**","/api/news/**").permitAll()
                .antMatchers("/api/common/models/price").permitAll()
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
    
    /**
     * 判断是否为开发环境
     * @return true 如果是 dev 环境
     */
    private boolean isDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("dev".equals(profile)) {
                return true;
            }
        }
        return false;
    }
}