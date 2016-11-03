package uq.deco2800.coaster.game.preservation;

import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.items.ItemRegistry;


/**
 * Created by ryanj on 18/09/2016.
 */
public class ExportableItem extends ExportableEntity {
	public long removeDelay;
	public String itemReference;

	public ExportableItem() {}

	public ExportableItem(ItemEntity iteme) {
		super(iteme);
		removeDelay = iteme.getRemoveDelay();
		itemReference = ItemRegistry.reverseLookup(iteme.getItem());
	}
}
