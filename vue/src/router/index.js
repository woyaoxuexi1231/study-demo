import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import EmployeeTable from "../components/EmployeeTable";
import Calendar from "../components/Calendar";
import MessageQueue from "../components/MessageQueue";
import WebSocket from "../components/WebSocket";

Vue.use(Router)

export default new Router({
    /*
    * 1. 创建路由组件
    * 2. 将路由与组件进行映射
    * 3. 创建router示例
    * */
    routes: [
        {
            path: '/',
            name: 'HelloWorld',
            component: HelloWorld
        },
        {
            path: '/employeeTable',
            name: 'EmployeeTable',
            component: EmployeeTable
        },
        {
            path: '/calendar',
            name: 'Calendar',
            component: Calendar
        },
        {
            path: '/messageQueue',
            name: 'MessageQueue',
            component: MessageQueue
        },
        {
            path: '/webSocket',
            name: 'WebSocket',
            component: WebSocket
        }
    ]
})
