import './assets/styles/main.css'

import { createApp } from 'vue'
import axios from "axios";
import App from './App.vue'
import router from './router'
import themeMixin from './mixins/theme.js'

const app = createApp(App)
app.mixin(themeMixin)
app.use(router)

app.config.globalProperties.$axios = axios;

app.mount('#app')