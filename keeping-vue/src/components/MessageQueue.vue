<template>
  <div>
    <el-container>
      <el-main>
        <div>
          <h1>RabbitMQ</h1>
          <el-button @click="sendRabbitMsg">点击发送</el-button>
          <!-- 使用 Element UI 的进度条组件 -->
          <p></p>
          <!-- 使用 Element UI 的环形进度条组件 -->
          <el-progress type="circle" :percentage="rabbitProgressValue" :status="rabbitProgressStatus"></el-progress>
          <p></p>
          <p></p>
        </div>
      </el-main>
      <el-main>
        <div>
          <h1>Redis</h1>
          <el-button @click="sendRedisMsg">点击发送</el-button>
          <!-- 使用 Element UI 的进度条组件 -->
          <p></p>
          <!-- 使用 Element UI 的环形进度条组件 -->
          <el-progress type="circle" :percentage="redisProgressValue" :status="redisProgressStatus"></el-progress>
          <p></p>
          <p></p>
        </div>
      </el-main>
    </el-container>

  </div>
</template>

<script>

export default {
  name: "MessageQueue",
  data() {
    return {
      // 进度条初始百分比
      rabbitProgressValue: 0,
      // 进度条初始状态，空字符串表示正常状态
      rabbitProgressStatus: 'success',

      // 进度条初始百分比
      redisProgressValue: 0,
      // 进度条初始状态，空字符串表示正常状态
      redisProgressStatus: 'success'
    };
  },
  methods: {
    // 发送消息
    sendRabbitMsg() {
      // 模拟请求发出前的进度
      this.rabbitProgressValue = 10;
      this.rabbitProgressStatus = 'success';
      const url = '/api/rabbimq/sendMsg';
      // const url = '/rabbimq/sendMsg';
      // 使用 axios 发送 GET 请求
      this.$axios.get(url)
        .then(response => {
          // 请求成功时的处理
          console.log('响应数据:', response.data);
          this.$message({
            message: '消息发送成功',
            type: 'success'
          });
          // 请求成功，设置百分比为 100%
          this.rabbitProgressValue = 100;
        })
        .catch(error => {
          // 请求失败时的处理
          console.error('请求失败:', error);
          // 请求失败，设置百分比为 50%，并显示失败状态
          this.rabbitProgressValue = 50;
          this.rabbitProgressStatus = 'exception';
          this.$message.error(url + ': ' + error);
        });
    },

    // 发送消息
    sendRedisMsg() {
      // 模拟请求发出前的进度
      this.redisProgressValue = 10;
      this.redisProgressStatus = 'success';
      const url = '/api/redispush/publish';
      // const url = '/redispush/publish';
      // 使用 axios 发送 GET 请求
      this.$axios.get(url)
        .then(response => {
          // 请求成功时的处理
          console.log('响应数据:', response.data);
          this.$message({
            message: '消息发送成功',
            type: 'success'
          });
          // 请求成功，设置百分比为 100%
          this.redisProgressValue = 100;
        })
        .catch(error => {
          // 请求失败时的处理
          console.error('请求失败:', error);
          // 请求失败，设置百分比为 50%，并显示失败状态
          this.redisProgressValue = 50;
          this.redisProgressStatus = 'exception';
          this.$message.error(url + ': ' + error);
        });
    },
  },
}
</script>

<style scoped>

</style>
