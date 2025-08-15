<template>
  <h2>你好 {{ name }}</h2>
</template>

<script setup>
// 用 defineProps 接收父组件传来的参数。
defineProps({
  name: String
})
</script>
