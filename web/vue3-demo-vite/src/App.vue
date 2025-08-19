<!--
<script setup> 是 Vue 3 的单文件组件（SFC）中一种更简洁的组合式 API 写法，用于替代传统的 <script> 块。
它是 Vue 3.2 引入的编译时语法糖，让组件逻辑更简洁、直观。
-->
<script setup>
// 组件化开发
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <!--
  组件化开发
  Vue 实现了自己的组件模型，使我们可以在每个组件内封装自定义内容与逻辑。
  组件名用 PascalCase（大驼峰），HTML 中用 <Hello />。
  -->
  <HelloWorld msg="..."/>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}

.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}

.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
