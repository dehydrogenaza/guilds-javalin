let ws;
let messages = [];

const messageInput = document.getElementById('message');
const sendButton = document.getElementById('send');
const chatList = document.getElementById('chat');


sendButton.addEventListener('click', () => {
  ws.send(messageInput.value)
  messageInput.value = '';
  messageInput.focus();
});

messageInput.addEventListener('keyup', (event) => {
  if (event.key === 'Enter') {
    sendButton.click();
  }
});

const createMessageList = () => messages.map((msg) => `<li class="list-group-item">${msg}</li>`).join('');

const initWebSocket = () => {
  ws = new WebSocket('ws://localhost:9090/chat');
  ws.onopen = () => {
    console.log('Connected');
  };
  ws.onmessage = (msg) => {
    console.log('Message received:', msg.data);
    messages.push(msg.data);
    chatList.innerHTML = createMessageList();
  };
  ws.onclose = () => {
    console.log('Disconnected, trying to reconnect in 3s...');
    setTimeout(initWebSocket, 3000);
  };
}

window.onload = initWebSocket
window.onhashchange = initWebSocket