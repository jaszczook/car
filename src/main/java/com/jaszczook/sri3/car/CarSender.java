package com.jaszczook.sri3.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class CarSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarSender.class);

	public static final String PIT_REQUEST_QUEUE = "pit-request.q";

	private final JmsTemplate jmsTopicTemplate;
	private final JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	public CarSender(JmsTemplate jmsTopicTemplate, JmsMessagingTemplate jmsMessagingTemplate) {
		this.jmsTopicTemplate = jmsTopicTemplate;
		this.jmsMessagingTemplate = jmsMessagingTemplate;
	}

	void sendCarData(CarData carData) {
		LOGGER.info("sending message='{}'", carData);
		jmsTopicTemplate.convertAndSend("carData.t", carData);
	}


	String pitStopRequest() {
		LOGGER.info("sending a pit stop request...");

		String request = "PIT STOP REQUEST";

		return jmsMessagingTemplate.convertSendAndReceive(PIT_REQUEST_QUEUE, request, String.class);
	}
}
