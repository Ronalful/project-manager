import './assets/styles/main.css'

import { createApp } from 'vue'
//import { createPinia } from 'pinia'
import axios from "axios";
import App from './App.vue'
import router from './router'
import themeMixin from './mixins/theme.js'

const app = createApp(App)
//const pinia = createPinia()

app.mixin(themeMixin)
app.use(router)
//app.use(pinia)

app.config.globalProperties.$axios = axios;

app.mount('#app')