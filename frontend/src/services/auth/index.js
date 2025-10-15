import apiClient from '@/api/index.js'
import router from '@/router';

export const authService = {
    async login({email, password}) {
        try {
            const response = await apiClient.post('/auth-api/login', {email, password})
            localStorage.access_token = response.data.accessToken
            router.push('/')
            return {success: true, data: response.data}
        } catch (error) {
            console.log('Login error:', error.response?.status, error.response?.data)
            // Проверяем, что это неактивированный аккаунт
            if (error.response?.status === 400) {
                const data = await this.initiateActivation({ email, password })

                sessionStorage.setItem('activation_token', data.accessToken) // Токен удалится при закрытии вкладки
                localStorage.setItem('refresh_token', data.refreshToken) // Долгосрочный
                sessionStorage.setItem('pending_activation_email', email)

                router.push('/login/activate')
                return {success: false, requiresActivation: true}

            } else if (error.response?.status === 403) {
                return {success: false, incorrectLoginPassword: true}
            }
            return {success: false, error: error}
        }
    },

    logout() {
        return apiClient.post('/auth-api/logout')
    },

    initiateResetPassword({email, secret}) {
        return apiClient.post('/auth-api/initiate-reset-password', {email, secret})
    },

    confirmResetPassword({password}) {
        return apiClient.post('/auth-api/confirm-reset-password', {password})
    },

    async initiateActivation({email, password}) {
        try {
            const response = await apiClient.post('/auth-api/initiate-activation', { email, password })
            return response.data
        } catch (error) {
            console.error('Activation initiation failed:', error)
            throw error
        }
    },

    async confirmActivation({secret, password}) {
        try {
            const response = await apiClient.post('/auth-api/confirm-activation', {secret, password})
            router.push('/')
            return {success: true, data: response.data}
        } catch (error) {
            console.error('Activation initiation failed:', error)
            throw error
        }
    },

}
