<!--
</template> 标签是 Vue.js 单文件组件中的一个标记，用于定义组件的模板部分结束。
在 Vue.js 的单文件组件中，模板部分通常用 <template> 标签包裹，用于定义组件的结构、布局和内容。
而 </template> 标签则表示模板的结束位置，即在这之后是组件的其他部分，如 JavaScript 代码和样式。
-->
<template>
  <div>
    <el-container>
      <!--侧边菜单栏-->
      <el-aside width="200px">
        <el-row class="tac">
          <el-col>
            <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
              <el-menu-item index="2">
                <i class="el-icon-user"></i>
                <router-link style="text-decoration: none; color: #333333" to="/employeeTable">简单的table</router-link>
              </el-menu-item>
              <el-submenu index="1">
                <template slot="title">
                  <i class="el-icon-chat-dot-round"></i>
                  <span>发布订阅</span>
                </template>
                <!--消息队列子页面-->
                <el-menu-item index="3">
                  <i class="el-icon-chat-dot-round"></i>
                  <router-link style="text-decoration: none;  color: #333333" to="/messageQueue">RabbitMQ</router-link>
                </el-menu-item>
                <!--websocket子页面-->
                <el-menu-item index="4">
                  <i class="el-icon-chat-dot-round"></i>
                  <router-link style="text-decoration: none; color: #333333" :to="{name:'WebSocket', params:{message:'Hello from parent!'}}">webSocket
                  </router-link>
                </el-menu-item>
              </el-submenu>
            </el-menu>
          </el-col>
        </el-row>
      </el-aside>
      <!--中间主体内容-->
      <el-container>
        <!--头部-->
        <el-header>
          <img style="width: 60px; height: 60px" src="./assets/logo.png">
          <div style="float: right">
            <el-badge :value="messages.length" class="item">
              <el-button size="small">消息</el-button>
            </el-badge>
          </div>
        </el-header>
        <!--主要内容-->
        <el-main>
          <!-- <router-link> 是 Vue Router 提供的用于创建页面导航链接的组件。-->
          <!-- 创建一个链接，当用户点击这个链接时，Vue Router 会导航到指定的路径 “/employeeTable” -->
          <!-- <router-link to="/progress">Progress</router-link>-->
          <!-- 这是 Vue Router 提供的组件，用于渲染当前路径对应的组件。它的作用是将页面内容动态加载到这个位置，实现单页面应用的页面切换效果。 -->
          <router-view/>
        </el-main>
        <!--页脚-->
        <el-footer>
          © 2024 154347188@qq.com
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default {
  name: 'App',
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
    // this.connect();
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
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

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.el-header {
  background-color: #ffffff;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-footer {
  background-color: #ffffff;
  color: #333;
  text-align: right;
  line-height: 60px;
}

.el-aside {
  background-color: #ffffff;
  color: #333;
  /*text-align: center;*/
  line-height: 200px;
}

.el-main {
  background-color: #ffffff;
  color: #333;
  /*text-align: center;*/
  line-height: 10px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.router-link-active {
  text-decoration: none;
  color: #000000;
}
</style>
