package com.jaszczook.sri3.car;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CarSenderConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Bean
	public ActiveMQConnectionFactory senderActiveMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setTrustAllPackages(true);

		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(senderActiveMQConnectionFactory());
	}

	@Bean
	public JmsTemplate jmsTopicTemplate() {
		JmsTemplate jmsTopicTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsTopicTemplate.setMessageConverter(senderJacksonJmsMessageConverter());
		jmsTopicTemplate.setPubSubDomain(true);

		return jmsTopicTemplate;
	}

	@Bean
	public JmsTemplate jmsQueueTemplate() {
		JmsTemplate jmsQueueTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsQueueTemplate.setReceiveTimeout(10 * 1000L);

		return jmsQueueTemplate;
	}

	@Bean
	public JmsMessagingTemplate jmsMessagingTemplate() {
		JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate();
		jmsMessagingTemplate.setJmsTemplate(jmsQueueTemplate());

		return jmsMessagingTemplate;
	}

	@Bean
	public MessageConverter senderJacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

		Map<String, Class<?>> typeIdMappings = new HashMap<>();
		typeIdMappings.put("CAR_DATA_TYPE", CarData.class);

		converter.setTypeIdMappings(typeIdMappings);
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("CAR_DATA_TYPE");

		return converter;
	}
}
