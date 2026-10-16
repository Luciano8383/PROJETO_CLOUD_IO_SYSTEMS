import axios from 'axios'                         
const api = axios.create({                        
baseURL: 'http://localhost:8080',               
// Biblioteca HTTP
// Cria uma instância reutilizável
// Endereço do backend
headers: { 'Content-Type': 'application/json' } // Envia JSON
})
api.interceptors.request.use((config) => {        
const token = localStorage.getItem('token')     
// Executa antes de cada requisição
// Busca o token salvo
if (token) config.headers.Authorization = 'Bearer ' + token // Envia Bearer Token
return config                                   
// Continua a requisição
})
export default api                                
// Permite usar api em outras telas