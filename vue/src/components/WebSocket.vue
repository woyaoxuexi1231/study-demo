<template>
  <div>
    <h1>WebSocket Test</h1>
    <div>
      <input v-model="name" placeholder="Enter your name">
      <button @click="sendMessage">Send</button>
    </div>
    <div v-for="message in messages">
      <p>{{ message.body }}</p>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default {
  name: "WebSocket",
  data() {
    return {
      socketUrl: '/api/gs-guide-websocket',
      reconnecting: false,
      stompClient: null,
      name: '',
      messages: []
    };
  },
  mounted() {
    this.connect();
  },
  methods: {
    connect() {
      const socket = new SockJS(this.socketUrl); // 连接到WebSocket服务器端点
      // const socket = new SockJS('ws://localhost:10088/gs-guide-websocket'); // 连接到WebSocket服务器端点
      // this.stompClient = Stomp.overWS(socket);
      // this.stompClient = Stomp.overWS('ws://localhost:10088/gs-guide-websocket');
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
          {},
          frame => {
            console.log('Connected: ' + frame);
            this.connectSucceed();
          },
          err => {
            console.error(err);
            this.reconnect(this.socketUrl, this.connectSucceed)
          }
      );

      //
      //
      // const webSocket = new WebSocket('ws://localhost:10088/gs-guide-websocket');
      // webSocket.onopen => (){
      //   console.log("WebSocket connection opened:", event);
      // };

    },
    reconnect(socketUrl, callback) {
      this.reconnecting = true
      let connected = false
      const timer = setInterval(() => {
        this.socket = new SockJS(socketUrl)
        this.stompClient = Stomp.over(this.socket)
        this.stompClient.connect(
            {},
            frame => {
              this.reconnectting = false
              connected = true
              console.log('Connected: ' + frame);
              clearInterval(timer)
              callback()
            },
            err => {
              console.log('Reconnect failed！');
              if (!connected) console.log(err);
            })
      }, 1000);
    },

    connectSucceed() {
      this.stompClient.subscribe('/topic/greetings', (greeting) => {
        console.log("receive message(/topic/greetings) ==> " + greeting)
        this.messages.push(greeting);
      });
    },

    sendMessage() {
      this.stompClient.send('/app/hello', {}, JSON.stringify({name: this.name}));
    }
  }
}
</script>

<style scoped>

</style>
