package spaceinvaders;

import java.util.Random;

public class MosquitoEntity extends Entity {
	private Game game;
	private Sprite frame1;
	private Sprite frame2;
	private boolean change = true;
	
	public MosquitoEntity(Game game,int x,int y) {
		super("sprites/alien.gif",x,y);
		
		Random num = new Random();
		
		frame1 = SpriteStore.get().getSprite("sprites/alien2.gif");
		frame2 = SpriteStore.get().getSprite("sprites/alien3.gif");
		
		this.game = game;
		
		dy = num.nextInt(45) + 20;
		dx = num.nextInt(80) - 40;
	}

	public void move(long delta) {
		
		super.move(delta);
		
		if(change){
			sprite = frame1;
		} else {
			sprite = frame2;
		}
		change = !change;
		
		if (y > 700) {
			
			game.mosquitoBreach(this);
		}
		
		if (x < 0 || x > 800){
			game.mosquitoOutOfBounds(this);
			
		}
	}
	public void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere
	}
}