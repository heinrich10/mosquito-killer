package spaceinvaders;

public class PlayerEntity extends Entity {
	private Game game;
	private Sprite frame1; 
	private Sprite frame2;

	public PlayerEntity(Game game,String ref,int x,int y) {
		super(ref,x,y);
		
		frame1 = SpriteStore.get().getSprite("sprites/rockman2.png");;
		frame2 = SpriteStore.get().getSprite("sprites/rockman1.jpg");
		
		this.game = game;
	}
	
	/**
	 * Request that the ship move itself based on an elapsed ammount of
	 * time
	 * 
	 * @param delta The time that has elapsed since last move (ms)
	 */
	public void move(long delta) {
		if ((dx < 0) && (x < 10)) {
			return;
		}
		if ((dx > 0) && (x > 750)) {
			return;
		}
		
		super.move(delta);
	}
	
	public void collidedWith(Entity other) {
		if (other instanceof MosquitoEntity) {
			game.mosquitoBreach(other);
		}
	}
	
	public void setFire(){
		sprite = frame2;
	}
	
	public void setStand(){
		sprite = frame1;
	}
	
}