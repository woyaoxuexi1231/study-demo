<script setup>

import {ref} from "vue";

const username = ref('')

</script>

<template>

  <!--v-model 是双向绑定，它既能把数据赋给控件，又能在用户操作时更新数据。-->
  <input v-model="username" placeholder="输入用户名">
  <p>你输入的是：{{ username }}</p>

</template>

<style scoped>

</style>