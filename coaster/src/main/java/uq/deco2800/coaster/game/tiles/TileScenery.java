package uq.deco2800.coaster.game.tiles;

public class TileScenery extends TileInfo {
	public TileScenery(Tiles type) {
		super(type);
	}

	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public boolean isLiquid() {
		return false;
	}
}
