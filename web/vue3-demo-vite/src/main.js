// main.js 是 Vue 3 应用的入口文件，负责初始化应用并挂载到 DOM


// 从vue包中导入createApp函数
// createApp是Vue 3的新API，用于创建应用实例
import {createApp} from 'vue'

// 导入全局样式文件
// 这个CSS文件会影响应用中的所有组件
import './style.css'

// 导入根组件App.vue
// 这是应用的入口组件，所有其他组件都将在这个组件内渲染
import App from './App.vue'

// 导入路由配置
// 通常这个router是从router/index.js文件中导出的路由实例
import router from './router'

// 导入Pinia状态管理库的createPinia函数
// Pinia是Vue的官方推荐状态管理库，替代了Vuex
import {createPinia} from 'pinia'
import HelloWorld from "./components/HelloWorld.vue";

// 创建Pinia实例
// 这会初始化一个全局状态管理容器
const pinia = createPinia()

// 创建Vue应用实例
const app = createApp(App);
app
    // 使用路由插件
    // 这会将路由功能集成到Vue应用中
    .use(router)

    // 使用Pinia插件
    // 这会将状态管理功能集成到Vue应用中
    .use(pinia)

    // 将应用挂载到DOM
    // '#app'对应public/index.html中的<div id="app"></div>
    .mount('#app')


// 一个 Vue 组件在使用前需要先被“注册”，这样 Vue 才能在渲染模板时找到其对应的实现。组件注册有两种方式：全局注册和局部注册。
// 可以使用 Vue 应用实例的 .component() 方法，让组件在当前 Vue 应用中全局可用。
app.component(
    // 注册的名字
    'MyComponent',
    // 组件的实现
    {
        /* ... */
    }
)
