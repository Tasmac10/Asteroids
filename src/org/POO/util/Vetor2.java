package org.POO.util;

public class Vetor2 {

	public double x;
	public double y;

	public Vetor2(double angulo) {
		this.x = Math.cos(angulo);
		this.y = Math.sin(angulo);
	}

	public Vetor2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vetor2(Vetor2 vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	public Vetor2 definir(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vetor2 adicionar(Vetor2 vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public Vetor2 escalar(double escalar) {
		this.x *= escalar;
		this.y *= escalar;
		return this;
	}

	public Vetor2 normalizar() {
		double comprimento = getComprimentoAoQuadrado();
		if (comprimento != 0.0f && comprimento != 1.0f) {
			comprimento = Math.sqrt(comprimento);
			this.x /= comprimento;
			this.y /= comprimento;
		}
		return this;
	}

	public double getComprimentoAoQuadrado() {
		return (x * x + y * y);
	}

	public double getDistanciaParaQuadrado(Vetor2 vec) {
		double dx = this.x - vec.x;
		double dy = this.y - vec.y;
		return (dx * dx + dy * dy);
	}
}
