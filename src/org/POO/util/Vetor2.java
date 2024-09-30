package org.POO.util;

public class Vetor2 {

	public double x;
	public double y;

	// Construtor que inicializa o vetor com base em um ângulo
	public Vetor2(double angulo) {
		this.x = Math.cos(angulo);
		this.y = Math.sin(angulo);
	}

	// Construtor que inicializa o vetor com coordenadas x e y
	public Vetor2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Construtor que copia as coordenadas de outro vetor
	public Vetor2(Vetor2 vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	// Define novas coordenadas para o vetor
	public Vetor2 definir(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	// Adiciona as coordenadas de outro vetor a este vetor
	public Vetor2 adicionar(Vetor2 vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	// Escala as coordenadas do vetor por um fator
	public Vetor2 escalar(double escalar) {
		this.x *= escalar;
		this.y *= escalar;
		return this;
	}

	// Normaliza o vetor para que ele tenha comprimento 1
	public Vetor2 normalizar() {
		double comprimento = getComprimentoAoQuadrado();
		if (comprimento != 0.0f && comprimento != 1.0f) {
			comprimento = Math.sqrt(comprimento);
			this.x /= comprimento;
			this.y /= comprimento;
		}
		return this;
	}

	// Retorna o comprimento ao quadrado do vetor
	public double getComprimentoAoQuadrado() {
		return (x * x + y * y);
	}

	// Calcula a distância ao quadrado entre este vetor e outro vetor
	public double getDistanciaParaQuadrado(Vetor2 vec) {
		double dx = this.x - vec.x;
		double dy = this.y - vec.y;
		return (dx * dx + dy * dy);
	}
}