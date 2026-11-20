import { defineStore } from 'pinia'
import api from '../services/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token'),
    usuario: JSON.parse(localStorage.getItem('usuario') || 'null'),
  }),
  getters: {
    logado: (state) => !!state.token,
    isAdmin: (state) => state.usuario?.perfil === 'ADMIN',
  },
  actions: {
    async cadastrar(nome, email, senha) {
      const { data } = await api.post('/auth/register', { nome, email, senha })
      return data
    },
    async login(email, senha) {
      const { data } = await api.post('/auth/login', { email, senha })
      this.token = data.token
      this.usuario = data.usuario
      localStorage.setItem('token', data.token)
      localStorage.setItem('usuario', JSON.stringify(data.usuario))
    },
    logout() {
      this.token = null
      this.usuario = null
      localStorage.removeItem('token')
      localStorage.removeItem('usuario')
    },
  },
})
