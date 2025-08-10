import {createApp} from 'vue'
import App from './App.vue'
import router from './router' // 导入路由配置

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


createApp(App)
    .use(router)
    .use(ElementPlus)
    .mount('#app');

import axios from 'axios';
import {useRouter} from 'vue-router';

// 创建 Axios 实例
const service = axios.create({
    baseURL: '/api', // 根据你的代理配置调整
    timeout: 5000,
    withCredentials: true, // 如果需要携带 Cookie
});

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        // 对 2xx 响应直接返回数据
        return response.data;
    },
    (error) => {
        const router = useRouter(); // 获取路由实例

        if (error.response?.status === 401) {
            // 401 未认证，跳转到登录页
            router.push('/login');

            // 可选：清除本地 token 或用户信息
            // localStorage.removeItem('token');
        }

        // 其他错误继续抛出
        return Promise.reject(error);
    }
);

export default service;