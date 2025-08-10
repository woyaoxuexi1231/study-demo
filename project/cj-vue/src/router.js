import {createRouter, createWebHistory} from 'vue-router'
import Login from './components/LoginPage.vue'
import Home from './components/DashboardPage.vue'
import axios from 'axios'
import SeckillPage from "@/components/SeckillPage.vue";

const routes = [
    {
        path: '/',
        component: Home
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        children: [
            {
                path: 'seckill',  // 注意这里没有前导斜杠
                name: 'Seckill',
                component: SeckillPage
                // 可以在这里添加更多子路由
            }
            // 可以添加更多子路由...
        ]
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach(async (to,
                         from,
                         next) => {

    if (to.path === '/login') {
        console.log(1)
        return next()
    }

    try {
        console.log(2)
        const res = await axios.get('/api/user-check') // 判断是否已登录
        if (res.status === 200) {
            console.log(3)
            next()
        } else {
            console.log(4)
            next('/login')
        }
    } catch (e) {
        next('/login')
    }
})

export default router
