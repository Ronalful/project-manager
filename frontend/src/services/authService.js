import apiClient from '@/api'

export const authService = {
    login(data) {
        return apiClient.post('/auth-api/login', data)
    },

    logout() {
        return apiClient.post('/auth-api/logout')
    },

    initiateResetPassword(data){
        return apiClient.post('/auth-api/initiate-reset-password', data)
    },

    confirmResetPassword(data){
        return apiClient.post('/auth-api/confirm-reset-password', data)
    },

    initiateActivation(data){
        return apiClient.post('/auth-api/initiate-activation', data)
    },

    confirmActivation(data){
        return apiClient.post('/auth-api/confirm-activation', data)
    },

}
