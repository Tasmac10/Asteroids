package org.psnbtech.entidade;

import java.awt.Polygon;

public enum TamanhoAsteroide {

	// Define os tamanhos dos asteroides com seus respectivos raios e valores de eliminação
	Pequeno(15.0, 100),
	Medio(25.0, 50),
	Grande(40.0, 20);

	private static final int NUMERO_DE_PONTOS = 5; // Número de pontos do polígono que representa o asteroide

	public final Polygon poligono; // Polígono que representa a forma do asteroide
	public final double raio; // Raio do asteroide
	public final int valorEliminacao; // Valor de eliminação do asteroide

	// Construtor do enum que inicializa o polígono, raio e valor de eliminação
	private TamanhoAsteroide(double raio, int valor) {
		this.poligono = gerarPoligono(raio);
		this.raio = raio + 1.0; // Adiciona 1.0 ao raio para evitar colisões precisas demais
		this.valorEliminacao = valor;
	}

	// Método que gera um polígono com base no raio fornecido
	private static Polygon gerarPoligono(double raio) {
		int[] x = new int[NUMERO_DE_PONTOS];
		int[] y = new int[NUMERO_DE_PONTOS];

		double angulo = (2 * Math.PI / NUMERO_DE_PONTOS); // Ângulo entre os pontos do polígono
		for (int i = 0; i < NUMERO_DE_PONTOS; i++) {
			x[i] = (int) (raio * Math.sin(i * angulo)); // Calcula a coordenada x do ponto
			y[i] = (int) (raio * Math.cos(i * angulo)); // Calcula a coordenada y do ponto
		}

		return new Polygon(x, y, NUMERO_DE_PONTOS); // Retorna o polígono gerado
	}
}