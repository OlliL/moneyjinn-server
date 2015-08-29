package org.laladev.moneyjinn.server.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "org.laladev")
public class MoneyJinnServer {

	public static void main(final String[] args) {
		SpringApplication.run(MoneyJinnServer.class, args);
	}

}