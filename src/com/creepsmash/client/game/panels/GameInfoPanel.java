package com.creepsmash.client.game.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.creepsmash.client.game.Game;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.contexts.ContextListener;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.gui.screens.GameResultScreen;
import com.creepsmash.client.gui.screens.GameScreen;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.messages.client.ExitGameMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;


/**
 * Class painting the game information about the player and his opponents.
 */
public class GameInfoPanel extends JPanel implements ContextListener {
	
	private static final long serialVersionUID = -1L;
	private static final NumberFormat decimalFormat = NumberFormat.getInstance();

	private Game game;

	private Context playerContext;
	private Context opponent1Context;
	private Context opponent2Context;
	private Context opponent3Context;
	
	private JLabel credits;
	private JLabel income;
	private JLabel playerLives;
	private JLabel incomeCounter;
	
	private JLabel opponent1Lives;
	private JLabel opponent2Lives;
	private JLabel opponent3Lives;
	
	/**
	 * Creates a new GameInfoPanel.
	 */
	public GameInfoPanel(GameScreen gameScreen) {
		this.game = gameScreen.getGame();

		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.setBounds(700, 0, 233, 100);

		JButton quitButton = new JButton("Quit");
		quitButton.setFont(new Font("Helvetica", Font.PLAIN, 8));
		quitButton.setBounds(163, 10, 60, 20);
		quitButton.setBackground(Color.BLACK);
		quitButton.setForeground(Color.GREEN);
		quitButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// sends a messages to all players that we have left
				SendMessageMessage sendMessageMessage = new SendMessageMessage();
				sendMessageMessage.setMessage("has left the game");
				Network.sendMessage(sendMessageMessage);

				Network.sendMessage(new ExitGameMessage());

				if (!GameInfoPanel.this.game.isRunning()) {
					Window.switchScreen(new GameResultScreen(GameInfoPanel.this.game.getPlayers()));
				} else {
					Window.popScreen();
				}
			}
		});
		this.add(quitButton);

		incomeCounter = new JLabel("Next income in: ");
		incomeCounter.setForeground(Color.WHITE);
		incomeCounter.setBounds(0, 0, 233, 15);
		
		credits = new JLabel("Credits: ");
		credits.setForeground(Color.WHITE);
		credits.setBounds(0, 15, 233, 15);
		
		income = new JLabel("Income: ");
		income.setForeground(Color.WHITE);
		income.setBounds(0, 30, 233, 15);
		
		playerLives = new JLabel("Lives: ");
		playerLives.setForeground(Color.WHITE);
		playerLives.setBounds(0, 45, 233, 15);
		
		opponent1Lives = new JLabel();
		opponent1Lives.setForeground(Color.WHITE);
		opponent1Lives.setBounds(0, 60, 233, 13);
		
		opponent2Lives = new JLabel();
		opponent2Lives.setForeground(Color.WHITE);
		opponent2Lives.setBounds(0, 73, 233, 13);
		
		opponent3Lives = new JLabel();
		opponent3Lives.setForeground(Color.WHITE);
		opponent3Lives.setBounds(0, 86, 233, 13);
		
		this.add(incomeCounter);
		this.add(credits);
		this.add(income);
		this.add(playerLives);
		this.add(opponent1Lives);
		this.add(opponent2Lives);
		this.add(opponent3Lives);
		
		for (int playerId : this.game.getPlayers().keySet()) {
			Context context = this.game.getContext(playerId);
			context.addContextListener(this);

			if (context == this.game.getPlayerCotext()) {
				this.playerContext = context;
			} else if (this.opponent1Context == null) {
				this.opponent1Context = context;
			} else if (this.opponent2Context == null) {
				this.opponent2Context = context;
			} else if (this.opponent3Context == null) {
				this.opponent3Context = context;
			}
			this.creditsChanged(context);
			this.incomeChanged(context);
			this.incomeTimeChanged(context, 15);
			this.livesChanged(context);
		}
	}

	public void creditsChanged(Context context) {
		if (context.equals(playerContext)) {
			this.credits.setText("Credits: " + format(context.getCredits()));
			this.repaint();
		}
	}

	public void incomeChanged(Context context) {
		if (context.equals(playerContext)) {
			this.income.setText("Income: " + format(context.getIncome()));
			this.repaint();
		}
	}

	public void incomeTimeChanged(Context context, int seconds) {
		if (context == playerContext) {
			this.incomeCounter.setText("New income in " + seconds);
			this.repaint();
		}
	}

	public void livesChanged(Context context) {
		if (context == playerContext) {
			this.playerLives.setText("Lives: " + context.getLives());
		} else if (context.equals(opponent1Context)) {
			this.opponent1Lives.setText(context.getPlayerName() + ": " + context.getLives());
		} else if (context.equals(opponent2Context)) {
			this.opponent2Lives.setText(context.getPlayerName() + ": " + context.getLives());
		} else if (context.equals(opponent3Context)) {
			this.opponent3Lives.setText(context.getPlayerName() + ": " + context.getLives());
		}
		this.repaint();
	}

	public void selectedChanged(Context context, String message) {
	}

	private String format(int value) {
		return decimalFormat.format(value);
	}

}
