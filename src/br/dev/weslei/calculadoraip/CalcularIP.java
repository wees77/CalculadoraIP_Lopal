package br.dev.weslei.calculadoraip;

import java.util.ArrayList;
import java.util.List;

public class CalcularIP {
    private String ip;
    private int cidr;
    private String classe;
    private String mascaraDecimal;

    public CalcularIP(String ipComMascara) {
        if (ipComMascara == null || !ipComMascara.contains("/")) {
            throw new IllegalArgumentException("Formato inválido. Use o formato: xxx.xxx.xxx.xxx/xx");
        }

        String[] partes = ipComMascara.split("/");

        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato inválido. Use o formato: xxx.xxx.xxx.xxx/xx");
        }

        this.ip = partes[0];

        try {
            this.cidr = Integer.parseInt(partes[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Máscara CIDR inválida. Use um número entre 0 e 32.");
        }

        if (cidr < 0 || cidr > 32) {
            throw new IllegalArgumentException("CIDR fora do intervalo. Deve estar entre 0 e 32.");
        }

        this.classe = descobrirClasseIP(ip);
        this.mascaraDecimal = gerarMascaraDecimal(cidr);
    }

    public String descobrirClasseIP(String ip) {
        String[] octetos = ip.split("\\.");
        int primeiroOcteto = Integer.parseInt(octetos[0]);

        if (primeiroOcteto >= 1 && primeiroOcteto <= 126)
            return "Classe A";
        else if (primeiroOcteto == 127)
            return "IP reservado (Loopback)";
        else if (primeiroOcteto >= 128 && primeiroOcteto <= 191)
            return "Classe B";
        else if (primeiroOcteto >= 192 && primeiroOcteto <= 223)
            return "Classe C";
        else if (primeiroOcteto >= 224 && primeiroOcteto <= 239)
            return "Classe D (Multicast)";
        else if (primeiroOcteto >= 240 && primeiroOcteto <= 254)
            return "Classe E (Experimental)";
        else
            return "IP inválido ou reservado";
    }

    public String gerarMascaraDecimal(int cidr) {
        int mascaraBin = (int) (0xffffffff << (32 - cidr));
        int octeto1 = (mascaraBin >> 24) & 0xFF;
        int octeto2 = (mascaraBin >> 16) & 0xFF;
        int octeto3 = (mascaraBin >> 8) & 0xFF;
        int octeto4 = mascaraBin & 0xFF;

        return octeto1 + "." + octeto2 + "." + octeto3 + "." + octeto4;
    }

    public String getMascaraBinario() {
        StringBuilder binario = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            if (i < cidr) {
                binario.append("1");
            } else {
                binario.append("0");
            }
            if ((i + 1) % 8 == 0 && (i + 1) < 32) { // Adiciona um ponto a cada 8 bits (um octeto)
                binario.append(".");
            }
        }
        return binario.toString();
    }

    public int getNumeroDeRedes() {
        int base;

        switch (classe) {
            case "Classe A":
                base = 8;
                break;
            case "Classe B":
                base = 16;
                break;
            case "Classe C":
                base = 24;
                break;
            default:
                return -1;
        }

        int bitsDeRede = cidr - base;
        return bitsDeRede >= 0 ? (int) Math.pow(2, bitsDeRede) : 1;
    }

    public int getNumeroDeHosts() {
        int bitsHost = 32 - cidr;
        if (bitsHost <= 0) {
            return 0;
        }
        return (int) Math.pow(2, bitsHost) - 2;
    }

    public List<String> getRedesDisponiveisComDetalhes() {
        List<String> detalhes = new ArrayList<>();

        int bitsHost = 32 - cidr;
        if (bitsHost <= 0 || cidr < 8 || cidr > 30) {
            detalhes.add("Listagem não aplicável para esse CIDR.");
            return detalhes;
        }

        int totalHosts = (int) Math.pow(2, bitsHost);
        int salto = totalHosts;

        String[] octetos = ip.split("\\.");
        int base1 = Integer.parseInt(octetos[0]);
        int base2 = Integer.parseInt(octetos[1]);
        int base3 = Integer.parseInt(octetos[2]);

        for (int i = 0; i < 256; i += salto) {
            int rede = i;
            int broadcast = rede + salto - 1;
            int primeiroHost = rede + 1;
            int ultimoHost = broadcast - 1;

            String redeStr = base1 + "." + base2 + "." + base3 + "." + rede;
            String broadcastStr = base1 + "." + base2 + "." + base3 + "." + broadcast;
            String primeiroStr = base1 + "." + base2 + "." + base3 + "." + primeiroHost;
            String ultimoStr = base1 + "." + base2 + "." + base3 + "." + ultimoHost;

            detalhes.add(
                "Rede: " + redeStr + "/" + cidr +
                "\n  Primeiro IP usável: " + primeiroStr +
                "\n  Último IP usável: " + ultimoStr +
                "\n  Broadcast: " + broadcastStr + "\n"
            );
        }

        return detalhes;
    }

    // Getters
    public String getIP() {
        return ip;
    }

    public int getCidr() {
        return cidr;
    }

    public String getClasse() {
        return classe;
    }

    public String getMascaraDecimal() {
        return mascaraDecimal;
    }
}