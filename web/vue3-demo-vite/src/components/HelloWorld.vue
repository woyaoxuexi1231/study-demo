<!--<script setup>：Vue3 推荐写法，代码更简洁。-->
<script setup>
// ref：创建响应式数据（类似 React 的 useState）
// 在 Vue 中，ref 是一个用于获取组件或 DOM 元素引用的特殊属性，同时也是 Composition API 中的一个响应式数据函数。
import {ref, reactive, computed, useTemplateRef, onMounted} from 'vue'

// 传递 props，接收由父组件传递的参数信息
// 一个组件需要显式声明它所接受的 props，这样 Vue 才能知道外部传入的哪些是 props，哪些是透传 attribute
const props = defineProps({
  msg: String,
});

// 当一个组件首次渲染时，Vue 会追踪在渲染过程中使用的每一个 ref。然后，当一个 ref 被修改时，它会触发追踪它的组件的一次重新渲染。
// 普通变量是无法被检测的
const count = ref(0)
const name = ref('小明') // 基础类型
const user = reactive({age: 20, name: '小红'}) // 对象类型

const author = reactive({
  name: 'John Doe',
  books: [
    'Vue 2 - Advanced Guide',
    'Vue 3 - Basic Guide',
    'Vue 4 - The Mystery'
  ]
})

// 一个计算属性 ref
// 计算属性值会基于其响应式依赖被缓存
// 一个计算属性仅会在其响应式依赖更新时才重新计算。这意味着只要 author.books 不改变，无论多少次访问 publishedBooksMessage 都会立即返回先前的计算结果，而不用重复执行 getter 函数。
// computed() 接受一个 getter 函数，返回一个只读的响应式 ref 对象。
const publishedBooksMessage = computed(() => {
  return author.books.length > 0 ? 'Yes' : 'No'
})

// 双向绑定
const username = ref('')

const todos = ref([
  {id: 1, text: '学习 Vue.js', completed: false},
  {id: 2, text: '完成项目', completed: false},
  {id: 3, text: '购物', completed: false},
  {id: 4, text: '健身', completed: false}
])
const selectedLanguages = ref([])

// 第一个参数必须与模板中的 ref 值匹配
const input = useTemplateRef('my-input')

// vue的生命周期 https://cn.vuejs.org/guide/essentials/lifecycle.html··
onMounted(() => {
  // 聚焦到输入框
  input.value.focus()
})

</script>

<template>

  <!--  最基本的数据绑定形式是文本插值，它使用的是“Mustache”语法 (即双大括号)： -->
  <!--  双大括号标签会被替换为相应组件实例中 msg 属性的值。同时每次 msg 属性更改时它也会同步更新。-->
  <h1>{{ msg }}</h1>

  <div>
    <h2>待办事项</h2>
    <div v-for="(item, index) in todos" :key="item.id" class="todo-item">
      <!--复选框的v-model行为在绑定到不同的数据结构上时会呈现不同的差异-->
      <!--当 v-model 用在 checkbox + 数组 时，Vue 会自动把 选中的 checkbox 的 value 值 加入到数组，取消选中时则移除。-->
      <!--当 v-model 用在 checkbox + 单个布尔值 时，它会把布尔值当作该 checkbox 的选中状态。-->
      <input
          type="checkbox"
          :id="'todo-' + item.id"
          :value="item.text"
          v-model="selectedLanguages">
      <!-- label的for标签用于关联表单控件（如复选框、单选框、输入框等）      -->
      <!-- :class 样式绑定 -->
      <label
          :for="'todo-' + item.id"
          :class="{ 'completed': selectedLanguages.includes(item.text) }">
        {{ item.text }}
      </label>
    </div>
    已完成：{{ selectedLanguages }}
  </div>

  <!--条件渲染，-->
  <p v-if="user.age >= 18">小红今年 {{ user.age }} 岁，成年人了</p>
  <p v-else>小红今年 {{ user.age }} 岁，还是个未成年</p>

  <div class="card">
    <!--@click：事件绑定（相当于 v-on:click）。-->
    <!--{{ }}：插值语法，用来显示变量。-->
    事件绑定
    <button type="button" @click="count++">count is {{ count }}</button>
  </div>

  <!--v-model 是双向绑定，它既能把数据赋给控件，又能在用户操作时更新数据。-->
  <input v-model="username" placeholder="输入用户名">
  <p>你输入的是：{{ username }}</p>

  <!--虽然 Vue 的声明性渲染模型为你抽象了大部分对 DOM 的直接操作
  但在某些情况下，我们仍然需要直接访问底层 DOM 元素。
  要实现这一点，我们可以使用特殊的 ref attribute：-->
  <input ref="my-input"/>

  <nav>
    <router-link to="/">首页</router-link>
    |
    <router-link to="/about">关于</router-link>
    |
    <router-link to="/todo">实战小项目（Todo List）</router-link>
  </nav>
  <router-view/>

</template>

<style scoped>
.read-the-docs {
  color: #888;
}

.completed {
  text-decoration: line-through;
  color: #999;
}

</style>
