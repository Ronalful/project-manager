import apiClient from '@/api'
import {authService} from "@/services/AuthService.js";

class TokenService{
    setTempTokens(accessToken, refreshToken) {
        sessionStorage.setItem('temp_access_token', accessToken)
        sessionStorage.setItem('temp_refresh_token', refreshToken)
    }

    setTokens(accessToken, refreshToken) {
       this.setAccessToken(accessToken)
       this.setRefreshToken(refreshToken)

       this.clearTempTokens()
    }

    setAccessToken(accessToken) {
        localStorage.setItem('access_token', accessToken)
    }

    setRefreshToken(refreshToken) {
        localStorage.setItem('refresh_token', refreshToken)
    }

    getAccessToken() {
        const tempToken = sessionStorage.getItem('temp_access_token')
        if (tempToken) return tempToken

        return localStorage.getItem('access_token')
    }

    getRefreshToken() {
        const tempToken = sessionStorage.getItem('temp_refresh_token')
        if (tempToken) return tempToken

        return localStorage.getItem('refresh_token')
    }

    clearTokens() {
        this.clearTempTokens()

        localStorage.removeItem('access_token')
        localStorage.removeItem('refresh_token')
    }

    clearTempTokens() {
        sessionStorage.removeItem('temp_access_token')
        sessionStorage.removeItem('temp_refresh_token')
    }

    isAuthenticated() {
        const token = this.getAccessToken()

        if (!token) {
            return false
        }

        return true
    }

    isUsingTempTokens() {
        return !!sessionStorage.getItem('temp_access_token')
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
        if(! await this.isAuthenticated()){
            await this.refreshAccessToken()
        }
        return true
    }
}

export const tokenService = new TokenService()