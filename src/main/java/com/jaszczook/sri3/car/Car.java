package com.jaszczook.sri3.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Car {

	private static final Logger LOGGER = LoggerFactory.getLogger(Car.class);

	private CarData carData;
	private final CarSender carSender;

	@Autowired
	public Car(CarSender carSender) {
		this.carSender = carSender;
	}

	@Scheduled(fixedRate = 15 * 1000)
	public void sendCarData() {
		updateCarData();
		carSender.sendCarData(carData);
	}

	@Scheduled(fixedRate = 30 * 1000)
	void sendPitStopRequest() {
		String response = carSender.pitStopRequest();
		LOGGER.info("response received from pit: {}", response);
	}

	private void updateCarData() {
		carData = new CarData(
				90 + Math.random() * (150 - 90),
				40 + Math.random() * (200 - 40),
				20 + Math.random() * (100 - 20)
		);
	}
}
