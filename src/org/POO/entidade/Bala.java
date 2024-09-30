package org.POO.entidade;

import java.awt.Graphics2D;

import org.POO.Jogo;
import org.POO.util.Vetor2;

public class Bala extends Entidade {

	// Constante que define a magnitude da velocidade da bala
	private static final double MAGNITUDO_VELOCIDADE = 6.75;

	// Constante que define a vida máxima da bala (em frames)
	private static final int VIDA_MAXIMA = 60;

	// Variável que armazena a vida atual da bala
	private int vida;

	// Construtor da bala
	public Bala(Entidade proprietario, double angulo) {
		// Inicializa a bala com a posição do proprietário, velocidade baseada no ângulo e raio de colisão
		super(new Vetor2(proprietario.posicao), new Vetor2(angulo).escalar(MAGNITUDO_VELOCIDADE), 2.0, 0);
		this.vida = VIDA_MAXIMA; // Define a vida inicial da bala
	}

	// Método para atualizar o estado da bala
	@Override
	public void atualizar(Jogo jogo) {
		super.atualizar(jogo); // Atualiza a posição da bala

		// Decrementa a vida da bala e a remove se necessário
		this.vida--;
		if (vida <= 0) {
			marcarParaRemocao(); // Marca a bala para remoção se a vida chegar a zero
		}
	}

	// Método para lidar com colisões
	@Override
	public void lidarComColisao(Jogo jogo, Entidade outra) {
		if (outra.getClass() != Jogador.class) {
			marcarParaRemocao(); // Marca a bala para remoção se colidir com algo que não seja o jogador
		}
	}

	// Método para desenhar a bala
	@Override
	public void desenhar(Graphics2D g, Jogo jogo) {
		g.drawOval(-1, -1, 2, 2); // Desenha a bala como um pequeno círculo
	}
}