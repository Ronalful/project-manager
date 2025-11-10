import {tokenService} from "@/services/TokenService.js";
import { defineStore } from 'pinia'
import {jwtUtils} from "@/utils/jwtParser.js";

//поменять на USER роли
export const useUserStore = defineStore('user', {
    state: () => ({
        user: {
            id: null,
            firstname: '',
            lastname: '',
            email: '',
            role: 'ADMIN',
        },
        access_token: '',
        refresh_token: '',
        isAuthenticated: false,
    }),
    getters: {
        //isAdmin: (state) => state.user.role === 'ADMIN',
        isAdmin(state){
            console.log(state.user.role)
            return state.user.role === 'ADMIN'
        },
        isUser: (state) => state.user.role === 'USER'
    },
    actions: {
        setAuth(accessToken, refreshToken = null) {

            const userData = jwtUtils.getUserFromToken(accessToken)

            if(userData){
                this.user = {
                    email: userData.email,
                    role: userData.role || 'ADMIN',
                }
            }

            this.accessToken = accessToken
            this.refreshToken = refreshToken

            tokenService.setTokens(this.accessToken, this.refreshToken)

            this.isAuthenticated = true

            localStorage.setItem('user', JSON.stringify(userData))
        },

        clearAuth() {
            this.user = {
                id: null,
                firstname: '',
                lastname: '',
                email: '',
                role: 'USER',
            }
            this.accessToken = null
            this.refreshToken = null
            this.isAuthenticated = false

            tokenService.clearTokens()
            localStorage.removeItem('user')
        }
    },
})