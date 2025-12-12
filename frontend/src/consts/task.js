export const TASK_STATUS = {
    PENDING: { id: 'pending', label: 'В ожидании', color: '#FFA500' },
    IN_PROGRESS: { id: 'in_progress', label: 'В работе', color: '#3498DB' },
    COMPLETED: { id: 'completed', label: 'Завершено', color: '#2ECC71' },
    BLOCKED: { id: 'blocked', label: 'Заблокировано', color: '#E74C3C' }
}

export const TASK_PRIORITY = {
    LOW: { id: 'low', label: 'Низкий', color: '#2ECC71', level: 1 },
    MEDIUM: { id: 'medium', label: 'Средний', color: '#F39C12', level: 2 },
    HIGH: { id: 'high', label: 'Высокий', color: '#E74C3C', level: 3 },
    CRITICAL: { id: 'critical', label: 'Критический', color: '#8B0000', level: 4 }
}