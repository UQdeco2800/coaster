package uq.deco2800.coaster.game.entities.lighting;


import uq.deco2800.coaster.game.tiles.Tile;

public class TileLight {
	private Tile tile;
	private int light;

	public TileLight(Tile tile, int light) {
		this.tile = tile;
		this.light = light;
	}

	public Tile getTile() { return this.tile; }

	public int getLight() { return this.light; }
}

