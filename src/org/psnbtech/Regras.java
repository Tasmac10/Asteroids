package org.psnbtech;

import javax.swing.*;
import java.awt.*;

public class Regras extends JFrame {

    public Regras() {
        super("Regras do Jogo");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JTextArea regrasText = new JTextArea();
        regrasText.setText("Regras do Jogo:\n\n" +
                "1. Use as setas ou W, A, S, D para mover a nave.\n" +
                "2. Pressione ESPAÇO para atirar.\n" +
                "3. Evite os asteroides e destrua-os para ganhar pontos.\n" +
                "4. Pressione P para pausar o jogo.\n" +
                "5. Pressione R para reiniciar o jogo após o fim.");
        regrasText.setEditable(false);
        regrasText.setWrapStyleWord(true);
        regrasText.setLineWrap(true);
        regrasText.setMargin(new Insets(10, 10, 10, 10));

        add(new JScrollPane(regrasText), BorderLayout.CENTER);

        setVisible(true);
    }
}