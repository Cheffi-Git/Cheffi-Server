package com.cheffi.common.config.mq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
		rabbitTemplate.setMessageConverter(converter);
		return rabbitTemplate;
	}

}
