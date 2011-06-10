package com.creepsmash.common.messages.server;

import com.creepsmash.common.messages.Message;

/**
 * Messages from server to client.
 */
public abstract class ServerMessage extends Message {
	public static ServerMessage renderMessageString(String messageString) {
		ServerMessage message = null;
		if (BuildTowerRoundMessage.PATTERN.matcher(messageString).matches()) {
			message = new BuildTowerRoundMessage();
		} else if (BuildCreepRoundMessage.PATTERN.matcher(messageString).matches()) {
			message = new BuildCreepRoundMessage();
		} else if (CreateGameResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new CreateGameResponseMessage();
		} else if (ErrorMessage.PATTERN.matcher(messageString).matches()) {
			message = new ErrorMessage();
		} else if (GamesMessage.PATTERN.matcher(messageString).matches()) {
			message = new GamesMessage();
		} else if (HighscoreResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new HighscoreResponseMessage();
		} else if (JoinGameResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new JoinGameResponseMessage();
		} else if (LoginResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new LoginResponseMessage();
		} else if (MessageMessage.PATTERN.matcher(messageString).matches()) {
			message = new MessageMessage();
		} else if (PlayerJoinedMessage.PATTERN.matcher(messageString).matches()) {
			message = new PlayerJoinedMessage();
		} else if (PlayerQuitMessage.PATTERN.matcher(messageString).matches()) {
			message = new PlayerQuitMessage();
		} else if (PlayersMessage.PATTERN.matcher(messageString).matches()) {
			message = new PlayersMessage();
		} else if (RegistrationResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new RegistrationResponseMessage();
		} else if (RoundMessage.PATTERN.matcher(messageString).matches()) {
			message = new RoundMessage();
		} else if (SellTowerRoundMessage.PATTERN.matcher(messageString).matches()) {
			message = new SellTowerRoundMessage();
		}else if (ChangeStrategyRoundMessage.PATTERN.matcher(messageString).matches()) {
			message = new ChangeStrategyRoundMessage();
		}else if (StartGameMessage.PATTERN.matcher(messageString).matches()) {
			message = new StartGameMessage();
		} else if (StartGameResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new StartGameResponseMessage();
		} else if (UpgradeTowerRoundMessage.PATTERN.matcher(messageString).matches()) {
			message = new UpgradeTowerRoundMessage();
		} else if (KickedMessage.PATTERN.matcher(messageString).matches()) {
			message = new KickedMessage();
		} else if (KickPlayerResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new KickPlayerResponseMessage();
		} else if (PingMessage.PATTERN.matcher(messageString).matches()) {
			message = new PingMessage();
		} else if (UpdateDataResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new UpdateDataResponseMessage();
		} else if (ScoreResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new ScoreResponseMessage();
		} else if (DeleteResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new DeleteResponseMessage();
		} else if (PasswordResetResponseMessage.PATTERN.matcher(messageString).matches()) {
			message = new PasswordResetResponseMessage();
		} else {
			System.err.println("Invalid message:\"" + messageString + "\"");
			return message;
		}
		message.initWithMessage(messageString);
		return message;
	}
}
