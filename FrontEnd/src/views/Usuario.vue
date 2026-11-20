<template>
  <main class="min-h-screen bg-[#f3f4f6] p-4 font-sans text-gray-800 md:p-8">
    <div class="mx-auto max-w-6xl space-y-6">
      <header class="flex flex-col gap-4 rounded-2xl bg-[#006464] p-6 text-white shadow-md sm:flex-row sm:items-center sm:justify-between">
        <div>
          <p class="text-xs font-bold uppercase tracking-[0.2em] text-[#00C9D1]">Cloud IO Systems</p>
          <h1 class="mt-2 text-2xl font-extrabold sm:text-3xl">Solicitações de acesso</h1>
          <p class="mt-1 text-sm text-gray-200">Revise os pedidos de cadastro antes de liberar o acesso.</p>
        </div>
        <router-link
          to="/"
          class="w-fit rounded-lg border border-white/40 px-4 py-2 text-sm font-semibold transition hover:bg-white/10"
        >
          Voltar ao início
        </router-link>
      </header>

      <p v-if="usuarioStore.erro" class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700" role="alert">
        {{ usuarioStore.erro }}
      </p>

      <section class="grid grid-cols-1 gap-4 sm:grid-cols-3" aria-label="Resumo das solicitações">
        <div class="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <p class="text-xs font-bold uppercase tracking-wider text-gray-500">Total de solicitações</p>
          <p class="mt-2 text-3xl font-extrabold text-[#006464]">{{ solicitacoes.length }}</p>
        </div>
        <div class="rounded-xl border border-amber-200 bg-white p-5 shadow-sm">
          <p class="text-xs font-bold uppercase tracking-wider text-gray-500">Pendentes</p>
          <p class="mt-2 text-3xl font-extrabold text-amber-600">{{ totalPendentes }}</p>
        </div>
        <div class="rounded-xl border border-green-200 bg-white p-5 shadow-sm">
          <p class="text-xs font-bold uppercase tracking-wider text-gray-500">Aprovadas</p>
          <p class="mt-2 text-3xl font-extrabold text-green-700">{{ totalAprovadas }}</p>
        </div>
      </section>

      <section class="space-y-4" aria-labelledby="lista-title">
        <div class="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h2 id="lista-title" class="text-xl font-bold text-gray-900">Cadastros recebidos</h2>
            <p class="mt-1 text-sm text-gray-600">Confira os dados informados e escolha uma ação.</p>
          </div>
          <label class="flex items-center gap-2 text-sm font-medium text-gray-700">
            Exibir
            <select
              v-model="filtro"
              class="rounded-lg border border-gray-300 bg-white px-3 py-2 focus:border-[#0F928C] focus:outline-none focus:ring-2 focus:ring-[#0F928C]/20"
            >
              <option value="PENDENTE">Pendentes</option>
              <option value="TODOS">Todas</option>
              <option value="APROVADO">Aprovadas</option>
              <option value="BLOQUEADO">Bloqueadas</option>
            </select>
          </label>
        </div>

        <div v-if="solicitacoesFiltradas.length" class="grid grid-cols-1 gap-4 lg:grid-cols-2">
          <article
            v-for="solicitacao in solicitacoesFiltradas"
            :key="solicitacao.id"
            class="rounded-xl border border-gray-200 bg-white p-5 shadow-sm"
          >
            <div class="flex items-start justify-between gap-3">
              <div class="flex min-w-0 items-center gap-3">
                <div class="flex h-11 w-11 shrink-0 items-center justify-center rounded-full bg-[#006464]/10 text-lg font-bold text-[#006464]">
                  {{ iniciais(solicitacao.nome) }}
                </div>
                <div class="min-w-0">
                  <h3 class="truncate font-bold text-gray-900">{{ solicitacao.nome }}</h3>
                  <p class="break-all text-sm text-gray-600">{{ solicitacao.email }}</p>
                </div>
              </div>
              <span
                class="shrink-0 rounded-full px-3 py-1 text-xs font-bold"
                :class="classesStatus(solicitacao.status)"
              >
                {{ rotuloStatus(solicitacao.status) }}
              </span>
            </div>

            <dl class="mt-5 grid grid-cols-2 gap-4 border-t border-gray-100 pt-4 text-sm">
              <div>
                <dt class="text-xs font-semibold uppercase tracking-wide text-gray-500">Solicitado em</dt>
                <dd class="mt-1 font-medium text-gray-800">{{ formatarData(solicitacao.criadoEm) }}</dd>
              </div>
            </dl>

            <div v-if="solicitacao.status === 'PENDENTE'" class="mt-5 flex flex-col gap-3 sm:flex-row sm:justify-end">
              <button
                type="button"
                class="rounded-lg border border-red-200 px-4 py-2 text-sm font-bold text-red-700 transition hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-red-300"
                @click="atualizarStatus(solicitacao.id, 'BLOQUEADO')"
              >
                Bloquear acesso
              </button>
              <button
                type="button"
                class="rounded-lg bg-[#006464] px-4 py-2 text-sm font-bold text-white transition hover:bg-[#0F928C] focus:outline-none focus:ring-2 focus:ring-[#0F928C]/50"
                @click="atualizarStatus(solicitacao.id, 'APROVADO')"
              >
                Aprovar acesso
              </button>
            </div>
          </article>
        </div>

        <div v-else class="rounded-xl border border-gray-200 bg-white p-8 text-center shadow-sm">
          <h3 class="font-bold text-gray-900">Nenhuma solicitação nesta categoria</h3>
          <p class="mt-1 text-sm text-gray-600">Altere o filtro para consultar outras solicitações.</p>
        </div>
      </section>

      <p v-if="usuarioStore.mensagem" class="rounded-lg border border-[#0F928C]/30 bg-white px-4 py-3 text-sm font-medium text-[#006464]" role="status">
        {{ usuarioStore.mensagem }}
      </p>
    </div>
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useUsuarioStore } from '../Stores/UsuarioStore';

const usuarioStore = useUsuarioStore();
const filtro = ref('PENDENTE');
const solicitacoes = computed(() => usuarioStore.solicitacoes);

const solicitacoesFiltradas = computed(() =>
  filtro.value === 'TODOS'
    ? solicitacoes.value
    : solicitacoes.value.filter((solicitacao) => solicitacao.status === filtro.value)
);
const totalPendentes = computed(() =>
  solicitacoes.value.filter((solicitacao) => solicitacao.status === 'PENDENTE').length
);
const totalAprovadas = computed(() =>
  solicitacoes.value.filter((solicitacao) => solicitacao.status === 'APROVADO').length
);

const atualizarStatus = async (id, status) => {
  await usuarioStore.alterarStatus(id, status);
};

const formatarData = (valor) => {
  if (!valor) return '—';
  const data = new Date(valor);
  return Number.isNaN(data.getTime()) ? '—' : data.toLocaleDateString('pt-BR');
};

const iniciais = (nome) =>
  nome.split(' ').filter(Boolean).slice(0, 2).map((parte) => parte[0]).join('').toUpperCase();

const rotuloStatus = (status) => ({
  PENDENTE: 'Pendente',
  APROVADO: 'Aprovado',
  BLOQUEADO: 'Bloqueado',
}[status] || status);

const classesStatus = (status) => ({
  PENDENTE: 'bg-amber-100 text-amber-800',
  APROVADO: 'bg-green-100 text-green-800',
  BLOQUEADO: 'bg-red-100 text-red-800',
}[status] || 'bg-gray-100 text-gray-700');

onMounted(() => {
  usuarioStore.listar();
});
</script>
