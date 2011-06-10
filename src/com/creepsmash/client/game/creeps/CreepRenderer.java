package com.creepsmash.client.game.creeps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.creepsmash.client.utils.Cache;


public class CreepRenderer {
	private Creep creep;

	private BufferedImage image;

	private AffineTransform translation;
	private AffineTransform rotation;

	public CreepRenderer(Creep creep) {
		this.creep = creep;
		this.translation = new AffineTransform();
		this.rotation = new AffineTransform();

		this.loadImage();
	}

	private void loadImage() {
		if (Cache.getInstance().hasCreepImg(this.creep.getType())) {
			this.image = Cache.getInstance().getCreepImg(this.creep.getType());
			return;
		}
		try {
			this.image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(this.creep.getType().getImageFileName())); 
			Cache.getInstance().putCreepImg(this.creep.getType(), this.image);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics2D g) {
		// only paint when creep is active
		if (this.creep.isActive()) {
			AffineTransform save = g.getTransform();

			translation.setToIdentity();
			translation.translate(this.creep.getX(), this.creep.getY());

			// transform to correct location
			g.transform(translation);

			// paint creep health bar
			paintHealthbar(g);

			rotation.setToIdentity();
			// rotate around the image center
			rotation.rotate(this.creep.getAngle(), 10, 10);

			// apply the rotation to the creep
			g.transform(rotation);

			g.drawImage(image, 0, 0, null);

			g.setTransform(save);
		}
	}


	/**
	 * Paints the health bar of the creeps.
	 * 
	 * @param g
	 *            the Graphics2D for drawing
	 */
	private void paintHealthbar(Graphics2D g) {
		Stroke s = g.getStroke();
		// set color of stroke
		g.setColor(this.getHealthBarColor());
		// make the line bigger
		g.setStroke(new BasicStroke(2));
		// paint the healthbar
		g.draw(this.getHealthBar());
		// set the stroke to what it was before
		g.setStroke(s);
	}

	private Line2D.Double getHealthBar() {
		double widht = ((double) this.creep.getHealth() / (double) this.creep.getType().getHealth() * 14d) + 3d;
		return new Line2D.Double(3d, 2d, widht, 2d);
	}

	private Color getHealthBarColor() {
		// calculate remaining percent of hit points
		float healthPercent = (float) this.creep.getHealth() / (float) this.creep.getType().getHealth() * 100;
		// change color according to remaining hit points
		if (healthPercent <= 10f)
			return Color.RED;
		else if (healthPercent <= 25f)
			return Color.ORANGE;
		else if (healthPercent <= 50f)
			return Color.YELLOW;
		return Color.GREEN;
		
	}
}
