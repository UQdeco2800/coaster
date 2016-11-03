package uq.deco2800.coaster.game.tiles;

public class TileGas extends TileInfo {
	public TileGas(Tiles type) {
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
