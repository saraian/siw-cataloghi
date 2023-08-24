package it.uniroma3.siw;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.uniroma3.siw.model.Credentials;

@Configuration
@EnableWebSecurity
public class AuthConfiguration{
	
	@Autowired
	private DataSource dataSource;
	
	//indica al sistema come recuperare username, password e ruoli nel database
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).
		authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?").
		usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}
	
	//indica come salvare la password nel database
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//definisce la logica di autenticazione effettiva
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().and().cors().disable().authorizeHttpRequests().
		requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/css/**", "images/**", "favicon.ico").permitAll().
		requestMatchers(HttpMethod.POST, "/register", "/login").permitAll().
		requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE).
		requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE).anyRequest().authenticated().
		and().formLogin().loginPage("/login").permitAll().
		defaultSuccessUrl("/success", true).failureUrl("/login?error=true").
		and().logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true).
		deleteCookies("JSESSIONID").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).
		clearAuthentication(true).permitAll();
		return httpSecurity.build();
	}
	
}
