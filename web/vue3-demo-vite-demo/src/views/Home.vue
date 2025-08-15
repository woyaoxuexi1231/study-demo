<script setup>
import { ref, onBeforeUnmount } from 'vue'
import { useCounterStore } from '../store/counter'

const counterStore = useCounterStore()
const newName = ref('')

const timer = setInterval(() => {
  console.log('定时器运行中...')
}, 5000)

onBeforeUnmount(() => {
  clearInterval(timer)
  console.log('Home组件即将卸载，已清除定时器')
})
</script>

<template>
  <div class="home">
    <h1>欢迎来到{{ counterStore.name }}应用</h1>

    <div class="name-editor">
      <input v-model="newName" placeholder="输入新名称" />
      <button @click="counterStore.setName(newName)">
        更新应用名称
      </button>
    </div>

    <div class="features">
      <div class="feature">
        <h3>组件通信</h3>
        <p>展示父子组件、兄弟组件间的通信方式</p>
      </div>

      <div class="feature">
        <h3>状态管理</h3>
        <p>使用Pinia管理全局状态</p>
      </div>

      <div class="feature">
        <h3>路由</h3>
        <p>基于Vue Router的页面导航</p>
      </div>

      <div class="feature">
        <h3>组合式API</h3>
        <p>使用setup语法糖组织代码逻辑</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home {
  text-align: center;
  padding: 20px;
}

.name-editor {
  margin: 20px 0;
}

.features {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.feature {
  flex: 1 1 300px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
</style>