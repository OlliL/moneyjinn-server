package org.laladev.moneyjinn.server.main;

import org.laladev.moneyjinn.server.config.MoneyjinnConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MoneyjinnConfiguration.class)
public class MoneyJinnServer {

	public static void main(final String[] args) {
		SpringApplication.run(MoneyJinnServer.class, args);
	}

}