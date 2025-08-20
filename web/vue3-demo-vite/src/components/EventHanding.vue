<script setup>
import {ref} from "vue";

const count = ref(0)
</script>

<template>
  <div class="card">
    <!--@click：事件绑定（相当于 v-on:click）。-->
    <!--{{ }}：插值语法，用来显示变量。-->
    事件绑定
    <button type="button" @click="count++">count is {{ count }}</button>
  </div>
</template>

<style scoped>

</style>