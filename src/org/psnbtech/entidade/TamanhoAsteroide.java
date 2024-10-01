package org.psnbtech.entidade;

import java.awt.Polygon;

public enum TamanhoAsteroide {

	Pequeno(15.0, 100),

	Medio(25.0, 50),

	Grande(40.0, 20);

	private static final int NUMERO_DE_PONTOS = 5;

	public final Polygon poligono;

	public final double raio;

	public final int valorEliminacao;

	private TamanhoAsteroide(double raio, int valor) {
		this.poligono = gerarPoligono(raio);
		this.raio = raio + 1.0;
		this.valorEliminacao = valor;
	}

	private static Polygon gerarPoligono(double raio) {

		int[] x = new int[NUMERO_DE_PONTOS];
		int[] y = new int[NUMERO_DE_PONTOS];

		double angulo = (2 * Math.PI / NUMERO_DE_PONTOS);
		for(int i = 0; i < NUMERO_DE_PONTOS; i++) {
			x[i] = (int) (raio * Math.sin(i * angulo));
			y[i] = (int) (raio * Math.cos(i * angulo));
		}

		return new Polygon(x, y, NUMERO_DE_PONTOS);
	}
}
