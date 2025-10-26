export default [
    {
        path: '/login',
        component: () => import('@/layouts/AuthLayout.vue'),
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
    }
]
