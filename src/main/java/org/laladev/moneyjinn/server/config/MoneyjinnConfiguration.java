package org.laladev.moneyjinn.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(value = "org.laladev")
@EnableAspectJAutoProxy
public class MoneyjinnConfiguration {

}
