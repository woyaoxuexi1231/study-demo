import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    // 这表示开发服务器将监听所有的网络接口。这意味着无论你的计算机有多少个网络适配器，开发服务器都会在每个适配器上监听指定的端口。
    host: '0.0.0.0',
    port: 10098,
    // 是否开启 https
    https: false,
    // 设置反向代理，跨域
    proxy: {
      '/api': {
        // 后台地址
        target: 'http://127.0.0.1:10001/',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      },
      '/api2': {
        // 后台地址
        target: 'http://127.0.0.1:8956/',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api2/, '')
      }
    }
  },
})
