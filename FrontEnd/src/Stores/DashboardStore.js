import { defineStore } from 'pinia'
import api from '../services/api'

export const useDashboardStore = defineStore('dashboard', {
  state: () => ({
    dados: null,
    carregando: false,
    erro: ''
  }),
  getters: {
    estatisticas: (state) => state.dados?.estatisticas || null,
    lote: (state) => state.dados?.lote || null
  },
  actions: {
    async carregar(loteId = null) {
      this.carregando = true
      this.erro = ''
      try {
        const params = loteId ? { loteId } : undefined
        const { data } = await api.get('/dashboard', { params })
        this.dados = data
        return data
      } catch (error) {
        this.erro =
          error.response?.data?.erro ||
          error.response?.data?.message ||
          'Não foi possível carregar os dados do dashboard.'
        throw error
      } finally {
        this.carregando = false
      }
    }
  }
})
