package org.psnbtech.entidade;

import java.awt.Graphics2D;

import org.psnbtech.Jogo;
import org.psnbtech.util.Vetor2;

public class Bala extends Entidade {

	private static final double MAGNITUDO_VELOCIDADE = 6.75;

	private static final int VIDA_MAXIMA = 60;

	private int vida;

	public Bala(Entidade proprietario, double angulo) {
		super(new Vetor2(proprietario.posicao), new Vetor2(angulo).escalar(MAGNITUDO_VELOCIDADE), 2.0, 0);
		this.vida = VIDA_MAXIMA;
	}

	@Override
	public void atualizar(Jogo jogo) {
		super.atualizar(jogo);

		// Decrementa a vida da bala e a remove se necessário.
		this.vida--;
		if (vida <= 0) {
			marcarParaRemocao();
		}
	}

	@Override
	public void lidarComColisao(Jogo jogo, Entidade outra) {
		if (outra.getClass() != Jogador.class) {
			marcarParaRemocao();
		}
	}

	@Override
	public void desenhar(Graphics2D g, Jogo jogo) {
		g.drawOval(-1, -1, 2, 2);
	}
}