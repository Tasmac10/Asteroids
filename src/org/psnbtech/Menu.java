package org.psnbtech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Menu extends JFrame {
    private static final int WIDTH = 550;
    private static final int HEIGHT = 550;

    public Menu() {
        setTitle("Menu do Jogo");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Carregar a fonte personalizada
        Font titleFont = loadCustomFont("fonte/joystix monospace.otf", 50);
        Font buttonFont = loadCustomFont("fonte/joystix monospace.otf", 20);

        // Painel para o menu
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                // Desenhar o contorno do título
                String title = "ASTEROIDES";
                g.setFont(titleFont);
                int titleWidth = g.getFontMetrics().stringWidth(title); // Calcular largura do título
                drawOutlinedText(g, title, (getWidth() - titleWidth) / 2, 120, titleFont, Color.WHITE, Color.BLACK);
            }
        };
        panel.setLayout(null); // Layout nulo para posicionamento absoluto

        // Botão Jogar
        JButton jogarButton = new JButton("JOGAR");
        jogarButton.setFont(buttonFont); // Usar fonte personalizada
        jogarButton.setForeground(Color.WHITE); // Texto em branco
        jogarButton.setBackground(Color.BLACK); // Botão preto
        jogarButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Contorno branco
        jogarButton.setBounds(175, 260, 200, 50); // Posição ajustada para baixo
        jogarButton.setFocusPainted(false);
        jogarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ação ao clicar no botão Jogar
                new Thread(() -> {
                    dispose(); // Fechar o menu
                    Jogo jogo = new Jogo();
                    jogo.iniciarJogo();
                }).start();
            }
        });
        panel.add(jogarButton);

        // Botão Regras
        JButton regrasButton = new JButton("REGRAS");
        regrasButton.setFont(buttonFont); // Usar fonte personalizada
        regrasButton.setForeground(Color.WHITE); // Texto em branco
        regrasButton.setBackground(Color.BLACK); // Botão preto
        regrasButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Contorno branco
        regrasButton.setBounds(175, 330, 200, 50); // Posição ajustada para baixo
        regrasButton.setFocusPainted(false);
        // Modifique o ActionListener do botão "REGRAS" na classe Menu
        regrasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRegras();
            }
        });
        panel.add(regrasButton);

        add(panel);
        setVisible(true);
    }

    private void mostrarRegras() {
        JDialog regrasDialog = new JDialog(this, "Regras do Jogo", true);
        regrasDialog.setSize(400, 300);
        regrasDialog.setLocationRelativeTo(this);
        regrasDialog.setResizable(false);

        JTextArea regrasText = new JTextArea();
        regrasText.setText("Regras do Jogo:\n\n1. Use as setas para mover.\n2. Pressione espaço para atirar.\n3. Evite os asteroides.\n4. Ganhe pontos destruindo asteroides.");
        regrasText.setEditable(false);
        regrasText.setWrapStyleWord(true);
        regrasText.setLineWrap(true);
        regrasText.setMargin(new Insets(10, 10, 10, 10));

        regrasDialog.add(new JScrollPane(regrasText));
        regrasDialog.setVisible(true);
    }

    // Método para carregar a fonte personalizada
    private Font loadCustomFont(String fontPath, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size); // Retorna uma fonte padrão em caso de erro
        }
    }

    // Método para desenhar texto com contorno
    private void drawOutlinedText(Graphics g, String text, int x, int y, Font font, Color outlineColor, Color fillColor) {
        g.setFont(font);
        // Desenhar contorno mais largo
        g.setColor(outlineColor);
        g.drawString(text, x - 3, y); // Esquerda
        g.drawString(text, x + 3, y); // Direita
        g.drawString(text, x, y - 3); // Cima
        g.drawString(text, x, y + 3); // Baixo
        g.drawString(text, x - 3, y - 3); // Canto superior esquerdo
        g.drawString(text, x + 3, y - 3); // Canto superior direito
        g.drawString(text, x - 3, y + 3); // Canto inferior esquerdo
        g.drawString(text, x + 3, y + 3); // Canto inferior direito

        // Desenhar texto preenchido
        g.setColor(fillColor);
        g.drawString(text, x, y);
    }

    public static void main(String[] args) {
        new Menu();
    }
}