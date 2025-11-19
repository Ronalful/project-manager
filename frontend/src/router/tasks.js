export default [
    {
        path: 'tasks',
        name: 'Tasks',
        component: () => import('@/views/tasks/TasksView.vue'),
        children: [
            {
                path: 'create',
                name: 'TaskCreate',
                component: () => import('@/views/tasks/TaskCreateView.vue'),
                meta: {requiresAdmin: true}
            },
        ]
    }
]

