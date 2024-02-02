package com.example.chatting;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChattingController {
	@GetMapping("/chat")
	public String chatting() {
		return "name";
	}

	@PostMapping("/name")
	public String name(@RequestParam(name = "message") String message, Model model) {
		model.addAttribute("message", message);
		return "chatting";
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public String greeting(@Payload String message) throws Exception {
		String processedMessage = message;
		return processedMessage;
	}
}
