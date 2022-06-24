package com.example.ConfluentKafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaConsumer {

	@KafkaListener(topics = "web-domain",groupId = "group_id")
	public void consumeMessage(String s) {
		System.out.println("in Consumer"+s);
		
	}
}
