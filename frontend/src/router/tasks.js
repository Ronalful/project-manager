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
            {
                path: ':id',
                children: [
                    {
                        path: '',
                        name: 'TaskDetail',
                        component: () => import('@/views/tasks/TaskDetailView.vue'),
                    },
                    {
                        path: 'edit',
                        name: 'TaskEdit',
                        component: () => import('@/views/tasks/TaskEditView.vue'),
                        meta: {requiresAdmin: true}
                    },
                ]
            },
        ]
    },
    {
        path: 'kanban',
        name: 'KanbanMain',
        component: () => import('@/views/tasks/KanbanView_test.vue'),
        children: [
            {
                path: ':id',
                name: 'Kanban',
                component: () => import('@/views/tasks/KanbanView.vue'),
            },
        ]
    }
]

