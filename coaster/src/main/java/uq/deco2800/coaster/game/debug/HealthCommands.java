package uq.deco2800.coaster.game.debug;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.world.World;

public class HealthCommands extends Command{
	
	private Player player;
	
	private CommandType commandType;
	public enum CommandType {
		HEAL,
		HEALTH
	}
	
	public HealthCommands(CommandType command) {
		this.commandType = command;
		switch (command) {
		case HEAL:
			commandName = "heal";
			argumentCount = 1;
			help = "Fully heals the player";
			break;
		case HEALTH:
			commandName = "health";
			argumentCount = 1;
			help = "health x\n\tChanges the player's max health to x (positive int)";
		}
	}

	@Override
	String execute(Object[] arguments) {
		player = World.getInstance().getFirstPlayer();
		switch (this.commandType) {
		case HEAL:
			int healAmount = player.getMaxHealth() - player.getHealth();
			player.addHealth(healAmount);
			return "Healed: "+healAmount;
		case HEALTH:
			int newHealth;
			try {
				newHealth = Integer.parseInt((String) arguments[0]);
			} catch (NumberFormatException e) {
				return "Positive integer only.";
			}
			if (newHealth > 0) {
				player.setMaxHealth(newHealth);
				player.addHealth(player.getMaxHealth() - player.getHealth());
				return "Max Health Now: " + newHealth;
			} else {
				return "Positive integer only.";
			}
		}
		return null;
	}
	
	
}
