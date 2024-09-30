package org.POO.entidade;

import java.awt.Graphics2D;

import org.POO.Jogo;
import org.POO.PainelMundo;
import org.POO.util.Vetor2;

public abstract class Entidade {

	protected Vetor2 posicao; // Posição da entidade no mundo
	protected Vetor2 velocidade; // Velocidade da entidade
	protected double rotacao; // Rotação da entidade
	protected double raio; // Raio de colisão da entidade
	private boolean precisaRemover; // Indica se a entidade deve ser removida
	private int pontuacaoKill; // Pontuação ao eliminar a entidade

	// Construtor da entidade
	public Entidade(Vetor2 posicao, Vetor2 velocidade, double raio, int pontuacaoKill) {
		this.posicao = posicao;
		this.velocidade = velocidade;
		this.raio = raio;
		this.rotacao = 0.0f;
		this.pontuacaoKill = pontuacaoKill;
		this.precisaRemover = false;
	}

	// Rotaciona a entidade por uma quantia especificada
	public void rotacionar(double quantia) {
		this.rotacao += quantia;
		this.rotacao %= Math.PI * 2;
	}

	// Retorna a pontuação ao eliminar a entidade
	public int getPontuacaoKill() {
		return pontuacaoKill;
	}

	// Marca a entidade para remoção
	public void marcarParaRemocao() {
		this.precisaRemover = true;
	}

	// Retorna a posição da entidade
	public Vetor2 getPosicao() {
		return posicao;
	}

	// Retorna a velocidade da entidade
	public Vetor2 getVelocidade() {
		return velocidade;
	}

	// Retorna a rotação da entidade
	public double getRotacao() {
		return rotacao;
	}

	// Retorna o raio de colisão da entidade
	public double getRaioColisao() {
		return raio;
	}

	// Verifica se a entidade precisa ser removida
	public boolean precisaRemover() {
		return precisaRemover;
	}

	// Atualiza a posição da entidade com base na sua velocidade
	public void atualizar(Jogo jogo) {
		posicao.adicionar(velocidade);
		if (posicao.x < 0.0f) {
			posicao.x += PainelMundo.TAMANHO_MUNDO;
		}
		if (posicao.y < 0.0f) {
			posicao.y += PainelMundo.TAMANHO_MUNDO;
		}
		posicao.x %= PainelMundo.TAMANHO_MUNDO;
		posicao.y %= PainelMundo.TAMANHO_MUNDO;
	}

	// Verifica se há colisão com outra entidade
	public boolean verificarColisao(Entidade entidade) {
		double raio = entidade.getRaioColisao() + getRaioColisao();
		return (posicao.getDistanciaParaQuadrado(entidade.posicao) < raio * raio);
	}

	// Método abstrato para lidar com colisões, a ser implementado pelas subclasses
	public abstract void lidarComColisao(Jogo jogo, Entidade outra);

	// Método abstrato para desenhar a entidade, a ser implementado pelas subclasses
	public abstract void desenhar(Graphics2D g, Jogo jogo);
}