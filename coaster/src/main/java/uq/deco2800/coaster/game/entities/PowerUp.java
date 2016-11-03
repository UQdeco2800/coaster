package uq.deco2800.coaster.game.entities;

import java.util.List;

import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.mechanics.BodyPart;

public class PowerUp extends ItemEntity {
	private float modifier;
	private long modifierValue;
	private long itemDecay;
	private String buffType;

	public PowerUp(Item item, String buff, float modifier, long modifierValue, long decay) {
		super(item, 1.5f, 1.5f);
		this.modifier = modifier;
		this.modifierValue = modifierValue;
		this.itemDecay = decay;
		this.buffType = buff;
		this.setVelocity(0, -20);
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		entities.forEach(entity -> {
			if (entity instanceof Player || entity instanceof CompanionNPC) {
				PlayerBuff newBuff = new PlayerBuff(this.buffType, this.modifier, this.modifierValue);
				if (entity instanceof Player) {
					((Player) entity).addPlayerBuff(newBuff);
					this.delete();
				} else {
					((CompanionNPC) entity).getOwner().addPlayerBuff(newBuff);
					this.delete();
				}

			}
		});

	}

	@Override
	protected void tick(long ms) {
		this.itemDecay -= ms;
		if (this.itemDecay <= 0) {
			this.delete();
		}
	}

	public float getModifier() {
		return this.modifier;
	}

	public long getModifierValue() {
		return this.modifierValue;
	}
}
