package com.jaszczook.sri3.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CarReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarReceiver.class);

	@JmsListener(destination = "car.q")
	public void receive(CarData carData) {
		LOGGER.info("WARNING!");
		LOGGER.info("received car data from car monitor = {}", carData);
	}
}
