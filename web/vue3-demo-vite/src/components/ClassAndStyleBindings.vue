<script setup>
import {ref} from "vue";

const isActive = ref(true)
const hasError = ref(false)
</script>

<template>
  <div :class="{ active: isActive }">
    s
  </div>
</template>

<style scoped>

</style>