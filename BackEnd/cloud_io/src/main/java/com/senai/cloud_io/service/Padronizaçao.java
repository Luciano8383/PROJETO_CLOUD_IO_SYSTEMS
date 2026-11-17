package com.senai.cloud_io.service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

final class Padronizacao {
    private Padronizacao() {}

    private static final Map<String, String> SEGMENTOS = Map.ofEntries(
            Map.entry("ind", "Indústria"),
            Map.entry("industria", "Indústria"),
            Map.entry("comercio", "Comércio"),
            Map.entry("servicos", "Serviços"),
            Map.entry("servico", "Serviços"),
            Map.entry("saude", "Saúde"),
            Map.entry("educacao", "Educação"),
            Map.entry("tecnologia", "Tecnologia"));

    private static final Set<String> PARTICULAS = Set.of("de", "da", "do", "das", "dos", "e");

    static String simples(String texto) {
        if (texto == null) return "";
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return semAcento.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
    }

    static String segmento(String texto) {
        if (texto == null) return null;
        String chave = simples(texto);
        return SEGMENTOS.get(chave);
    }

    static String tituloNome(String texto) {
        if (texto == null || texto.isBlank()) return "";

        String[] partes = texto.trim().split("\\s+");
        StringBuilder saida = new StringBuilder();
        for (int i = 0; i < partes.length; i++) {
            String parte = partes[i].replaceAll("[^\\p{L}\\p{N}]", "");
            if (parte.isEmpty()) continue;

            String lower = parte.toLowerCase(Locale.ROOT);
            if (i > 0 && PARTICULAS.contains(lower)) {
                saida.append(lower);
            } else {
                saida.append(Character.toUpperCase(lower.charAt(0)))
                        .append(lower.substring(1));
            }
            if (i < partes.length - 1) {
                saida.append(' ');
            }
        }
        return saida.toString();
    }
}

