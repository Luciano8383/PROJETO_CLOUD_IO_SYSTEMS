
<template>
  <!-- A div abaixo substitui a antiga tag <body> -->
  <div class="bg-[#484848] flex items-center justify-center min-h-screen">

    <div class="bg-white rounded-lg shadow-xl flex flex-col md:flex-row max-w-4xl w-full relative overflow-hidden">
        
        <!-- Painel Esquerdo (Verde-azulado) -->
        <div class="bg-gradient-to-br from-[#006464] to-[#0F928C] text-white md:w-1/2 p-12 flex flex-col relative order-2 md:order-1">
            <h1 class="text-4xl font-bold mb-2">WELCOME</h1>
            <p class="text-xl font-semibold mb-20">CLOUD IO SYSTEMS</p>
            
            <!-- Círculos Azulados -->
            <div class="absolute inset-0 z-0 pointer-events-none">
                <div class="gradient-sphere absolute top-[-50%] left-[-10%] w-[120%] h-[120%] rounded-full opacity-60"></div>
                <div class="gradient-sphere absolute bottom-[10%] left-[20%] w-[35%] h-[35%] rounded-full opacity-70"></div>
            </div>
            
            <!-- Botão "Acessar o site" -->
            <div class="mt-auto relative z-10">
                <button class="bg-[#BEED3B] text-[#484848] font-medium px-4 py-2 rounded-full flex items-center gap-2 hover:bg-[#BEED3B]/90 transition">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M13.5 6H5.25A2.25 2.25 0 0 0 3 8.25v10.5A2.25 2.25 0 0 0 5.25 21h10.5A2.25 2.25 0 0 0 18 18.75V10.5m-10.5 6L21 3m0 0h-5.25M21 3v5.25" />
                    </svg>
                    Acessar o site
                </button>
            </div>
        </div>

        <!-- Painel Direito (Login) -->
        <div class="bg-white p-8 md:p-12 md:w-1/2 flex flex-col justify-center order-1 md:order-2">
            <h2 class="text-4xl font-bold text-black mb-8">LOGIN</h2>
            
            <form class="space-y-6" @submit.prevent="entrar">
                <div>
                    <input v-model.trim="loginEmail" type="email" placeholder="E-mail" aria-label="E-mail" autocomplete="email" required class="w-full px-5 py-4 bg-gray-100 rounded-lg placeholder:text-gray-400 focus:ring-2 focus:ring-[#0F928C] focus:bg-white focus:outline-none transition">
                </div>
                <div>
                    <input v-model="loginSenha" type="password" placeholder="Senha" aria-label="Senha" autocomplete="current-password" required class="w-full px-5 py-4 bg-gray-100 rounded-lg placeholder:text-gray-400 focus:ring-2 focus:ring-[#0F928C] focus:bg-white focus:outline-none transition">
                </div>
                <div>
                    <button type="submit" :disabled="entrando" class="w-full px-5 py-4 bg-[#006464] text-white font-bold rounded-lg hover:bg-[#0F928C] transition disabled:cursor-not-allowed disabled:opacity-60">
                        {{ entrando ? 'ENTRANDO...' : 'LOGIN' }}
                    </button>
                </div>
                <p v-if="erroLogin" class="text-sm text-red-700" role="alert">{{ erroLogin }}</p>
            </form>

            <div class="mt-6 border-t border-gray-200 pt-5">
                <p class="text-center text-sm text-gray-600">Ainda não tem uma conta?</p>
                <button
                    type="button"
                    class="mt-3 w-full rounded-lg border border-[#006464] px-5 py-3 font-bold text-[#006464] transition hover:bg-[#006464]/5 focus:outline-none focus:ring-2 focus:ring-[#0F928C]/50"
                    :aria-expanded="cadastroAberto"
                    aria-controls="cadastro-form"
                    @click="alternarCadastro"
                >
                    {{ cadastroAberto ? 'Fechar cadastro' : 'Solicitar cadastro' }}
                </button>
            </div>

            <form
                v-if="cadastroAberto"
                id="cadastro-form"
                class="mt-5 space-y-4"
                @submit.prevent="cadastrar"
            >
                <h3 class="text-lg font-bold text-gray-900">Solicitação de acesso</h3>
                <input
                    v-model.trim="nome"
                    type="text"
                    placeholder="Nome"
                    aria-label="Nome"
                    autocomplete="name"
                    required
                    class="w-full rounded-lg bg-gray-100 px-5 py-3 placeholder:text-gray-400 transition focus:bg-white focus:outline-none focus:ring-2 focus:ring-[#0F928C]"
                >
                <input
                    v-model.trim="email"
                    type="email"
                    placeholder="E-mail"
                    aria-label="E-mail para cadastro"
                    autocomplete="email"
                    required
                    class="w-full rounded-lg bg-gray-100 px-5 py-3 placeholder:text-gray-400 transition focus:bg-white focus:outline-none focus:ring-2 focus:ring-[#0F928C]"
                >
                <input
                    v-model="senha"
                    type="password"
                    placeholder="Senha"
                    aria-label="Senha para cadastro"
                    autocomplete="new-password"
                    required
                    minlength="8"
                    class="w-full rounded-lg bg-gray-100 px-5 py-3 placeholder:text-gray-400 transition focus:bg-white focus:outline-none focus:ring-2 focus:ring-[#0F928C]"
                >
                <button
                    type="submit"
                    :disabled="enviando"
                    class="w-full rounded-lg bg-[#006464] px-5 py-3 font-bold text-white transition hover:bg-[#0F928C] disabled:cursor-not-allowed disabled:opacity-60"
                >
                    {{ enviando ? 'Enviando solicitação...' : 'Enviar solicitação' }}
                </button>
                <p v-if="mensagem" class="text-sm text-[#006464]" role="status" aria-live="polite">
                    {{ mensagem }}
                </p>
                <p v-if="erro" class="text-sm text-red-700" role="alert">
                    {{ erro }}
                </p>
                <p class="text-center text-xs text-gray-500">Aguarde a aprovação do administrador.</p>
            </form>
        </div>

        <!-- Botão de Pesquisa Flutuante (Inferior Direito) -->
        <button class="absolute bottom-6 right-6 z-20 bg-[#00C9D1] text-[#484848] p-4 rounded-full shadow-lg hover:bg-[#BEED3B] transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z" />
            </svg>
        </button>

    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '../Stores/auth.js';
import api from '../services/api';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const loginEmail = ref('');
const loginSenha = ref('');
const entrando = ref(false);
const erroLogin = ref('');
const cadastroAberto = ref(false);
const enviando = ref(false);
const nome = ref('');
const email = ref('');
const senha = ref('');
const mensagem = ref('');
const erro = ref('');

const entrar = async () => {
    if (entrando.value) return;

    entrando.value = true;
    erroLogin.value = '';

    try {
        await authStore.login(loginEmail.value, loginSenha.value);
        const redirect = route.query.redirect;
        const destino = typeof redirect === 'string' && redirect.startsWith('/') && !redirect.startsWith('//')
            ? redirect
            : '/dashboard';
        await router.push(destino);
    } catch (error) {
        erroLogin.value = error.response?.data?.message || 'Não foi possível entrar. Verifique seu e-mail e sua senha.';
    } finally {
        entrando.value = false;
    }
};

const alternarCadastro = () => {
    cadastroAberto.value = !cadastroAberto.value;
    mensagem.value = '';
    erro.value = '';
};

const cadastrar = async () => {
    if (enviando.value) return;

    enviando.value = true;
    mensagem.value = '';
    erro.value = '';

    try {
        await api.post('/auth/register', {
            nome: nome.value,
            email: email.value,
            senha: senha.value,
        });
        mensagem.value = 'Solicitação enviada. Aguarde a aprovação do administrador.';
        nome.value = '';
        email.value = '';
        senha.value = '';
    } catch (error) {
        erro.value = error.response?.data?.message || 'Não foi possível cadastrar.';
    } finally {
        enviando.value = false;
    }
};
</script>

<!-- A tag style deve ficar fora do template, preferencialmente no final -->
<style scoped>
/* Estilos personalizados para os círculos e botão flutuante */
.gradient-sphere {
    background: radial-gradient(circle, #00C9D1 0%, #0F928C 100%);
}
</style>