package org.psnbtech.entity;

import java.awt.Graphics2D;

import org.psnbtech.Game;
import org.psnbtech.WorldPanel;
import org.psnbtech.util.Vector2;

public abstract class Entity {

	protected Vector2 position;

	protected Vector2 velocity;

	protected double rotation;

	protected double radius;

	private boolean needsRemoval;

	private int killScore;
	

	public Entity(Vector2 position, Vector2 velocity, double radius, int killScore) {
		this.position = position;
		this.velocity = velocity;
		this.radius = radius;
		this.rotation = 0.0f;
		this.killScore = killScore;
		this.needsRemoval = false;
	}

	public void rotate(double amount) {
		this.rotation += amount;
		this.rotation %= Math.PI * 2;
	}

	public int getKillScore() {
		return killScore;
	}

	public void flagForRemoval() {
		this.needsRemoval = true;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public double getRotation() {
		return rotation;
	}

	public double getCollisionRadius() {
		return radius;
	}

	public boolean needsRemoval() {
		return needsRemoval;
	}

	public void update(Game game) {
		position.add(velocity);
		if(position.x < 0.0f) {
			position.x += WorldPanel.WORLD_SIZE;
		}
		if(position.y < 0.0f) {
			position.y += WorldPanel.WORLD_SIZE;
		}
		position.x %= WorldPanel.WORLD_SIZE;
		position.y %= WorldPanel.WORLD_SIZE;
	}

	public boolean checkCollision(Entity entity) {

		double radius = entity.getCollisionRadius() + getCollisionRadius();
		return (position.getDistanceToSquared(entity.position) < radius * radius);
	}

	public abstract void handleCollision(Game game, Entity other);

	public abstract void draw(Graphics2D g, Game game);
}
