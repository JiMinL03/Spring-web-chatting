package com.example.chatting;

import java.io.IOException;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChattingController {
	private final SimpMessagingTemplate messagingTemplate;
	private Count count = new Count();
	private name name = new name();
	
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
	public String greeting(@Payload name name) throws Exception {
		String nameContent = name.getNameContent();
		String messageContent = name.getMessageContent();
		return "[" + nameContent + "]" + "님: " + messageContent;
	}

	@MessageMapping("/countUser")
	@SendTo("/topic/countUser")
	public String countUser(@Payload Count count) {
        if (count.getLeaveUser() != 0) {
            this.count.setUserCount(this.count.getUserCount() - count.getLeaveUser());
        }
        this.count.setUserCount(this.count.getUserCount() + 1);
        return String.valueOf(this.count.getUserCount());
    }

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());

		// name 객체 생성 및 초기화
		String nameContent = this.name.getNameContent();
		// 연결된 모든 클라이언트에게 입장 알림 메시지를 보냄
		messagingTemplate.convertAndSend("/topic/greetings", "[" + nameContent + "]" + "님" + "이 입장하셨습니다.");
	}
}
