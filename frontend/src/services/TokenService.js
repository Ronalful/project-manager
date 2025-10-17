import apiClient from '@/api'
import {authService} from "@/services/AuthService.js";

class TokenService{
    setTokens(accessToken, refreshToken) {
       this.setAccessToken(accessToken)
       this.setRefreshToken(refreshToken)
    }

    setAccessToken(accessToken) {
        localStorage.setItem('access_token', accessToken)
    }

    setRefreshToken(refreshToken) {
        localStorage.setItem('refresh_token', refreshToken)
    }

    getAccessToken() {
        return localStorage.getItem('access_token')
    }

    getRefreshToken() {
        return localStorage.getItem('refresh_token')
    }

    clearTokens() {
        localStorage.removeItem('access_token')
        localStorage.removeItem('refresh_token')
    }

    isAuthenticated() {
        return !!this.getAccessToken()
    }

    async checkTokenValidity() {
        const token = this.getAccessToken()
        if (!token) return false

        return await authService.isTokenValid();
    }

    async refreshAccessToken(){
        const refreshToken = this.getRefreshToken()
        if (!refreshToken) throw new Error('No refresh token')

        try {
            const response = await authService.refreshToken();
            this.setAccessToken(response.accessToken)
        } catch (error) {
            this.clearTokens()
            throw error
        }
    }

    async updateTokenIfIsInvalid(){
        if(! await this.checkTokenValidity()){
            await this.refreshAccessToken()
        }
        return true
    }
}

export const tokenService = new TokenService()