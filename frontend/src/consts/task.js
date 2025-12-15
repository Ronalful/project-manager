export const TASK_STATUS = {
    NOT_STARTED: { id: 'NOT_STARTED', label: 'Открыта', color: '#717679' },
    EVALUATION: { id: 'EVALUATION', label: 'В оценке', color: '#c8b839' },
    IN_PROGRESS: { id: 'IN_PROGRESS', label: 'В работе', color: '#DF3873' },
    IN_TEST: { id: 'IN_TEST', label: 'В тестировании', color: '#64F5E5' },
    COMPLETED: { id: 'COMPLETED', label: 'Завершено', color: '#20DEB6' },
}

export const TASK_PRIORITY = {
    NONE: {id: 'NONE', label: 'Не назначен', color: '#717679', level: 0},
    LOW: { id: 'low', label: 'Низкий', color: '#64F5E5', level: 1 },
    MEDIUM: { id: 'medium', label: 'Средний', color: '#20DEB6', level: 2 },
    HIGH: { id: 'high', label: 'Высокий', color: '#c8b839', level: 3 },
    CRITICAL: { id: 'critical', label: 'Критический', color: '#DF3873', level: 4 }
}