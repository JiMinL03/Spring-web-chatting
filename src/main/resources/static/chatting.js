// 웹소켓 연결 코드
var socket = new SockJS('/gs-guide-websocket');
var stompClient = Stomp.over(socket);
let userCountStr  = 0;
let leaveUserStr  = 0;

stompClient.connect({}, function(frame) {
	// /topic/greetings 주제를 구독
	stompClient.subscribe('/topic/greetings', function(message) {
		// 받은 메시지를 처리하여 페이지에 실시간으로 업데이트
		updatePage(message.body);
	});

	stompClient.subscribe('/topic/countUser', function(message) {
		// 받은 메시지를 처리하여 페이지에 실시간으로 업데이트
		userCountStr = parseInt(message.body);
		countUser(message.body);
	});
	sendCountUser();
	function sendCountUser() {
		// 서버에 메시지를 보낼 주제와 내용 설정
		let destination = '/app/countUser';  // 해당 주제는 서버에서 처리해야 합니다.
		let leaveUser = leaveUserStr.toString();
		let userCount = userCountStr .toString();
		var dataToSend = {
			leaveUser: leaveUser,
			userCount: userCount
		};
		// stompClient.send를 사용하여 서버에 메시지 전송
		stompClient.send(destination, {}, JSON.stringify(dataToSend));
	}
});

function countUser(message) {
	const userCountElement = document.getElementById('countUser');
	userCountElement.innerText = "접속인원: " + message;
}

// 페이지 업데이트 함수
function updatePage(message) {
	$("#talk_container").append("<tr><td>" + message + "</td></tr>");
}

document.getElementById('send').addEventListener('click', function() {
	event.preventDefault();
	var messageContent = $("#message").val();
	var nameContent = $("#name").data("message");
	// JSON 객체를 생성하여 두 값을 포함시킴
	var dataToSend = {
		messageContent: messageContent,
		nameContent: nameContent
	};
	console.log(dataToSend);
	stompClient.send("/app/hello", {}, JSON.stringify(dataToSend));
});

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
});