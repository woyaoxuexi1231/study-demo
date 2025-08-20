<script setup>
import {ref} from "vue";

const awesome = ref(false)
</script>

<template>
  <button @click="awesome = !awesome">Toggle</button>

  <h1 v-if="awesome">Vue is awesome!</h1>
  <h1 v-else>Oh no 😢</h1>
</template>

<style scoped>

</style>