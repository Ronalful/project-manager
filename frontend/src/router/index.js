import {createRouter, createWebHistory} from 'vue-router'
import HomeView from "@/views/HomeView.vue";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: HomeView
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/login/LoginView.vue'),
    },
    {
        path: '/login/forgot',
        name: 'ForgotPassword',
        component: () => import('../views/login/ForgotView.vue'),
    },
]

const router = createRouter({
    history: createWebHistory(), // Используем HTML5 history API
    routes: routes
})

export default router