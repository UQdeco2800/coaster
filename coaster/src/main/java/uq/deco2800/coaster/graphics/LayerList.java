package uq.deco2800.coaster.graphics;

/**
 * An enum listing all the layers on which entities can be rendered.
 * NOTE: The order of this enum matters, as the first layer here will be
 * rendered behind the second layer, and so on.
 * NOTE: This just applies to entities
 * @author WilliamHayward
 */
public enum LayerList {
	DEFAULT,
	DECORATIONS,
	ITEMS,
	NPCS,
	PARTICLES,
	PLAYERS
}
