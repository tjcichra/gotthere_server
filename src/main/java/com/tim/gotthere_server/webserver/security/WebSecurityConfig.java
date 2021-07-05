package com.tim.gotthere_server.webserver.security;

import com.tim.gotthere_server.database.UserRepository;
import com.tim.gotthere_server.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/css/login.css").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable().cors()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> conf = auth.inMemoryAuthentication();
//		User tim = new User();
//		tim.setUsername("tim");
//		tim.setPassword("test");
//
//		userRepository.save(tim);

		for(User li : userRepository.findAll()) {
			conf.withUser(li.getUsername()).password("{noop}" + li.getPassword()).roles("USER");
			System.out.println(li.getUsername() + " " + li.getPassword());
		}
	}
}
