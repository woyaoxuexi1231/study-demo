<!--
<script setup> 是 Vue 3 的单文件组件（SFC）中一种更简洁的组合式 API 写法，用于替代传统的 <script> 块。
它是 Vue 3.2 引入的编译时语法糖，让组件逻辑更简洁、直观。
-->
<script setup>
import MyComponent from "./components/MyComponent.vue";
import {ref} from "vue";

const callback = ref((s) => {
  console.log(s);
})
</script>

<template>
  <!--
  组件化开发
  Vue 实现了自己的组件模型，使我们可以在每个组件内封装自定义内容与逻辑。
  组件名用 PascalCase（大驼峰），HTML 中用 <Hello />。
  -->
  <!--  <HelloWorld msg="..."/>-->
  <nav>
    <router-link to="/">首页</router-link>
    |
    <router-link to="/about">关于</router-link>
    |
    <router-link to="/todo">实战小项目（Todo List）</router-link>
  </nav>
  <router-view/>
  <MyComponent @some-event="callback"/>
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
