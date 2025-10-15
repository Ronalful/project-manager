import apiClient from '@/api'
import router from '@/router/';

export const authService = {
    async login({email, password}) {
        try {
            const response = await apiClient.post('/auth-api/login', {email, password})
            router.push('/')
            return {success: true, data: response.data}
        } catch (error) {
            console.log('Login error:', error.response?.status, error.response?.data)
            // Проверяем, что это ошибка активации
            if (error.response?.status === 400) {
                localStorage.setItem('pending_activation_email', email)
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

    initiateActivation({email, password}) {
        return apiClient.post('/auth-api/initiate-activation', {email, password})
    },

    confirmActivation({secret, password}) {
        return apiClient.post('/auth-api/confirm-activation', {secret, password})
    },

}
