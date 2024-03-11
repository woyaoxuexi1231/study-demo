<template>
  <div>
    <el-button @click="startProgress">点击发送</el-button>

    <el-popover
        placement="bottom"
        title="标题"
        width="200"
        trigger="click"
        content="这是一段内容,这是一段内容,这是一段内容,这是一段内容。">
      <el-button slot="reference">click 激活</el-button>
    </el-popover>

    <!-- 使用 Element UI 的进度条组件 -->
    <p></p>
    <!-- 使用 Element UI 的环形进度条组件 -->
    <el-progress type="circle" :percentage="progressValue" :status="progressStatus"></el-progress>
    <p></p>

    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="一致性 Consistency" name="1">
        <div>与现实生活一致：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念；</div>
        <div>在界面中一致：所有的元素和结构需保持一致，比如：设计样式、图标和文本、元素的位置等。</div>
      </el-collapse-item>
      <el-collapse-item title="反馈 Feedback" name="2">
        <div>控制反馈：通过界面样式和交互动效让用户可以清晰的感知自己的操作；</div>
        <div>页面反馈：操作后，通过页面元素的变化清晰地展现当前状态。</div>
      </el-collapse-item>
      <el-collapse-item title="效率 Efficiency" name="3">
        <div>简化流程：设计简洁直观的操作流程；</div>
        <div>清晰明确：语言表达清晰且表意明确，让用户快速理解进而作出决策；</div>
        <div>帮助用户识别：界面简单直白，让用户快速识别而非回忆，减少用户记忆负担。</div>
      </el-collapse-item>
      <el-collapse-item title="可控 Controllability" name="4">
        <div>用户决策：根据场景可给予用户操作建议或安全提示，但不能代替用户进行决策；</div>
        <div>结果可控：用户可以自由的进行操作，包括撤销、回退和终止当前操作等。</div>
      </el-collapse-item>
    </el-collapse>
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
      progressStatus: 'success',
      visible: false,
      activeName: '0',
    };
  },
  methods: {
    // 发送消息
    startProgress() {
      // 模拟请求发出前的进度
      this.progressValue = 10;
      this.progressStatus = 'success';

      // 模拟加载过程，每隔一段时间增加进度
      // const interval = setInterval(() => {
      //   this.percentage += 10; // 每次增加 10%
      //   if (this.percentage >= 100) {
      //     clearInterval(interval); // 达到 100% 后停止加载
      //   }
      // }, 500); // 每隔 500 毫秒增加一次进度

      // 使用 axios 发送 GET 请求
      this.$axios.get('/api/rabbit/sentSampleMsg2')
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
    },
  },
}
</script>

<style scoped>

</style>
