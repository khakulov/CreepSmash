package com.creepsmash.client.game.creeps;

import java.awt.Graphics2D;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.utils.SoundManager;
import com.creepsmash.common.CreepType;


/**
 * Implementation for a creep.
 */
public class CreepImpl implements Creep {
	private int senderId;

	private double speed;
	private int health;
	private long round;

	private double x;
	private double y;
	private double angle;

	private CreepType type;
	private int totalSegmentSteps; // the total segment steps moved


	private Context context;
	private int slowTime;

	private int segment; // the current segment
	private int segmentStep; // the step within the segment (1000 steps per segment)

	private CreepRenderer renderer;

	private boolean active;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param context
	 *            the game context
	 * @param type
	 *            the type of the creep
	 */
	protected CreepImpl(Context context, CreepType type) {
		this.context = context;
		this.type = type;
		this.active = false;
		this.segment = 0;
		this.segmentStep = 0;
		this.totalSegmentSteps = 0;

		// set the position at this point to avoid that towers
		// recognize the creep with the wrong position
		// at the first appearance of the creep.
		double[] pos = context.getGameBoard().getPath().getStep(this.segment, this.segmentStep);
		this.x = pos[0];
		this.y = pos[1];
		this.health = type.getHealth();
		this.speed = type.getSpeed();
		this.renderer = new CreepRenderer(this);
	}

	@Override
	public void update(long round) {
		if (this.round > round)
			return;
		if (this.round == round)
			this.active = true;

		if (this.slowTime > 0) {
			this.slowTime--;
			if (this.slowTime == 0) {
				this.speed = this.type.getSpeed();
			}
		}

		if (this.getHealth() < this.type.getHealth()) {
			int health = getHealth() + this.type.getRegeneration();
			if (health < this.type.getHealth()) 
				this.setHealth(health);
			else
				this.setHealth(this.type.getHealth());
		}

		this.totalSegmentSteps += this.speed;

		// change the segmentStep
		this.segmentStep += this.speed;
		if (this.segmentStep > 1000) {
			// if the segmentStep exceeds the max steps
			// per segment we need to correct
			this.segmentStep -= 1000;
			// and go to the next segment
			this.segment++;
			if (this.segment >= this.context.getGameBoard().getPath().size() - 1) {
				// we have run all along the path for simplicity reset the segment
				// later we need to remove a life from the player
				this.segment = 0;
				// add the creep to the transfer collection
				// this moves the creep to the next player
				this.context.getTransfer().add(this);
				this.context.removeLive();
				Core.logger.info("Remove life from " + this.context.getPlayerName()
						+ " by: " + this.senderId + " with "
						+ this.getType().getName() + " (" + this.context.getLives()
						+ " Lifes now)");

				// play transfer music
				SoundManager.creepGoesToNextPlayerSound(this.type);
				return;
			}
		}

		double[] ret = this.context.getGameBoard().getPath().getStep(this.segment, this.segmentStep);
		this.x = ret[0];
		this.y = ret[1];
		this.angle = ret[2];
	}

	@Override
	public void paint(Graphics2D graphics) {
		this.renderer.paint(graphics);
	}

	@Override
	public int getSenderId() {
		return senderId;
	}

	@Override
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	@Override
	public double getSpeed() {
		return this.speed;
	}

	@Override
	public void slow(double speed, int slowTime) {
		this.speed = speed;
		this.slowTime = slowTime;
	}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public void setHealth(int h) {
		this.health = h;
	}

	@Override
	public void setRound(long round) {
		this.round = round;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public double getAngle() {
		return angle;
	}

	@Override
	public CreepType getType() {
		return this.type;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public int getTotalSegmentSteps() {
		return this.totalSegmentSteps;
	}
}
