<template>
  <div>
    <el-button @click="startProgress">点击发送</el-button>
    <!-- 使用 Element UI 的进度条组件 -->
    <p></p>
    <!-- 使用 Element UI 的环形进度条组件 -->
    <el-progress type="circle" :percentage="progressValue" :status="progressStatus"></el-progress>
    <p></p>
    <p></p>
  </div>
</template>

<script>

export default {
  name: "MessageQueue",
  data() {
    return {
      // 进度条初始百分比
      progressValue: 0,
      // 进度条初始状态，空字符串表示正常状态
      progressStatus: 'success'
    };
  },
  methods: {
    // 发送消息
    startProgress() {
      // 模拟请求发出前的进度
      this.progressValue = 10;
      this.progressStatus = 'success';
      const url = '/api/rabbit/sentSampleMsg';
      // 使用 axios 发送 GET 请求
      this.$axios.get(url)
        .then(response => {
          // 请求成功时的处理
          console.log('响应数据:', response.data);
          // 请求成功，设置百分比为 100%
          this.progressValue = 100;
        })
        .catch(error => {
          // 请求失败时的处理
          console.error('请求失败:', error);
          // 请求失败，设置百分比为 50%，并显示失败状态
          this.progressValue = 50;
          this.progressStatus = 'exception';
          this.$message.error(url + ': ' + error);
        });
    },
  },
}
</script>

<style scoped>

</style>
