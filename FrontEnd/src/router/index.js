import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../Stores/auth.js'
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
        component: () => import('../views/Upload.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/relatorio',
        name: 'Relatorio',
        component: () => import('../views/Relatorio.vue')
    },
    {
        path: '/usuarios',
        alias: ['/Usuario', '/usuario'],
        name: 'Usuario',
        component: () => import('../views/Usuario.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to) => {
    const auth = useAuthStore()
    if (to.meta.requiresAuth && !auth.logado) return '/upload'
    if (to.meta.admin && !auth.isAdmin) return '/dashboard'
})

export default router