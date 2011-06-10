package com.creepsmash.client.game.towers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.creepsmash.client.game.grids.EmptyGrid;
import com.creepsmash.client.game.grids.Grid;
import com.creepsmash.client.utils.Cache;
import com.creepsmash.common.IConstants;


public class TowerRenderer {
	private Tower tower;

	private AffineTransform translation;
	
	protected BufferedImage image;

	private AlphaComposite myAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
	private AlphaComposite noAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);

	public TowerRenderer(Tower tower) {
		this.tower = tower;

		this.translation = new AffineTransform();
		this.translation.setToIdentity();
		this.translation.translate(this.tower.getGrid().getX(), this.tower.getGrid().getY());

		this.loadImage();
	}

	public void loadImage() {
		if (Cache.getInstance().hasArrayTowerImg(this.tower.getType())) {
			this.image = Cache.getInstance().getTowerImg(this.tower.getType());
			return;
		}
		try {
			this.image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(this.tower.getType().getImageFileName()));
			Cache.getInstance().putTowerImg(this.tower.getType(), this.image);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics2D graphics) {
		AffineTransform save = graphics.getTransform();
		graphics.transform(translation);
		graphics.drawImage(image, 0, 0, null);
		graphics.setTransform(save);
	}

	public void paintEffect(Graphics2D graphics) {
		AffineTransform save = graphics.getTransform();

		graphics.transform(translation);

		if (this.tower.isSelected()) {
			graphics.setColor(Color.YELLOW);
			Arc2D rangeArc = new Arc2D.Float();
			rangeArc.setArcByCenter(Grid.SIZE / 2, Grid.SIZE / 2, this.tower.getType().getRange(), 0.0, 360.0, Arc2D.CHORD);

			if (EmptyGrid.ALPHA) {
				graphics.setComposite(myAlpha);
				graphics.fill(rangeArc);
				graphics.setComposite(noAlpha);
			}

			graphics.draw(rangeArc);
			graphics.drawRect(0, 0, Grid.SIZE, Grid.SIZE);
		}

		// works only if a client does not lag too much
		// or the build time can be bigger than USER_ACTION_DELAY
		if (this.tower.getBuildTime() > 0) {
			
			graphics.setColor(Color.BLACK);
			graphics.fillRect(20 - 20 * this.tower.getBuildTime() / IConstants.USER_ACTION_DELAY,
					1, 20 * this.tower.getBuildTime() / IConstants.USER_ACTION_DELAY, 19);
		}

		if (this.tower.getUpgradeTime() > 0) {
			graphics.setColor(Color.BLUE);
			graphics.fillRect(1, 1, 20 * this.tower.getUpgradeTime() / IConstants.USER_ACTION_DELAY, 3);
		}

		if (this.tower.getSellTime() > 0) {
			graphics.setColor(Color.RED);
			graphics.fillRect(1, 1, 20 * this.tower.getSellTime() / IConstants.USER_ACTION_DELAY,3);
		}
		
		if (this.tower.getChangeStrategyTime() > 0) {
			graphics.setColor(Color.ORANGE);
			graphics.fillRect(1, 1, 20 * this.tower.getChangeStrategyTime() / IConstants.USER_ACTION_DELAY,3);
		}

		graphics.setTransform(save);
	}
}
