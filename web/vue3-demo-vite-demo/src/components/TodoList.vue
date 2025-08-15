<script setup>
import { ref, watch, onMounted } from 'vue'
import { useLocalStorage } from '@vueuse/core'

const props = defineProps({
  initialTodos: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['todo-added', 'todo-deleted'])

const todos = useLocalStorage('todos', props.initialTodos)
const newTodo = ref('')
const filter = ref('all')

const filteredTodos = computed(() => {
  switch (filter.value) {
    case 'active':
      return todos.value.filter(todo => !todo.completed)
    case 'completed':
      return todos.value.filter(todo => todo.completed)
    default:
      return todos.value
  }
})

function addTodo() {
  if (newTodo.value.trim()) {
    todos.value.push({
      id: Date.now(),
      title: newTodo.value.trim(),
      completed: false
    })
    emit('todo-added', newTodo.value)
    newTodo.value = ''
  }
}

function deleteTodo(id) {
  todos.value = todos.value.filter(todo => todo.id !== id)
  emit('todo-deleted', id)
}

function toggleTodo(todo) {
  todo.completed = !todo.completed
}

watch(todos, (newVal) => {
  console.log('待办事项已更新:', newVal)
}, { deep: true })

onMounted(() => {
  console.log('TodoList组件已挂载')
})
</script>

<template>
  <div class="todo-list">
    <h2>待办事项</h2>

    <form @submit.prevent="addTodo">
      <input
          v-model="newTodo"
          placeholder="添加新任务..."
          v-focus
      />
      <button type="submit">添加</button>
    </form>

    <div class="filters">
      <button
          v-for="f in ['all', 'active', 'completed']"
          :key="f"
          :class="{ active: filter === f }"
          @click="filter = f"
      >
        {{ f }}
      </button>
    </div>

    <ul v-if="filteredTodos.length">
      <li v-for="todo in filteredTodos" :key="todo.id">
        <input
            type="checkbox"
            :checked="todo.completed"
            @change="toggleTodo(todo)"
        />
        <span :class="{ completed: todo.completed }">{{ todo.title }}</span>
        <button @click="deleteTodo(todo.id)">删除</button>
      </li>
    </ul>
    <p v-else>暂无待办事项</p>
  </div>
</template>

<style scoped>
.todo-list {
  max-width: 500px;
  margin: 0 auto;
}

.completed {
  text-decoration: line-through;
  color: #888;
}

.filters button.active {
  font-weight: bold;
  color: blue;
}
</style>