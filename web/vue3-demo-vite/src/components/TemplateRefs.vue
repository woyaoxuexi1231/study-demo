<script setup>
import { useTemplateRef, onMounted } from 'vue'

// the first argument must match the ref value in the template
const input = useTemplateRef('my-input')

// vue的生命周期 https://cn.vuejs.org/guide/essentials/lifecycle.html··
onMounted(() => {
  input.value.focus()
})
</script>

<template>
  <input ref="my-input" />
</template>