package br.dev.weslei.calculadoraip;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Digite um IP com máscara (formato: xxx.xxx.xxx.xxx/xx): ");
            String entrada = scanner.nextLine();

            CalcularIP calc = new CalcularIP(entrada);

            System.out.println("IP: " + calc.getIP());
            System.out.println("CIDR: /" + calc.getCidr());
            System.out.println("Classe: " + calc.getClasse());
            System.out.println("Máscara decimal: " + calc.getMascaraDecimal());
            System.out.println("IP em binário: " + calc.getIPBinario());

            int numRedes = calc.getNumeroDeRedes();
            if (numRedes == -1) {
                System.out.println("Número de redes não aplicável para esta classe de IP.");
            } else {
                System.out.println("Número de redes possíveis: " + numRedes);
            }

            System.out.println("Número de hosts disponíveis: " + calc.getNumeroDeHosts());

            System.out.println("\nRedes disponíveis com detalhes:");
            for (String rede : calc.getRedesDisponiveisComDetalhes()) {
                System.out.println(rede);
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}