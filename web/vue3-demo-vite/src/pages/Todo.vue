<template>
  <h2>待办事项</h2>
  <input v-model="newTodo" @keyup.enter="addTodo" placeholder="输入任务">
  <ul>
    <li v-for="(todo, i) in todos" :key="i">
      <input type="checkbox" v-model="todo.done">
      <span :style="{ textDecoration: todo.done ? 'line-through' : '' }">
        {{ todo.text }}
      </span>
      <button @click="remove(i)">删除</button>
    </li>
  </ul>
</template>

<script setup>
import { ref } from 'vue'

const newTodo = ref('')
const todos = ref([])

function addTodo() {
  if (newTodo.value.trim()) {
    todos.value.push({ text: newTodo.value, done: false })
    newTodo.value = ''
  }
}

function remove(index) {
  todos.value.splice(index, 1)
}
</script>
