package uq.deco2800.coaster.game.tiles;

public class TileLiquid extends TileInfo {
	public TileLiquid(Tiles type) {
		super(type);
	}

	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public boolean isLiquid() {
		return true;
	}
}
