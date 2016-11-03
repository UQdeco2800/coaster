package uq.deco2800.coaster.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.graphics.Viewport;

/**
 * An AABB is an axis-aligned bounding box
 * Which is a fancy term for hitbox that doesn't rotate
 * 
 * Okay the way we do this is pretty weird bc I didn't want massive changes to every entity
 * <p>
 * Every entity has a field called bounds. This defines their 'main' hitbox. Their sprite is drawn to fill this box
 * <p>
 * Any additional hitboxes should be designated a child using makeChild(true); <br>
 * This means that their position will be updated to reflect their parent hitbox <br>
 * Note that this is defined by an offset and we do not know which hitbox is the actual parent.
 *
 */
public class AABB {
	private static final float EPSILON = 0.000001f;
	//Note: posX and posY are stored as center points, NOT top left
	//Width and height are stored as half the actual value as well, just to make calculating collision a little easier
	private float offsetX; // offset from the parent AABB, should be 0 for parents
	private float offsetY; // offset from the parent AABB, should be 0 for parents
	private float posX; // in-game position in tiles
	private float posY; // in-game position in tiles
	private float halfWidth;
	private float halfHeight;
	
	private float startLeft; // saves the left edge position at the start of the tick
	private float startTop; // saves the top edge position at the start of the tick
	
	private boolean activeInCollisions; // saved whether or not this AABB is actually used for collisions. 
										// setting this to false can make bounds a sprite-only container
	private boolean isChild; // whether or not this is a parent or child
	private BodyPart bodyPart; // stores which part of the entity this corresponds to e.g. head, shield
	
	private Color renderColour; // Helps to distinguish AABBs when rendering hitboxes for debugging
	
	/** 
	 * Constructor for AABBs with offset
	 * Are set as parents by default
	 * 
	 * @param posX
	 * @param posY
	 * @param width
	 * @param height
	 * @param offsetX
	 * @param offsetY
	 */
	public AABB(float posX, float posY, float width, float height, float offsetX, float offsetY) {
		this.activeInCollisions = true;
		this.isChild = false;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.posX = posX + halfWidth + offsetX;
		this.posY = posY + halfHeight + offsetY;
		
		this.bodyPart = BodyPart.MAIN;
		
		if (Math.abs(offsetX) > EPSILON || Math.abs(offsetY) > EPSILON) {
			this.renderColour = new Color(1, 0, 1, 0.5);
		} else if (Math.abs(width - 1f) < EPSILON && Math.abs(height - 1f) < EPSILON) {
			this.renderColour = new Color(0, 1, 0, 0.2);
		} else if (height > 1f) {
			this.renderColour = new Color(Math.random(), Math.random(), Math.random(), 0.35);
		} else {
			this.renderColour = new Color(1, 0, 0, 0.2);
		}
	}

	/**
	 *  default constructor for bounds/parent AABBs with no offset
	 *  
	 * @param posX 		left edge location in tiles
	 * @param posY		top edge location in tiles
	 * @param width		width in tiles
	 * @param height	height in tiles
	 */
	public AABB(float posX, float posY, float width, float height) {
		this(posX, posY, width, height, 0, 0);
	}

	/**
	 * Sets whether or not this hitbox collides with anything
	 * @param value if true, this hitbox can collide. If false, this hitbox will always return false to collides()
	 */
	public void setActive(boolean value) {
		this.activeInCollisions = value;
	}
	
	/**
	 * Used to set whether or not this is a child hitbox
	 * @param value 		true to make this a child, false to make a parent
	 */
	public void makeChild(boolean value) {
		this.isChild = value;
	}
	
	/**
	 * Detects a collision with a single hitbox
	 * @param other 	another hitbox
	 * @return			true if this hitbox collides (over laps) with the other hitbox
	 */
	public boolean collides(AABB other) {
		if (!activeInCollisions) {
			return false;
		}
		return (Math.abs(posX - other.posX()) + EPSILON < halfWidth + other.halfWidth) &&
				(Math.abs(posY - other.posY()) + EPSILON < halfHeight + other.halfHeight);
	}
	
	/**
	 * Checks for collisions between this hitbox and all hitboxes associated with the input entity
	 * @param other		another entity
	 * @return 			VOID if there is no collision, otherwise the body part with which the collision occurred
	 */
	public BodyPart collides(Entity other) {
		BodyPart location = BodyPart.VOID;
		if (this.collides(other.getBounds())) {
			location = other.getBounds().getBodyPart();
		}
		if (other.hitboxes != null) {
			for (AABB hitbox : other.hitboxes) {
				if (this.collides(hitbox) && hitbox.getBodyPart().compareTo(location) > 0) {
					location = hitbox.getBodyPart();
				}
			}
		}
		return location;
	}
	
	/**
	 * Sets this hitbox's position relative to the input hitbox
	 * <p>
	 * Depending on whether this is a parent or child, the location is set to maintain the desired offset
	 * So if this is a child that has positive X offset, this is moved to be at the right of the input parent
	 * If this is a parent, and the child has positive X offset, this is moved to be at the left of the child.
	 * i.e. the offset is maintained with the child stuck in place.
	 * 
	 * Offsets are flipped with facing to maintain hitbox -> body part mapping.
	 * 
	 * @require that the is a parent/child relationship between this and the input hitbox.
	 * @require facing == -1 || facing == 1
	 * 
	 * @param otherBounds
	 * @param facing
	 */
	public void setPos(AABB otherBounds, int facing) {
		if (isChild) {
			if (facing == 1) {
				posX = otherBounds.ownerLeft() + halfWidth + offsetX;
			} else if (facing == -1) {
				posX = otherBounds.right() - halfWidth - offsetX;
			}
			posY = otherBounds.top() + halfHeight + offsetY;
		} else {
			if (facing == 1) {
				setX(otherBounds.ownerLeft());
			} else if (facing == -1) {
				setRightX(otherBounds.posX() + otherBounds.halfWidth + otherBounds.offsetX);
			}
			setY(otherBounds.ownerTop());
		}
	}

	
	/**
	 * Sets this hitbox's position to the input coordinates
	 * 
	 * @param x			new left edge
	 * @param y			new top edge
	 */
	public void setPos(float x, float y) {
		posX = x + halfWidth;
		posY = y + halfHeight;
	}
	
	/**
	 * Sets this hitbox's left edge to the input value
	 * @param x			new left edge
	 */
	public void setX(float x) {
		posX = x + halfWidth;
	}
	
	/**
	 * Sets this hitbox's right edge to the input value
	 * @param x			new right edge
	 */
	public void setRightX(float x) {
		posX = x - halfWidth;
	}

	/**
	 * Sets this hitbox's top edge to the input value
	 * @param y			new top edge
	 */
	public void setY(float y) {
		posY = y + halfHeight;
	}
	
	/**
	 * Sets this hitbox's bottom edge to the input value
	 * @param y			new bottom edge
	 */
	public void setBottomY(float y) {
		posY = y - halfHeight;
	}

	/** 
	 * Gets the whole width
	 * @return the hitbox's width
	 */
	public float getWidth() {
		return halfWidth * 2;
	}

	/** 
	 * Gets the whole height
	 * @return the hitbox's height
	 */
	public float getHeight() {
		return halfHeight * 2;
	}
	
	/** 
	 * Gets the half width
	 * @return the hitbox's halfWidth
	 */
	public float getHalfWidth() {
		return halfWidth;
	}

	/** 
	 * Gets the half width
	 * @return the hitbox's halfWidth
	 */
	public float getHalfHeight() {
		return halfHeight;
	}

	/** 
	 * Gets the left edge of the parent hitbox (i.e. accounts for the offset)
	 * @return the left edge of the parent hitbox
	 */
	public float ownerLeft() {
		return posX - halfWidth - offsetX;
	}

	/** 
	 * Gets the top edge of the parent hitbox (i.e. accounts for the offset)
	 * @return the top edge of the parent hitbox
	 */
	public float ownerTop() {
		return posY - halfHeight - offsetY;
	}

	/** 
	 * Gets the right edge of the parent hitbox (i.e. accounts for the offset)
	 * @return the right edge of the parent hitbox
	 */
	public float ownerRight() {
		return posX + halfWidth - offsetX;
	}

	/** 
	 * Gets the bottom edge of the parent hitbox (i.e. accounts for the offset)
	 * @return the bottom edge of the parent hitbox
	 */
	public float ownerBottom() {
		return posY + halfHeight - offsetY;
	}
	
	/** 
	 * Gets the left edge of this hitbox
	 * @return the hitbox's left edge
	 */
	public float left() {
		return posX - halfWidth;
	}

	/** 
	 * Gets the top edge of this hitbox
	 * @return the hitbox's top edge
	 */
	public float top() {
		return posY - halfHeight;
	}

	/** 
	 * Gets the right edge of this hitbox
	 * @return the hitbox's right edge
	 */
	public float right() {
		return posX + halfWidth;
	}

	/** 
	 * Gets the bottom edge of this hitbox
	 * @return the hitbox's bottom edge
	 */
	public float bottom() {
		return posY + halfHeight;
	}


	/** 
	 * Gets the centre x-coordinate
	 * @return the hitbox's centre-x
	 */
	public float posX() {
		return posX;
	}

	/** 
	 * Gets the centre y-coordinate
	 * @return the hitbox's centre-y
	 */
	public float posY() {
		return posY;
	}
	
	/** 
	 * Stores the current coordinates to be retrieved later
	 */
	public void saveCoords() {
		this.startLeft = left();
		this.startTop = top();
	}
	
	/** 
	 * Gets the saved left edge coordinate
	 * @return the saved left edge coordinate
	 */
	public float startLeft() {
		return startLeft;
	}
	
	/** 
	 * Gets the saved right edge coordinate
	 * @return the saved right edge coordinate
	 */
	public float startRight() {
		return startLeft + 2 * halfWidth;
	}
	
	/** 
	 * Gets the saved top edge coordinate
	 * @return the saved top edge coordinate
	 */
	public float startTop() {
		return startTop;
	}
	
	/** 
	 * Gets the saved bottom edge coordinate
	 * @return the saved bottom edge coordinate
	 */
	public float startBottom() {
		return startTop + 2 * halfHeight;
	}

	/** 
	 * Gets the BodyPart assigned to this hitbox
	 * @return the assigned BodyPart
	 */
	public BodyPart getBodyPart() {
		return bodyPart;
	}
	
	/** 
	 * Assigns a BodyPart to this hitbox
	 * @param the assigned BodyPart
	 */
	public void setBodyPart(BodyPart bodyPart) {
		this.bodyPart = bodyPart;
	}
	

	/**
	 * Sets the values of the AABB to the input values To be used for collision detection (i.e. in Entity.updatePhysics)
	 * NOT state changes (e.g. moving -> sliding)
	 */
	public void setAABB(float posX, float posY, float width, float height) {
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.posX = posX + halfWidth;
		this.posY = posY + halfHeight;
	}
	
	
	/**
	 * I put this here to allow custom colours for debugging
	 * 
	 * Fills in a rectangle with the specified colour
	 * 
	 * @param gc			the graphics context to draw on
	 * @param viewport		the viewport to draw on
	 */
	public void render(GraphicsContext gc,  Viewport viewport) {
		float tileSize = viewport.getTileSideLength();
		int leftBorder = viewport.getLeftBorder();
		int topBorder = viewport.getTopBorder();

		int left = (int) Math.floor(viewport.getLeft());
		int top = (int) Math.floor(viewport.getTop());

		float subTileShiftX = (viewport.getLeft() - left) * tileSize;
		float subTileShiftY = (viewport.getTop() - top) * tileSize;

		float x = (left() - left) * tileSize + leftBorder - subTileShiftX;
		float y = (top() - top) * tileSize + topBorder - subTileShiftY;

		gc.setFill(this.renderColour);
		gc.fillRect(x, y, getWidth() * tileSize, getHeight() * tileSize);
	}
	
	/**
	 * returns the coordinates of the four edged of this hitbox, to be used in debugging
	 */
	@Override
	public String toString() {
		String stringRep = "";
		stringRep += "L: " + String.format("%.2f", ownerLeft()) + "   R: " + String.format("%.2f", ownerRight());
		stringRep += "   U: " + String.format("%.2f", ownerTop()) + "   D: " + String.format("%.2f", ownerBottom());
		return stringRep;
	}

}
