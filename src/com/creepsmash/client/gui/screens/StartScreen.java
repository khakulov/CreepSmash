package com.creepsmash.client.gui.screens;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import com.creepsmash.client.Core;
import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;



/**
 * The welcome screen for the game.
 */
public class StartScreen extends Screen {

	private static final long serialVersionUID = 1L;


	/**
	 * Creates a new instance.
	 * @param core the core of the game.
	 */
	public StartScreen() {
		super();
		JLabel startLabel = new JLabel("<html><center>"
				+ "<FONT color=#00ff33 face=Verdana size=28>" 
				+ "<br><br><br>CreepSmash<br>"
				+ "<FONT color=#D3CA24 size=3><br><br><br>"
				+ "* Multiplayer Vectorized Tower Defence *<br>"
				+ "Click to begin, F1 for Help<br><br><br><br><br>"
				+ "<FONT color=#42BD09>"
				+ "In a human unreachable, plane ( 2D-Graphic ) future<br>"
				+ "<br>"
				+ " - there is somewhere far, far away... -<br>"
				+ "<br>"
			    + "... at the end of an unknown galaxy, an intergalactic "
			    		+ "competition-planet named TW3000D.<br>"
			    + "Only one strong and brave hero of every race is allowed "
			    		+ "to fight there for honor and glory.<br>"
				+ "<br>"
			    + "For a new time!<br>"
				+ "<br>"
			    + "So every comrade-in-arms gets an armory of towers and "
			    		+ "creeps. Creeps are producing solar power energy, "
			    		+ "and caused by that,<br>"
			    + "they increase income! Even after their brain-dead, the "
			    		+ "solar panel will still supply energy, therefore"
			    		+ " income to war chest.<br>"
				+ "Lifepoints indicate health of every combatant. To stand "
						+ "against an opposing assault, the brave hero has to"
						+ " defend<br>"
				+ "his battelzone. This is done by buying and placing towers"
						+ " as smart as possible."
				+ "<br>"
				+ "For a creep, it's feasible to cross the landmark of a "
						+ "hero's battlezone. If this happens, lifepoints will "
						+ "decrease<br>"
				+ "until there are no more left - and if: dead will arise over "
						+ "this looser-battle-land.<br>"
				+ "<br>"
				+ "But the winner, that glory, women's beloved hero with "
						+ "still having lifepoints, will guide his race into "
						+ "a shiny new future,<br>"
				+ "over and over fulfilled with everything you wish him to "
						+ "be with.<br>"
				+ "<br>"
				+ "Will you be that hero?"
			    + "</center></html>");
		startLabel.setBounds(350, 250, 400, 30);
		this.add(startLabel);
		this.setBackground(Color.BLACK);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.GREEN);
		Graphics2D g2d = (Graphics2D) g;
		AlphaComposite myAlpha = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.22f);
		g2d.setComposite(myAlpha);
		g2d.fillRoundRect(30, 100, Core.WIDTH - 60, 100, 20, 20);
		AlphaComposite noAlpha = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f);
		g2d.setComposite(noAlpha);
		
		
	}

	@Override
	public void end() {
	}

	@Override
	public void start() {
	}

	@Override
    public void mouseReleased(MouseEvent e) {
    	Window.pushScreen(new LoginScreen());
    }
}
