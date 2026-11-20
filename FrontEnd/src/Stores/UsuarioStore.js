import { defineStore } from 'pinia'
import api from '../services/api'

export const useUsuarioStore = defineStore('usuarios', {
  state: () => ({
    solicitacoes: [],
    carregando: false,
    erro: '',
    mensagem: ''
  }),
  actions: {
    async listar(status = null) {
      this.carregando = true
      this.erro = ''
      try {
        const params = status ? { status } : undefined
        const { data } = await api.get('/admin/usuarios', { params })
        this.solicitacoes = data
        return data
      } catch (error) {
        this.erro = error.response?.data?.erro || 'Não foi possível carregar as solicitações.'
        throw error
      } finally {
        this.carregando = false
      }
    },
    async alterarStatus(id, status) {
      this.erro = ''
      this.mensagem = ''
      try {
        await api.patch(`/admin/usuarios/${id}/status`, { status })
        this.mensagem = status === 'APROVADO'
          ? 'Solicitação aprovada.'
          : 'Solicitação bloqueada.'
        await this.listar()
      } catch (error) {
        this.erro = error.response?.data?.erro || 'Não foi possível alterar o status.'
        throw error
      }
    }
  }
})
