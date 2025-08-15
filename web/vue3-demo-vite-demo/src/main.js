import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 全局样式
import './styles/main.css'

// 创建应用实例
const app = createApp(App)

// 使用Pinia状态管理
app.use(createPinia())

// 使用路由
app.use(router)

// 全局组件注册
app.component('Modal', () => import('./components/Modal.vue'))

// 全局指令
app.directive('focus', {
    mounted(el) {
        el.focus()
    }
})

// 全局混入（谨慎使用）
app.mixin({
    computed: {
        $appName() {
            return 'Vue3综合示例'
        }
    }
})

// 应用实例会暴露一个 .config 对象允许我们配置一些应用级的选项，例如定义一个应用级的错误处理器，用来捕获所有子组件上的错误
app.config.errorHandler = (err) => {
    /* 处理错误 */
}

// 应用实例必须在调用了 .mount() 方法后才会渲染出来。该方法接收一个“容器”参数，可以是一个实际的 DOM 元素或是一个 CSS 选择器字符串
// 挂载应用
app.mount('#app')