import {tokenService} from "@/services/TokenService.js";
import {defineStore} from 'pinia'
import {jwtUtils} from "@/utils/jwtParser.js";
import {userAllService} from "@/services/UserAllService.js";

//поменять на USER роли
export const useUserStore = defineStore('user', {
    state: () => ({
        user: {
            id: null,
            firstname: '',
            lastname: '',
            email: '',
            role: '',
        },
        access_token: '',
        refresh_token: '',
        isAuthenticated: false,
    }),
    getters: {
        isAdmin(state) {
            return state.user.role === 'ADMIN'
        },
        isUser (state) {
            return state.user.role === 'USER'
        }
    },
    actions: {
        async setAuth(accessToken, refreshToken = null) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken

            tokenService.setTokens(this.accessToken, this.refreshToken)

            try {
                const userData = await userAllService.getMyInfo()

                if (userData.success) {
                    this.user = {
                        id: userData.data.id,
                        firstname: userData.data.firstname,
                        lastname: userData.data.lastname,
                        email: userData.data.email,
                        role: userData.data.role,
                    }
                }
            } catch (error) {
                console.log('Error load info: ', error)
            }
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
        }
    },
})