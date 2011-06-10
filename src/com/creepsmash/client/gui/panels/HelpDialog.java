package com.creepsmash.client.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * The HelpGamePanel gives a help for players. It describes how the game works.
 * It gives also informations about our team.
 */

public class HelpDialog extends JDialog {

	private static final long serialVersionUID = 4L;

	private JEditorPane htmlDisplay = new JEditorPane();
	private JScrollPane jScrollPaneHelp = new JScrollPane();
	private JPanel content = new JPanel();

	private JButton quit;
	
	/**
	 * Constructor for the HelpGamePanel.
	 * @param owner
	 * @param title 
	 */
	public HelpDialog(JFrame owner) {
		super(owner);
		this.init();
	}

	/**
	 * This method initializes htmlDisplay.
	 * 
	 * @return htmlDisplay an EditorPane
	 */
	private JEditorPane gethtmlDisplay() {

		htmlDisplay.setEditable(false);
		htmlDisplay.setCaretPosition(0);
		htmlDisplay.setContentType("text/html");
		htmlDisplay.setOpaque(false);

		return htmlDisplay;
	}

	/**
	 * This method initializes jScrollPane.
	 * 
	 * @return jScrollPaneHelp an JScrollPane
	 */
	private JScrollPane getJScrollPaneHelp() {

		jScrollPaneHelp.setBounds(10, 10, 600, 600);
		jScrollPaneHelp
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPaneHelp.setViewportView(gethtmlDisplay());
		jScrollPaneHelp.getViewport().setOpaque(false);
		jScrollPaneHelp.setOpaque(false);

		return jScrollPaneHelp;
	}

	/**
	 * Creates a new instance of HelpGamePanel.
	 * 
	 */
	public HelpDialog() {
		this.init();
	}

	/**
	 * Initialize the Panel.
	 * 
	 */
	private void init() {
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.setResizable(false);
		
		content.setLayout(null);
		content.setBounds(0, 0, 630, 680);
		content.setBackground(Color.BLACK);
		
		this.quit = new JButton("Exit Help");
		this.quit.setBackground(Color.BLACK);
		this.quit.setForeground(Color.GREEN);
		this.quit.setBounds(265, 620, 100, 20);
		this.quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		try {
			final URL url = getClass().getClassLoader().getResource(
					"com/creepsmash/client/resources/help/index.html");
			htmlDisplay.setPage(url);

		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("HTML-Seite konnte nicht geladen werden!");
		}

		htmlDisplay.addHyperlinkListener(new HyperlinkListener() {

			public void hyperlinkUpdate(HyperlinkEvent e) {
				try {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						htmlDisplay.setPage(e.getURL());
					}
				} catch (IOException ex) {
					ex.printStackTrace();
					System.out
							.println("HTML-Seite konnte nicht geladen werden!");
				}
			}

		});

		content.add(getJScrollPaneHelp());
		content.add(quit);
		this.add(content);
	}
}

