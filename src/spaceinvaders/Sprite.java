package spaceinvaders;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * A sprite to be displayed on the screen. Note that a sprite
 * contains no state information, i.e. its just the image and 
 * not the location. This allows us to use a single sprite in
 * lots of different places without having to store multiple 
 * copies of the image.
 * 
 * @author Kevin Glass
 */
public class Sprite {
	/** The image to be drawn for this sprite */
	private Image image;
	
	/**
	 * Create a new sprite based on an image
	 * 
	 * @param image The image that is this sprite
	 */
	public Sprite(Image image) {
		this.image = image;
	}
	
	/**
	 * Get the width of the drawn sprite
	 * 
	 * @return The width in pixels of this sprite
	 */
	public int getWidth() {
		return image.getWidth(null);
	}

	/**
	 * Get the height of the drawn sprite
	 * 
	 * @return The height in pixels of this sprite
	 */
	public int getHeight() {
		return image.getHeight(null);
	}
	
	/**
	 * Draw the sprite onto the graphics context provided
	 * 
	 * @param g The graphics context on which to draw the sprite
	 * @param x The x location at which to draw the sprite
	 * @param y The y location at which to draw the sprite
	 */
	public void draw(Graphics g,int x,int y) {
		g.drawImage(image,x,y,null);
		
		//System.out.println(g.toString());
		//Graphics2D g2d=(Graphics2D)g;
		//g2d.rotate(0.0011);
		//System.out.println(x + "..." + y);
		//g2d.drawImage(image, x, y, null);
	}
}