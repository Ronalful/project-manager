
export default [
    {
        path: '/',
        name: 'Default',
        component: () => import('@/layouts/DefaultLayuot.vue'),
        meta: {requiresAuth: true},
        children:[
            {
                path: '',
                name: 'Home',
                component: () => import('@/views/HomeView.vue')
            }
        ]
    },
]