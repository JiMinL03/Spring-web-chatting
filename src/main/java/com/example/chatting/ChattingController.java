package com.example.chatting;

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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChattingController {
	private final SimpMessagingTemplate messagingTemplate;
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
		return "[" +nameContent +"]"+ "님: " + messageContent;
	}
	
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());

        // name 객체 생성 및 초기화
        name name = new name();
		String nameContent = name.getNameContent();
        // 연결된 모든 클라이언트에게 입장 알림 메시지를 보냄
        messagingTemplate.convertAndSend("/topic/greetings", "[" +nameContent +"]"+ "님"+"이 입장하셨습니다.");
    }
}
