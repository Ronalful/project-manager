import apiClient from '@/api/index.js'

export const projectAdminService = {
    async getAllProjects(){
        try {
            const response = await apiClient.get('/admin/projects')
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    },

    async createProject({name, description}){
        try {
            const response = await apiClient.post('/admin/projects', {name, description})
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    },

    async assignDeveloper({projectId, userId}){
        try {
            const response = await apiClient.post('/project-assignments/assign', {projectId, userId})
            console.log(response)
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorAssignDeveloper: true}
        }
    }
}