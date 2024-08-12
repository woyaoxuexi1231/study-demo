// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
// main.ts
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import axios from 'axios';

const app = createApp(App);

// 设置 axios 到 Vue 的原型上
app.config.globalProperties.$axios = axios;
app.use(ElementPlus);
app.use(router);
app.mount('#app');


