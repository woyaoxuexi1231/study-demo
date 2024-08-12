import { createRouter, createWebHistory } from 'vue-router'
import HelloWorld from '../components/HelloWorld.vue'
import EmployeeTable from "../components/EmployeeTable.vue";
import Calendar from "../components/Calendar.vue";
import MessageQueue from "../components/MessageQueue.vue";
import WebSocket from "../components/WebSocket.vue";
import WebSocketStomp from "../components/WebSocketStomp.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
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
    },
    {
      path: '/webSocketStomp',
      name: 'WebSocketStomp',
      component: WebSocketStomp
    }
  ]
})

export default router
