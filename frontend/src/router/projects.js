export default [
    {
        path: 'projects',
        children: [
            {
                path: '',
                name: 'Projects',
                component: () => import('@/views/projects/ProjectsView.vue'),
            },
            {
                path: ':id',
                children: [
                    {
                        path: '',
                        name: 'ProjectDetail',
                        component: () => import('@/views/projects/ProjectDetailView.vue'),
                    },
                    {
                        path: 'edit',
                        name: 'ProjectEdit',
                        component: () => import('@/views/projects/ProjectEditView.vue'),
                        meta: {requiresAdmin: true}
                    },
                ]
            },
            {
                path: 'create',
                name: 'ProjectCreate',
                component: () => import('@/views/projects/ProjectCreateView.vue'),
                meta: {requiresAdmin: true}
            },
        ]
    }
]

