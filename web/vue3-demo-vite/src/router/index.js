import {createRouter, createWebHistory} from 'vue-router'
import Home from '../pages/Home.vue'
import About from '../pages/About.vue'
import Todo from "../pages/Todo.vue";
import Ref from "../components/Ref.vue";
import ComputedProperties from "../components/computedProperties.vue";
import ClassAndStyleBindings from "../components/ClassAndStyleBindings.vue";
import ConditionalRendering from "../components/ConditionalRendering.vue";
import ListRendering from "../components/ListRendering.vue";
import EventHanding from "../components/EventHanding.vue";
import FormInputBindings from "../components/FormInputBindings.vue";
import Watchers from "../components/Watchers.vue";
import TemplateRefs from "../components/TemplateRefs.vue";

const routes = [
    {path: '/', component: Home},
    {path: '/about', component: About},
    {path: '/todo', component: Todo},
    {path: '/ref', component: Ref},
    {path: '/computed-properties', component: ComputedProperties},
    {path: '/class-and-style-bindings', component: ClassAndStyleBindings},
    {path: '/conditional-rendering', component: ConditionalRendering},
    {path: '/list-rendering', component: ListRendering},
    {path: '/event-handing', component: EventHanding},
    {path: '/form-input-bindings', component: FormInputBindings},
    {path: '/watchers', component: Watchers},
    {path: '/template-refs', component: TemplateRefs},
]

export default createRouter({
    history: createWebHistory(),
    routes
})
