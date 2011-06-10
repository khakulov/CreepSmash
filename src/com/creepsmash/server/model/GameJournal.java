package com.creepsmash.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GameJournal")
public class GameJournal {

	@Id
	private int id;
	
	private String name;
	
	private String map;
	
	private long start_date;
	
	private long end_date;
	
	private int numPlayers;
	
	private String player1;
	
	private String player2;
	
	private String player3;
	
	private String player4;
	
	private int player1_score;
	
	private int player2_score;
	
	private int player3_score;
	
	private int player4_score;
	
	private int player1_position;

	private int player2_position;
	
	private int player3_position;
	
	private int player4_position;
	
	private int score1;
	
	private int score2;
	
	private int score3;
	
	private int score4;
	
	private String ip1;
	
	private String ip2;
	
	private String ip3;
	
	private String ip4;

	private String mac1;
	
	private String mac2;
	
	private String mac3;
	
	private String mac4;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the map
	 */
	public String getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(String map) {
		this.map = map;
	}

	/**
	 * @return the start_date
	 */
	public long getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date the start_date to set
	 */
	public void setStart_date(long start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public long getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date the end_date to set
	 */
	public void setEnd_date(long end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the numPlayers
	 */
	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * @param numPlayers the numPlayers to set
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	/**
	 * @return the player1
	 */
	public String getPlayer1() {
		return player1;
	}

	/**
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	/**
	 * @return the player2
	 */
	public String getPlayer2() {
		return player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	/**
	 * @return the player3
	 */
	public String getPlayer3() {
		return player3;
	}

	/**
	 * @param player3 the player3 to set
	 */
	public void setPlayer3(String player3) {
		this.player3 = player3;
	}

	/**
	 * @return the player4
	 */
	public String getPlayer4() {
		return player4;
	}

	/**
	 * @param player4 the player4 to set
	 */
	public void setPlayer4(String player4) {
		this.player4 = player4;
	}

	/**
	 * @return the player1_score
	 */
	public int getPlayer1_score() {
		return player1_score;
	}

	/**
	 * @param player1_score the player1_score to set
	 */
	public void setPlayer1_score(int player1_score) {
		this.player1_score = player1_score;
	}

	/**
	 * @return the player2_score
	 */
	public int getPlayer2_score() {
		return player2_score;
	}

	/**
	 * @param player2_score the player2_score to set
	 */
	public void setPlayer2_score(int player2_score) {
		this.player2_score = player2_score;
	}

	/**
	 * @return the player3_score
	 */
	public int getPlayer3_score() {
		return player3_score;
	}

	/**
	 * @param player3_score the player3_score to set
	 */
	public void setPlayer3_score(int player3_score) {
		this.player3_score = player3_score;
	}

	/**
	 * @return the player4_score
	 */
	public int getPlayer4_score() {
		return player4_score;
	}

	/**
	 * @param player4_score the player4_score to set
	 */
	public void setPlayer4_score(int player4_score) {
		this.player4_score = player4_score;
	}

	/**
	 * @return the player1_position
	 */
	public int getPlayer1_position() {
		return player1_position;
	}

	/**
	 * @param player1_position the player1_position to set
	 */
	public void setPlayer1_position(int player1_position) {
		this.player1_position = player1_position;
	}

	/**
	 * @return the player2_position
	 */
	public int getPlayer2_position() {
		return player2_position;
	}

	/**
	 * @param player2_position the player2_position to set
	 */
	public void setPlayer2_position(int player2_position) {
		this.player2_position = player2_position;
	}

	/**
	 * @return the player3_position
	 */
	public int getPlayer3_position() {
		return player3_position;
	}

	/**
	 * @param player3_position the player3_position to set
	 */
	public void setPlayer3_position(int player3_position) {
		this.player3_position = player3_position;
	}

	/**
	 * @return the player4_position
	 */
	public int getPlayer4_position() {
		return player4_position;
	}

	/**
	 * @param player4_position the player4_position to set
	 */
	public void setPlayer4_position(int player4_position) {
		this.player4_position = player4_position;
	}

	/**
	 * @return the score1
	 */
	public int getScore1() {
		return score1;
	}

	/**
	 * @param score1 the score1 to set
	 */
	public void setScore1(int score1) {
		this.score1 = score1;
	}

	/**
	 * @return the score2
	 */
	public int getScore2() {
		return score2;
	}

	/**
	 * @param score2 the score2 to set
	 */
	public void setScore2(int score2) {
		this.score2 = score2;
	}

	/**
	 * @return the score3
	 */
	public int getScore3() {
		return score3;
	}

	/**
	 * @param score3 the score3 to set
	 */
	public void setScore3(int score3) {
		this.score3 = score3;
	}

	/**
	 * @return the score4
	 */
	public int getScore4() {
		return score4;
	}

	/**
	 * @param score4 the score4 to set
	 */
	public void setScore4(int score4) {
		this.score4 = score4;
	}

	/**
	 * @return the ip1
	 */
	public String getIp1() {
		return ip1;
	}

	/**
	 * @param ip1 the ip1 to set
	 */
	public void setIp1(String ip1) {
		this.ip1 = ip1;
	}

	/**
	 * @return the ip2
	 */
	public String getIp2() {
		return ip2;
	}

	/**
	 * @param ip2 the ip2 to set
	 */
	public void setIp2(String ip2) {
		this.ip2 = ip2;
	}

	/**
	 * @return the ip3
	 */
	public String getIp3() {
		return ip3;
	}

	/**
	 * @param ip3 the ip3 to set
	 */
	public void setIp3(String ip3) {
		this.ip3 = ip3;
	}

	/**
	 * @return the ip4
	 */
	public String getIp4() {
		return ip4;
	}

	/**
	 * @param ip4 the ip4 to set
	 */
	public void setIp4(String ip4) {
		this.ip4 = ip4;
	}

	/**
	 * @return the mac1
	 */
	public String getMac1() {
		return mac1;
	}

	/**
	 * @param mac1 the mac1 to set
	 */
	public void setMac1(String mac1) {
		this.mac1 = mac1;
	}

	/**
	 * @return the mac2
	 */
	public String getMac2() {
		return mac2;
	}

	/**
	 * @param mac2 the mac2 to set
	 */
	public void setMac2(String mac2) {
		this.mac2 = mac2;
	}

	/**
	 * @return the mac3
	 */
	public String getMac3() {
		return mac3;
	}

	/**
	 * @param mac3 the mac3 to set
	 */
	public void setMac3(String mac3) {
		this.mac3 = mac3;
	}

	/**
	 * @return the mac4
	 */
	public String getMac4() {
		return mac4;
	}

	/**
	 * @param mac4 the mac4 to set
	 */
	public void setMac4(String mac4) {
		this.mac4 = mac4;
	}
}
