import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/auth-api': {
        target: 'http://localhost:8222',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/auth-api/, '/api/v1/auth'),
      },
      '/admin':{
        target: 'http://localhost:8222',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/admin/, '/api/v1/admin'),
      },
      '/project-assignments':{
        target: 'http://localhost:8222',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/project-assignments/, '/api/v1/project-assignments'),
      },
      '/user':{
        target: 'http://localhost:8222',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/user/, '/api/v1/user'),
      },
      '/all':{
        target: 'http://localhost:8222',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/all/, '/api/v1/all'),
      },
    },
  }
})
