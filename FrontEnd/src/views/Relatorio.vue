<template>
  <main class="min-h-screen bg-[#f3f4f6] p-4 font-sans text-gray-800 md:p-8">
    <div class="mx-auto max-w-5xl space-y-6">
      <header class="flex flex-col gap-4 rounded-xl bg-[#006464] p-6 text-white shadow-md sm:flex-row sm:items-center sm:justify-between">
        <div>
          <p class="text-xs font-bold uppercase tracking-[0.2em] text-[#00C9D1]">Cloud IO Systems</p>
          <h1 class="mt-2 text-2xl font-extrabold sm:text-3xl">Relatório de inconsistências</h1>
          <p class="mt-1 break-all text-sm text-gray-200">
            <template v-if="uploadStore.nomeArquivoProcessado">
              Planilha analisada: {{ uploadStore.nomeArquivoProcessado }}
            </template>
            <template v-else>
              Resultado da última planilha processada
            </template>
          </p>
        </div>
        <router-link
          to="/upload"
          class="shrink-0 rounded-lg border border-[#00C9D1] px-4 py-2 text-center text-sm font-bold text-[#00C9D1] transition hover:bg-[#00C9D1] hover:text-[#484848]"
        >
          Analisar outra planilha
        </router-link>
      </header>

      <section class="grid grid-cols-1 gap-4 sm:grid-cols-3" aria-label="Resumo da análise">
        <div class="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <p class="text-xs font-bold uppercase tracking-wider text-gray-500">Registros analisados</p>
          <p class="mt-2 text-3xl font-extrabold text-[#006464]">{{ uploadStore.totalClientes }}</p>
        </div>
        <div class="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <p class="text-xs font-bold uppercase tracking-wider text-gray-500">Linhas com inconsistências</p>
          <p class="mt-2 text-3xl font-extrabold" :class="inconsistencias.length ? 'text-red-600' : 'text-[#006464]'">
            {{ inconsistencias.length }}
          </p>
        </div>
        <div class="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <p class="text-xs font-bold uppercase tracking-wider text-gray-500">Problemas encontrados</p>
          <p class="mt-2 text-3xl font-extrabold" :class="totalProblemas ? 'text-red-600' : 'text-[#006464]'">
            {{ totalProblemas }}
          </p>
        </div>
      </section>

      <section v-if="inconsistencias.length" class="space-y-4" aria-labelledby="achados-title">
        <div class="flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h2 id="achados-title" class="text-xl font-bold text-gray-900">Achados por linha</h2>
            <p class="text-sm text-gray-600">A numeração considera a linha do cabeçalho como linha 1.</p>
          </div>
          <p class="text-sm font-semibold text-red-700">Revise os dados antes de utilizá-los.</p>
        </div>

        <article
          v-for="registro in inconsistencias"
          :key="registro.linha"
          class="overflow-hidden rounded-xl border border-red-200 bg-white shadow-sm"
        >
          <div class="flex flex-col gap-2 border-b border-gray-100 bg-red-50 px-5 py-4 sm:flex-row sm:items-center sm:justify-between">
            <h3 class="font-bold text-gray-900">
              Linha {{ registro.linha }} — {{ registro.nome_cliente || "Sem nome" }}
            </h3>
            <span class="w-fit rounded-full bg-red-100 px-3 py-1 text-xs font-bold text-red-700">
              {{ registro.motivos.length }} {{ registro.motivos.length === 1 ? "problema" : "problemas" }}
            </span>
          </div>

          <div class="grid gap-5 px-5 py-4 md:grid-cols-[1fr_1.2fr]">
            <dl class="grid grid-cols-2 gap-x-4 gap-y-3 text-sm">
              <div>
                <dt class="text-xs font-semibold uppercase text-gray-500">Cliente</dt>
                <dd class="mt-1 break-words">{{ registro.nome_cliente || "—" }}</dd>
              </div>
              <div>
                <dt class="text-xs font-semibold uppercase text-gray-500">Segmento</dt>
                <dd class="mt-1 break-words">{{ formatValue(registro.segmento) }}</dd>
              </div>
              <div>
                <dt class="text-xs font-semibold uppercase text-gray-500">Consultor</dt>
                <dd class="mt-1 break-words">{{ formatValue(registro.consultor) }}</dd>
              </div>
              <div>
                <dt class="text-xs font-semibold uppercase text-gray-500">Nível</dt>
                <dd class="mt-1 break-words">{{ formatValue(registro.nivel_cliente) }}</dd>
              </div>
              <div class="col-span-2">
                <dt class="text-xs font-semibold uppercase text-gray-500">Faturamento anual</dt>
                <dd class="mt-1 break-words">{{ formatValue(registro.faturamento_anual) }}</dd>
              </div>
            </dl>

            <div>
              <h4 class="text-sm font-bold text-red-800">Problemas identificados</h4>
              <ul class="mt-2 list-inside list-disc space-y-1 text-sm text-gray-700">
                <li v-for="motivo in registro.motivos" :key="motivo">{{ motivo }}</li>
              </ul>
            </div>
          </div>
        </article>
      </section>

      <section v-else-if="uploadStore.totalClientes" class="rounded-xl border border-green-200 bg-green-50 p-6 text-center">
        <h2 class="text-lg font-bold text-green-800">Nenhuma inconsistência encontrada</h2>
        <p class="mt-1 text-sm text-green-700">
          Os {{ uploadStore.totalClientes }} registros analisados passaram pelas validações disponíveis.
        </p>
      </section>

      <section v-else class="rounded-xl border border-gray-200 bg-white p-8 text-center shadow-sm">
        <h2 class="text-lg font-bold text-gray-900">Nenhuma planilha analisada nesta sessão</h2>
        <p class="mt-2 text-sm text-gray-600">
          Envie um arquivo .xlsx, .xls ou .csv para gerar o relatório com os problemas encontrados.
        </p>
        <router-link to="/upload" class="mt-4 inline-block font-bold text-[#006464] underline hover:text-[#0F928C]">
          Ir para o envio da planilha
        </router-link>
      </section>
    </div>
  </main>
</template>

<script setup>
import { computed } from 'vue';
import { useUploadStore } from '../Stores/UploadStore';

const uploadStore = useUploadStore();
const inconsistencias = computed(() => uploadStore.inconsistencias);
const totalProblemas = computed(() =>
  inconsistencias.value.reduce((total, registro) => total + registro.motivos.length, 0)
);
const formatValue = (value) =>
  value === null || value === undefined || String(value).trim() === '' ? '—' : value;
</script>
