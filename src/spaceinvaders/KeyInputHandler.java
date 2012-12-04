package spaceinvaders;

import java.awt.event.KeyEvent;

public class KeyInputHandler extends java.awt.event.KeyAdapter {

	/**
	 * @param args
	 */
	
	private boolean pause = true;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean firePressed = false;
	
	public void keyPressed(KeyEvent e) {
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
	
	public void keyReleased(KeyEvent e) {
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

	public void keyTyped(KeyEvent e) {
		pause = false;
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
	
	public boolean gamePaused(){
		return pause;
	}
	
	public void pauseGame(){
		pause = true;
	}
	
}
	


