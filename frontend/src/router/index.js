import {createRouter, createWebHistory} from 'vue-router'
import {tokenService} from "@/services/TokenService.js"
import authRouters from "./auth.js"
import adminRouters from "./admin.js"
import defaultRouters from "./default.js"
import NotFoundView from "@/views/NotFoundView.vue";

const routes = [
        ...authRouters,
        ...adminRouters,
        ...defaultRouters,
    {
        path: '/:catchAll(.*)',  // Ловит любой путь, какой не был ранее объявлен
        name: 'NotFound',
        component: NotFoundView
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

router.beforeEach(async(to, from, next) => {
    const isAuthenticated = tokenService.isAuthenticated()
    const isUsingTempTokens = tokenService.isUsingTempTokens()

    if (to.meta.requiresActivationToken){
        if(isAuthenticated){
            next({name: 'Home'})
            return
        }
        else{
            if (!isUsingTempTokens){
                next({name: 'Login'})
                return
            }
        }
    }

    if (to.meta.requiresAuth){
        if(!isAuthenticated) {
            next({
                name: 'Login',
                query: {redirect: to.fullPath}
            })
            return
        }
    }

    if (to.meta.requiresGuest) {
        if (isAuthenticated) {
            next({name: 'Home'})
            return
        }
    }

    next()
})

export default router