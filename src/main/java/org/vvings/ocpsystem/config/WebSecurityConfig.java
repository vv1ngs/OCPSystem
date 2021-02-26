package org.vvings.ocpsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsUtils;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.service.loginService;
import org.vvings.ocpsystem.util.JacksonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author vvings
 * @Date 2020/12/23 20:11
 * @Version 1.0
 */
@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private loginService userService;
    @Autowired
    private UrlFilter urlFilter;
    @Autowired
    private DecisionManager decisionManager;
    @Autowired
    private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(myPasswordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Bean
    public PasswordEncoder myPasswordEncoder() {
        return new MyPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/operationUser/addOperationUser", "/static/**","/register");
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out = httpServletResponse.getWriter();
                HttpSession h=httpServletRequest.getSession();
                authentication.getCredentials();
                out.write(JacksonUtil.ObjToString(ServerResponse.createByLoginSuccess(httpServletRequest.getSession(false).getId())));
                out.flush();
                out.close();
            }
        });
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out = httpServletResponse.getWriter();
                ServerResponse serverResponse=null;
                if (e instanceof UsernameNotFoundException  || e instanceof BadCredentialsException) {
                    serverResponse=ServerResponse.createByErrorCode(ResponseCode.LOGIN_FAIL.getCode(), e .getMessage());
                }
                else {
                    serverResponse=ServerResponse.createByError();
                }
                out.write(JacksonUtil.ObjToString(serverResponse));
                out.flush();
                out.close();
            }
        });
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                       o.setSecurityMetadataSource(urlFilter);
                       o.setAccessDecisionManager(decisionManager);
                       return o;
                    }
                }).and().formLogin().loginPage("/loginPage").loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password").permitAll().failureHandler(new AuthenticationFailureHandler(){
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    PrintWriter out = httpServletResponse.getWriter();
                    ServerResponse serverResponse=null;
                    if (e instanceof UsernameNotFoundException  || e instanceof BadCredentialsException) {
                        serverResponse=ServerResponse.createByErrorCode(ResponseCode.LOGIN_FAIL.getCode(), e .getMessage());
                    }
                    else {
                        serverResponse=ServerResponse.createByError();
                    }
                    out.write(JacksonUtil.ObjToString(serverResponse));
                    out.flush();
                    out.close();
                }
            }).successHandler(new AuthenticationSuccessHandler(){
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out = httpServletResponse.getWriter();
                HttpSession h=httpServletRequest.getSession();
                authentication.getCredentials();
                out.write(JacksonUtil.ObjToString(ServerResponse.createByLoginSuccess(httpServletRequest.getSession(false).getId())));
                out.flush();
                out.close();
            }
        }).and().logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out = httpServletResponse.getWriter();
                out.write(JacksonUtil.ObjToString(ServerResponse.createBySuccess("成功登出")));
                out.flush();
                out.close();
            }
        }).permitAll()
            .and().csrf().disable().exceptionHandling().accessDeniedHandler(authenticationAccessDeniedHandler)
        .and().sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).expiredSessionStrategy(new ReLoginExpiredSessionStrategy()).sessionRegistry(sessionRegistry);
    }
}

