<!--<script setup>：Vue3 推荐写法，代码更简洁。-->
<script setup>
// ref：创建响应式数据（类似 React 的 useState）
// 在 Vue 中，ref 是一个用于获取组件或 DOM 元素引用的特殊属性，同时也是 Composition API 中的一个响应式数据函数。
import {ref, reactive, computed, useTemplateRef, onMounted, watch} from 'vue'

// 传递 props，接收由父组件传递的参数信息
// 一个组件需要显式声明它所接受的 props，这样 Vue 才能知道外部传入的哪些是 props，哪些是透传 attribute
const props = defineProps({
  msg: String,
});




// 双向绑定
const username = ref('')



// 第一个参数必须与模板中的 ref 值匹配
const input = useTemplateRef('my-input')


onMounted(() => {
  // 聚焦到输入框
  input.value.focus()
})


</script>

<template>

  <!--  最基本的数据绑定形式是文本插值，它使用的是“Mustache”语法 (即双大括号)： -->
  <!--  双大括号标签会被替换为相应组件实例中 msg 属性的值。同时每次 msg 属性更改时它也会同步更新。-->
  <h1>{{ msg }}</h1>

  <!--虽然 Vue 的声明性渲染模型为你抽象了大部分对 DOM 的直接操作
  但在某些情况下，我们仍然需要直接访问底层 DOM 元素。
  要实现这一点，我们可以使用特殊的 ref attribute：-->
  <input ref="my-input"/>
  <router-view/>

</template>

<style scoped>
.read-the-docs {
  color: #888;
}


</style>
