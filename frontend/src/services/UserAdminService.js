import apiClient from '@/api/index.js'
export const userAdminService = {
    async getAllUsers(){
        try {
            const response = await apiClient.get('/admin/users')
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    },

    async getAllDevelopers(){
        try {
            const response = await apiClient.get('/admin/users' + '?role=USER')
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    },
}