package com.creepsmash.client.game.panels;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.BoardLocation;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.contexts.Path;
import com.creepsmash.client.game.grids.EmptyGrid;
import com.creepsmash.client.game.grids.Grid;
import com.creepsmash.client.game.grids.HolyGrid;
import com.creepsmash.client.game.grids.PathGrid;
import com.creepsmash.client.game.towers.Tower;


/**
 * Holds the whole grid and acts as an mouseListener on the BoardPanel.
 */
public class GameBoard implements MouseListener, MouseMotionListener {

	private static final int WIDTH = 16;
	private static final int HEIGHT = 16;

	private Context context;
	private Grid[][] gridArray;

	private BufferedImage image;
	private boolean clearImg = true;
	
	private BufferedImage imgBackground;
	private BufferedImage imgBackgroundRotated;
	
	private Color playerContextAlphaColor = null;

	private Path path;
	private BufferedReader br;
	private Point[] mapWithPath;
	private Point[] mapWithHoly;
	
	/**
	 * Creates a new instance of GameBoard.
	 * 
	 * @param context the gameContext
	 */
	public GameBoard(Context context) {
		this.context = context;
		this.setPath(new Path());
		this.imgBackground = new BufferedImage(321, 321, BufferedImage.TYPE_INT_RGB);
		this.imgBackgroundRotated = new BufferedImage(321, 321, BufferedImage.TYPE_INT_RGB);

		this.loadMap();
		this.rotateMap(context.getLocation());
		this.initializeGridArray();
		
		this.image = new BufferedImage(321, 321, BufferedImage.TYPE_INT_RGB);
	}

	private void initializeGridArray() {
		this.gridArray = new Grid[WIDTH][HEIGHT];

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				gridArray[i][j] = new EmptyGrid(i * Grid.SIZE, j * Grid.SIZE, this.context);
			}
		}

		for (int i = 0; i < this.mapWithPath.length; i++) {
			int x = (int) this.mapWithPath[i].getX();
			int y = (int) this.mapWithPath[i].getY();
			this.path.addSegment(x, y);
			gridArray[x][y] = new PathGrid(x * Grid.SIZE, y * Grid.SIZE, this.context);
		}

		// add HolyGrids to gridArray
		for (int i = 0; i < this.mapWithHoly.length; i++) {
			int xHoly = (int) this.mapWithHoly[i].getX();
			int yHoly = (int) this.mapWithHoly[i].getY();
			gridArray[xHoly][yHoly] = new HolyGrid(WIDTH * Grid.SIZE, HEIGHT * Grid.SIZE, this.context);
		}
	}

	/**
	 * dummy method for a fake map :).
	 */
	private void loadMap() {

		String filename = "";

		ArrayList<String> lineWithPath = new ArrayList<String>();
		ArrayList<String> lineWithHoly = new ArrayList<String>();
		String tempStr = null;

		InputStream res =
			this.getClass().getClassLoader().getResourceAsStream(
					this.context.getGame().getMap().getFilename());
		if (res == null) {
			Core.logger.severe("Cannot open map!");
		}
		br = new BufferedReader(new InputStreamReader(res));
		
		try {
			while ((tempStr = br.readLine()) != null) {
				if (tempStr.contains(",")) {
					lineWithPath.add(tempStr);
				}
				if (tempStr.contains(";")) {
					lineWithHoly.add(tempStr);
				}
				if (tempStr.contains(".bmp") || tempStr.contains(".png") 
						|| tempStr.contains(".jpg")) {
					filename = tempStr.trim();
				}
				if (tempStr.contains("SET_ALPHA_BACKGROUND_COLOR:")) {
					String[] split = tempStr.trim().split(":");
					if (split[1].equals("GRAY")) {
						playerContextAlphaColor = Color.GRAY;
					}
					if (split[1].equals("WHITE")) {
						playerContextAlphaColor = Color.WHITE;
					}
					if (split[1].equals("BLUE")) {
						playerContextAlphaColor = Color.BLUE;
					}
					if (split[1].equals("RED")) {
						playerContextAlphaColor = Color.RED;
					}
					if (split[1].equals("YELLOW")) {
						playerContextAlphaColor = Color.YELLOW;
					}
					if (split[1].equals("MAGENTA")) {
						playerContextAlphaColor = Color.MAGENTA;
					}
					if (split[1].equals("CYAN")) {
						playerContextAlphaColor = Color.CYAN;
					}
					if (split[1].equals("GREEN")) {
						playerContextAlphaColor = Color.GREEN;
					}
					if (split[1].equals("OFF")) {
						playerContextAlphaColor = null;
					}
				}
			}
		} catch (IOException e) {
			Core.logger.severe("Cannot read map!");
		}

		mapWithPath = new Point[lineWithPath.size()];
		for (int i = 0; i < mapWithPath.length; i++) {
			tempStr = (String) lineWithPath.get(i);
			String[] split = tempStr.split(",");
			mapWithPath[i] = new Point(Integer.parseInt(split[0]),
					Integer.parseInt(split[1]));	
		}
		
		mapWithHoly = new Point[lineWithHoly.size()];
		for (int i = 0; i < mapWithHoly.length; i++) {
			tempStr = (String) lineWithHoly.get(i);
			String[] split = tempStr.split(";");
			mapWithHoly[i] = new Point(Integer.parseInt(split[0]),
					Integer.parseInt(split[1]));	
		}
		
		try {
			imgBackground = ImageIO.read(
					this.getClass().getClassLoader().getResourceAsStream(
							"com/creepsmash/client/resources/maps/" + filename
					)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rotates the map.
	 * @param location the boardLocation
	 */
	private void rotateMap(BoardLocation location) {
		
		Graphics2D bgGraphic = imgBackgroundRotated.createGraphics();
		
		switch(location) {
		case TOPLEFT:
			// top left is reading location
			bgGraphic.drawImage(imgBackground, 0, 0, null);
			break;
		case TOPRIGHT:
			for (int i = 0; i < mapWithPath.length; i++) {
				mapWithPath[i] = new Point((WIDTH - 1) 
						- (int) mapWithPath[i].getY(), 
						(int) mapWithPath[i].getX());
			}
			for (int i = 0; i < mapWithHoly.length; i++) {
			mapWithHoly[i] = new Point((WIDTH - 1) - (int) mapWithHoly[i].getY(), 
					(int) mapWithHoly[i].getX());
			}
			
			bgGraphic.rotate(90 * Math.PI / 180, 160.5, 160.5);
			bgGraphic.drawImage(imgBackground, 0, 0, null);
			break;
		case BOTTOMRIGHT:
			for (int i = 0; i < mapWithPath.length; i++) {
				mapWithPath[i] = new Point((WIDTH - 1) 
						- (int) mapWithPath[i].getX(),
						(WIDTH - 1) - (int) mapWithPath[i].getY());
			}
			for (int i = 0; i < mapWithHoly.length; i++) {
				mapWithHoly[i] = new Point((WIDTH - 1) 
						- (int) mapWithHoly[i].getX(),
						(WIDTH - 1) - (int) mapWithHoly[i].getY());
			}
			bgGraphic.rotate(180 * Math.PI / 180, 160.5, 160.5);
			bgGraphic.drawImage(imgBackground, 0, 0, null);
			break;
		case BOTTOMLEFT:
			for (int i = 0; i < mapWithPath.length; i++) {
				mapWithPath[i] = new Point((int) mapWithPath[i].getY(),
						(WIDTH - 1) - (int) mapWithPath[i].getX());
			}
			for (int i = 0; i < mapWithHoly.length; i++) {
				mapWithHoly[i] = new Point((int) mapWithHoly[i].getY(),
						(WIDTH - 1) - (int) mapWithHoly[i].getX());
			}
			bgGraphic.rotate(270 * Math.PI / 180, 160.5, 160.5);
			bgGraphic.drawImage(imgBackground, 0, 0, null);
			break;
		
		default:
			Core.logger.severe("Unknown BoardLocation");
			break;
		}
	}

	/**
	 * paint method to paint the board.
	 * 
	 * @param g the graphics2d object
	 */
	public void paint(Graphics2D g) {
		// paint grid background into bufferedImage
		if (clearImg) {

			Graphics2D g2d = (Graphics2D) image.getGraphics();
			
			g2d.drawImage(imgBackgroundRotated, 0, 0, null);

			/* draws a filled, blended, rect around the current 
			 * player, turn it off or on, just as you like it
			 */
			if (this.context == this.context.getGame().getPlayerCotext()
					&& playerContextAlphaColor != null) {

				//g2d.setColor(playerContextAlphaColor);
				//g2d.drawRect(0, 0, 319, 320);
				
				AlphaComposite myAlpha = AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.12f);
				g2d.setComposite(myAlpha);
				g2d.fillRoundRect(0, 0, 320, 320, 20, 20);
				AlphaComposite noAlpha = AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1.0f);
				g2d.setComposite(noAlpha);
			}
			

			
			for (int i = 0; i < gridArray.length; i++) {
				for (int j = 0; j < gridArray[0].length; j++) {
					/* maybe you like it, turn it on
					 * this shows the grid  
					 */  
					//gridArray[i][j].paint(g2d);

					
					// paint the tower in the grid
					if (gridArray[i][j].getTower() != null) {
                        gridArray[i][j].getTower().paint(g2d);
                    }
				}
			}
			
			
			g2d.dispose();
			clearImg = false;
			
		}
		// paint image
		g.drawImage(image, 0, 0, null);

		// paint the grid highlighting
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[0].length; j++) {
				gridArray[i][j].paintHighlight(g);
			}
		}
		
	}

	/**
	 * Sets a boolean so that the grid repaints its image.
	 */
    public void clearImage() {
        this.clearImg = true;
    }

	/**
	 * deselects all towers.
	 */
	public void deSelectTowers() {
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[0].length; j++) {
				if (gridArray[i][j].getTower() != null) {
					gridArray[i][j].getTower().setSelected(false);
				}
			}
		}
	}
	
	/**
	 * removes the tower from the grid.
	 * @param id towerID
	 */
	public void removeTower(int id) {
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[0].length; j++) {
				if (gridArray[i][j].getTower() != null) {
					if (gridArray[i][j].getTower().getId() == id) {
						gridArray[i][j] = new EmptyGrid(
								i * Grid.SIZE, j * Grid.SIZE,
								this.context);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Gets a grid for a position on the GameBoard. This method is designed to
	 * be used with mouse events. Translation uses the board offset and the grid
	 * size.
	 * 
	 * @param x the x location
	 * @param y the y location
	 * @return a grid or null if no grid was found
	 */
	private Grid getGridForLocation(int x, int y) {

		int xReal = (x - (int) context.getLocation().getX()) / Grid.SIZE;
		int yReal = (y - (int) context.getLocation().getY()) / Grid.SIZE;
		if (xReal > gridArray.length || yReal > gridArray[0].length) {
			return null;
		}
		return gridArray[xReal][yReal];
	}

	/**
	 * Getter for a grid by position.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the grid
	 */
	public Grid getGrid(int x, int y) {
		x = x / Grid.SIZE;
		y = y / Grid.SIZE;
		if (x < gridArray.length && y < gridArray[0].length) {
			return this.gridArray[x][y];
		} else {
			return null;
		}
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * Process a mouse click. 
	 * @param g the grid on which the click happend
	 */
	private void processClick(Grid g) {
		if (context.getNextTower() != null && this.context.buyTower(context.getNextTower(), g)) {
			return;

		// case2: select empty field 
		} else if (g.isFree()) {
			context.setSelectedTower(null);
			context.setNextTower(null);
			deSelectTowers();
			context.fireSelectedChangedEvent("empty");

		// case3: select tower	
		} else { 
			Tower t = g.getTower(); // get that tower object
			if (t == null) {
				this.context.setSelectedTower(null);
				return;
			}
			deSelectTowers();
			t.setSelected(true);
			this.context.setSelectedTower(t);
			context.setNextTower(null);
			context.fireSelectedChangedEvent("tower");
			context.fireSelectedChangedEvent("strategy");
		}
	}
	
	public void mouseClicked(MouseEvent e) {

		// this method is not really good for click detection
		// 1 pixel dragging while clicking and this event is not fired
		// so we have to build a workaround for this with the other events

		if (context.getLocation().getBounds().contains(e.getPoint())) {

			Grid g = getGridForLocation(e.getX(), e.getY());

			processClick(g);
		} 
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		if (context.getLocation().getBounds().contains(e.getPoint())) {
			Grid g = getGridForLocation(e.getX(), e.getY());
			processClick(g);
		} 
	}

	public void mouseMoved(MouseEvent e) {
		this.clearHighlight();
		if (context.getLocation().getBounds().contains(e.getPoint())) {
			Grid g = getGridForLocation(e.getX(), e.getY());
			g.setHighlight(true);
		}
	}

	public Grid getHighlightedGrid() {
		Grid g = null;
		// get the highlighted grid (cell)
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[0].length; j++) {
				if (gridArray[i][j].getHighlight())
					g = gridArray[i][j];
			}
		}
		return g;
	}

	/**
	 * Sets the highlight for all grids to false.
	 */
	private void clearHighlight() {
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[0].length; j++) {
				gridArray[i][j].setHighlight(false);
			}
		}
	}
}
