package org.psnbtech.entidade;

import java.awt.Graphics2D;
import java.util.Random;

import org.psnbtech.Jogo;
import org.psnbtech.PainelMundo;
import org.psnbtech.util.Vetor2;

public class Asteroide extends Entidade {

	// Constantes para configuração da rotação do asteroide
	private static final double ROTACAO_MINIMA = 0.0075;
	private static final double ROTACAO_MAXIMA = 0.0175;
	private static final double VARIACAO_ROTACAO = ROTACAO_MAXIMA - ROTACAO_MINIMA;

	// Constantes para configuração da velocidade do asteroide
	private static final double VELOCIDADE_MINIMA = 0.75;
	private static final double VELOCIDADE_MAXIMA = 1.65;
	private static final double VARIACAO_VELOCIDADE = VELOCIDADE_MAXIMA - VELOCIDADE_MINIMA;

	// Constantes para configuração da distância de spawn do asteroide
	private static final double DISTANCIA_MINIMA = 200.0;
	private static final double DISTANCIA_MAXIMA = PainelMundo.TAMANHO_MUNDO / 2.0;
	private static final double VARIACAO_DISTANCIA = DISTANCIA_MAXIMA - DISTANCIA_MINIMA;

	private static final float ATUALIZACOES_SPAWN = 10; // Número de atualizações para spawn

	private TamanhoAsteroide tamanho; // Tamanho do asteroide
	private double velocidadeRotacao; // Velocidade de rotação do asteroide

	// Construtor para criar um asteroide com tamanho grande
	public Asteroide(Random random) {
		super(calcularPosicao(random), calcularVelocidade(random), TamanhoAsteroide.Grande.raio, TamanhoAsteroide.Grande.valorEliminacao);
		this.velocidadeRotacao = -ROTACAO_MINIMA + (random.nextDouble() * VARIACAO_ROTACAO);
		this.tamanho = TamanhoAsteroide.Grande;
	}

	// Construtor para criar um asteroide com base em um asteroide pai e um tamanho específico
	public Asteroide(Asteroide pai, TamanhoAsteroide tamanho, Random random) {
		super(new Vetor2(pai.posicao), calcularVelocidade(random), tamanho.raio, tamanho.valorEliminacao);
		this.velocidadeRotacao = ROTACAO_MINIMA + (random.nextDouble() * VARIACAO_ROTACAO);
		this.tamanho = tamanho;

		// Atualiza a posição do asteroide várias vezes para evitar spawn em cima do pai
		for (int i = 0; i < ATUALIZACOES_SPAWN; i++) {
			atualizar(null);
		}
	}

	// Método estático para calcular a posição inicial do asteroide
	private static Vetor2 calcularPosicao(Random random) {
		Vetor2 vec = new Vetor2(PainelMundo.TAMANHO_MUNDO / 2.0, PainelMundo.TAMANHO_MUNDO / 2.0);
		return vec.adicionar(new Vetor2(random.nextDouble() * Math.PI * 2).escalar(DISTANCIA_MINIMA + random.nextDouble() * VARIACAO_DISTANCIA));
	}

	// Método estático para calcular a velocidade inicial do asteroide
	private static Vetor2 calcularVelocidade(Random random) {
		return new Vetor2(random.nextDouble() * Math.PI * 2).escalar(VELOCIDADE_MINIMA + random.nextDouble() * VARIACAO_VELOCIDADE);
	}

	// Método para atualizar o estado do asteroide
	@Override
	public void atualizar(Jogo jogo) {
		super.atualizar(jogo);
		rotacionar(velocidadeRotacao); // Aplica a rotação ao asteroide
	}

	// Método para desenhar o asteroide
	@Override
	public void desenhar(Graphics2D g, Jogo jogo) {
		g.drawPolygon(tamanho.poligono); // Desenha o polígono que representa o asteroide
	}

	// Método para lidar com colisões
	@Override
	public void lidarComColisao(Jogo jogo, Entidade outra) {
		if (outra.getClass() != Asteroide.class) {
			if (tamanho != TamanhoAsteroide.Pequeno) {
				TamanhoAsteroide tamanhoSpawn = TamanhoAsteroide.values()[tamanho.ordinal() - 1];
				for (int i = 0; i < 2; i++) {
					jogo.registrarEntidade(new Asteroide(this, tamanhoSpawn, jogo.getAleatorio()));
				}
			}
			marcarParaRemocao(); // Marca o asteroide para remoção
			jogo.adicionarPontuacao(getPontuacaoKill()); // Adiciona a pontuação ao jogador
		}
	}
}