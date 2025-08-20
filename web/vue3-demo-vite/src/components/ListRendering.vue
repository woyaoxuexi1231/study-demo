<script setup>
import {ref} from "vue";

const todos = ref([
  {id: 1, text: '学习 Vue.js', completed: false},
  {id: 2, text: '完成项目', completed: false},
  {id: 3, text: '购物', completed: false},
  {id: 4, text: '健身', completed: false}
])
const selectedLanguages = ref([])
</script>

<template>

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
</template>

<style scoped>

.completed {
  text-decoration: line-through;
  color: #999;
}
</style>