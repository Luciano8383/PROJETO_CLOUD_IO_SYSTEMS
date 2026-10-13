



<template>
  <!-- Fundo principal alterado para BRANCO (bg-white) -->
  <div class="bg-[#484848] text-white min-h-screen flex items-center justify-center p-4 font-sans selection:bg-[#00C9D1] selection:text-[#484848]">
    
    <!-- Card Principal de Upload (Mantém o tema escuro interno para destacar o componente CTI Insights) -->
    <div class="w-full max-w-2xl bg-[#006464] border border-[#0F928C] rounded-2xl shadow-2xl overflow-hidden">
      
      <!-- Cabeçalho (Header) -->
      <header class="px-6 py-4 flex items-center justify-between border-b border-[#0F928C]/50">
        <div class="flex items-center space-x-2.5">
          <!-- Ícone do gráfico de linhas (Cyan/Azul Claro) -->
         <a href="#" class="inline-block transition hover:opacity-80">
    <img 
      src="../assets/Image_2.jpg" 
      alt="Logo Cloud IO Systems" 
      class="h-24 w-auto object-contain" 
    />
  </a>
          
        </div>

      </header>

      <!-- Corpo da Tela de Upload -->
      <main class="p-8 sm:p-10 space-y-7">
        
        <!-- Título e Subtítulo -->
        <div class="text-center space-y-1.5">
          <h1 class="text-2xl sm:text-3xl font-extrabold text-white tracking-wide">
            Upload de Planilha de Clientes
          </h1>
          <p class="text-xs text-black-400">
            Suporta arquivos nos formatos .xlsx, .xls e .csv
          </p>
        </div>

        <!-- Área de Drag & Drop -->
        <div 
          @dragover.prevent="onDragOver"
          @dragleave.prevent="onDragLeave"
          @drop.prevent="onDrop"
          @click="triggerFileInput"
          :class="[
            'relative border-2 border-dashed rounded-xl p-8 sm:p-10 text-center cursor-pointer transition-all duration-200',
            isDragging 
              ? 'border-[#00C9D1] bg-[#484848]' 
              : selectedFile 
                ? 'border-[#BEED3B]/70 bg-[#484848]' 
                : 'border-[#0F928C] bg-[#484848] hover:bg-[#484848]/80 hover:border-[#00C9D1]'
          ]"
        >
          <input 
            type="file" 
            ref="fileInput" 
            class="hidden" 
            accept=".xlsx, .xls, .csv" 
            @change="onFileSelected"
          />

          <!-- Estado Inicial (Nenhum arquivo selecionado) -->
          <div v-if="!selectedFile" class="space-y-4">
            <!-- Ícone Nuvem com Seta -->
            <div class="w-16 h-16 mx-auto bg-[#006464] border border-[#0F928C] rounded-2xl flex items-center justify-center text-[#00C9D1] shadow-inner">
              <svg class="w-9 h-9" fill="currentColor" viewBox="0 0 24 24">
                <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"/>
              </svg>
            </div>
            
            <p class="text-gray-100 font-semibold text-base">
              Arraste e solte o arquivo aqui
            </p>
            <p class="text-gray-400 text-xs">
              ou clique para selecionar do computador
            </p>
          </div>

          <!-- Estado com Arquivo Selecionado -->
          <div v-else class="space-y-3" @click.stop>
            <div class="w-12 h-12 mx-auto bg-[#BEED3B]/15 text-[#BEED3B] border border-[#BEED3B]/40 rounded-full flex items-center justify-center">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
            </div>
            <div>
              <p class="text-white font-medium text-sm break-all">
                {{ selectedFile.name }}
              </p>
              <p class="text-gray-400 text-xs mt-1">
                {{ formatFileSize(selectedFile.size) }}
              </p>
            </div>
            <button 
              @click="removeFile" 
              type="button" 
              class="text-xs text-red-400 hover:text-red-300 font-medium underline mt-1 transition"
            >
              Remover ou trocar arquivo
            </button>
          </div>
        </div>

        <!-- Mensagens de Erro e Sucesso -->
        <div v-if="errorMessage" class="p-3 bg-red-500/10 border border-red-500/30 text-red-400 text-xs rounded-lg text-center">
          {{ errorMessage }}
        </div>

        <div v-if="successMessage" class="p-3 bg-[#BEED3B]/10 border border-[#BEED3B]/40 text-[#BEED3B] text-xs rounded-lg text-center">
          {{ successMessage }}
        </div>

        <!-- Barra de Progresso do Processamento -->
        <div v-if="isProcessing" class="space-y-1.5">
          <div class="flex justify-between text-xs font-medium text-gray-300">
            <span>Processando arquivo...</span>
            <span class="text-[#00C9D1] font-bold">{{ uploadProgress }}%</span>
          </div>
          <div class="w-full bg-[#484848] h-2 rounded-full overflow-hidden border border-[#0F928C]/50">
            <div 
              class="bg-[#00C9D1] h-full transition-all duration-150 shadow-[0_0_10px_#00C9D1]"
              :style="{ width: uploadProgress + '%' }"
            ></div>
          </div>
        </div>

        <!-- Botões de Ação -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-1">
          <!-- Botão Selecionar Arquivo -->
          <button 
            @click="triggerFileInput"
            type="button"
            :disabled="isProcessing"
            class="w-full py-3.5 px-4 bg-[#484848] hover:bg-[#484848]/80 text-gray-200 font-bold text-xs tracking-wider uppercase rounded-lg border border-[#0F928C] transition duration-200 disabled:opacity-50"
          >
            SELECIONAR ARQUIVO
          </button>

          <!-- Botão Enviar e Processar -->
          <button 
            @click="processUpload"
            type="button"
            :disabled="!selectedFile || isProcessing"
            class="w-full py-3.5 px-4 bg-[#BEED3B] hover:bg-[#BEED3B]/90 text-[#484848] font-bold text-xs tracking-wider uppercase rounded-lg shadow-lg shadow-[#484848]/40 transition duration-200 disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <span v-if="!isProcessing">ENVIAR E PROCESSAR</span>
            <span v-else>PROCESSANDO...</span>
          </button>
        </div>

      </main>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue';

const fileInput = ref(null);
const selectedFile = ref(null);
const isDragging = ref(false);
const isProcessing = ref(false);
const uploadProgress = ref(0);
const errorMessage = ref('');
const successMessage = ref('');

const allowedExtensions = ['xlsx', 'xls', 'csv'];

const triggerFileInput = () => {
  if (isProcessing.value) return;
  fileInput.value.click();
};

const validateFile = (file) => {
  if (!file) return false;
  
  const extension = file.name.split('.').pop().toLowerCase();
  if (!allowedExtensions.includes(extension)) {
    errorMessage.value = `Formato inválido (.${extension}). Por favor, envie arquivos .xlsx, .xls ou .csv.`;
    selectedFile.value = null;
    return false;
  }

  errorMessage.value = '';
  return true;
};

const handleFile = (file) => {
  successMessage.value = '';
  if (validateFile(file)) {
    selectedFile.value = file;
  }
};

const onFileSelected = (event) => {
  const files = event.target.files;
  if (files.length > 0) {
    handleFile(files[0]);
  }
};

const onDragOver = () => {
  if (isProcessing.value) return;
  isDragging.value = true;
};

const onDragLeave = () => {
  isDragging.value = false;
};

const onDrop = (event) => {
  if (isProcessing.value) return;
  isDragging.value = false;
  const files = event.dataTransfer.files;
  if (files.length > 0) {
    handleFile(files[0]);
  }
};

const removeFile = () => {
  selectedFile.value = null;
  errorMessage.value = '';
  successMessage.value = '';
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

const processUpload = () => {
  if (!selectedFile.value || isProcessing.value) return;

  isProcessing.value = true;
  uploadProgress.value = 0;
  errorMessage.value = '';
  successMessage.value = '';

  const interval = setInterval(() => {
    uploadProgress.value += 10;
    if (uploadProgress.value >= 100) {
      uploadProgress.value = 100;
      clearInterval(interval);
      
      setTimeout(() => {
        isProcessing.value = false;
        successMessage.value = `Arquivo "${selectedFile.value.name}" processado com sucesso!`;
        selectedFile.value = null;
        if (fileInput.value) fileInput.value.value = '';
      }, 400);
    }
  }, 150);
};
</script>

<style scoped>
</style>