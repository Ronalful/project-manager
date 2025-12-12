import apiClient from '@/api/index.js'
import router from "@/router/index.js";

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

    async assignDevelopers({projectId, userIds}){
        try {
            const response = await apiClient.post('/project-assignments/assign', {projectId, userIds})
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorAssignDeveloper: true}
        }
    },

    async unassignDevelopers({projectId, userIds}){
        try {
            const response = await apiClient.post('/project-assignments/unassign', {projectId, userIds})
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorAssignDeveloper: true}
        }
    },

    async getProjectInfo(projectId){
        try {
            const response = await apiClient.get(`/admin/projects/${projectId}`)
            return {success: true, data: response.data}
        }
        catch (error){
            if (error.response?.status === 404 || error.response?.status === 403) {
                await router.replace({
                    name: 'NotFound',
                    query: {returnTo: '/projects'}
                })
                return {success: false, projectNotFound: true}
            }
            return {success: false, errorLoadProjects: true}
        }
    },

    async editProject({id, name, description}){
        try {
            const response = await apiClient.put('/admin/projects', {id, name, description})
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadProjects: true}
        }
    },

    async deleteProject(projectId){
        try {
            const response = await apiClient.delete(`/admin/projects/${projectId}`)
            return {success: true, data: response.data}
        }
        catch (error){
            console.log(error)
            return {success: false, errorLoadProjects: true}
        }
    }
}