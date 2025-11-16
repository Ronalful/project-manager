import apiClient from '@/api'
import {authService as AuthServise, authService} from "@/services/AuthService.js";

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

    getTempAccessToken() {
        return sessionStorage.getItem('temp_access_token')
    }

    getTempRefreshToken() {
        return sessionStorage.getItem('temp_refresh_token')
    }

    getRegularAccessToken() {
        return localStorage.getItem('access_token')
    }

    getRegularRefreshToken() {
        return localStorage.getItem('refresh_token')
    }

    getAccessToken() {
        return this.getTempAccessToken() ? this.getTempAccessToken() : this.getRegularAccessToken()
    }

    getRefreshToken() {
        return this.getTempRefreshToken() ? this.getTempRefreshToken() : this.getRegularRefreshToken()
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
        const token = this.getRegularAccessToken()

        if (!token) return false
        else return true
    }

    isUsingTempTokens() {
        const token = this.getTempAccessToken()

        if (!token) return false
        else return true
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