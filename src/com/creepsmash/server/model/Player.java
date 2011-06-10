package com.creepsmash.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * A registered and persistable player.
 */
@Entity
@Table(name = "Player")
public class Player {

	@Id
	private String name;

	private String password;

	private String email;

	private int oldElopoints;
	
	private int elopoints;
	
	private long lastlogin;
	
	private boolean blocked;
	
	private int permission;
	
	private String ip;
	
	private String mac;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the oldElopoints
	 */
	public int getOldElopoints() {
		return this.oldElopoints;
	}

	/**
	 * @param oldElopoints the oldElopoints to set
	 */
	public void setOldElopoints(int oldElopoints) {
		this.oldElopoints = oldElopoints;
	}

	/**
	 * @return the elopoints
	 */
	public int getElopoints() {
		return this.elopoints;
	}

	/**
	 * @param elopoints the elopoints to set
	 */
	public void setElopoints(int elopoints) {
		this.elopoints = elopoints;
	}

	/**
	 * @return the lastlogin
	 */
	public long getLastlogin() {
		return lastlogin;
	}

	/**
	 * @param lastlogin the lastlogin to set
	 */
	public void setLastlogin(long lastlogin) {
		this.lastlogin = lastlogin;
	}

	/**
	 * @return the blocked
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * @param blocked the blocked to set
	 */
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	/**
	 * @return the permission
	 */
	public int getPermission() {
		return permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(int permissions) {
		this.permission = permissions;
	}
	
	public void addPermission(int permission) {
		this.permission |= permission;
	}
	
	public void removePermission(int permission) {
		this.permission &= ~permission;
	}
	
	public boolean hasPermission(int permission) {
		return (this.permission & permission) != 0;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	

}
