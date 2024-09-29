package org.psnbtech.util;

public class Relogio {

	private float millisPorCiclo;

	private long ultimaAtualizacao;

	private int ciclosDecorridos;

	private float ciclosExcedentes;

	private boolean pausado;

	public Relogio(float ciclosPorSegundo) {
		setCiclosPorSegundo(ciclosPorSegundo);
		resetar();
	}

	public void setCiclosPorSegundo(float ciclosPorSegundo) {
		this.millisPorCiclo = (1.0f / ciclosPorSegundo) * 1000;
	}

	public void resetar() {
		this.ciclosDecorridos = 0;
		this.ciclosExcedentes = 0.0f;
		this.ultimaAtualizacao = getHorarioAtual();
		this.pausado = false;
	}

	public void atualizar() {
		// Obter o tempo atual e calcular o delta do tempo.
		long atualizacaoAtual = getHorarioAtual();
		float delta = (float)(atualizacaoAtual - ultimaAtualizacao) + ciclosExcedentes;

		if (!pausado) {
			this.ciclosDecorridos += (int)Math.floor(delta / millisPorCiclo);
			this.ciclosExcedentes = delta % millisPorCiclo;
		}

		this.ultimaAtualizacao = atualizacaoAtual;
	}

	public void setPausado(boolean pausado) {
		this.pausado = pausado;
	}

	public boolean isPausado() {
		return pausado;
	}

	public boolean cicloDecorrido() {
		if (ciclosDecorridos > 0) {
			this.ciclosDecorridos--;
			return true;
		}
		return false;
	}

	public boolean verificarCicloDecorrido() {
		return (ciclosDecorridos > 0);
	}

	private static final long getHorarioAtual() {
		return (System.nanoTime() / 1000000L);
	}

}
