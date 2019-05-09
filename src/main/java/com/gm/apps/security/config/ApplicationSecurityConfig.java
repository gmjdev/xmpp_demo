/******************************************************************************
 * Copyright (C) 2019 Girish Mahajan
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * @Written by Girish
 * @Project demo
 * @Date May 5, 2019
 ******************************************************************************/

package com.gm.apps.security.config;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.gm.apps.demo.services.OAuth2UserService;
import com.gm.apps.demo.xmpp.core.services.impl.CustomUserDetailsServiceImpl;
import com.gm.apps.security.core.authorization.oauth2.CookieOAuth2AuthorizationRequestRepository;
import com.gm.apps.security.core.jwt.authentication.JwtAuthenticationProvider;
import com.gm.apps.security.core.jwt.filter.JwtAuthenticationFilter;
import com.gm.apps.security.core.oauth2.authentication.OAuth2AuthenticationFailureHandler;
import com.gm.apps.security.core.oauth2.authentication.OAuth2AuthenticationSuccessHandler;
import com.gm.apps.security.core.oauth2.config.OAuth2Config;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@Import({OAuth2Config.class})
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final String[] defaultWhiteListUrls = {"/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif",
      "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js", "/h2-console/**"};
  private static final String[] authWhiteListUrls = {"/auth/**", "/oauth2/**", "/h2-console/**"};
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationSecurityConfig.class);

  @Autowired
  private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  @Autowired
  private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;
  @Autowired
  public CookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

  @Bean
  public UserDetailsService customUserDetailsService() {
    return new CustomUserDetailsServiceImpl();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
        .userDetailsService(customUserDetailsService())
        .passwordEncoder(passwordEncoder())
        .and()
        .authenticationProvider(jwtAuthenticationProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Bean
  public OAuth2UserService oauth2UserService() {
    return new OAuth2UserService();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http.cors()
        .and()
          .headers().frameOptions().sameOrigin()
        .and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .csrf().disable()
          .formLogin().disable()
          .httpBasic().disable()
        .exceptionHandling()
        .authenticationEntryPoint((request,response,err) -> {
          LOGGER.info("Authentication entry point...");
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        })
        .and()
          .authorizeRequests()
          .antMatchers(defaultWhiteListUrls).permitAll()
          .antMatchers(authWhiteListUrls).permitAll()
          .anyRequest().authenticated()
        .and()
          .oauth2Login()
            .authorizationEndpoint()
              .baseUri("/oauth2/authorize")
              .authorizationRequestRepository(cookieAuthorizationRequestRepository)
            .and()
              .redirectionEndpoint()
              .baseUri("/oauth2/callback/*")
            .and()
              .userInfoEndpoint()
              .userService(oauth2UserService())
            .and()
              .successHandler(oAuth2AuthenticationSuccessHandler)
              .failureHandler(oAuth2AuthenticationFailureHandler);

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    //@formatter:on
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.debug(true);
  }
}
