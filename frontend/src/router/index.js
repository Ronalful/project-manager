import {createRouter, createWebHistory} from 'vue-router'
import HomeView from "@/views/HomeView.vue";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: HomeView // тут будет основной шаблон
    },
    {
        path: '/login',
        component: () => import('@/views/auth/AuthLayout.vue'),
        children: [
            {
                path: '',
                name: 'Login',
                component: () => import('@/views/auth/LoginView.vue'),
            },
            {
                path: 'forgot',
                name: 'ForgotPassword',
                component: () => import('@/views/auth/ForgotView.vue'),
            },
            {
                path: 'activate',
                name: 'AccountActivation',
                component: () => import('@/views/auth/ActivateView.vue'),
                meta: { requiresActivationToken: true },
            },
        ]
    },

]

const router = createRouter({
    history: createWebHistory(), // Используем HTML5 history API
    routes: routes
})

router.beforeEach((to, from, next) => {
    if (to.meta.requiresActivationToken) {
        const hasToken = sessionStorage.getItem('activation_token')
        if (!hasToken) {
            next({ name: 'Login' })
            return
        }
    }
    next()
})

export default router