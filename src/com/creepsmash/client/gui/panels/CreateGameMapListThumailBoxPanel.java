package com.creepsmash.client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.creepsmash.common.GameMap;


/**
 * The Create GameList Panel @ThumailBox
 * @see CreateGameMapListFrame
 */
public class CreateGameMapListThumailBoxPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 4L;

	public int mapID;
	public GameMap map;
	private JLabel name;
	private CreateGameMapListFrame creategameListPanel;
	
	public CreateGameMapListThumailBoxPanel(CreateGameMapListFrame createGameListPanel, GameMap map){
		this.mapID = map.ordinal();
		this.map = map;
		this.creategameListPanel = createGameListPanel;
		init();
	}
	/**
	 * Initialize the Panel.
	 * 
	 */
	private void init() {
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setSize(new Dimension(100, 110));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);
		
		name = new JLabel(this.map.toString());
		name.setSize(new Dimension(100, 10));
		name.setBackground(Color.BLACK);
		name.setFont(new Font("Arial", Font.PLAIN, 9));
		name.setForeground(Color.green);
		name.setHorizontalAlignment(JLabel.CENTER);
		name.setVerticalAlignment(JLabel.CENTER);
		
		ImageIcon preview = null;
		
		InputStream file = this.getClass().getClassLoader().getResourceAsStream(this.map.getPictureThumbnailPath());
			
		if (file != null) {

			try {
				preview = new ImageIcon(ImageIO.read(file));
				
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			preview.setImage(preview.getImage().getScaledInstance(100, 100,
					Image.SCALE_SMOOTH));
			
		}
		
		
		final JLabel previewLabel = new JLabel(preview);
		previewLabel.setSize(new Dimension(100, 100));
		previewLabel.setBackground(Color.BLACK);

		this.add(previewLabel, BorderLayout.NORTH);
		this.add(name, BorderLayout.SOUTH);
							
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.creategameListPanel.selectMap(this.mapID);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		name.setFont(new Font("Arial", Font.BOLD, 9));
		name.setForeground(Color.red);
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		name.setFont(new Font("Arial", Font.PLAIN, 9));
		name.setForeground(Color.green);
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
    
}
