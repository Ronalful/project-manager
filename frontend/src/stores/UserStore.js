import {tokenService} from "@/services/TokenService.js";
import {defineStore} from 'pinia'
import {jwtUtils} from "@/utils/jwtParser.js";
import {userAllService} from "@/services/UserAllService.js";

//поменять на USER роли
export const useUserStore = defineStore('UserStore', {
    state: () => ({
        id: null,
        firstname: '',
        lastname: '',
        email: '',
        role: '',
    }),

    persist: true, // Включить сохранение для всего хранилища

    getters: {
        isAdmin(state) {
            return state.role === 'ADMIN'
        },
        isUser(state) {
            return state.role === 'USER'
        },
        fullname(state) {
           return `${state.firstname} ${state.lastname}`.trim()
        }
    },
    actions: {
        async setAuth(accessToken, refreshToken) {

            tokenService.setTokens(accessToken, refreshToken)

            try {
                const userData = await userAllService.getMyInfo()

                if (userData.success) {
                    this.id = userData.data.id
                    this.firstname = userData.data.firstname
                    this.lastname = userData.data.lastname
                    this.email = userData.data.email
                    this.role = userData.data.role
                }
            } catch (error) {
                console.log('Error load info: ', error)
            }
        },

        clearAuth() {
            this.$reset()
            tokenService.clearTokens()
        }
    },
})