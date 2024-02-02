// 웹소켓 연결 코드
var socket = new SockJS('/gs-guide-websocket');
var stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    // /topic/greetings 주제를 구독
    stompClient.subscribe('/topic/greetings', function(message) {
        // 받은 메시지를 처리하여 페이지에 실시간으로 업데이트
        console.log('Received WebSocket message: ' + message.body);
        updatePage(message.body);
    });
});

// 페이지 업데이트 함수
function updatePage(message) {
    // 메시지를 사용하여 페이지 업데이트 로직을 수행
    console.log('Received message: ' + message);
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

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
});