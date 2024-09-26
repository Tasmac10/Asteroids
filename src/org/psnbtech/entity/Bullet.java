package org.psnbtech.entity;

import java.awt.Graphics2D;

import org.psnbtech.Game;
import org.psnbtech.util.Vector2;

public class Bullet extends Entity {

	private static final double VELOCITY_MAGNITUDE = 6.75;

	private static final int MAX_LIFESPAN = 60;

	private int lifespan;

	public Bullet(Entity owner, double angle) {
		super(new Vector2(owner.position), new Vector2(angle).scale(VELOCITY_MAGNITUDE), 2.0, 0);
		this.lifespan = MAX_LIFESPAN;
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		//Decrement the lifespan of the bullet, and remove it if needed.
		this.lifespan--;
		if(lifespan <= 0) {
			flagForRemoval();
		}
	}

	@Override
	public void handleCollision(Game game, Entity other) {
		if(other.getClass() != Player.class) {
			flagForRemoval();
		}
	}
	
	@Override
	public void draw(Graphics2D g, Game game) {
		g.drawOval(-1, -1, 2, 2);
	}

}
