package com.creepsmash.client.game.panels;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * The GamePanel contains all player maps.
 * Main purpose is to manage fast drawing.
 */
public class BoardPanel extends Canvas {

	private static final long serialVersionUID = 1L;

	public static boolean ANTIALIAS = false;

	/**
	 * Creates a new BoardPanel with the specified height and width.
	 */
	public BoardPanel() {
		this.setForeground(Color.BLACK);
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(700, 700));
		this.setSize(700, 700);
		this.setBounds(0, 0, 700, 700);
		this.setIgnoreRepaint(true);
	}

	/**
	 * Gets the Graphics2D object for the game.
	 * 
	 * @return the Graphics2D object
	 */
	public Graphics2D getGraphics() {
		if (this.getBufferStrategy() == null)
			this.createBufferStrategy(2);
		Graphics2D graphics = (Graphics2D) this.getBufferStrategy().getDrawGraphics();
		if (ANTIALIAS)
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// paint the background black
		graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
		return graphics;
	}
}
