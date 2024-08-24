package io.security.tetesecurity;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilerChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                //default formLogin 설정
                //.formLogin(Customizer.withDefaults());
                .formLogin(form -> form
//                        .loginPage("/loginPage")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/", true)  //alwaysUse가 ture :: 무조건 defaultSuccessUrl로 감, false :: 이전 redirect Url로 감
                        .failureUrl("/failure")
                        .usernameParameter("userId") // 사용할 ID 필드 name
                        .passwordParameter("passwd") // 사용할 패스워드 필드 name
                        .successHandler((request, response, authentication) -> {
                            System.out.println("success !! " + authentication);
                            response.sendRedirect("/home");
                        })
                        .failureHandler((request, response, exception) -> {
                          System.out.println("fail..." + exception.getMessage());
                          response.sendRedirect("/login");
                        })
                        .permitAll()




                );
        return http.build();

    }

    @Bean
    public UserDetailsService userDetailService(){
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

}
