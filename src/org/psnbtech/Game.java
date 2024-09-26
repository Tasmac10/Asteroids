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

import org.psnbtech.entity.Asteroid;
import org.psnbtech.entity.Entity;
import org.psnbtech.entity.Player;
import org.psnbtech.util.Clock;

public class Game extends JFrame {

	private static final int FRAMES_PER_SECOND = 60;

	private static final long FRAME_TIME = (long)(1000000000.0 / FRAMES_PER_SECOND);

	private static final int DISPLAY_LEVEL_LIMIT = 60;

	private static final int DEATH_COOLDOWN_LIMIT = 200;

	private static final int RESPAWN_COOLDOWN_LIMIT = 100;

	private static final int INVULN_COOLDOWN_LIMIT = 0;

	private static final int RESET_COOLDOWN_LIMIT = 120;

	private WorldPanel world;

	private Clock logicTimer;

	private Random random;


	private List<Entity> entities;


	private List<Entity> pendingEntities;

	private Player player;

	private int deathCooldown;

	private int showLevelCooldown;

	private int restartCooldown;

	private int score;

	private int lives;

	private int level;

	private boolean isGameOver;

	private boolean restartGame;

	private Game() {
		super("Asteroids");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		add(this.world = new WorldPanel(this), BorderLayout.CENTER);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				switch(e.getKeyCode()) {

				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(!checkForRestart()) {
						player.setThrusting(true);
					}
					break;

				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if(!checkForRestart()) {
						player.setRotateLeft(true);
					}
					break;

				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if(!checkForRestart()) {
						player.setRotateRight(true);
					}
					break;

				case KeyEvent.VK_SPACE:
					if(!checkForRestart()) {
						player.setFiring(true);
					}
					break;

				case KeyEvent.VK_P:
					if(!checkForRestart()) {
						logicTimer.setPaused(!logicTimer.isPaused());
					}
					break;

				default:
					checkForRestart();
					break;

				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {

				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					player.setThrusting(false);
					break;

				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					player.setRotateLeft(false);
					break;

				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					player.setRotateRight(false);
					break;

				case KeyEvent.VK_SPACE:
					player.setFiring(false);
					break;
				}
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private boolean checkForRestart() {
		boolean restart = (isGameOver && restartCooldown <= 0);
		if(restart) {
			restartGame = true;
		}
		return restart;
	}

	private void startGame() {
		this.random = new Random();
		this.entities = new LinkedList<Entity>();
		this.pendingEntities = new ArrayList<>();
		this.player = new Player();

		resetGame();

		this.logicTimer = new Clock(FRAMES_PER_SECOND);
		while(true) {
			long start = System.nanoTime();

			logicTimer.update();
			for(int i = 0; i < 5 && logicTimer.hasElapsedCycle(); i++) {
				updateGame();
			}

			world.repaint();


			long delta = FRAME_TIME - (System.nanoTime() - start);
			if(delta > 0) {
				try {
					Thread.sleep(delta / 1000000L, (int) delta % 1000000);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateGame() {

		entities.addAll(pendingEntities);
		pendingEntities.clear();

		if(restartCooldown > 0) {
			this.restartCooldown--;
		}

		if(showLevelCooldown > 0) {
			this.showLevelCooldown--;
		}

		if(isGameOver && restartGame) {
			resetGame();
		}

		if(!isGameOver && areEnemiesDead()) {
			this.level++;
			this.showLevelCooldown = DISPLAY_LEVEL_LIMIT;

			resetEntityLists();

			player.reset();
			player.setFiringEnabled(true);

			for(int i = 0; i < level + 2; i++) {
				registerEntity(new Asteroid(random));
			}
		}

		if(deathCooldown > 0) {
			this.deathCooldown--;
			switch(deathCooldown) {

			case RESPAWN_COOLDOWN_LIMIT:
				player.reset();
				player.setFiringEnabled(false);
				break;

			case INVULN_COOLDOWN_LIMIT:
				player.setFiringEnabled(true);
				break;

			}
		}

		if(showLevelCooldown == 0) {

			for(Entity entity : entities) {
				entity.update(this);
			}

			for(int i = 0; i < entities.size(); i++) {
				Entity a = entities.get(i);
				for(int j = i + 1; j < entities.size(); j++) {
					Entity b = entities.get(j);
					if(i != j && a.checkCollision(b) && ((a != player && b != player) || deathCooldown <= INVULN_COOLDOWN_LIMIT)) {
						a.handleCollision(this, b);
						b.handleCollision(this, a);
					}
				}
			}

			Iterator<Entity> iter = entities.iterator();
			while(iter.hasNext()) {
				if(iter.next().needsRemoval()) {
					iter.remove();
				}
			}
		}
	}

	private void resetGame() {
		this.score = 0;
		this.level = 0;
		this.lives = 3;
		this.deathCooldown = 0;
		this.isGameOver = false;
		this.restartGame = false;
		resetEntityLists();
	}

	private void resetEntityLists() {
		pendingEntities.clear();
		entities.clear();
		entities.add(player);
	}

	private boolean areEnemiesDead() {
		for(Entity e : entities) {
			if(e.getClass() == Asteroid.class) {
				return false;
			}
		}
		return true;
	}

	public void killPlayer() {
		this.lives--;
		if(lives == 0) {
			this.isGameOver = true;
			this.restartCooldown = RESET_COOLDOWN_LIMIT;
			this.deathCooldown = Integer.MAX_VALUE;
		} else {
			this.deathCooldown = DEATH_COOLDOWN_LIMIT;
		}
		player.setFiringEnabled(false);
	}
	public void addScore(int score) {
		this.score += score;
	}
	public void registerEntity(Entity entity) {
		pendingEntities.add(entity);
	}
	public boolean isGameOver() {
		return isGameOver;
	}
	public boolean isPlayerInvulnerable() {
		return (deathCooldown > INVULN_COOLDOWN_LIMIT);
	}
	public boolean canDrawPlayer() {
		return (deathCooldown <= RESPAWN_COOLDOWN_LIMIT);
	}
	public int getScore() {
		return score;
	}
	public int getLives() {
		return lives;
	}
	public int getLevel() {
		return level;
	}
	public boolean isPaused() {
		return logicTimer.isPaused();
	}
	public boolean isShowingLevel() {
		return (showLevelCooldown > 0);
	}
	public Random getRandom() {
		return random;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public Player getPlayer() {
		return player;
	}
	public static void main(String[] args) {
		Game game = new Game();
		game.startGame();
	}

}
