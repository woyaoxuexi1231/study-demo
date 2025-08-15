// vite.config.js 是 Vite 构建工具 的配置文件，用于自定义 Vite 的行为（如插件、开发服务器、构建输出等）

// 1. 从 Vite 导入 `defineConfig` 辅助函数
import { defineConfig } from 'vite'

// 2. 从 Vite 的 Vue 插件导入 `vue` 插件（用于处理 .vue 文件）
import vue from '@vitejs/plugin-vue'

// 3. 导出 Vite 的配置对象（使用 `defineConfig` 包裹以获得类型提示）
// https://vite.dev/config/
export default defineConfig({
  // 4. 配置插件：这里添加了 Vue 插件
  plugins: [vue()],  // 启用 Vue 单文件组件（SFC）支持
})
