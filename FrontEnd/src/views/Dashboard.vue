
<template>
  <div class="min-h-screen bg-[#f3f4f6] text-gray-800 font-sans p-4 md:p-6 selection:bg-[#00C9D1] selection:text-[#484848]">
    
    <!-- Dashboard Container Main Card -->
    <div class="max-w-7xl mx-auto bg-white border border-gray-300 rounded-lg shadow-md overflow-hidden">
      
      <!-- Top Header / Bar -->
      <header class="bg-[#006464] text-white px-6 py-3.5 flex flex-col sm:flex-row justify-between items-center gap-4">
        <a href="#" class="inline-block transition hover:opacity-80">
    <img 
      src="../assets/Image_2.jpg" 
      alt="Logo Cloud IO Systems" 
      class="h-24 w-auto object-contain" 
    />
  </a>
        
        <!-- Filter Dropdown -->
        <div class="flex items-center space-x-2 text-sm">
          <label for="segment-filter" class="text-gray-300">Filtros:</label>
          <div class="relative">
            <select 
              id="segment-filter"
              v-model="selectedFilter" 
              class="bg-transparent text-white border-none pr-7 py-1 text-sm font-medium focus:outline-none cursor-pointer appearance-none"
            >
              <option value="Todos os Segmentos" class="bg-[#484848] text-white">Todos os Segmentos ▼</option>
              <option value="Indústria" class="bg-[#484848] text-white">Indústria ▼</option>
              <option value="Comércio" class="bg-[#484848] text-white">Comércio ▼</option>
              <option value="Serviços" class="bg-[#484848] text-white">Serviços ▼</option>
            </select>
            
          </div>
        </div>
      </header>

      <!-- Dashboard Body Container -->
      <div class="bg-[#0F928C] p-6 space-y-6">
        
        <!-- Top Metrics KPI Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          
          <!-- Card 1: Total de Clientes -->
          <div class="bg-white border border-gray-200 rounded-md p-6 text-center shadow-xs">
            <h2 class="text-xs font-bold text-gray-500 uppercase tracking-wider mb-2">
              TOTAL DE CLIENTES
            </h2>
            <p class="text-4xl font-extrabold text-[#006464] tracking-tight">
              {{ filteredMetrics.totalClientes }}
            </p>
          </div>

          <!-- Card 2: Faturamento Médio -->
          <div class="bg-white border border-gray-200 rounded-md p-6 text-center shadow-xs">
            <h2 class="text-xs font-bold text-gray-500 uppercase tracking-wider mb-2">
              FATURAMENTO MÉDIO
            </h2>
            <p class="text-4xl font-extrabold text-[#006464] tracking-tight">
              {{ filteredMetrics.faturamentoMedio }}
            </p>
          </div>

          <!-- Card 3: Serviços Ativos -->
          <div class="bg-white border border-gray-200 rounded-md p-6 text-center shadow-xs">
            <h2 class="text-xs font-bold text-gray-500 uppercase tracking-wider mb-2">
              SERVIÇOS ATIVOS
            </h2>
            <p class="text-4xl font-extrabold text-[#006464] tracking-tight">
              {{ filteredMetrics.servicosAtivos }}
            </p>
          </div>

        </div>

        <!-- Charts Grid Section -->
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
          
          <!-- Pie Chart Card: Distribuição por Segmento -->
          <div class="lg:col-span-5 bg-white border border-gray-200 rounded-md p-6 flex flex-col justify-between">
            <h3 class="text-base font-bold text-gray-800 text-center mb-6">
              Distribuição por Segmento
            </h3>

            <div class="flex flex-col sm:flex-row items-center justify-around gap-6 h-full">
              <!-- Pie Canvas Wrapper -->
              <div class="relative w-48 h-48 flex-shrink-0">
                <canvas ref="pieCanvas"></canvas>
              </div>

              <!-- Custom Legend -->
              <div class="space-y-3 text-sm text-gray-600">
                <div class="flex items-center space-x-3">
                  <span class="w-5 h-4 bg-[#484848] inline-block rounded-xs"></span>
                  <span>Indústria</span>
                </div>
                <div class="flex items-center space-x-3">
                  <span class="w-5 h-4 bg-[#00C9D1] inline-block rounded-xs"></span>
                  <span>Comércio</span>
                </div>
                <div class="flex items-center space-x-3">
                  <span class="w-5 h-4 bg-[#BEED3B] inline-block rounded-xs"></span>
                  <span>Serviços</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Bar Chart Card: Clientes por Nível (A/B/C) -->
          <div class="lg:col-span-7 bg-white border border-gray-200 rounded-md p-6">
            <h3 class="text-base font-bold text-gray-800 text-center mb-4">
              Clientes por Nível (A/B/C)
            </h3>

            <!-- Bar Canvas Wrapper -->
            <div class="relative h-64 w-full">
              <canvas ref="barCanvas"></canvas>
            </div>
          </div>

        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import {
  Chart,
  PieController,
  BarController,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip
} from 'chart.js';

// Registrar componentes do Chart.js
Chart.register(PieController, BarController, ArcElement, BarElement, CategoryScale, LinearScale, Tooltip);

// Referências dos Canvas
const pieCanvas = ref(null);
const barCanvas = ref(null);

let pieChartInstance = null;
let barChartInstance = null;

// Filtro Selecionado
const selectedFilter = ref('Todos os Segmentos');

// Dados dos Indicadores por Filtro
const metricsData = {
  'Todos os Segmentos': { totalClientes: '1.250', faturamentoMedio: 'R$ 450k', servicosAtivos: '3.420' },
  'Indústria': { totalClientes: '562', faturamentoMedio: 'R$ 620k', servicosAtivos: '1.800' },
  'Comércio': { totalClientes: '263', faturamentoMedio: 'R$ 310k', servicosAtivos: '670' },
  'Serviços': { totalClientes: '425', faturamentoMedio: 'R$ 380k', servicosAtivos: '950' }
};

// Dados para os Gráficos
const chartDatasets = {
  'Todos os Segmentos': {
    pie: [45, 21, 34],
    bar: [150, 450, 650]
  },
  'Indústria': {
    pie: [100, 0, 0],
    bar: [70, 210, 282]
  },
  'Comércio': {
    pie: [0, 100, 0],
    bar: [30, 83, 150]
  },
  'Serviços': {
    pie: [0, 0, 100],
    bar: [50, 157, 218]
  }
};

// Computed metric
const filteredMetrics = computed(() => metricsData[selectedFilter.value] || metricsData['Todos os Segmentos']);

// Inicialização dos Gráficos
const initCharts = () => {
  const dataset = chartDatasets[selectedFilter.value];

  // Gráfico de Rosca/Pizza (Pie Chart)
  if (pieCanvas.value) {
    pieChartInstance = new Chart(pieCanvas.value, {
      type: 'pie',
      data: {
        labels: ['Indústria', 'Comércio', 'Serviços'],
        datasets: [{
          data: dataset.pie,
          backgroundColor: ['#484848', '#00C9D1', '#BEED3B'],
          borderWidth: 1.5,
          borderColor: '#ffffff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: { enabled: true }
        }
      }
    });
  }

  // Gráfico de Barras (Bar Chart)
  if (barCanvas.value) {
    barChartInstance = new Chart(barCanvas.value, {
      type: 'bar',
      data: {
        labels: ['Nível A', 'Nível B', 'Nível C'],
        datasets: [{
          data: dataset.bar,
          backgroundColor: '#006464',
          borderWidth: 0,
          barThickness: 65
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: {
            beginAtZero: true,
            max: 700,
            ticks: {
              stepSize: 100,
              color: '#484848',
              font: { size: 11 }
            },
            grid: {
              color: '#d1d5db'
            }
          },
          x: {
            ticks: {
              color: '#484848',
              font: { size: 11 }
            },
            grid: {
              display: false
            }
          }
        }
      }
    });
  }
};

// Atualização dinâmica ao mudar o filtro
const updateCharts = () => {
  const dataset = chartDatasets[selectedFilter.value];
  
  if (pieChartInstance) {
    pieChartInstance.data.datasets[0].data = dataset.pie;
    pieChartInstance.update();
  }

  if (barChartInstance) {
    barChartInstance.data.datasets[0].data = dataset.bar;
    barChartInstance.update();
  }
};

watch(selectedFilter, () => {
  updateCharts();
});

onMounted(() => {
  initCharts();
});
</script>

<style scoped>
/* Ajuste fino da renderização de fontes e transições */
select {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}
</style>