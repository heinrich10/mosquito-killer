package spaceinvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.text.normalizer.RangeValueIterator;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic. 
 * 
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 * 
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alient killed, played died) and will take
 * appropriate game actions.
 * 
 * @author Kevin Glass
 */
public class Game {
	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy strategy;
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	/** The list of all the entities that exist in our game */
	private ArrayList entities = new ArrayList();
	/** The list of entities that need to be removed from the game this loop */
	private ArrayList removeList = new ArrayList();
	/** The entity representing the player */
	private ShipEntity ship;
	/** The speed at which the player's ship should move (pixels/sec) */
	private double moveSpeed = 300;
	/** The time at which last fired a shot */
	private long lastFire = 0;
	/** The interval between our players shot (ms) */
	private long firingInterval = 500;
	/** The number of aliens left on the screen */
	private int alienCount = 0;
	
	private int playerLife = 20;
	
	private int mosquitoKills = 0;
	
	/** The message to display which waiting for a key press */
	
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;
	/** The last time at which we recorded the frame rate */
	private long lastFpsTime;
	/** The current number of frames recorded */
	private int fps;
	/** The normal title of the game window */
	private String windowTitle = "Space Invaders 102";
	/** The game window that we'll update with the frame count */
	//private JFrame container;
	
	//test
	private int x = 0;
    private int y = 0;
    
    KeyInputHandler kih = null;
    GameCanvas gc = null;
	/**
	 * Construct our game and set it running.
	 */
	public Game() {
		//create KeyInputHandler and pass it to GameCanvas
    	kih = new KeyInputHandler();
    	gc = new GameCanvas(kih);
		
		// initialise the entities in our game so there's something
		// to see at startup
		initEntities();
	}
	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startGame() {
		// clear out any existing entities and intialise a new set
		entities.clear();
		initEntities();
	}
	
	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entitiy will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		// create the player ship and place it roughly in the center of the screen
		entities.clear();
		ship = new ShipEntity(this,"sprites/rockman2.png",370,550);
		entities.add(ship);
		alienCount = 0;
		
	}
	
	/**
	 * Notification from a game entity that the logic of the game
	 * should be run at the next opportunity (normally as a result of some
	 * game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
	
	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		gc.setMessage("Oh no! They got you, try again?");
		kih.pauseGame();
	}
	
	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		gc.setMessage("Well done! You Win!");
		kih.pauseGame();
	}
	
	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!
		alienCount--;
		mosquitoKills++;
		if (alienCount == 0) {
			notifyWin();
		}
		
		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		//for (int i=0;i<entities.size();i++) {
		//	Entity entity = (Entity) entities.get(i);
			
		//	if (entity instanceof AlienEntity) {
				// speed up by 2%
		//		entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
		//	}
		//}
	}
	
	public void mosquitoBreach(Entity entity){
		alienCount--;
		playerLife--;
		
		removeEntity(entity);
		if(playerLife == 0){
			this.notifyDeath();
		}
	}
	
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this 
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this,"sprites/shot.gif",ship.getX()+40,ship.getY()-30);
		ship.setFire();
		
		entities.add(shot);
	}
	
	/**
	 * The main game loop. This loop is running during all game
	 * play as is responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves
	 * - Moving the game entities
	 * - Drawing the screen contents (entities, text)
	 * - Updating game events
	 * - Checking Input
	 * <p>
	 */
	public void gameLoop() {
		long lastLoopTime = SystemTimer.getTime();
	
		// keep looping round til the game ends
		while (gameRunning) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			//temp declare
			Entity alien = null;
			// update our FPS counter if a second has passed since
			// we last recorded
			
			// Get hold of a graphics context for the accelerated 
			// surface and blank it out
			//make into function
			
			//Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			//g.setColor(Color.black);
			//g.fillRect(0,0,800,800);
			
			//waitingForKeyPress = kih.keyPressed();
			
			gc.updateGraphics();
			
			// cycle round asking each entity to move itself
			if (!kih.gamePaused()) {
				
				long delta = SystemTimer.getTime() - lastLoopTime;
				lastLoopTime = SystemTimer.getTime();

				// update the frame counter
				lastFpsTime += delta;
				fps++;
				
				if (lastFpsTime >= 1000) {
					String title = windowTitle+" (FPS: "+fps+");   Life: " + playerLife;
					gc.setTitle(title);
					lastFpsTime = 0;
					fps = 0;
					
					//if(mosquitoKills == 2){
					//	kih.pauseGame();
					//}
						//test alien
						Random num = new Random();
						int x, y; 
						
						if(num.nextBoolean()){
							int minHospitalX = 60;
							int maxHospitalX = 193;
							int minHospitalY = 180;
							int maxHospitalY = 250;
							x = (int) ((int) minHospitalX + num.nextFloat() * (maxHospitalX - minHospitalX));
							y = (int) ((int) minHospitalY + num.nextFloat() * (maxHospitalY - minHospitalY));
						}else{
							int minLakeX = 500;
							int maxLakeX = 653;
							int minLakeY = 100;
							int maxLakeY = 180;
							x = (int) ((int) minLakeX + num.nextFloat() * (maxLakeX - minLakeX));
							y = (int) ((int) minLakeY + num.nextFloat() * (maxLakeY - minLakeY));
						}
		
						if(alienCount < 20){
							alien = new AlienEntity(this, x, y);
							entities.add(alien);
							alienCount++;
							//System.out.println("alien count: " + alienCount);
						}
						
					}
					
					for (int i=0;i<entities.size();i++) {
						Entity entity = (Entity) entities.get(i);
						
						entity.move(delta);
						
						
						
					}
					
					for (int i=0;i<entities.size();i++) {
						Entity entity = (Entity) entities.get(i);
						gc.draw(entity);
						//entity.draw(g);
					}
					
					for (int p=0;p<entities.size();p++) {
						for (int s=p+1;s<entities.size();s++) {
							Entity me = (Entity) entities.get(p);
							Entity him = (Entity) entities.get(s);
							
							if (me.collidesWith(him)) {
								me.collidedWith(him);
								him.collidedWith(me);
							}
						}
					}
					
					entities.removeAll(removeList);
					removeList.clear();

					// if a game event has indicated that game logic should
					// be resolved, cycle round every entity requesting that
					// their personal logic should be considered.
					if (logicRequiredThisLoop) {
						for (int i=0;i<entities.size();i++) {
							Entity entity = (Entity) entities.get(i);
							entity.doLogic();
						}
						
						logicRequiredThisLoop = false;
					}
					
					for (int i=0;i<entities.size();i++) {
						Entity entity = (Entity) entities.get(i);
						
						if (entity instanceof AlienEntity) {
							entity.setVerticalMovement(50);
							entity.setHorizontalMovement(0);
							//entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);

								AlienEntity newEntity = (AlienEntity) entity;
								
							
						}
					}
					
					ship.setHorizontalMovement(0);
					
					
					//get controls from KeyInputHandler
					movementControls();
					
					
					// we want each frame to take 10 milliseconds, to do this
					// we've recorded when we started the frame. We add 10 milliseconds
					// to this and then factor in the current time to give 
					// us our final value to wait for
					SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
					
				
					
					
				
			} else {
				gc.waitForKeyPress();
				//mosquitoKills++;
				
			}
			
			// cycle round drawing all the entities we have in the game
			
			
			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify 
			// both entities that the collision has occured
			
			
			// remove any entity that has been marked for clear up
			
			
			// if we're waiting for an "any key" press then draw the 
			// current message 
			
			
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			gc.destroy();
			//g.dispose();
			gc.showx();
			//strategy.show();
			
			// resolve the movement of the ship. First assume the ship 
			// isn't moving. If either cursor key is pressed then
			// update the movement appropraitely
			
			
			
			
			
			
			
		}
	}
	
	private void reinit() {
		// TODO Auto-generated method stub
		entities.clear();
		ship = new ShipEntity(this,"sprites/ship.gif",370,550);
		entities.add(ship);
		
	}

	private void movementControls() {
		// TODO Auto-generated method stub
		if ((kih.leftPressed()) && (!kih.rightPressed())) {
			ship.setHorizontalMovement(-moveSpeed);
			

			
		} else if ((kih.rightPressed()) && (!kih.leftPressed())) {
			ship.setHorizontalMovement(moveSpeed);
			
		}
		
		// if we're pressing fire, attempt to fire
		if (kih.firePressed()) {
			tryToFire();
			
		}else{
			ship.setStand();
		}
	}

	/**
	 * A class to handle keyboard input from the user. The class
	 * handles both dynamic input during game play, i.e. left/right 
	 * and shoot, and more static type input (i.e. press any key to
	 * continue)
	 * 
	 * This has been implemented as an inner class more through 
	 * habbit then anything else. Its perfectly normal to implement
	 * this as seperate class if slight less convienient.
	 * 
	 * @author Kevin Glass
	 */
	public static void main(String[] args){
		Game g = new Game();
		
		g.gameLoop();
	}
}
