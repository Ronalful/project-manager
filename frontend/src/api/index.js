import axios from 'axios'

const apiClient = axios.create({
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// Интерцептор для автоматической подстановки токена
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('access_token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => Promise.reject(error)
)

// Интерцептор для обработки ошибок
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('access_token')
            // Перенаправление на логин
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default apiClient