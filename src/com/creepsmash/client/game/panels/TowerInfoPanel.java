package com.creepsmash.client.game.panels;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.creepsmash.common.TowerType;


/**
 * Panel that shows the attributes of the tower the user wants to build.
 */
public class TowerInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel nameInfo;
	private JLabel priceInfo;
	private JLabel damageInfo;
	private JLabel speedInfo;
	private JLabel rangeInfo;
	private JLabel specialInfo;
	
	/**
	 * creates new instance of BuildTowerInfoPanle.
	 */
	public TowerInfoPanel() {
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.setBounds(700, 200, 233, 125);

		nameInfo = new JLabel();
		nameInfo.setFont(new Font("Helvetica", Font.BOLD, 15));
		nameInfo.setBounds(5, 5, 200, 20);
		nameInfo.setForeground(Color.GREEN);
		this.add(nameInfo);
		
		priceInfo = new JLabel();
		priceInfo.setBounds(5, 35, 200, 15);
		priceInfo.setForeground(Color.GREEN);
		this.add(priceInfo);
		
		damageInfo = new JLabel();
		damageInfo.setBounds(5, 50, 200, 15);
		damageInfo.setForeground(Color.GREEN);
		this.add(damageInfo);
		
		speedInfo = new JLabel();
		speedInfo.setBounds(5, 65, 200, 15);
		speedInfo.setForeground(Color.GREEN);
		this.add(speedInfo);
		
		rangeInfo = new JLabel();
		rangeInfo.setBounds(5, 80, 200, 15);
		rangeInfo.setForeground(Color.GREEN);
		this.add(rangeInfo);
		
		specialInfo = new JLabel();
		specialInfo.setFont(new Font("Helvetica", Font.BOLD, 10));
		specialInfo.setBounds(5, 95, 200, 15);
		specialInfo.setForeground(Color.GREEN);
		this.add(specialInfo);
	}

	public void showTowerInfo(TowerType type) {
		this.nameInfo.setText(type.getName());
		this.priceInfo.setText("Price: " + format(type.getPrice()));
		this.damageInfo.setText("Damage: " + format(type.getDamage()));
		this.speedInfo.setText("Speed: " + type.getSpeedString());
		this.rangeInfo.setText("Range: " + (int) type.getRange());
		this.specialInfo.setText(type.getSpecial());
	}

	private String format(int value) {
		return NumberFormat.getInstance().format(value);
	}
}
