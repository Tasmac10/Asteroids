package org.POO.entidade;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.POO.Jogo;
import org.POO.PainelMundo;
import org.POO.util.Vetor2;

public class Jogador extends Entidade {

	// Constantes para configuração do jogador
	private static final double ROTACAO_PADRAO = -Math.PI / 2.0;
	private static final double MAGNITUDE_TURBO = 0.0385;
	private static final double MAGNITUDE_VELOCIDADE_MAX = 6.5;
	private static final double VELOCIDADE_ROTACAO = 0.052;
	private static final double TAXA_DESACELERACAO = 0.995;
	private static final int MAX_BALAS = 4;
	private static final int TAXA_DISPARO = 4;
	private static final int MAX_DISPAROS_CONSECUTIVOS = 8;
	private static final int MAX_SUPERAQUECIMENTO = 30;

	// Variáveis de estado do jogador
	private boolean turboPressionado;
	private boolean rotacionarEsquerdaPressionado;
	private boolean rotacionarDireitaPressionado;
	private boolean dispararPressionado;
	private boolean disparoHabilitado;
	private int disparosConsecutivos;
	private int cooldownDisparo;
	private int cooldownSuperaquecimento;
	private int quadroAnimacao;
	private List<Bala> balas;

	// Construtor do jogador
	public Jogador() {
		super(new Vetor2(PainelMundo.TAMANHO_MUNDO / 2.0, PainelMundo.TAMANHO_MUNDO / 2.0), new Vetor2(0.0, 0.0), 10.0, 0);
		this.balas = new ArrayList<>();
		this.rotacao = ROTACAO_PADRAO;
		this.turboPressionado = false;
		this.rotacionarEsquerdaPressionado = false;
		this.rotacionarDireitaPressionado = false;
		this.dispararPressionado = false;
		this.disparoHabilitado = true;
		this.cooldownDisparo = 0;
		this.cooldownSuperaquecimento = 0;
		this.quadroAnimacao = 0;
	}

	// Métodos para definir o estado do jogador
	public void setTurbinando(boolean estado) {
		this.turboPressionado = estado;
	}

	public void setRotacionarEsquerda(boolean estado) {
		this.rotacionarEsquerdaPressionado = estado;
	}

	public void setRotacionarDireita(boolean estado) {
		this.rotacionarDireitaPressionado = estado;
	}

	public void setDisparando(boolean estado) {
		this.dispararPressionado = estado;
	}

	public void setDisparoHabilitado(boolean estado) {
		this.disparoHabilitado = estado;
	}

	// Método para resetar o estado do jogador
	public void resetar() {
		this.rotacao = ROTACAO_PADRAO;
		posicao.definir(PainelMundo.TAMANHO_MUNDO / 2.0, PainelMundo.TAMANHO_MUNDO / 2.0);
		velocidade.definir(0.0, 0.0);
		balas.clear();
	}

	// Método para atualizar o estado do jogador
	@Override
	public void atualizar(Jogo jogo) {
		super.atualizar(jogo);

		// Incrementa o quadro de animação
		this.quadroAnimacao++;

		// Rotaciona o jogador se necessário
		if(rotacionarEsquerdaPressionado != rotacionarDireitaPressionado) {
			rotacionar(rotacionarEsquerdaPressionado ? -VELOCIDADE_ROTACAO : VELOCIDADE_ROTACAO);
		}

		// Aplica turbo se pressionado
		if(turboPressionado) {
			velocidade.adicionar(new Vetor2(rotacao).escalar(MAGNITUDE_TURBO));

			// Limita a velocidade máxima
			if(velocidade.getComprimentoAoQuadrado() >= MAGNITUDE_VELOCIDADE_MAX * MAGNITUDE_VELOCIDADE_MAX) {
				velocidade.normalizar().escalar(MAGNITUDE_VELOCIDADE_MAX);
			}
		}

		// Aplica desaceleração
		if(velocidade.getComprimentoAoQuadrado() != 0.0) {
			velocidade.escalar(TAXA_DESACELERACAO);
		}

		// Remove balas que precisam ser removidas
		Iterator<Bala> iter = balas.iterator();
		while(iter.hasNext()) {
			Bala bala = iter.next();
			if(bala.precisaRemover()) {
				iter.remove();
			}
		}

		// Gerencia o cooldown de disparo e superaquecimento
		this.cooldownDisparo--;
		this.cooldownSuperaquecimento--;
		if(disparoHabilitado && dispararPressionado && cooldownDisparo <= 0 && cooldownSuperaquecimento <= 0) {
			if(balas.size() < MAX_BALAS) {
				this.cooldownDisparo = TAXA_DISPARO;

				// Cria e registra uma nova bala
				Bala bala = new Bala(this, rotacao);
				balas.add(bala);
				jogo.registrarEntidade(bala);
			}

			// Gerencia o superaquecimento
			this.disparosConsecutivos++;
			if(disparosConsecutivos == MAX_DISPAROS_CONSECUTIVOS) {
				this.disparosConsecutivos = 0;
				this.cooldownSuperaquecimento = MAX_SUPERAQUECIMENTO;
			}
		} else if(disparosConsecutivos > 0) {
			this.disparosConsecutivos--;
		}
	}

	// Método para lidar com colisões
	@Override
	public void lidarComColisao(Jogo jogo, Entidade other) {
		if(other.getClass() == Asteroide.class) {
			jogo.matarJogador();
		}
	}

	// Método para desenhar o jogador
	@Override
	public void desenhar(Graphics2D g, Jogo jogo) {
		if(!jogo.isJogadorInvulneravel() || jogo.isPausado() || quadroAnimacao % 20 < 10) {
			g.drawLine(-10, -8, 10, 0);
			g.drawLine(-10, 8, 10, 0);
			g.drawLine(-6, -6, -6, 6);

			// Desenha o turbo se pressionado
			if(!jogo.isPausado() && turboPressionado && quadroAnimacao % 6 < 3) {
				g.drawLine(-6, -6, -12, 0);
				g.drawLine(-6, 6, -12, 0);
			}
		}
	}
}