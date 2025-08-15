# Vue 3 + Vite

This template should help get you started developing with Vue 3 in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

Learn more about IDE Support for Vue in the [Vue Docs Scaling up Guide](https://vuejs.org/guide/scaling-up/tooling.html#ide-support).





## 文件结构



大部分一致，多了两个文件



- **vite.config.js** - Vite 特有的配置文件，替代了 vue.config.js：配置开发服务器、构建选项、插件等、通常比 webpack 配置更简单
- **.vscode 目录**（可选）- 包含 VS Code 编辑器的特定配置、如 settings.json、扩展推荐等





## package.json

`package.json` 在 Node.js/JavaScript 生态中相当于 Java 项目中的 **`pom.xml`（Maven）** 或 **`build.gradle`（Gradle）**，但功能更广泛，还融合了部分 `MANIFEST.MF` 和项目元数据的作用。

```json
{
  "name": "vue3-demo-vite",          // 项目名称：vue3-demo-vite
  "private": true,                   // 标记为私有项目（不会发布到npm）
  "version": "0.0.0",                // 项目版本号：0.0.0（初始版本）
  "type": "module",                  // 使用ES模块规范（而非CommonJS）

  // 定义可通过npm/yarn运行的脚本命令
  "scripts": {
    "dev": "vite",                   // 开发模式：启动Vite开发服务器
    "build": "vite build",           // 生产构建：用Vite打包项目
    "preview": "vite preview"        // 本地预览：启动生产构建的预览服务器
  },

  // 生产环境依赖
  "dependencies": {
    "vue": "^3.5.18"                 // Vue 3框架，版本要求≥3.5.18且<4.0.0
  },

  // 开发环境依赖
  "devDependencies": {
    "@vitejs/plugin-vue": "^6.0.1",  // Vite的Vue插件，用于.vue文件处理
    "vite": "^7.1.0"                 // Vite构建工具，版本要求≥7.1.0且<8.0.0
  }
}
```

