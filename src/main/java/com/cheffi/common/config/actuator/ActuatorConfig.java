package com.cheffi.common.config.actuator;

import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Actuator endpoint bean등록 예시
 */
@Configuration
public class ActuatorConfig {

	@Bean
	public InMemoryHttpExchangeRepository createTraceRepository() {
		return new InMemoryHttpExchangeRepository();
	}
}
