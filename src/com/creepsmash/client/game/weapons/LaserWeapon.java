package com.creepsmash.client.game.weapons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.grids.Grid;


public class LaserWeapon extends AbstractWeapon {
	@Override
	public void attack(Creep creep) {
		this.target = creep;
		this.damageCreep(this.target, this.tower.getType().getDamage());
		if (this.target.getHealth() <= 0)
			this.target = null;
	}

	@Override
	public void paintEffect(Graphics2D graphics) {
		if (this.target == null || !this.target.isActive())
			return;
		if (this.tower.getCoolDown() < this.tower.getType().getSpeed()-1)
			return;
		Stroke s = graphics.getStroke();
		Color c = graphics.getColor();

		Line2D beam = new Line2D.Double(this.tower.getGrid().getLocation()[0] + Grid.SIZE / 2,
										this.tower.getGrid().getLocation()[1] + Grid.SIZE / 2,
										this.target.getX() + Grid.SIZE / 2,
										this.target.getY() + Grid.SIZE / 2);

		graphics.setStroke(new BasicStroke(3));
		graphics.setColor(Color.WHITE);
		graphics.draw(beam);

		graphics.setStroke(new BasicStroke(1));
		graphics.setColor(Color.BLUE);
		graphics.draw(beam);

		graphics.setStroke(s);
		graphics.setColor(c);
	}
}
