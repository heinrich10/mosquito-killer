package spaceinvaders;

import java.awt.event.KeyEvent;

public class KeyInputHandler extends java.awt.event.KeyAdapter {

	/**
	 * @param args
	 */
	
	private boolean keyPressed = false;
	
	/**
	 * Notification from AWT that a key has been pressed. Note that
	 * a key being pressed is equal to being pushed down but *NOT*
	 * released. Thats where keyTyped() comes in.
	 *
	 * @param e The details of the key that was pressed 
	 */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	
	public void keyPressed(KeyEvent e) {
		// if we're waiting for an "any key" typed then we don't 
		// want to do anything with just a "press"
		//if (waitingForKeyPress) {
		//	return;
		//}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			firePressed = true;
		}
	} 
	
	/**
	 * Notification from AWT that a key has been released.
	 *
	 * @param e The details of the key that was released 
	 */
	public void keyReleased(KeyEvent e) {
		// if we're waiting for an "any key" typed then we don't 
		// want to do anything with just a "released"
		//if (waitingForKeyPress) {
		//	return;
		//}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			firePressed = false;
		}
	}

	/**
	 * Notification from AWT that a key has been typed. Note that
	 * typing a key means to both press and then release it.
	 *
	 * @param e The details of the key that was typed. 
	 */
	public void keyTyped(KeyEvent e) {
		// if we're waiting for a "any key" type then
		// check if we've recieved any recently. We may
		// have had a keyType() event from the user releasing
		// the shoot or move keys, hence the use of the "pressCount"
		// counter.
		//if (waitingForKeyPress) {
			keyPressed = true;
		// if we hit escape, then quit the game
		if (e.getKeyChar() == 27) {
			System.exit(0);
		}
	}
	
	public boolean leftPressed(){
		return leftPressed;
	}
	
	public boolean rightPressed(){
		return rightPressed;
	}
	
	public boolean firePressed(){
		return firePressed;
	}
	
	public boolean keyPressed(){
		return keyPressed;
	}
	
	public void resetKey(){
		keyPressed = false;
	}
	
}
	


