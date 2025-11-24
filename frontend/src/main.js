import './assets/styles/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from "axios";
import App from './App.vue'
import router from './router'
import themeMixin from './mixins/theme.js'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.mixin(themeMixin)
app.use(router)
app.use(pinia)

app.config.globalProperties.$axios = axios;

app.mount('#app')