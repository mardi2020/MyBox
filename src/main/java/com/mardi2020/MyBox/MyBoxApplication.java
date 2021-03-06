package com.mardi2020.MyBox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//SecurityAutoConfiguration.class
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBoxApplication.class, args);
	}

}
