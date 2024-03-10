// 这里使用 ES6 的模块导入语法，从 Vue 3 的包中导入 createApp 方法。createApp 是用于创建一个 Vue 应用实例的工厂函数。
import {createApp} from 'vue'
// 这里导入了一个名为 App 的组件，通常是应用程序的根组件。.vue 文件是单文件组件 (Single File Component) 的扩展，其中包含了组件的模板、脚本和样式。
import App from './App.vue'
// createApp(App) 创建了一个 Vue 应用实例，并将根组件 App 传递给了这个实例。
// .mount('#app') 将应用实例挂载到页面上的具有 id="app" 的 DOM 元素上。这意味着 Vue 应用将渲染到页面上具有 id="app" 的元素内部。
createApp(App).mount('#app')
