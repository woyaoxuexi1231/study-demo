<template>
  <div>
    <el-button @click="startProgress">点击发送</el-button>
    <!-- 使用 Element UI 的进度条组件 -->
    <p></p>
    <!-- 使用 Element UI 的环形进度条组件 -->
    <el-progress type="circle" :percentage="progressValue" :status="progressStatus"></el-progress>
  </div>
</template>

<script>
export default {
  name: "Progress",
  data() {
    return {
      progressValue: 0, // 进度条初始百分比
      progressStatus: ''  // 进度条初始状态，空字符串表示正常状态
    };
  },
  methods: {
    startProgress() {
      // 模拟请求发出前的进度
      this.progressValue = 10;
      this.progressStatus = '';

      // 模拟加载过程，每隔一段时间增加进度
      // const interval = setInterval(() => {
      //   this.percentage += 10; // 每次增加 10%
      //   if (this.percentage >= 100) {
      //     clearInterval(interval); // 达到 100% 后停止加载
      //   }
      // }, 500); // 每隔 500 毫秒增加一次进度

      // 使用 axios 发送 GET 请求
      this.$axios.get('/api/rabbit/sentSampleMsg')
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
        });
    }
  }
}
</script>

<style scoped>

</style>
