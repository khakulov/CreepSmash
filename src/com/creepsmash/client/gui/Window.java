package com.creepsmash.client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.JFrame;

import com.creepsmash.client.Core;
import com.creepsmash.client.network.Network;



/**
 * This class manages the displayed screens
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	private static Stack<Screen> screens = new Stack<Screen>();

	private static final Window INSTANCE = new Window();
	/**
	 * Creates a new core instance.
	 */
	public Window() {

		this.setTitle("CreepSmash 1.0");
		this.getContentPane().setPreferredSize(Core.SCREENSIZE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - Core.WIDTH)/2, (screenSize.height - Core.HEIGHT)/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Network.disconnect();
			}
		});
		this.pack();
	}

	/**
	 * Adds a screen to the top of the stack and makes it visible. If there is a
	 * screen on the stack, its end method is called.
	 * 
	 * @param screen
	 *            the game screen to add
	 */
	public static void pushScreen(Screen screen) {
		if (!screens.empty()) {
			Screen oldScreen = screens.peek();
			oldScreen.end();
			INSTANCE.remove(oldScreen);
		}

		screen.initialize();
		INSTANCE.add(screen);
		screens.push(screen);
		screen.validate();
		screen.repaint();
		screen.start();
		INSTANCE.repaint();
		INSTANCE.setVisible(true);
	}



	/**
	 * Removes a screen from the stack.
	 */
	public static void popScreen() {
		if (!screens.empty()) {
			Screen oldScreen = screens.pop();
			oldScreen.end();
			INSTANCE.remove(oldScreen);

			Screen screen = screens.peek();
			INSTANCE.add(screen);
			screen.validate();
			screen.repaint();
			screen.start();
			INSTANCE.repaint();
		}
	}

	/**
	 * Clear the screen.
	 */
	public static void clearScreen() {
		while (!screens.empty()) {
			Screen screen = screens.pop();
			INSTANCE.remove(screen);
			screen.end();
		}
		INSTANCE.repaint();
	}

	/**
	 * Remove actual screen from list and add a new screen to list
	 */
	public static void switchScreen(Screen screen) {
		if (!screens.empty()) {
			Screen oldScreen = screens.pop();
			oldScreen.end();
			INSTANCE.remove(oldScreen);
		}

		screen.initialize();
		INSTANCE.add(screen);
		screens.push(screen);
		screen.validate();
		screen.repaint();
		screen.start();
		INSTANCE.repaint();
	}
}