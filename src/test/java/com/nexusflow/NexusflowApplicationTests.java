package com.nexusflow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nexusflow.services.EmailService;


@SpringBootTest
class NexusflowApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	@Test
	void sendEmailTest() {
		emailService.sendEmail("waseemk.aws@gmail.com", "Test From NexusFlow", 
		"This is test email from NexusFlow application");
		
	}

}
