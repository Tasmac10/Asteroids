package org.psnbtech.entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.psnbtech.Game;
import org.psnbtech.WorldPanel;
import org.psnbtech.util.Vector2;

public class Player extends Entity {
	
	private static final double DEFAULT_ROTATION = -Math.PI / 2.0;

	private static final double THRUST_MAGNITUDE = 0.0385;

	private static final double MAX_VELOCITY_MAGNITUDE = 6.5;

	private static final double ROTATION_SPEED = 0.052;

	private static final double SLOW_RATE = 0.995;

	private static final int MAX_BULLETS = 4;

	private static final int FIRE_RATE = 4;

	private static final int MAX_CONSECUTIVE_SHOTS = 8;

	private static final int MAX_OVERHEAT = 30;

	private boolean thrustPressed;

	private boolean rotateLeftPressed;

	private boolean rotateRightPressed;

	private boolean firePressed;

	private boolean firingEnabled;

	private int consecutiveShots;

	private int fireCooldown;

	private int overheatCooldown;

	private int animationFrame;

	private List<Bullet> bullets;

	public Player() {
		super(new Vector2(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0), new Vector2(0.0, 0.0), 10.0, 0);
		this.bullets = new ArrayList<>();
		this.rotation = DEFAULT_ROTATION;
		this.thrustPressed = false;
		this.rotateLeftPressed = false;
		this.rotateRightPressed = false;
		this.firePressed = false;
		this.firingEnabled = true;
		this.fireCooldown = 0;
		this.overheatCooldown = 0;
		this.animationFrame = 0;
	}

	public void setThrusting(boolean state) {
		this.thrustPressed = state;
	}

	public void setRotateLeft(boolean state) {
		this.rotateLeftPressed = state;
	}

	public void setRotateRight(boolean state) {
		this.rotateRightPressed = state;
	}

	public void setFiring(boolean state) {
		this.firePressed = state;
	}
		

	public void setFiringEnabled(boolean state) {
		this.firingEnabled = state;
	}

	public void reset() {
		this.rotation = DEFAULT_ROTATION;
		position.set(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0);
		velocity.set(0.0, 0.0);
		bullets.clear();
	}
		
	@Override
	public void update(Game game) {
		super.update(game);
		
		//Increment the animation frame.
		this.animationFrame++;

		if(rotateLeftPressed != rotateRightPressed) {
			rotate(rotateLeftPressed ? -ROTATION_SPEED : ROTATION_SPEED);
		}
		

		if(thrustPressed) {

			velocity.add(new Vector2(rotation).scale(THRUST_MAGNITUDE));

			if(velocity.getLengthSquared() >= MAX_VELOCITY_MAGNITUDE * MAX_VELOCITY_MAGNITUDE) {
				velocity.normalize().scale(MAX_VELOCITY_MAGNITUDE);
			}
		}
		

		if(velocity.getLengthSquared() != 0.0) {
			velocity.scale(SLOW_RATE);
		}
		

		Iterator<Bullet> iter = bullets.iterator();
		while(iter.hasNext()) {
			Bullet bullet = iter.next();
			if(bullet.needsRemoval()) {
				iter.remove();
			}
		}

		this.fireCooldown--;
		this.overheatCooldown--;
		if(firingEnabled && firePressed && fireCooldown <= 0 && overheatCooldown <= 0) {

			if(bullets.size() < MAX_BULLETS) {
				this.fireCooldown = FIRE_RATE;
				
				Bullet bullet = new Bullet(this, rotation);
				bullets.add(bullet);
				game.registerEntity(bullet);
			}

			this.consecutiveShots++;
			if(consecutiveShots == MAX_CONSECUTIVE_SHOTS) {
				this.consecutiveShots = 0;
				this.overheatCooldown = MAX_OVERHEAT;
			}
		} else if(consecutiveShots > 0) {
			this.consecutiveShots--;
		}
	}
	
	@Override
	public void handleCollision(Game game, Entity other) {

		if(other.getClass() == Asteroid.class) {
			game.killPlayer();
		}
	}
	
	@Override
	public void draw(Graphics2D g, Game game) {

		if(!game.isPlayerInvulnerable() || game.isPaused() || animationFrame % 20 < 10) {

			g.drawLine(-10, -8, 10, 0);
			g.drawLine(-10, 8, 10, 0);
			g.drawLine(-6, -6, -6, 6);

			if(!game.isPaused() && thrustPressed && animationFrame % 6 < 3) {
				g.drawLine(-6, -6, -12, 0);
				g.drawLine(-6, 6, -12, 0);
			}
		}
	}
	
}
