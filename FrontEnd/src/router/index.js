import { createRouter, createWebHistory } from 'vue-router'

import Home from '../views/Home.vue'
import Login from '../views/Login.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
     {
        path: '/upload',
        name: 'Upload',
        component: () => import('../views/Upload.vue')
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
    },
    {
        path: '/relatorio',
        name: 'Relatorio',
        component: () => import('../views/Relatorio.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})
export default router