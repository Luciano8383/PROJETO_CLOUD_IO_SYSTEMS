import {defineStore} from "pinia";
import * as XLSX from "xlsx";

export const useUploadStore = defineStore("upload", {
  state: () => ({
    arquivo: null,
    nomeArquivoProcessado: "",
    dadosOriginais: [],
    dadosTratados: [],
    erros: [],
    carregando: false
    }),


getters: {
  totalClientes: (state) => state.dadosTratados.length,
  totalErros: (state) => state.erros.length,
  clientesNivelA: (state) => state.dadosTratados.filter((cliente) => cliente.nivel_cliente === "A").length,

  inconsistencias: (state) => 
state.dadosTratados.reduce((linhasComErro, cliente, index) => {
  const motivos = [];
  const nivel = String (cliente.nivel_cliente|| "").trim().toUpperCase();
  const faturamentoOriginal = cliente.faturamento_anual;
  const faturamentoTexto = String(faturamentoOriginal ?? "").trim().toLowerCase();
  const faturamento = typeof faturamentoOriginal === "number"
    ? faturamentoOriginal
    : Number(faturamentoTexto.replace(/\./g, "").replace(",", "."));

  if (!faturamentoTexto || !Number.isFinite(faturamento) || faturamento <= 0) {
    motivos.push("Faturamento não pode ser vazio e deve ser maior que zero.");
  }

  if (!String (cliente.consultor || "").trim()) {
    motivos.push("Consultor não pode ser vazio.");
  }

  if (!String (cliente.nome_cliente || "").trim()) {
    motivos.push("Nome do cliente não pode ser vazio.");
  }

  if (!String (cliente.segmento || "").trim()) {
    motivos.push("Segmento não pode ser vazio.");
  }

  if (!["A", "B", "C"].includes(nivel)) {
    motivos.push("Nível do cliente deve ser A, B ou C.");
  }

  if (motivos.length > 0) {
    linhasComErro.push({
      ...cliente,
      linha: index + 2,
      motivos
    });
  }

  return linhasComErro;
}, []),

  temDados: (state) => state.dadosTratados.length > 0,
},

actions: {
  selecionarArquivo(file) {this.arquivo = file; this.erros = []},
  validarArquivo() {
    if (!this.arquivo) {
      this.erros.push("Nenhum arquivo selecionado.");
      return false;
    }

    const nome = this.arquivo.name.toLowerCase();
    const valido = nome.endsWith(".xlsx") || nome.endsWith(".xls") || nome.endsWith(".csv");

    if (!valido) this.erros.push("Formato invalido.")
    return valido;
  },

  normalizarSegmento(valor) {
    const raw = String(valor).trim();
    if (!raw) return "-";

    const compactado = raw.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase()

    const mapaSegmentos = {
      ind :"Indústria",
      industrial: "Indústria",
      comercio: "Comércio",
      servicos: "Serviços",
      servico: "Serviços",
      educacao: "Educação",
      saude: "Saúde",
      tecnologia: "Tecnologia",
    };

    return mapaSegmentos [compactado] || raw;
    
  },
 
  async processarPlanilha() {
    if (!this.validarArquivo()) return;

    this.carregando = true;

    try {
      const buffer = await this.arquivo.arrayBuffer();
      const workbook = XLSX.read(buffer, { type: "array" });
      const primeiraAba = workbook.SheetNames[0];

      if (!primeiraAba) {
        throw new Error("A planilha não contém nenhuma aba para análise.");
      }

      const planilha = workbook.Sheets[primeiraAba];
      const linhas = XLSX.utils.sheet_to_json(planilha, { defval: "" });

      if (linhas.length === 0) {
        throw new Error("A primeira aba não contém registros para análise.");
      }

      this.dadosOriginais = linhas;
      this.dadosTratados = linhas.map((linha)=> this.tratarLinha(linha));
      this.nomeArquivoProcessado = this.arquivo.name;
    } finally {
      this.carregando = false;
    }
  },
  tratarLinha(linha) {
    const segmentoNormalizado = this.normalizarSegmento(linha.segmento);
    return {
      ...linha,
      consultor: String(linha.consultor || "").trim(), 
      segmento: segmentoNormalizado,
      nome_cliente: String(linha.nome_cliente || "").trim() || "sem nome",
      nivel_cliente: String(linha.nivel_cliente || linha.nivel || "").trim().toUpperCase() || "SEM NÍVEL",

    }
  }
}
});
