package org.POO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import javax.swing.JPanel;

import org.POO.entidade.Entidade;
import org.POO.util.Vetor2;

public class PainelMundo extends JPanel {

	public static final int TAMANHO_MUNDO = 550;

	private static final Font FONTE_TITULO = new Font("Dialog", Font.PLAIN, 25);
	private static final Font FONTE_SUBTITULO = new Font("Dialog", Font.PLAIN, 15);

	private Jogo jogo;

	public PainelMundo(Jogo jogo) {
		this.jogo = jogo;

		setPreferredSize(new Dimension(TAMANHO_MUNDO, TAMANHO_MUNDO));
		setBackground(Color.BLACK);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Necessário, senão a renderização fica confusa.

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
		AffineTransform identidade = g2d.getTransform();

		Iterator<Entidade> iter = jogo.getEntidades().iterator();
		while (iter.hasNext()) {
			Entidade entidade = iter.next();
			if (entidade != jogo.getJogador() || jogo.podeDesenharJogador()) {
				Vetor2 pos = entidade.getPosicao();

				desenharEntidade(g2d, entidade, pos.x, pos.y);
				g2d.setTransform(identidade);

				double raio = entidade.getRaioColisao();
				double x = (pos.x < raio) ? pos.x + TAMANHO_MUNDO
						: (pos.x > TAMANHO_MUNDO - raio) ? pos.x - TAMANHO_MUNDO : pos.x;
				double y = (pos.y < raio) ? pos.y + TAMANHO_MUNDO
						: (pos.y > TAMANHO_MUNDO - raio) ? pos.y - TAMANHO_MUNDO : pos.y;

				if (x != pos.x || y != pos.y) {
					desenharEntidade(g2d, entidade, x, y);
					g2d.setTransform(identidade);
				}
			}
		}

		if (!jogo.isFimDeJogo()) {
			g.drawString("Pontuação: " + jogo.getPontuacao(), 10, 15);
		}

		if (jogo.isFimDeJogo()) {
			desenharTextoCentralizado("Fim de Jogo", FONTE_TITULO, g2d, -25);
			desenharTextoCentralizado("Pontuação Final: " + jogo.getPontuacao(), FONTE_SUBTITULO, g2d, 10);
			desenharTextoCentralizado("Aperte P para Perder novamente kkkk", FONTE_SUBTITULO, g2d, 200);
		} else if (jogo.isPausado()) {
			desenharTextoCentralizado("Pausado", FONTE_TITULO, g2d, -25);
		} else if (jogo.isMostrandoNivel()) {
			desenharTextoCentralizado("Nível: " + jogo.getNivel(), FONTE_TITULO, g2d, -25);
		}

		g2d.translate(15, 30);
		g2d.scale(0.85, 0.85);
		for (int i = 0; i < jogo.getVidas(); i++) {
			g2d.drawLine(-8, 10, 0, -10);
			g2d.drawLine(8, 10, 0, -10);
			g2d.drawLine(-6, 6, 6, 6);
			g2d.translate(30, 0);
		}
	}

	private void desenharTextoCentralizado(String texto, Font fonte, Graphics2D g, int y) {
		g.setFont(fonte);
		g.drawString(texto, TAMANHO_MUNDO / 2 - g.getFontMetrics().stringWidth(texto) / 2, TAMANHO_MUNDO / 2 + y);
	}

	private void desenharEntidade(Graphics2D g2d, Entidade entidade, double x, double y) {
		g2d.translate(x, y);
		double rotacao = entidade.getRotacao();
		if (rotacao != 0.0f) {
			g2d.rotate(entidade.getRotacao());
		}
		entidade.desenhar(g2d, jogo);
	}
}
