import apiClient from '@/api/index.js'
import router from '@/router/index.js';
import {tokenService} from '@/services/TokenService.js'

export const authService = {
    async login({email, password}) {
        try {
            const response = await apiClient.post('/auth-api/login', {email, password})
            tokenService.setTokens(response.data.accessToken, response.data.refreshToken)
            router.push('/')
            return {success: true, data: response.data}
        } catch (error) {
            console.log('Login error: ', error.response?.status, error.response?.data)
            if (error.response?.status === 400) {
                // Проверяем, что это неактивированный аккаунт
                if (error.response?.data === "User is disabled") {
                    const responseActivation = await this.initiateActivation({email, password})

                    tokenService.setTempTokens(responseActivation.accessToken, responseActivation.refreshToken)

                    router.push('/login/activate')
                    return {success: false, requiresActivation: true}
                } else if (error.response?.data === "Password expired") {
                    return {success: false, passwordExpired: true}
                } else {
                    return {success: false, error: error}
                }

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
            const response = await apiClient.post('/auth-api/initiate-reset-password', {email, secretPhrase})
            tokenService.setTempTokens(response.data?.accessToken, response.data?.refreshToken)
            router.push('/login/recovery')
            return {success: true, data: response.data}
        } catch (error) {
            console.log('Initiate Reset Password error:', error.response?.status, error.response?.data)
            // Неверное секретное слово
            if (error.response?.status === 400 && error.response?.data === "Incorrect secret phrase") {
                console.log('incorrect secret')
                return {success: false, incorrectSecretPhrase: true}
            }
            // Нет такого пользователя
            else if (error.response?.status === 404 && error.response?.data === "User not found") {
                console.log('user not found')
                return {success: false, userDoesNotExist: true}
            }
            return {success: false, error: error}
        }
    },

    async confirmResetPassword({password}) {
        try {
            const response = await apiClient.post('/auth-api/confirm-reset-password', {password})
            tokenService.clearTokens()
            router.push('/login')
            return {success: true, data: response.data}
        } catch (error) {
            console.error('Confirm recovery password failed:', error)
            return {success: false, error: error}
        }
    },

    async initiateActivation({email, password}) {
        try {
            const response = await apiClient.post('/auth-api/initiate-activation', {email, password})
            return response.data
        } catch (error) {
            console.error('Activation initiation failed:', error)
            throw error
        }
    },

    async confirmActivation({secretPhrase, password}) {
        try {
            const response = await apiClient.post('/auth-api/confirm-activation', {secretPhrase, password})
            tokenService.setTokens(response.accessToken, response.refreshToken)
            return {success: true, data: response.data}
        } catch (error) {
            console.error('Activation initiation failed:', error)
            throw error
        }
    },

    async refreshToken() {
        try {
            return await apiClient.post('/auth-api/refresh-token')
        } catch (error) {
            console.error('Error refresh token: ', error)
            throw error
        }
    },

    async isTokenValid(token) {
        try {
            const response = await apiClient.get('/auth-api/is-token-valid/' + token)
            return response.status === 200
        } catch (error) {
            console.log('Token validation failed:', error)
            return false
        }
    }

}
