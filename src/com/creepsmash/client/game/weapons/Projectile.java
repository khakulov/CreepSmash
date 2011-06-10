package com.creepsmash.client.game.weapons;

import java.util.ArrayList;

import com.creepsmash.client.game.creeps.Creep;


public class Projectile {

	private double x;
	private double y;
	private float speed;
	private Creep target;
	private ArrayList<Creep> splashTargets = new ArrayList<Creep>();
	private int status = 0;

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @return the target
	 */
	public Creep getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(Creep target) {
		this.target = target;
	}
	/**
	 * @return the splashTargets
	 */
	public ArrayList<Creep> getSplashTargets() {
		return splashTargets;
	}
	/**
	 * @param splashTargets the splashTargets to set
	 */
	public void setSplashTargets(ArrayList<Creep> splashTargets) {
		this.splashTargets = splashTargets;
	}
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
