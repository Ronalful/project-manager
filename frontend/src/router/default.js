
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
            },
            {
                path: 'projects',
                children:[
                    {
                        path: '',
                        name: 'Projects',
                        component: () => import('@/views/projects/ProjectsView.vue'),
                    },
                    {
                        path: ':id/',
                        children:[
                            {
                                path: '',
                                name: 'ProjectDetail',
                                component: () => import('@/views/projects/ProjectDetailView.vue'),
                            },
                            {
                                path: 'edit/',
                                name: 'ProjectEdit',
                                component: () => import('@/views/projects/ProjectEditView.vue'),
                                meta: {requiresAdmin: true}
                            }
                        ]
                    },
                ],
            },
        ]
    },
]