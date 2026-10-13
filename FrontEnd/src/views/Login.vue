
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
        <div class="bg-white p-12 md:w-1/2 flex flex-col justify-center order-1 md:order-2">
            <h2 class="text-4xl font-bold text-black mb-8">LOGIN</h2>
            
            <form class="space-y-6" @submit.prevent>
                <div>
                    <input type="email" placeholder="Your email" class="w-full px-5 py-4 bg-gray-100 rounded-lg placeholder:text-gray-400 focus:ring-2 focus:ring-[#0F928C] focus:bg-white focus:outline-none transition">
                </div>
                <div>
                    <input type="password" placeholder="Enter Password" class="w-full px-5 py-4 bg-gray-100 rounded-lg placeholder:text-gray-400 focus:ring-2 focus:ring-[#0F928C] focus:bg-white focus:outline-none transition">
                </div>
                <div>
                    <button type="submit" class="w-full px-5 py-4 bg-[#006464] text-white font-bold rounded-lg hover:bg-[#0F928C] transition">
                        LOGIN
                    </button>
                </div>
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

<!-- A tag style deve ficar fora do template, preferencialmente no final -->
<style scoped>
/* Estilos personalizados para os círculos e botão flutuante */
.gradient-sphere {
    background: radial-gradient(circle, #00C9D1 0%, #0F928C 100%);
}
</style>