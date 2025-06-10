package gui;
import br.dev.weslei.calculadoraip.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaCalculadora extends JFrame {

	private JTextField inputField;
    private JTextArea outputArea;

    public TelaCalculadora() {
        setTitle("Calculadora de IP");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Tenta aplicar o tema moderno Nimbus
        aplicarNimbus();

        // Painel superior (entrada)
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputField = new JTextField(20);
        JButton calcularBtn = new JButton("Calcular");

        inputPanel.add(new JLabel("Digite o IP com máscara (ex: xxx.xxx.x.x/xx):"));
        inputPanel.add(inputField);
        inputPanel.add(calcularBtn);

        // Área de saída
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13)); // Fonte monoespaçada
        outputArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Ação do botão
        calcularBtn.addActionListener(this::calcular);

        // Adiciona componentes
        add(inputPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void aplicarNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus não disponível, usando visual padrão.");
        }
    }

    private void calcular(ActionEvent e) {
        outputArea.setText(""); //

        String entrada = inputField.getText().trim();

        try {
            CalcularIP calc = new CalcularIP(entrada);

            outputArea.append("📌 Resultado da Sub-rede\n\n");
            outputArea.append("IP: " + calc.getIP() + "\n");
            outputArea.append("CIDR: /" + calc.getCidr() + "\n");
            outputArea.append("Classe: " + calc.getClasse() + "\n");
            outputArea.append("Máscara decimal: " + calc.getMascaraDecimal() + "\n");
            outputArea.append("IP em binário: " + calc.getMascaraBinario() + "\n");

            int numRedes = calc.getNumeroDeRedes();
            if (numRedes == -1) {
                outputArea.append("Número de redes: Não aplicável\n");
            } else {
                outputArea.append("Número de redes possíveis: " + numRedes + "\n");
            }

            outputArea.append("Número de hosts disponíveis: " + calc.getNumeroDeHosts() + "\n\n");

            outputArea.append("🧩 Redes disponíveis:\n\n");
            List<String> redes = calc.getRedesDisponiveisComDetalhes();
            for (String rede : redes) {
                outputArea.append(rede + "\n");
            }

        } catch (Exception ex) {
            outputArea.setText("❌ Erro: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaCalculadora().setVisible(true);
        });
    }
}