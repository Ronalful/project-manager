import apiClient from '@/api/index.js'
export const userAllService = {
    async getMyInfo(){
        try {
            const response = await apiClient.get('/all/users/info')
            return {success: true, data: response.data}
        }
        catch (error){
            return {success: false, errorLoadMyInfo: true}
        }
    },
}