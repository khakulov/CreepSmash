package com.creepsmash.client.game.grids;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.towers.Tower;


/**
 * Grid for all the places the player can build his towers onto.
 */
public class EmptyGrid implements Grid {

	private Context context;
	private int xLocation;
	private int yLocation;
	private int[] location = new int[2];
	private Tower tower;
	private boolean highlight = false;
	private AffineTransform pos = new AffineTransform();
	private Color gridColor = Color.ORANGE;
	private final Color gridHighlightColor = new Color(255, 255, 255);
	private final Color gridHighlightColorUnavailable = new Color(255, 0, 0);
	
	public static boolean ALPHA = false;
	
	private AlphaComposite myAlpha = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER, 0.1f);
	private AlphaComposite noAlpha = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER, 1.0f);

	/**
	 * Creates a new instance of EmptyGrid.
	 * 
	 * @param x
	 *            the x location on the board
	 * @param y
	 *            thy y location on the board
	 * @param context
	 *            the gameContext
	 */
	public EmptyGrid(int x, int y, Context context) {
		this.xLocation = x;
		this.yLocation = y;
		this.location[0] = x;
		this.location[1] = y;
		this.context = context;
		if (this.context == this.context.getGame().getPlayerCotext()) {
			gridColor = new Color(255, 100, 0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int[] getLocation() {
		return location;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFree() {
		return (this.tower == null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub
		AffineTransform save = g.getTransform();

		pos.setToIdentity();
		pos.translate(xLocation, yLocation);

		g.transform(pos);

		g.setColor(gridColor);
		g.drawLine(0, 0, SIZE, 0);
		g.drawLine(SIZE, 0, SIZE, SIZE);
		g.drawLine(0, SIZE, SIZE, SIZE);
		g.drawLine(0, 0, 0, SIZE);
		g.setTransform(save);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paintHighlight(Graphics2D g) {
		if (this.highlight) {
			AffineTransform save = g.getTransform();

			pos.setToIdentity();
			pos.translate(xLocation, yLocation);

			g.transform(pos);

			if (context.getNextTower() != null) {

				if (context.getCredits() < context.getNextTower().getPrice()) {
					g.setColor(gridHighlightColorUnavailable);
				} else {
					g.setColor(gridHighlightColor);
				}
				if (this.getTower() == null) {
					Arc2D rangeArc = new Arc2D.Float();

					rangeArc.setArcByCenter(Grid.SIZE / 2, Grid.SIZE / 2,
							this.context.getNextTower().getRange(), 0.0, 360.0,
							Arc2D.CHORD);
				
					if (ALPHA) {
						g.setComposite(myAlpha);
						g.fill(rangeArc);
						g.setComposite(noAlpha);
					}
					
					g.draw(rangeArc);
					
					g.fillRect(1, 1, SIZE - 1, SIZE - 1);
				} else {
					g.setColor(Color.WHITE);
					g.drawRect(0, 0, SIZE, SIZE);
				}

			} else {
				g.setColor(Color.WHITE);
				g.drawRect(0, 0, SIZE, SIZE);
			}

			g.setTransform(save);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeTower() {
		this.tower = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTower(Tower t) {
		this.tower = t;
	}

	/**
	 * Getter for the tower in the grid.
	 * 
	 * @return the tower or null if no tower is in the grid
	 */
	public Tower getTower() {
		return this.tower;
	}

	/**
	 * @param highlight
	 *            the highlight to set
	 */
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
	
	public boolean getHighlight() {
		return this.highlight;
	}

	@Override
	public int getX() {
		return xLocation;
	}

	@Override
	public int getY() {
		return yLocation;
	}
}
