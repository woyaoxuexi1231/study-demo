import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '首页' }
    },
    {
        path: '/about',
        name: 'About',
        component: () => import('../views/About.vue'),
        meta: { title: '关于', requiresAuth: false }
    },
    {
        path: '/todos',
        name: 'Todos',
        component: () => import('../views/Todos.vue'),
        meta: { title: '待办事项' }
    },
    {
        path: '/users',
        name: 'Users',
        component: () => import('../views/Users.vue'),
        meta: { title: '用户列表' }
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return { top: 0 }
        }
    }
})

// 路由守卫
// router.beforeEach((to, from, next) => {
//     document.title = `${to.meta.title} | ${process.env.VUE_APP_TITLE}`
//     next()
// })

router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | ${import.meta.env.VITE_APP_TITLE}`;
    next();
});

export default router