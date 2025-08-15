<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCounterStore } from '../store/counter'

const route = useRoute()
const router = useRouter()
const counterStore = useCounterStore()

const routes = [
  { path: '/', name: '首页' },
  { path: '/todos', name: '待办事项' },
  { path: '/users', name: '用户列表' },
  { path: '/about', name: '关于' }
]

const isActive = (path) => route.path === path
</script>

<template>
  <header class="app-header">
    <div class="logo">
      <router-link to="/">
        {{ counterStore.name }} App
      </router-link>
    </div>

    <nav>
      <router-link
          v-for="r in routes"
          :key="r.path"
          :to="r.path"
          :class="{ active: isActive(r.path) }"
      >
        {{ r.name }}
      </router-link>
    </nav>

    <div class="counter">
      <button @click="counterStore.increment">
        计数: {{ counterStore.count }} (×2: {{ counterStore.doubleCount }})
      </button>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 20px;
  background-color: #2c3e50;
  color: white;
  margin-bottom: 20px;
}

.logo a {
  color: white;
  text-decoration: none;
  font-weight: bold;
  font-size: 1.2rem;
}

nav a {
  color: #bdc3c7;
  text-decoration: none;
  margin-left: 15px;
  padding: 5px 10px;
  border-radius: 4px;
}

nav a.active {
  color: white;
  background-color: #34495e;
}

.counter button {
  padding: 5px 10px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>