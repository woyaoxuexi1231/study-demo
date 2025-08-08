<template>
  <HelloWorld msg="Welcome to Your Vue.js App"/>
</template>

<script>
import Login from './components/Login.vue'

export default {
  name: 'App',
  components: {
    HelloWorld: Login
  }
}
</script>

<style>
</style>
