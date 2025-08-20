<script setup>

// 当一个组件首次渲染时，Vue 会追踪在渲染过程中使用的每一个 ref。然后，当一个 ref 被修改时，它会触发追踪它的组件的一次重新渲染。
// 普通变量是无法被检测的
import {reactive, ref} from "vue";

const count = ref(0)
const name = ref('小明') // 基础类型
const user = reactive({age: 20, name: '小红'}) // 对象类型

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