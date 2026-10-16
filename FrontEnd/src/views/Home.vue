<template>
  <div class="bg-[#484848] text-white min-h-screen flex flex-col justify-between font-sans selection:bg-[#00C9D1] selection:text-[#484848]">
    
    <!-- Navbar / Cabeçalho -->
   <header class="w-full max-w-7xl mx-auto px-8 py-5 flex justify-between items-center z-20 bg-[#006464] border border-[#0F928C] rounded-2xl shadow-lg shadow-black/20">
  <a href="#" class="inline-block transition hover:opacity-80">
    <img 
      src="../assets/Image_2.jpg" 
      alt="Logo Cloud IO Systems" 
      class="h-24 w-auto object-contain" 
    />
  </a>

  <nav class="hidden md:flex items-center space-x-12 text-base font-medium text-white">
    <a href="#" class="hover:text-[#00C9D1] transition">Home</a>
    <a href="#" class="hover:text-[#00C9D1] transition">Platform</a>
    <a href="#" class="hover:text-[#00C9D1] transition">Services</a>
    <a href="#" class="hover:text-[#00C9D1] transition">Solutions</a>
    <a href="#" class="hover:text-[#00C9D1] transition">Company</a>
  </nav>

  <div>
    <a href="#" class="px-7 py-3.5 rounded-full border border-[#BEED3B]/50 text-[#BEED3B] hover:bg-[#BEED3B] hover:text-[#484848] transition font-medium text-sm">
      Login (CTA)
    </a>
  </div>
</header>

    <!-- Seção Principal (Hero Section) -->
    <main class="relative w-full max-w-7xl mx-auto px-6 py-12 flex-grow flex flex-col justify-center">
      
      <!-- Fundo Decorativo com Efeitos de Glow -->
      <div class="absolute inset-0 pointer-events-none z-0 overflow-hidden">
        <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-[#00C9D1]/20 rounded-full blur-[120px]"></div>
      </div>

      <div class="relative z-10 grid grid-cols-1 lg:grid-cols-12 gap-8 items-center">
        
        <!-- Card Esquerdo: Cloud IO Analytics -->
        <div class="lg:col-span-3 order-2 lg:order-1">
          <div class="bg-[#006464]/80 border border-[#0F928C]/50 rounded-xl p-4 shadow-2xl backdrop-blur-md transform transition hover:scale-105 duration-300">
            <div class="flex items-center justify-between pb-3 border-b border-gray-700/50 text-xs text-gray-400 mb-3">
              <span class="font-bold text-gray-200 uppercase tracking-wider">CLOUD IO ANALYTICS</span>
              <div class="flex space-x-1">
                <span class="w-2 h-2 rounded-full bg-gray-600"></span>
                <span class="w-2 h-2 rounded-full bg-gray-600"></span>
              </div>
            </div>
            <!-- Mockup de Gráficos -->
            <div class="space-y-3">
              <div class="bg-[#0F928C]/30 rounded p-2 text-xs text-[#00C9D1] font-medium">Contas analytics</div>
              <div class="h-20 bg-gradient-to-t from-[#006464]/60 to-[#00C9D1]/30 rounded-lg flex items-end p-2 border border-[#00C9D1]/30">
                <div class="w-full h-full bg-[#00C9D1]/10 rounded-sm"></div>
              </div>
              <div class="grid grid-cols-5 gap-1 items-end h-16 pt-2">
                <div class="bg-[#00C9D1]/60 h-6 rounded-t"></div>
                <div class="bg-[#00C9D1]/80 h-10 rounded-t"></div>
                <div class="bg-[#00C9D1] h-14 rounded-t"></div>
                <div class="bg-[#00C9D1]/70 h-8 rounded-t"></div>
                <div class="bg-[#00C9D1] h-12 rounded-t"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- Conteúdo Central: Logotipo e Texto de Impacto -->
        <div class="lg:col-span-6 text-center order-1 lg:order-2 space-y-6">
          
          <!-- Logo Cloud IO Systems -->
          <div class="inline-flex flex-col items-center">
            <div class="relative flex items-center justify-center">
              <svg class="w-44 h-24 text-[#00C9D1]" viewBox="0 0 200 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M40 70 C 20 70, 20 40, 45 40 C 45 20, 85 10, 110 30 C 130 15, 170 25, 165 50 C 185 50, 185 70, 160 70 Z" stroke="currentColor" stroke-width="6" stroke-linecap="round" fill="none"/>
                <path d="M110 25 L160 25 M145 15 L160 25 L145 35" stroke="#00C9D1" stroke-width="5" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M90 75 L40 75 M55 65 L40 75 L55 85" stroke="#00C9D1" stroke-width="5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="text-3xl font-black tracking-wider text-white -mt-2">
              CLOUD <span class="text-[#00C9D1]">IO</span>
            </div>
            <div class="text-xs tracking-[0.3em] font-semibold text-gray-300">SYSTEMS</div>
          </div>

          <!-- Título Principal -->
          <h1 class="text-3xl md:text-4xl lg:text-5xl font-extrabold tracking-tight leading-tight">
            ORGANIZANDO SEUS DADOS,<br/>
            GERANDO INSIGHTS ESTRATÉGICOS.
          </h1>

          <!-- Subtítulo -->
          <p class="text-gray-300 text-sm md:text-base max-w-xl mx-auto leading-relaxed">
            Potencialize sua empresa com inteligência de dados automatizada e segura no Cloud IO Systems.
          </p>

          <!-- Botões CTA -->
          <div class="flex flex-col sm:flex-row items-center justify-center gap-4 pt-4">
            <button class="w-full sm:w-auto px-8 py-3.5 bg-[#BEED3B] hover:bg-[#BEED3B]/90 text-[#484848] font-bold rounded-full shadow-lg shadow-[#BEED3B]/30 transition duration-200">
              COMEÇAR AGORA
            </button>
            <button class="w-full sm:w-auto px-8 py-3.5 border border-[#0F928C] hover:border-[#00C9D1] text-white font-bold rounded-full backdrop-blur-sm hover:bg-[#0F928C]/30 transition duration-200">
              AGENDAR DEMO
            </button>
          </div>
        </div>

        <!-- Card Direito: Client Segments -->
        <div class="lg:col-span-3 order-3">
          <div class="bg-[#006464]/80 border border-[#0F928C]/50 rounded-xl p-4 shadow-2xl backdrop-blur-md transform transition hover:scale-105 duration-300">
            <div class="flex items-center justify-between pb-3 border-b border-gray-700/50 text-xs text-gray-400 mb-3">
              <span class="font-bold text-gray-200 uppercase tracking-wider">CLIENT SEGMENTS</span>
              <div class="flex space-x-1">
                <span class="w-2 h-2 rounded-full bg-gray-600"></span>
                <span class="w-2 h-2 rounded-full bg-gray-600"></span>
              </div>
            </div>
            <!-- Heatmap Grid Simulation -->
            <div class="grid grid-cols-2 gap-2 h-40">
              <div class="bg-gradient-to-br from-[#00C9D1] via-[#0F928C] to-[#BEED3B] rounded p-1 text-[10px] text-[#484848] font-semibold">Client</div>
              <div class="bg-gradient-to-br from-[#0F928C] to-[#006464] rounded p-1 text-[10px] text-white font-semibold">Segments</div>
              <div class="bg-gradient-to-br from-[#00C9D1] to-[#006464] rounded p-1 text-[10px] text-white font-semibold">Segments</div>
              <div class="bg-gradient-to-br from-[#0F928C] to-[#BEED3B] rounded p-1 text-[10px] text-[#484848] font-semibold">Segments</div>
            </div>
          </div>
        </div>

      </div>

      <!-- Seção Inferior: Recursos e Gráfico Adicional -->
      <div class="mt-16 grid grid-cols-1 lg:grid-cols-12 gap-8 items-end">
        
        <!-- Lista de Benefícios (Esquerda) -->
        <div class="lg:col-span-7 grid grid-cols-1 sm:grid-cols-2 gap-6">
          <div class="sm:col-span-2">
            <h2 class="text-2xl md:text-3xl font-bold">Por que o Cloud<br/>IO Systems?</h2>
          </div>

          <div class="flex items-start space-x-3">
            <div class="p-2 bg-[#006464]/50 rounded-lg border border-[#0F928C]/50 text-[#00C9D1]">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path></svg>
            </div>
            <div>
              <h3 class="font-bold text-white text-base">Eficiência</h3>
              <p class="text-xs text-gray-400 mt-1">Inteligência de empresa no Cloud IO Systems.</p>
            </div>
          </div>

          <div class="flex items-start space-x-3">
            <div class="p-2 bg-[#006464]/50 rounded-lg border border-[#0F928C]/50 text-[#00C9D1]">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path></svg>
            </div>
            <div>
              <h3 class="font-bold text-white text-base">Insights em Tempo Real</h3>
            </div>
          </div>

          <div class="flex items-start space-x-3 sm:col-start-2">
            <div class="p-2 bg-[#006464]/50 rounded-lg border border-[#0F928C]/50 text-[#00C9D1]">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path></svg>
            </div>
            <div>
              <h3 class="font-bold text-white text-base">Segurança</h3>
              <p class="text-xs text-gray-400 mt-1">Potencialize sua empresa no Cloud IO Systems.</p>
            </div>
          </div>
        </div>

        <!-- Card Inferior Direito: Service Mix -->
        <div class="lg:col-span-5">
          <div class="bg-[#006464]/80 border border-[#0F928C]/50 rounded-xl p-4 shadow-2xl backdrop-blur-md">
            <div class="flex items-center justify-between pb-3 border-b border-gray-700/50 text-xs text-gray-400 mb-3">
              <span class="font-bold text-gray-200">Service Mix</span>
              <div class="flex space-x-1">
                <span class="w-2 h-2 rounded-full bg-gray-600"></span>
                <span class="w-2 h-2 rounded-full bg-gray-600"></span>
              </div>
            </div>
            <div class="h-32 flex items-end justify-between gap-1 pt-4 px-2 border-b border-gray-700/50 pb-2">
              <div v-for="bar in [40, 60, 80, 50, 90, 70, 85]" :key="bar" class="w-full bg-[#00C9D1]/80 rounded-t" :style="{ height: bar + '%' }"></div>
            </div>
            <div class="flex justify-between text-[9px] text-gray-400 pt-2 px-1">
              <span>JAN</span><span>FEV</span><span>MAR</span><span>ABR</span><span>MAY</span><span>JUN</span><span>NOV</span>
            </div>
          </div>
        </div>

      </div>

    </main>

    <!-- Rodapé -->
    <footer class="w-full border-t border-[#484848] bg-[#484848] py-6 z-20">
      <div class="max-w-7xl mx-auto px-6 flex flex-col md:flex-row justify-between items-center text-xs text-gray-400 gap-4">
        <div>
          Legal | Legency | All rights reserved.
        </div>

        <div class="flex space-x-6">
          <a href="#" class="hover:text-white transition">Contact</a>
          <span>|</span>
          <a href="#" class="hover:text-white transition">Platform</a>
          <span>|</span>
          <a href="#" class="hover:text-white transition">Contact us</a>
        </div>

        <div class="flex space-x-4 text-gray-400">
          <a href="#" class="hover:text-[#00C9D1] transition"><span class="sr-only">Facebook</span>FB</a>
          <a href="#" class="hover:text-[#00C9D1] transition"><span class="sr-only">Twitter</span>TW</a>
          <a href="#" class="hover:text-[#00C9D1] transition"><span class="sr-only">LinkedIn</span>LN</a>
          <a href="#" class="hover:text-[#00C9D1] transition"><span class="sr-only">Instagram</span>IG</a>
        </div>
      </div>
    </footer>

  </div>
</template>

<script setup>
// Lógica reativa da página pode ser inserida aqui
</script>

<style scoped>
/* Ajustes específicos se necessário */
</style>