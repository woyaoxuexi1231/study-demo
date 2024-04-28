<template>
  <div>
    <h1>WebSocket</h1>
    <el-row>
      <el-col :span="12">
        <el-form inline>
          <el-form-item label="WebSocket connection:">
            <el-button id="connect" type="primary" @click="connectWebSocket" :disabled=connected>Connect</el-button>
            <el-button id="disconnect" type="danger" @click="disconnectWebSocket" :disabled=!connected>Disconnect
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <el-form inline>
          <el-form-item label="What is your name?">
            <el-input v-model="message" placeholder="Your name here..."></el-input>
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
export default {
  data() {
    return {
      connected: false,
      name: '',
      messages: [],
      message: '',
      socket: null
    };
  },
  methods: {
    connectWebSocket() {
      this.socket = new WebSocket("ws://localhost:10001/myWsSpring");

      this.socket.onopen = () => {
        console.log('WebSocket连接已建立');
        this.$message({
          message: 'WebSocket连接已建立',
          type: 'success'
        });
        this.setConnected(true);
      };

      this.socket.onmessage = (event) => {
        console.log('收到消息:', event.data);
        this.showGreeting(event.data);
      };

      this.socket.onclose = () => {
        console.log('WebSocket连接已关闭');
        this.$message({
          message: 'WebSocket连接已关闭',
          type: 'warning'
        });
        this.setConnected(false);
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket发生错误:', error);
        this.$message.error('WebSocket发生错误: ' + error);
      };
    },
    disconnectWebSocket() {
      if (this.socket) {
        this.socket.close();
        this.socket = null;
      }
      this.setConnected(false);
      console.log("Disconnected");
    },
    sendMessage() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(this.message);
        console.log('消息已发送:', this.message);
      } else {
        console.error('WebSocket连接未建立或已关闭，无法发送消息');
      }
    },
    setConnected(connected) {
      this.connected = Boolean(connected);
    },
    showGreeting(message) {
      this.messages.push({message});
    }
  }
};
</script>

<style>
/* 添加你的样式 */
</style>
