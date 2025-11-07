import apiClient from '@/api/index.js'
import router from '@/router/index.js';
import {tokenService} from '@/services/TokenService.js'

export const projectAdminService = {
    async getAllProjects(){
        try {
            const response = await apiClient.get('/admin/projects')
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    }
}