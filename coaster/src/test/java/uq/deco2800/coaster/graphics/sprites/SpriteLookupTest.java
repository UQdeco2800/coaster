package uq.deco2800.coaster.graphics.sprites;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import uq.deco2800.coaster.TestHelper;

import static org.junit.Assert.assertEquals;

/**
 * Created by rcarrier on 8/09/2016.
 */
public class SpriteLookupTest {
	public static final Logger logger = LoggerFactory.getLogger(SpriteLookupTest.class);

	@Test
	public void lookupSprite() {
		TestHelper.load();
		Set<SpriteList> keys = SpriteCache.defaultSpriteKeys();
		for (SpriteList i : keys) {
			assert i == SpriteCache.lookupSprite(SpriteCache.getSpriteInfo(i));
		}

		SpriteInfo tempSprite = SpriteCache.getSpriteInfo((SpriteList) keys.toArray()[0]);
		SpriteInfo testSprite = new SpriteInfo(tempSprite.frameWidth, tempSprite.frameHeight, tempSprite.numFrames,
				tempSprite.frameDuration + 1, tempSprite.frames);
		assert SpriteCache.lookupSprite(testSprite) == SpriteList.NULL;
	}

	@Test
	public void spriteLoadExceptionTest() {
		TestHelper.load();

		boolean caught = false;
		try {
			SpriteCache.getSpriteInfo(SpriteList.NULL);
		} catch (SpriteLoadException e) {
			caught = true;
		}
		assertEquals(true, caught);
	}

}
