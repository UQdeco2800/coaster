package uq.deco2800.coaster.game.tiles;

public class TileSolid extends TileInfo {
	public TileSolid(Tiles type) {
		super(type);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public boolean isLiquid() {
		return false;
	}
}
