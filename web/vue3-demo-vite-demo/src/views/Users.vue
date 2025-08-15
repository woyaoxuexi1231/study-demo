<script setup>
import { ref } from 'vue'
import { useFetch } from '../composables/useFetch'
import UserCard from '../components/UserCard.vue'
import Modal from '../components/Modal.vue'

const { data: users, error, loading, fetchData } = useFetch(
    'https://jsonplaceholder.typicode.com/users'
)

const selectedUser = ref()
const showModal = ref(false)

function showUserDetails(user) {
  selectedUser.value = user
  showModal.value = true
}

// 初始加载数据
fetchData()
</script>

<template>
  <div class="users-page">
    <h1>用户列表</h1>

    <button @click="fetchData" :disabled="loading">
      {{ loading ? '加载中...' : '刷新数据' }}
    </button>

    <div v-if="error" class="error">
      加载失败: {{ error }}
    </div>

    <div v-else-if="loading" class="loading">
      加载中...
    </div>

    <div v-else class="user-grid">
      <UserCard
          v-for="user in users"
          :key="user.id"
          :user="user"
          @click="showUserDetails(user)"
      />
    </div>

    <Modal v-model="showModal" title="用户详情">
      <div v-if="selectedUser">
        <h3>{{ selectedUser.name }}</h3>
        <p><strong>邮箱:</strong> {{ selectedUser.email }}</p>
        <p><strong>电话:</strong> {{ selectedUser.phone }}</p>
        <p><strong>公司:</strong> {{ selectedUser.company.name }}</p>
      </div>
    </Modal>
  </div>
</template>

<style scoped>
.user-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.loading, .error {
  padding: 20px;
  text-align: center;
  font-size: 18px;
}

.error {
  color: red;
}
</style>