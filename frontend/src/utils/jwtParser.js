// utils/jwtUtils.js
import { jwtDecode } from 'jwt-decode'

export const jwtUtils = {
    /**
     * Декодирует JWT токен с помощью jwt-decode
     */
    decodeToken(token) {
        try {
            return jwtDecode(token)
        } catch (error) {
            console.error('Failed to decode JWT token:', error)
            return null
        }
    },

    /**
     * Проверяет expiration токена
     */
    isTokenExpired(token) {
        try {
            const decoded = this.decodeToken(token)
            if (!decoded || !decoded.exp) return true
            return Date.now() >= decoded.exp * 1000
        } catch (error) {
            return true
        }
    },

    /**
     * Получает данные пользователя из токена
     */
    getUserFromToken(token) {
        const decoded = this.decodeToken(token)

        if (!decoded) return null

        return {
            id: null,
            firstname: '',
            lastname: '',
            email: decoded.sub,
            role: decoded.roles[0].authority
        }
    },
}