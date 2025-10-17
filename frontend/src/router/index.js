import {createRouter, createWebHistory} from 'vue-router'
import HomeView from "@/views/HomeView.vue";
import {tokenService} from "@/services/TokenService.js";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: HomeView,
        meta: {requiresAuth: true}
    },
    {
        path: '/login',
        component: () => import('@/views/auth/AuthLayout.vue'),
        meta: {requiresGuest: true},
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
                path: 'recovery',
                name: 'RecoveryPassword',
                component: () => import('@/views/auth/RecoveryView.vue'),
                meta: {requiresActivationToken: true},
            },
            {
                path: 'activate',
                name: 'AccountActivation',
                component: () => import('@/views/auth/ActivateView.vue'),
                meta: {requiresActivationToken: true},
            },
        ]
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

router.beforeEach((to, from, next) => {
    const isAuthenticated = tokenService.isAuthenticated()
    const isTempAuth = tokenService.isUsingTempTokens()

    if (to.meta.requiresAuth && !isAuthenticated) {
        next({
            name: 'Login',
            query: {redirect: to.fullPath}
        })
        return
    }

    if (to.meta.requiresGuest && isAuthenticated) {
        next({name: 'Home'})
        return
    }

    if(to.meta.requiresActivationToken && !isTempAuth){
        next({name: 'Login'})
        return
    }

    next()
})

export default router