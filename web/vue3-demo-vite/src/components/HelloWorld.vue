<!--<script setup>：Vue3 推荐写法，代码更简洁。-->
<script setup>
// ref：创建响应式数据（类似 React 的 useState）
// 在 Vue 中，ref 是一个用于获取组件或 DOM 元素引用的特殊属性，同时也是 Composition API 中的一个响应式数据函数。
import { ref, reactive } from 'vue'

defineProps({
  msg: String,
})

const count = ref(0)
const count2 = 2;

console.log("count: " + count)
console.log("count2: " + count2)


const name = ref('小明') // 基础类型
const user = reactive({ age: 20, city: '上海' }) // 对象类型


const age = ref(19)
const list = ref(['苹果', '香蕉', '西瓜'])

// 双向绑定
const username = ref('')

import { useCounterStore } from '../stores/counter'
const counter = useCounterStore()

</script>

<template>

<!--  最基本的数据绑定形式是文本插值，它使用的是“Mustache”语法 (即双大括号)： -->
<!--  双大括号标签会被替换为相应组件实例中 msg 属性的值。同时每次 msg 属性更改时它也会同步更新。-->
  <h1>{{ msg }}</h1>

  <div class="card">
<!--@click：事件绑定（相当于 v-on:click）。-->
<!--{{ }}：插值语法，用来显示变量。-->
    <button type="button" @click="count++">count is {{ count }}</button>
    <p>
      Edit
      <code>components/HelloWorld.vue</code> to test HMR
    </p>
  </div>

  <p v-if="age >= 18">成年人</p>
  <p v-else>未成年</p>

  <ul>
    <li v-for="(item, i) in list" :key="i">{{ item }}</li>
  </ul>

  <input v-model="username" placeholder="输入用户名">
  <p>你输入的是：{{ username }}</p>


  <nav>
    <router-link to="/">首页</router-link> |
    <router-link to="/about">关于</router-link> |
    <router-link to="/todo">实战小项目（Todo List）</router-link>
  </nav>
  <router-view />


  <p>{{ counter.count }}</p>
  <button @click="counter.increment">+1</button>

</template>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
