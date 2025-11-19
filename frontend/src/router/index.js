import {createRouter, createWebHistory} from 'vue-router'
import {tokenService} from "@/services/TokenService.js"
import authRouters from "./auth.js"
import adminRouters from "./admin.js"
import defaultRouters from "./default.js"
import NotFoundView from "@/views/NotFoundView.vue";
import {useUserStore} from "@/stores/user.js";

const routes = [
        ...authRouters,
        ...adminRouters,
        ...defaultRouters,
    {
        path: '/404',
        name: 'NotFound',
        component: NotFoundView
    },
    {
        path: '/:catchAll(.*)',
        name: 'CatchAll',
        redirect: '/404'
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

    if(to.meta.requiresAdmin){
        //const userStore = useUserStore()
        // if(!userStore.isAdmin){
        //     // что-нибудь придумать для доступа
        //     return
        // }
    }

    next()
})

export default router