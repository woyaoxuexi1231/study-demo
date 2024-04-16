<template>
  <div>
    <h1>WebSocket 示例</h1>
    <p>从WebSocket接收的消息: "{{ messageFromServer }}"</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ws: null, // 用于存储WebSocket对象
      messageFromServer: '', // 从服务器接收的消息
    };
  },
  created() {
    this.connectToWebSocket();
  },
  methods: {
    connectToWebSocket() {
      // 假设你的WebSocket服务器地址为 ws://localhost:8080
      this.ws = new WebSocket("ws://localhost:10088/myWsSpring");
      this.ws.onopen = () => {
        console.log('连接到WebSocket服务器');
        // WebSocket连接成功后发送一个消息
        this.ws.send('Hello Server!');
      };
      this.ws.onerror = (error) => {
        console.error('WebSocket错误:', error);
      };
      this.ws.onmessage = (event) => {
        // 当从服务器接收到消息时更新messageFromServer
        this.messageFromServer = event.data;
        console.log('从服务器接收到消息:', event.data);
      };
      this.ws.onclose = () => {
        console.log('WebSocket连接已关闭');
      };
    },
  },
};
</script>

<style>
/* 添加你的样式 */
</style>
