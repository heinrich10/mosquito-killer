package spaceinvaders;

import java.util.ArrayList;
import java.util.Random;

public class Game{
	
	public boolean gameRunning = true;
	private ArrayList entities = new ArrayList();
	private ArrayList removeList = new ArrayList();
	private PlayerEntity player;
	private double moveSpeed = 300;
	private long lastFire = 0;
	private long firingInterval = 500;
	private boolean newGame = true;
	private long lastFpsTime;
	private int fps;
	private String windowTitle = "";
	private KeyInputHandler kih = null;
    private GameCanvas gc = null;
    private Story popUp = null;
    private StoryHelper sh = null;
    private AttributeStore attribute = null; 
    private MainMenu mm = null;
    
    private Sprite[] sp = new Sprite[100];
    
	public Game() {
		//create KeyInputHandler and pass it to GameCanvas
		kih = new KeyInputHandler();
    	gc = new GameCanvas(kih);
    	popUp = new Story("");
    	sh = new StoryHelper();
    	mm = new MainMenu();
    	mm.setVisible(true);
    	player = new PlayerEntity(this,"sprites/rockman2.png",370,550);
    	
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
					String title = windowTitle+" (FPS: "+fps+");   Life: " + attribute.playerLife();
					gc.setTitle(title);
					lastFpsTime = 0;
					fps = 0;
					DataStore ds = new DataStore();
					int limit;
					if(attribute.mosquitoKills() <= 10){
						ds = stageOneSpawn(ds);
						limit = 20;
						
						if(attribute.mosquitoKills() == 10){
							kih.pauseGame();
							popUp.setText(sh.stageTwo());
							popUp.setVisible(true);
							
							
						}
					} else if(attribute.mosquitoKills() > 10 && attribute.mosquitoKills() <= 25){
						ds = stageOneSpawn(ds);
						limit = 10;
						
						//add rain
						Random num = new Random();
						for(int i = 1; i < 100 ; i++){
							
				    		gc.rainDrops(sp[i], num.nextInt(800), num.nextInt(800));
				    	}
						if(attribute.mosquitoKills() == 25){
							kih.pauseGame();
							popUp.setText(sh.stageThree());
							popUp.setVisible(true);
						}
					} else {
						ds = stageThreeSpawn(ds);
						limit = 35;
					}
				
					if(attribute.mosquitoCount() < limit){
						alien = new MosquitoEntity(this, ds.getX(), ds.getY());
						entities.add(alien);
						attribute.mosquitoSpawn();
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
						
						if(mm.gameStarted()){
							popUp.setText(sh.stageOne());
							popUp.setVisible(true);
							startGame();
						}
						
						
					    
					}
					
					if(mm.loadGame()){
						
						SaveLoadHelper slh = new SaveLoadHelper();
						
						ArrayList load = null;
						load = (ArrayList) slh.loadFileEntity();
						
						for(int i = 0; i < load.size(); i++){
							DataStore loadObject = (DataStore) load.get(i);
							if(loadObject.getType().compareTo("mosquito") == 0){
								MosquitoEntity mos = new MosquitoEntity(this, loadObject.getX(), loadObject.getY());
								entities.add(i, mos);
							}else if(loadObject.getType().compareTo("player") == 0){
								player = null;
								player = new PlayerEntity(this, "sprites/rockman2.png", loadObject.getX(), loadObject.getY());
								entities.add(i, player);
							}
									
									
							
						}
						System.out.println("load");
						attribute = (AttributeStore) slh.loadFileAtrribute();
						newGame = false;
						mm.dontLoad();
//						
					}
					
					if(popUp.isOk()){
						attribute.mosquitoKill();
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
		attribute = new AttributeStore();
		
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
		attribute.mosquitoKill();
		
		if (attribute.mosquitoCount() == 0) {
			notifyWin();
		}
		
	}
	
	public void mosquitoBreach(Entity entity){
		attribute.mosquitoBreach();
		
		
		removeEntity(entity);
		if(attribute.noLife()){
			notifyDeath();
		}
	}
	
	public void mosquitoOutOfBounds(Entity entity){
		attribute.mosquitoOutOfBounds();
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
		
		if(kih.saveGame()){
			SaveLoadHelper slh = new SaveLoadHelper();
			ArrayList save = new ArrayList(entities.size());
			for(int i = 0; i < entities.size(); i++){
				Entity tempEntity = null;
				String name = "";
				tempEntity = (Entity) entities.get(i);
				if(tempEntity instanceof MosquitoEntity){
					name = "mosquito";
				}else if(tempEntity instanceof PlayerEntity){
					name = "player";
				}
				entities.remove(i);
				DataStore saveObject = new DataStore(tempEntity.getX(),tempEntity.getY(), name);
				save.add(i, saveObject);
				
			}
					
			//slh.saveFile(this);
			slh.saveFileEntities(save);
			slh.saveFileAttributes(attribute);
			System.out.println("save");
			System.exit(0);
		}
	}
	
	public static void main(String args[]) {
		Game g = new Game();
		g.gameLoop();
	}

	
}
