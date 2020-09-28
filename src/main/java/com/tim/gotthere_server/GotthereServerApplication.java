package com.tim.gotthere_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class GotthereServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GotthereServerApplication.class, args);
	}

}
