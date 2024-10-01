package org.psnbtech.entidade;

import java.awt.Graphics2D;

import org.psnbtech.Jogo;
import org.psnbtech.PainelMundo;
import org.psnbtech.util.Vetor2;

public abstract class Entidade {

	protected Vetor2 posicao;
	protected Vetor2 velocidade;
	protected double rotacao;
	protected double raio;
	private boolean precisaRemover;
	private int pontuacaoKill;

	public Entidade(Vetor2 posicao, Vetor2 velocidade, double raio, int pontuacaoKill) {
		this.posicao = posicao;
		this.velocidade = velocidade;
		this.raio = raio;
		this.rotacao = 0.0f;
		this.pontuacaoKill = pontuacaoKill;
		this.precisaRemover = false;
	}

	public void rotacionar(double quantia) {
		this.rotacao += quantia;
		this.rotacao %= Math.PI * 2;
	}

	public int getPontuacaoKill() {
		return pontuacaoKill;
	}

	public void marcarParaRemocao() {
		this.precisaRemover = true;
	}

	public Vetor2 getPosicao() {
		return posicao;
	}

	public Vetor2 getVelocidade() {
		return velocidade;
	}

	public double getRotacao() {
		return rotacao;
	}

	public double getRaioColisao() {
		return raio;
	}

	public boolean precisaRemover() {
		return precisaRemover;
	}

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

	public boolean verificarColisao(Entidade entidade) {
		double raio = entidade.getRaioColisao() + getRaioColisao();
		return (posicao.getDistanciaParaQuadrado(entidade.posicao) < raio * raio);
	}

	public abstract void lidarComColisao(Jogo jogo, Entidade outra);

	public abstract void desenhar(Graphics2D g, Jogo jogo);
}
