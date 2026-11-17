package com.senai.cloud_io.service;

import static com.senai.cloud_io.service.Colunas.CIDADE;
import static com.senai.cloud_io.service.Colunas.CODIGO;
import static com.senai.cloud_io.service.Colunas.CONSULTOR;
import static com.senai.cloud_io.service.Colunas.DATA;
import static com.senai.cloud_io.service.Colunas.FATURAMENTO;
import static com.senai.cloud_io.service.Colunas.NIVEL;
import static com.senai.cloud_io.service.Colunas.NOME;
import static com.senai.cloud_io.service.Colunas.SERVICOS;
import static com.senai.cloud_io.service.Colunas.SEGMENTO;
import static com.senai.cloud_io.service.Colunas.UF;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.senai.cloud_io.config.ApiException;
import com.senai.cloud_io.config.AppProperties;
import com.senai.cloud_io.dto.ClienteDTO;
import com.senai.cloud_io.dto.UploadDtos.EnvioValidadasRequest;
import com.senai.cloud_io.dto.UploadDtos.LinhaEnvio;
import com.senai.cloud_io.service.ClienteValidacaoService.ResultadoLinha;

@Service
public class UploadService {
    private final ClienteValidacaoService validacao;
    private final AppProperties props;
    private final PythonEtlService python;

    public UploadService(ClienteValidacaoService validacao, AppProperties props, PythonEtlService python) {
        this.validacao = validacao;
        this.props = props;
        this.python = python;
    }

    public Map<String, Object> receberValidadas(EnvioValidadasRequest req) {
        if (req == null || req.linhas() == null || req.linhas().isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Não há linhas validadas para enviar.");
        }
        if (req.linhas().size() > props.upload().maxLinhas()) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A planilha passou do limite de " + props.upload().maxLinhas() + " linhas.");
        }

        String nome = nomeSeguro(req.arquivoNome());
        int descartadas = req.descartadas() == null ? 0 : Math.max(0, req.descartadas());

        List<ClienteDTO> brutas = new ArrayList<>(req.linhas().size());
        int posicao = 2;
        for (LinhaEnvio e : req.linhas()) {
            Map<String, String> campos = new LinkedHashMap<>();
            campos.put(CODIGO, e.codigoCliente());
            campos.put(NOME, e.nomeCliente());
            campos.put(CONSULTOR, e.consultor());
            campos.put(SEGMENTO, e.segmento());
            campos.put(NIVEL, e.nivelCliente());
            campos.put(FATURAMENTO, e.faturamentoAnual());
            campos.put(SERVICOS, e.servicosContratados());
            campos.put(DATA, e.dataContratacao());
            campos.put(CIDADE, e.cidade());
            campos.put(UF, e.uf());
            int numero = e.numeroLinha() == null ? posicao : e.numeroLinha();
            brutas.add(new ClienteDTO(numero, campos));
            posicao++;
        }
        return registrar(nome, brutas, descartadas);
    }

    private Map<String, Object> registrar(String nome, List<ClienteDTO> brutas, int descartadas) {
        List<ResultadoLinha> resultado = validacao.validar(brutas);

        List<Map<String, Object>> validas = new ArrayList<>();
        List<String> rejeitadas = new ArrayList<>();
        int comAviso = 0;
        for (ResultadoLinha r : resultado) {
            if (!r.valida()) {
                rejeitadas.add("Linha " + r.numero() + ": " + String.join(" | ", r.erros()));
                continue;
            }
            if (!r.avisos().isEmpty()) comAviso++;
            validas.add(paraPython(r));
        }
        if (validas.isEmpty()) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Nenhuma linha válida. Corrija a planilha e envie novamente.");
        }

        int recebidas = resultado.size() + descartadas;
        int invalidas = rejeitadas.size() + descartadas;
        python.enviar(nome, recebidas, invalidas, comAviso, validas);

        Map<String, Object> saida = new LinkedHashMap<>();
        saida.put("arquivoNome", nome);
        saida.put("status", "PROCESSADO");
        saida.put("totalLinhas", recebidas);
        saida.put("linhasValidas", validas.size());
        saida.put("linhasInvalidas", invalidas);
        saida.put("rejeitadas", rejeitadas);
        return saida;
    }

    private Map<String, Object> paraPython(ResultadoLinha r) {
        Map<String, String> v = r.valores();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("linha", r.numero());
        m.put("codigo_cliente", vazioParaNulo(v.get(CODIGO)));
        m.put("nome_cliente", vazioParaNulo(v.get(NOME)));
        m.put("consultor", vazioParaNulo(v.get(CONSULTOR)));
        m.put("segmento", vazioParaNulo(v.get(SEGMENTO)));
        m.put("nivel_cliente", vazioParaNulo(v.get(NIVEL)));
        m.put("faturamento_anual", vazioParaNulo(v.get(FATURAMENTO)));
        m.put("servicos_contratados", vazioParaNulo(v.get(SERVICOS)));
        m.put("data_contratacao", vazioParaNulo(v.get(DATA)));
        m.put("cidade", vazioParaNulo(v.get(CIDADE)));
        m.put("uf", vazioParaNulo(v.get(UF)));
        return m;
    }

    private static String vazioParaNulo(String s) {
        return s == null || s.isEmpty() ? null : s;
    }

    private static String nomeSeguro(String original) {
        String nome = original == null ? "planilha" : original.replace('\\', '/');
        nome = nome.substring(nome.lastIndexOf('/') + 1).replaceAll("[\\p{Cntrl}]", "").strip();
        if (nome.isEmpty()) nome = "planilha";
        return nome.length() > 255 ? nome.substring(nome.length() - 255) : nome;
    }
}

