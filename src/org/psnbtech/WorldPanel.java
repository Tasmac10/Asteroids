package org.psnbtech;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import javax.swing.JPanel;

import org.psnbtech.entity.Entity;
import org.psnbtech.util.Vector2;

public class WorldPanel extends JPanel {

	public static final int WORLD_SIZE = 550;

	private static final Font TITLE_FONT = new Font("Dialog", Font.PLAIN, 25);

	private static final Font SUBTITLE_FONT = new Font("Dialog", Font.PLAIN, 15);


	private Game game;
	

	public WorldPanel(Game game) {
		this.game = game;


		setPreferredSize(new Dimension(WORLD_SIZE, WORLD_SIZE));
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //Required, otherwise rendering gets messy.
		

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.WHITE);
		AffineTransform identity = g2d.getTransform();

		Iterator<Entity> iter = game.getEntities().iterator();
		while(iter.hasNext()) {
			Entity entity = iter.next();
			if(entity != game.getPlayer() || game.canDrawPlayer()) {
				Vector2 pos = entity.getPosition();

				drawEntity(g2d, entity, pos.x, pos.y);
				g2d.setTransform(identity);

				double radius = entity.getCollisionRadius();
				double x = (pos.x < radius) ? pos.x + WORLD_SIZE
						: (pos.x > WORLD_SIZE - radius) ? pos.x - WORLD_SIZE : pos.x;
				double y = (pos.y < radius) ? pos.y + WORLD_SIZE
						: (pos.y > WORLD_SIZE - radius) ? pos.y - WORLD_SIZE : pos.y;

				if(x != pos.x || y != pos.y) {
					drawEntity(g2d, entity, x, y);
					g2d.setTransform(identity);
				}
			}	
		}
		

		if(!game.isGameOver()) {
			g.drawString("Score: " + game.getScore(), 10, 15);
		}
		

		if(game.isGameOver()) {
			drawTextCentered("org.psnbtech.Game Over", TITLE_FONT, g2d, -25);
			drawTextCentered("Final Score: " + game.getScore(), SUBTITLE_FONT, g2d, 10);
		} else if(game.isPaused()) {
			drawTextCentered("Paused", TITLE_FONT, g2d, -25);
		} else if(game.isShowingLevel()) {
			drawTextCentered("Level: " + game.getLevel(), TITLE_FONT, g2d, -25);
		}
		

		g2d.translate(15, 30);
		g2d.scale(0.85, 0.85);
		for(int i = 0; i < game.getLives(); i++) {
			g2d.drawLine(-8, 10, 0, -10);
			g2d.drawLine(8, 10, 0, -10);
			g2d.drawLine(-6, 6, 6, 6);
			g2d.translate(30, 0);
		}
	}

	private void drawTextCentered(String text, Font font, Graphics2D g, int y) {
		g.setFont(font);
		g.drawString(text, WORLD_SIZE / 2 - g.getFontMetrics().stringWidth(text) / 2, WORLD_SIZE / 2 + y);
	}

	private void drawEntity(Graphics2D g2d, Entity entity, double x, double y) {
		g2d.translate(x, y);
		double rotation = entity.getRotation();
		if(rotation != 0.0f) {
			g2d.rotate(entity.getRotation());
		}
		entity.draw(g2d, game);
	}

}
