package jihun.todo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  /**
   * security와 h2-console 함께 사용하는법.
   * https://github.com/HomoEfficio/dev-tips/blob/master/Spring%20Security%EC%99%80%20h2-console%20%ED%95%A8%EA%BB%98%20%EC%93%B0%EA%B8%B0.md
   */

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .authorizeHttpRequests(auth -> auth.
                    anyRequest().authenticated()
            )
            .csrf().ignoringAntMatchers("/h2-console/**").and().headers().frameOptions().sameOrigin().and()
            .build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // 정적파일 혹은 그 밖에 security 의 관리 하에 두지 않는 것들.
    return (web) -> web.ignoring().antMatchers("/todo/**");
  }
}
