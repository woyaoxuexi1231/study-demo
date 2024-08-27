<template>
  <div>
    <h1>WebSocketStomp</h1>
    <el-row>
      <el-col :span="12">
        <el-form inline>
          <el-form-item label="WebSocket connection:">
            <el-button id="connect" type="primary" @click="connectWebSocket" :disabled="connected">Connect</el-button>
            <el-button id="disconnect" type="danger" @click="disconnectWebSocket" :disabled="!connected">Disconnect
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <el-form inline>
          <el-form-item label="What is your name?">
            <el-input v-model="name" placeholder="Your name here..."></el-input>
          </el-form-item>
          <el-form-item>
            <el-button id="send" type="primary" @click="sendMessage" :disabled="!connected">Send</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-table :data="messages" border style="width: 100%">
      <el-table-column prop="message" label="Greetings"></el-table-column>
    </el-table>
  </div>
</template>

<script>
/*
* `webstomp-client` 和 `stompjs` 都是在 JavaScript 环境下实现 STOMP 协议（Simple (or Streaming) Text Oriented Message Protocol）的客户端库。尽管它们的目标相同，即在 Web 应用中通过 WebSocket 提供 STOMP 协议支持，但它们之间存在一些区别：

### webstomp-client

- **最新版本和维护情况：** `webstomp-client` 是一个较新且活跃维护的库，它基于 `stompjs`，但对新版浏览器和模块加载方式（例如 ES6 模块）提供了更好的支持。
- **兼容性和特性：** 它更注重于现代应用开发的兼容性，如提供 ES6 模块导入支持，以及更好地与现代构建工具（如 webpack）配合使用。
- **API 设计：** 在 API 设计方面，`webstomp-client` 旨在提供简洁、易用的接口，它继承了 `stompjs` 的功能同时添加了一些改进和新特性。

### stompjs

- **历史和稳定性：** `stompjs` 是一个历史更悠久，广泛使用的 JavaScript STOMP 客户端实现。作为 STOMP 协议在前端应用中的早期实现之一，它提供了一套完整功能来通过 WebSocket 使用 STOMP。
- **文档和社区支持：** 由于它的历史更长，`stompjs` 拥有较为丰富的文档和广大的用户社区，这有助于解决开发中可能遇到的问题。
- **浏览器兼容性：** 相对于 `webstomp-client`，`stompjs` 在处理兼容性方面可能更加传统，但同时也意味着对旧浏览器有更好的支持。

### 总结

选择哪一个库，往往取决于你的项目需求和开发环境：

- 如果你正在开发一个现代的 Web 应用，并且使用了最新的 JavaScript 特性和构建工具，`webstomp-client` 可能更合适，因为它在这些方面提供了更好的支持和兼容性。
- 如果你需要更广泛的浏览器兼容性支持，或者你的项目已经在使用 `stompjs` 并且对它已经很熟悉，那么选择继续使用 `stompjs` 也是合理的。

在多数现代Web应用开发场景中，`webstomp-client`的现代化特性可能会是一个更受欢迎的选项。
* */

/*
在使用 SockJS 时都会遇到这个问题, 会重复请求两次
GET http://localhost:10099/api/gs-guide-websocket/iframe.html 404 (Not Found)  iframe.js:101
GET http://localhost:10099/api/gs-guide-websocket/iframe.html 404 (Not Found)  iframe.js:101

加载 `iframe.html` 文件的行为通常是由 `SockJS` 库触发的，这个文件通常被用来处理跨域问题或提供额外的支持。
跨域资源共享（CORS）是一种机制，允许在 Web 应用程序中发送跨源 HTTP 请求。然而，WebSocket 不支持跨域请求。
因此，`SockJS` 使用了一种技巧来模拟跨域请求，这就需要加载 `iframe.html` 文件。该文件通过使用 `postMessage` API 来与主页面通信，以解决跨域问题。
加载 `iframe.html` 文件可能会导致 404 错误的原因可能是文件不存在或者路径配置不正确。这通常是由于服务器端的配置问题导致的，或者是由于前端应用部署方式不正确导致的。在前面的回答中提到的解决方案可以帮助你排除这个问题。


* */

import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';
import Stomp from 'webstomp-client';

export default {
  data() {
    return {
      // socketUrl: '/api/gs-guide-websocket',
      socketUrl: '/gs-guide-websocket',
      name: '',
      messages: [],
      connected: false,
      stompClient: null,
    };
  },
  methods: {
    connectWebSocket() {
      // Initialize the SockJs connection
      // const socket = new SockJS('/api/gs-guide-websocket'); // <--- Update this endpoint
      const socket = new SockJS(this.socketUrl); // 连接到WebSocket服务器端点
      this.stompClient = Stomp.over(socket);

      // Connect to WebSocket server
      this.stompClient.connect(
        {},
        success => {

          this.connected = true;
          console.log('Connected: ' + success);
          this.$message({
            message: 'WebSocketStomp连接已建立',
            type: 'success'
          });

          // Subscribe to a topic
          this.stompClient.subscribe('/topic/greetings', message => {

            // 使用 sockjs-client
            // console.log("收到消息:", message.body)
            // this.handleMessage(message.body);

            // 使用 webstomp-client
            // const body = JSON.parse(message.body);
            console.log("收到消息:", message.body)
            this.handleMessage(message.body);
          });
        },
        error => {
          // 在这里处理连接错误或连接断开的情况
          console.log('Connection error or disconnected:', error);
          this.$message.error('WebSocketStomp发生错误或断开连接:' + error);
          this.connected = false;
        }
      );
    },
    disconnectWebSocket() {
      // Disconnect from WebSocket server
      if (this.stompClient) {
        this.stompClient.disconnect(() => {
          console.log("Disconnected");
          this.connected = false;
        });
      }
    },
    sendMessage() {
      // Send message to WebSocket server
      if (this.stompClient && this.connected) {

        // 使用 sockjs-client
        // this.stompClient.send('/app/hello', {}, JSON.stringify({name: this.name}));

        // 使用 webstomp-client
        const message = JSON.stringify({name: this.name});
        this.stompClient.send('/app/hello', message);
        // 使用json格式传输
        // this.stompClient.send('/app/hello', message, {'content-type': 'application/json'});
      }
    },
    handleMessage(message) {
      // Add received message to the messages array
      // console.log(message)
      this.messages.push({message});
      // console.log(this.messages)
    },
  },
};
</script>
