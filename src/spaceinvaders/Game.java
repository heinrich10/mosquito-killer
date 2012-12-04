package spaceinvaders;

import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

public class Game {
	
	private boolean gameRunning = true;
	private ArrayList entities = new ArrayList();
	private ArrayList removeList = new ArrayList();
	private PlayerEntity player;
	private double moveSpeed = 300;
	private long lastFire = 0;
	private long firingInterval = 500;
	private int mosquitoCount = 0;
	private int playerLife = 20;
	private int mosquitoKills = 0;
	private boolean newGame = true;
	private long lastFpsTime;
	private int fps;
	private String windowTitle = "Space Invaders 102";
	private KeyInputHandler kih = null;
    private GameCanvas gc = null;
    private Story popUp = null;
    private StoryHelper sh = null;
    
    private Sprite[] sp = new Sprite[100];
    
	public Game() {
		//create KeyInputHandler and pass it to GameCanvas
    	kih = new KeyInputHandler();
    	gc = new GameCanvas(kih);
    	popUp = new Story("");
    	sh = new StoryHelper();
    	
    	for(int i = 1; i < 100; i++){
    		sp[i] = SpriteStore.get().getSprite("sprites/rain.gif");
    	}
	}
	
	public void gameLoop() {
		long lastLoopTime = SystemTimer.getTime();
	
		while (gameRunning) {
			Entity alien = null;
			
			gc.updateGraphics();

			if (!kih.gamePaused()) {
				
				long delta = SystemTimer.getTime() - lastLoopTime;
				lastLoopTime = SystemTimer.getTime();

				lastFpsTime += delta;
				fps++;
				
				if (lastFpsTime >= 1000) {
					String title = windowTitle+" (FPS: "+fps+");   Life: " + playerLife;
					gc.setTitle(title);
					lastFpsTime = 0;
					fps = 0;
					DataStore ds = new DataStore();
					int limit;
					if(mosquitoKills <= 10){
						ds = stageOneSpawn(ds);
						limit = 20;
						
						if(mosquitoKills == 10){
							kih.pauseGame();
							popUp.setText(sh.stageTwo());
							popUp.setVisible(true);
							
						}
					} else if(mosquitoKills > 10 && mosquitoKills < 25){
						ds = stageOneSpawn(ds);
						limit = 10;
						
						//add rain
						Random num = new Random();
						for(int i = 1; i < 100 ; i++){
							
				    		gc.rainDrops(sp[i], num.nextInt(800), num.nextInt(800));
				    	}
						if(mosquitoKills == 25){
							kih.pauseGame();
							popUp.setText(sh.stageThree());
							popUp.setVisible(true);
						}
					} else {
						ds = stageThreeSpawn(ds);
						limit = 35;
					}
					System.out.println(mosquitoKills);
		
					if(mosquitoCount < limit){
						alien = new MosquitoEntity(this, ds.getX(), ds.getY());
						entities.add(alien);
						mosquitoCount++;
					}
						
				}
					
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					entity.move(delta);
				}
					
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					gc.draw(entity);
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

				player.setHorizontalMovement(0);
				
				//get controls from KeyInputHandler
				movementControls();
					
				SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
				
				} else {
					gc.waitForKeyPress();
					
					if(newGame){
						popUp.setText(sh.stageOne());
						popUp.setVisible(true);
					    startGame();
					}
					
					if(popUp.isOk()){
						mosquitoKills++;
						popUp.reinit();
					}
					lastLoopTime = SystemTimer.getTime();
				
			}
		
			gc.destroy();
			
			gc.showx();
		}
	}
	
	private void startGame() {
		entities.clear();
		initEntities();
		newGame = false;
	}
	
	private void initEntities() {
		entities.clear();
		player = new PlayerEntity(this,"sprites/rockman2.png",370,550);
		entities.add(player);
		mosquitoCount = 0;
		playerLife = 20;
		mosquitoKills = 0;
	}
	

	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
	
	public void notifyDeath() {
		gc.setMessage("Oh no! They got you, try again?");
		newGame = true;
		kih.pauseGame();
	}
	
	public void notifyWin() {
		gc.setMessage("Well done! You Win!");
		newGame = true;
		kih.pauseGame();
	}
	
	public void notifyMosquitoKilled() {
		mosquitoCount--;
		mosquitoKills++;
		
		if (mosquitoCount == 0) {
			notifyWin();
		}
		
	}
	
	public void mosquitoBreach(Entity entity){
		mosquitoCount--;
		playerLife--;
		
		removeEntity(entity);
		if(playerLife == 0){
			notifyDeath();
		}
	}
	
	public void mosquitoOutOfBounds(Entity entity){
		mosquitoCount--;
		removeEntity(entity);
	}
	
	public void tryToFire() {
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this,"sprites/shot.gif",player.getX()+40,player.getY()-30);
		player.setFire();
		
		entities.add(shot);
	}
	
	

	private DataStore stageThreeSpawn(DataStore ds) {
		// TODO Auto-generated method stub
		
		Random num = new Random();
		int x, y;
		
		x = num.nextInt(800);
		y = num.nextInt(500);
		
		ds.setX(x);
		ds.setY(y);
		return ds;
	}

	private DataStore stageOneSpawn(DataStore ds) {
		// TODO Auto-generated method stub
		
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
		ds.setX(x);
		ds.setY(y);
		return ds;
	}

	private void movementControls() {
		// TODO Auto-generated method stub
		if ((kih.leftPressed()) && (!kih.rightPressed())) {
			player.setHorizontalMovement(-moveSpeed);
			
		} else if ((kih.rightPressed()) && (!kih.leftPressed())) {
			player.setHorizontalMovement(moveSpeed);
			
		}
		
		// if we're pressing fire, attempt to fire
		if (kih.firePressed()) {
			tryToFire();
		}else{
			player.setStand();
		}
	}

	public static void main(String[] args){
		Game g = new Game();
		
		g.gameLoop();
	}
}
