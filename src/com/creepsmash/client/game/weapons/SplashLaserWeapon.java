package com.creepsmash.client.game.weapons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.grids.Grid;


public class SplashLaserWeapon extends AbstractWeapon {
	protected List<Creep> targets;

	@Override
	public void attack(Creep creep) {
		this.target = creep;
		this.targets = new ArrayList<Creep>();
		List<Creep> creeps = this.tower.getContext().getCreeps();
		// find creeps in splashradius
		for (Creep cr: creeps) {
			if (cr.isActive()) {
				if (this.inRange(this.target.getX(), this.target.getY(),
								 cr.getX(), cr.getY(),
								 this.tower.getType().getSplashRadius())) {
					this.targets.add(cr);
				}
			}
		}
		// damage targets
		double xDiff, yDiff, dist, reduction;
		for (Creep cr : this.targets) {
			xDiff = cr.getX() - this.target.getX();
			yDiff = cr.getY() - this.target.getY();
			dist = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

			// if the distance is high the damage becomes lower
			reduction = (this.tower.getType().getDamageReductionAtRadius() / this.tower.getType().getSplashRadius()) * dist;
			reduction = this.tower.getType().getDamage() * reduction;
			this.damageCreep(cr, this.tower.getType().getDamage() - (int) reduction);
		}
		if (this.target.getHealth() <= 0) {
			this.target = null;
			this.targets = null;
		}
	}

	@Override
	public void paintEffect(Graphics2D graphics) {
		if (this.target == null || !this.target.isActive())
			return;
		if (this.tower.getCoolDown() < this.tower.getType().getSpeed()-1)
			return;
		Stroke s = graphics.getStroke();
		Color c = graphics.getColor();

		Line2D beam = new Line2D.Double(this.tower.getGrid().getX() + Grid.SIZE / 2,
									    this.tower.getGrid().getY() + Grid.SIZE / 2,
				                 		this.target.getX() + Grid.SIZE / 2,
				                 		this.target.getY() + Grid.SIZE / 2);

		graphics.setStroke(new BasicStroke(3));
		graphics.setColor(Color.WHITE);
		graphics.draw(beam);

		graphics.setStroke(new BasicStroke(1));
		graphics.setColor(Color.BLUE);
		graphics.draw(beam);

		for (Creep cr : this.targets) {
			Line2D splash = new Line2D.Double(this.target.getX() + Grid.SIZE / 2,
											  this.target.getY() + Grid.SIZE / 2,
					                   		  cr.getX() + Grid.SIZE / 2,
					                   		  cr.getY() + Grid.SIZE / 2);
			graphics.setStroke(new BasicStroke(1));
			graphics.setColor(Color.WHITE);
			graphics.draw(splash);
		}

		graphics.setStroke(s);
		graphics.setColor(c);
	}

	private boolean inRange(double x1, double y1, double x2, double y2, double range) {
		return (((x2-x1) * (x2-x1)) + ((y2-y1) * (y2-y1))) < (range*range);
	}
}
