import apiClient from '@/api/index.js'

export const projectUserService = {
    async getProjectInfo(projectId){
        try {
            const response = await apiClient.get('/user/projects/' + projectId)
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    },

    async getMyProjects(){
        try {
            const response = await apiClient.get('/user/projects')
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    },
}