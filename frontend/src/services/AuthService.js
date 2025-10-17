import apiClient from '@/api/index.js'
import router from '@/router/index.js';
import { tokenService } from '@/services/TokenService.js'

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

                tokenService.setTokens(data.accessToken, data.refreshToken)

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

    async isTokenValid(){
        try {
            const response = await apiClient.get('/auth-api/is-token-valid')
            return response.status === 200
        } catch (error) {
            console.warn('Token validation failed:', error)
            return false
        }
    }

}
