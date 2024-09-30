package org.psnbtech;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import org.psnbtech.entidade.Asteroide;
import org.psnbtech.entidade.Entidade;
import org.psnbtech.entidade.Jogador;
import org.psnbtech.util.Relogio;

public class Jogo extends JFrame {

	// Constantes do jogo
	private static final int QUADROS_POR_SEGUNDO = 60;
	private static final long TEMPO_QUADRO = (long)(1000000000.0 / QUADROS_POR_SEGUNDO);
	private static final int LIMITE_EXIBIR_NIVEL = 60;
	private static final int LIMITE_TEMPO_MORTE = 200;
	private static final int LIMITE_TEMPO_RESPAWN = 100;
	private static final int LIMITE_TEMPO_INVULNERAVEL = 0;
	private static final int LIMITE_TEMPO_RESET = 120;

	// Variáveis do jogo
	private PainelMundo mundo;
	private Relogio relogioLogico;
	private Random aleatorio;
	private List<Entidade> entidades;
	private List<Entidade> entidadesPendentes;
	private Jogador jogador;
	private int tempoMorte;
	private int tempoReiniciar;
	private int tempoMostrarNivel;
	private int pontuacao;
	private int vidas;
	private int nivel;
	private boolean fimDeJogo;
	private boolean reiniciarJogo;

	// Construtor do jogo
	public Jogo() {
		super("Asteroides");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		// Adiciona o painel do mundo ao frame
		add(this.mundo = new PainelMundo(this), BorderLayout.CENTER);

		// Adiciona o listener de teclado
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
					// Implementação das ações de tecla pressionada
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
					// Implementação das ações de tecla liberada
				}
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// Verifica se o jogo deve ser reiniciado
	private boolean verificarReinicio() {
		boolean reiniciar = (fimDeJogo && tempoReiniciar <= 0);
		if(reiniciar) {
			reiniciarJogo = true;
		}
		return reiniciar;
	}

	// Inicia o jogo
	public void iniciarJogo() {
		this.aleatorio = new Random();
		this.entidades = new LinkedList<>();
		this.entidadesPendentes = new ArrayList<>();
		this.jogador = new Jogador();

		resetarJogo();

		this.relogioLogico = new Relogio(QUADROS_POR_SEGUNDO);
		while(true) {
			long inicio = System.nanoTime();

			relogioLogico.atualizar();
			for(int i = 0; i < 5 && relogioLogico.cicloDecorrido(); i++) {
				atualizarJogo();
			}

			mundo.repaint();

			long delta = TEMPO_QUADRO - (System.nanoTime() - inicio);
			if(delta > 0) {
				try {
					Thread.sleep(delta / 1000000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Atualiza o estado do jogo
	private void atualizarJogo() {
		entidades.addAll(entidadesPendentes);
		entidadesPendentes.clear();

		if(tempoReiniciar > 0) {
			this.tempoReiniciar--;
		}

		if(tempoMostrarNivel > 0) {
			this.tempoMostrarNivel--;
		}

		if(fimDeJogo && reiniciarJogo) {
			resetarJogo();
		}

		if(!fimDeJogo && inimigosEstaoMortos()) {
			this.nivel++;
			this.tempoMostrarNivel = LIMITE_EXIBIR_NIVEL;

			resetarListasEntidades();

			jogador.resetar();
			jogador.setDisparoHabilitado(true);

			for(int i = 0; i < nivel + 2; i++) {
				registrarEntidade(new Asteroide(aleatorio));
			}
		}

		if(tempoMorte > 0) {
			this.tempoMorte--;
			switch(tempoMorte) {
				case LIMITE_TEMPO_RESPAWN:
					// Implementação do respawn
					break;
				case LIMITE_TEMPO_INVULNERAVEL:
					// Implementação da invulnerabilidade
					break;
			}
		}

		if(tempoMostrarNivel == 0) {
			for(Entidade entidade : entidades) {
				entidade.atualizar(this);
			}

			for(int i = 0; i < entidades.size(); i++) {
				Entidade a = entidades.get(i);
				for(int j = i + 1; j < entidades.size(); j++) {
					Entidade b = entidades.get(j);
					// Implementação da verificação de colisão
				}
			}

			Iterator<Entidade> iterador = entidades.iterator();
			while(iterador.hasNext()) {
				if(iterador.next().precisaRemover()) {
					iterador.remove();
				}
			}
		}
	}

	// Reseta o estado do jogo
	private void resetarJogo() {
		this.pontuacao = 0;
		this.nivel = 0;
		this.vidas = 3;
		this.tempoMorte = 0;
		this.fimDeJogo = false;
		this.reiniciarJogo = false;
		resetarListasEntidades();
	}

	// Reseta as listas de entidades
	private void resetarListasEntidades() {
		entidadesPendentes.clear();
		entidades.clear();
		entidades.add(jogador);
	}

	// Verifica se todos os inimigos estão mortos
	private boolean inimigosEstaoMortos() {
		for(Entidade e : entidades) {
			if(e.getClass() == Asteroide.class) {
				return false;
			}
		}
		return true;
	}

	// Mata o jogador
	public void matarJogador() {
		this.vidas--;
		if(vidas == 0) {
			this.fimDeJogo = true;
			this.tempoReiniciar = LIMITE_TEMPO_RESET;
			this.tempoMorte = Integer.MAX_VALUE;
		} else {
			this.tempoMorte = LIMITE_TEMPO_MORTE;
		}
		jogador.setDisparoHabilitado(false);
	}

	// Adiciona pontuação ao jogador
	public void adicionarPontuacao(int pontuacao) {
		this.pontuacao += pontuacao;
	}

	// Registra uma nova entidade no jogo
	public void registrarEntidade(Entidade entidade) {
		entidadesPendentes.add(entidade);
	}

	// Verifica se o jogo acabou
	public boolean isFimDeJogo() {
		return fimDeJogo;
	}

	// Verifica se o jogador está invulnerável
	public boolean isJogadorInvulneravel() {
		return (tempoMorte > LIMITE_TEMPO_INVULNERAVEL);
	}

	// Verifica se o jogador pode ser desenhado
	public boolean podeDesenharJogador() {
		return (tempoMorte <= LIMITE_TEMPO_RESPAWN);
	}

	// Retorna a pontuação do jogador
	public int getPontuacao() {
		return pontuacao;
	}

	// Retorna o número de vidas do jogador
	public int getVidas() {
		return vidas;
	}

	// Retorna o nível atual do jogo
	public int getNivel() {
		return nivel;
	}

	// Verifica se o jogo está pausado
	public boolean isPausado() {
		return relogioLogico.isPausado();
	}

	// Verifica se o nível está sendo mostrado
	public boolean isMostrandoNivel() {
		return (tempoMostrarNivel > 0);
	}

	// Retorna o gerador de números aleatórios
	public Random getAleatorio() {
		return aleatorio;
	}

	// Retorna a lista de entidades
	public List<Entidade> getEntidades() {
		return entidades;
	}

	// Retorna o jogador
	public Jogador getJogador() {
		return jogador;
	}

	// Método principal para iniciar o jogo
	public static void main(String[] args) {
		new Menu();
	}
}