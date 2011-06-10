package com.creepsmash.client.game.panels;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.creepsmash.common.CreepType;


/**
 * Panel that shows the attributes of the creep the user wants to send.
 */
public class CreepInfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel nameInfo;
	private JLabel priceInfo;
	private JLabel incomeInfo;
	private JLabel healthInfo;
	private JLabel speedInfo;
	private JLabel specialInfo;

	/**
	 * creates new instance of CreepInfoPanle.
	 */
	public CreepInfoPanel() {

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

		incomeInfo = new JLabel();
		incomeInfo.setBounds(5, 50, 200, 15);
		incomeInfo.setForeground(Color.GREEN);
		this.add(incomeInfo);

		healthInfo = new JLabel();
		healthInfo.setBounds(5, 65, 200, 15);
		healthInfo.setForeground(Color.GREEN);
		this.add(healthInfo);

		speedInfo = new JLabel();
		speedInfo.setBounds(5, 80, 200, 15);
		speedInfo.setForeground(Color.GREEN);
		this.add(speedInfo);	

		specialInfo = new JLabel();
		specialInfo.setBounds(5, 95, 200, 15);
		specialInfo.setForeground(Color.GREEN);
		this.add(specialInfo);
	}

	public void showCreepInfo(CreepType type) {
		this.nameInfo.setText(type.getName());
		this.priceInfo.setText("Price: " + format(type.getPrice()));
		this.incomeInfo.setText("income: +" + format(type.getIncome()));
		this.healthInfo.setText("Health: " + format(type.getHealth()));
		this.speedInfo.setText("Speed: " + type.getSpeedString());
		this.specialInfo.setText(type.getSpecial());
	}

	private String format(int value) {
		return NumberFormat.getInstance().format(value);
	}
}
