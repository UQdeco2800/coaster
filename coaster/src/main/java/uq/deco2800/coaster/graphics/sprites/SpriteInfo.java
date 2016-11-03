package uq.deco2800.coaster.graphics.sprites;

//An animated sprite. This class contains information about the sprite, as well as the frames itself. This is _not_
//an instance of a sprite -- it only contains the image data and the metadata of a sprite.
public class SpriteInfo {
	public int frameWidth;
	public int frameHeight;
	public int numFrames;
	//Although frameDuration is in SpriteInfo, it is also in Sprite -- the one in Sprite is simply loaded from here.
	//This is just the default.
	public int frameDuration;
	public FrameCollection frames;

	public SpriteInfo(int frameWidth, int frameHeight, int numFrames, int frameDuration, FrameCollection frames) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.numFrames = numFrames;
		this.frameDuration = frameDuration;
		this.frames = frames;
	}
}

