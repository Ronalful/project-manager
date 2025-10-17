import apiClient from '@/api/index.js'
import router from '@/router/index.js';
import { tokenService } from '@/services/TokenService.js'

export const authService = {
    async login({email, password}) {
        try {
            const response = await apiClient.post('/auth-api/login', {email, password})
            tokenService.setTokens(response.accessToken, response.refreshToken)
            alert('login0');
            router.push('/')
            return {success: true, data: response.data}
        } catch (error) {
            console.log('Login error:', error.response?.status, error.response?.data)
            // Проверяем, что это неактивированный аккаунт
            if (error.response?.status === 400) {
                const data = await this.initiateActivation({ email, password })

                tokenService.setTempTokens(data.accessToken, data.refreshToken)

                router.push('/login/activate')
                return {success: false, requiresActivation: true}

            } else if (error.response?.status === 403) {
                return {success: false, incorrectLoginPassword: true}
            }
            return {success: false, error: error}
        }
    },

    async logout() {
        try {
            await apiClient.post('/auth-api/logout')
            router.push('/login')
        } catch (error) {
            console.warn('Logout request failed:', error)
        } finally {
            tokenService.clearTokens()
        }
    },

    async initiateResetPassword({email, secretPhrase}) {
        try {
            const response = apiClient.post('/auth-api/initiate-reset-password', {email, secretPhrase})
            tokenService.setTempTokens(response.accessToken, response.refreshToken)
            return {success: true, data: response.data}
        } catch (error) {
            console.log('Login error:', error.response?.status, error.response?.data)

            // Неверное секретное слово
            if (error.response?.status === 400) {

                router.push('/login/activate')
                return {success: false, requiresActivation: true}

            }
            // Нет такого пользователя
            else if (error.response?.status === 403) {
                return {success: false, userDoesNotExist: true}
            }
            return {success: false, error: error}
        }
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

    async confirmActivation({secretPhrase, password}) {
        try {
            const response = await apiClient.post('/auth-api/confirm-activation', {secretPhrase, password})
            router.push('/')
            return {success: true, data: response.data}
        } catch (error) {
            console.error('Activation initiation failed:', error)
            throw error
        }
    },

    async refreshToken(){
        try {
            return await apiClient.post('/auth-api/refresh-token')
        } catch (error) {
            console.error('Error refresh token: ', error)
            throw error
        }
    },

    async isTokenValid(token){
        try {
            const response = await apiClient.get('/auth-api/is-token-valid/' + token)
            return response.status === 200
        } catch (error) {
            console.log('Token validation failed:', error)
            return false
        }
    }

}
