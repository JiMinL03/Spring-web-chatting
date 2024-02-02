package com.example.chatting;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChattingController {
	@GetMapping("/chat")
	public String chatting() {
		return "chatting";
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public String greeting(@Payload String message) throws Exception {
		String processedMessage = message;
		return processedMessage;
	}
}
