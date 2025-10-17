import axios from 'axios'
import {tokenService} from "@/services/TokenService.js";

const apiClient = axios.create({
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

const NON_AUTH_ENDPOINTS = [
    '/auth-api/initiate-reset-password',
    '/auth-api/confirm-reset-password',
    '/auth-api/confirm-activation',
    '/auth-api/initiate-activation',
]

// Интерцептор для автоматической подстановки токена
apiClient.interceptors.request.use(
    (config) => {
        const token = tokenService.getAccessToken()
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