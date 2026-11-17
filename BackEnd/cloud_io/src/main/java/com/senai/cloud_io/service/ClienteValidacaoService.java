package com.senai.cloud_io.service;

import static com.senai.cloud_io.service.Colunas.CODIGO;
import static com.senai.cloud_io.service.Colunas.CONSULTOR;
import static com.senai.cloud_io.service.Colunas.DATA;
import static com.senai.cloud_io.service.Colunas.FATURAMENTO;
import static com.senai.cloud_io.service.Colunas.NIVEL;
import static com.senai.cloud_io.service.Colunas.NOME;
import static com.senai.cloud_io.service.Colunas.OBRIGATORIAS;
import static com.senai.cloud_io.service.Colunas.SERVICOS;
import static com.senai.cloud_io.service.Colunas.SEGMENTO;
import static com.senai.cloud_io.service.Colunas.TAMANHO_MAXIMO;
import static com.senai.cloud_io.service.Colunas.TODAS;
import static com.senai.cloud_io.service.Colunas.UF;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.senai.cloud_io.dto.ClienteDTO;

@Service
public class ClienteValidacaoService {

    public record ResultadoLinha(int numero, Map<String, String> valores, List<String> erros, List<String> avisos) {
        public boolean valida() {
            return erros.isEmpty();
        }
    }

    private static final Pattern RE_CODIGO = Pattern.compile("^[A-Za-z0-9_-]{1,60}$");
    private static final Pattern RE_UF = Pattern.compile("^[A-Za-z]{2}$");
    private static final Pattern DATA_ISO = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})([T ].*)?$");
    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("d/M/uuuu");
    private static final BigDecimal LIMITE_FATURAMENTO = new BigDecimal("1000000000000");
    private static final Set<String> NIVEIS = Set.of("A", "B", "C");

    public List<ResultadoLinha> validar(List<ClienteDTO> linhas) {
        List<ResultadoLinha> saida = new ArrayList<>();
        Map<String, Integer> codigos = new HashMap<>();
        Map<String, Integer> conteudos = new HashMap<>();
        Map<String, String> codigoDoConteudo = new HashMap<>();
        LocalDate hoje = LocalDate.now();

        for (ClienteDTO linha : linhas) {
            List<String> erros = new ArrayList<>();
            List<String> avisos = new ArrayList<>();
            Map<String, String> valores = new LinkedHashMap<>();

            for (String campo : TODAS) {
                String bruto = linha.campos().get(campo);
                String limpo = bruto == null ? "" : bruto.strip().replaceAll("\\s{2,}", " ");
                if (bruto != null && !limpo.equals(bruto)) {
                    avisos.add(campo + ": espaços extras removidos");
                }
                valores.put(campo, limpo);
                Integer max = TAMANHO_MAXIMO.get(campo);
                if (max != null && limpo.length() > max) {
                    erros.add(campo + ": excede " + max + " caracteres");
                    valores.put(campo, limpo.substring(0, max));
                }
            }

            for (String campo : OBRIGATORIAS) {
                if (valores.get(campo).isEmpty()) erros.add(campo + ": campo obrigatório vazio");
            }

            String codigo = valores.get(CODIGO);
            if (!codigo.isEmpty()) {
                if (!RE_CODIGO.matcher(codigo).matches()) {
                    erros.add(CODIGO + ": use apenas letras, números, '-' ou '_'");
                }
                String chave = codigo.toUpperCase();
                Integer primeira = codigos.putIfAbsent(chave, linha.numero());
                if (primeira != null) {
                    erros.add(CODIGO + ": código duplicado (já usado na linha " + primeira + ")");
                }
            }

            String segmento = valores.get(SEGMENTO);
            if (!segmento.isEmpty()) {
                String padrao = Padronizacao.segmento(segmento);
                if (padrao == null) {
                    erros.add(SEGMENTO + ": '" + segmento + "' não reconhecido (use Indústria, Comércio, Serviços, Saúde, Educação ou Tecnologia)");
                } else if (!padrao.equals(segmento)) {
                    avisos.add(SEGMENTO + ": '" + segmento + "' será padronizado para " + padrao);
                }
            }

            String consultor = valores.get(CONSULTOR);
            if (!consultor.isEmpty()) {
                String ajustado = Padronizacao.tituloNome(consultor);
                if (!ajustado.equals(consultor)) avisos.add(CONSULTOR + ": '" + consultor + "' será padronizado para " + ajustado);
            }

            String nivel = valores.get(NIVEL);
            if (!nivel.isEmpty()) {
                String maiusculo = nivel.toUpperCase();
                if (!NIVEIS.contains(maiusculo)) {
                    erros.add(NIVEL + ": '" + nivel + "' inválido (use A, B ou C)");
                } else if (!maiusculo.equals(nivel)) {
                    avisos.add(NIVEL + ": '" + nivel + "' será convertido para " + maiusculo);
                }
            }

            String fat = valores.get(FATURAMENTO);
            if (!fat.isEmpty()) {
                Optional<BigDecimal> numero = lerDecimal(fat);
                if (numero.isEmpty()) {
                    erros.add(FATURAMENTO + ": '" + fat + "' não é um número");
                } else if (numero.get().signum() < 0) {
                    erros.add(FATURAMENTO + ": não pode ser negativo");
                } else if (numero.get().compareTo(LIMITE_FATURAMENTO) >= 0) {
                    erros.add(FATURAMENTO + ": valor acima do permitido");
                } else {
                    if (numero.get().signum() == 0) avisos.add(FATURAMENTO + ": valor zerado");
                    valores.put(FATURAMENTO, numero.get().setScale(2, RoundingMode.HALF_UP).toPlainString());
                }
            }

            String servicos = valores.get(SERVICOS);
            if (!servicos.isEmpty() && Arrays.stream(servicos.split(";")).allMatch(s -> s.isBlank())) {
                erros.add(SERVICOS + ": nenhum serviço informado");
            }

            String data = valores.get(DATA);
            if (!data.isEmpty()) {
                Optional<LocalDate> d = lerData(data);
                if (d.isEmpty() || d.get().getYear() < 1990) {
                    erros.add(DATA + ": '" + data + "' não é uma data válida (use dd/mm/aaaa ou aaaa-mm-dd)");
                } else {
                    if (d.get().isAfter(hoje)) avisos.add(DATA + ": data no futuro");
                    valores.put(DATA, d.get().toString());
                }
            }

            String uf = valores.get(UF);
            if (!uf.isEmpty() && !RE_UF.matcher(uf).matches()) {
                erros.add(UF + ": '" + uf + "' inválida (use a sigla com 2 letras)");
            }

            if (!codigo.isEmpty() && !valores.get(NOME).isEmpty()) {
                String chave = valores.get(NOME).toLowerCase() + "|" + valores.get(FATURAMENTO) + "|" + valores.get(DATA);
                Integer anterior = conteudos.putIfAbsent(chave, linha.numero());
                if (anterior != null && !codigo.equalsIgnoreCase(codigoDoConteudo.get(chave))) {
                    avisos.add(NOME + ": possível duplicidade (mesmo nome, faturamento e data da linha " + anterior + ")");
                }
                codigoDoConteudo.putIfAbsent(chave, codigo);
            }

            saida.add(new ResultadoLinha(linha.numero(), valores, erros, avisos));
        }
        return saida;
    }

    static Optional<BigDecimal> lerDecimal(String texto) {
        String t = texto.replace("R$", "").replace(' ', ' ').replaceAll("\\s", "");
        if (t.isEmpty() || !t.matches("^-?[0-9.,]+$")) return Optional.empty();
        boolean ponto = t.contains("."), virgula = t.contains(",");
        if (ponto && virgula) {
            if (t.lastIndexOf(',') > t.lastIndexOf('.')) t = t.replace(".", "").replace(',', '.');
            else t = t.replace(",", "");
        } else if (virgula) {
            t = t.chars().filter(c -> c == ',').count() > 1 ? t.replace(",", "") : t.replace(',', '.');
        } else if (ponto && t.matches("^-?\\d{1,3}(\\.\\d{3})+$")) {
            t = t.replace(".", "");
        }
        try {
            return Optional.of(new BigDecimal(t));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    static Optional<LocalDate> lerData(String texto) {
        try {
            var iso = DATA_ISO.matcher(texto);
            if (iso.matches()) return Optional.of(LocalDate.parse(iso.group(1)));
            return Optional.of(LocalDate.parse(texto, BR));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
