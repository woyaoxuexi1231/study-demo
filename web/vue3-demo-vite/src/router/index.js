import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Home.vue'
import About from '../pages/About.vue'
import Todo from "../pages/Todo.vue";

const routes = [
    { path: '/', component: Home },
    { path: '/about', component: About },
    {path: '/todo', component: Todo}
]

export default createRouter({
    history: createWebHistory(),
    routes
})
