package spaceinvaders;

import java.io.Serializable;

public class AttributeStore implements Serializable {
	private int mosquitoCount;
	private int playerLife;
	private int mosquitoKills;
	
	public AttributeStore(){
		mosquitoCount = 0;
		playerLife = 20;
		mosquitoKills = 0;
	}
	
	public AttributeStore(int mosquitoCount, int playerLife, int mosquitoKills){
		this.mosquitoCount = mosquitoCount;
		this.playerLife = playerLife;
		this.mosquitoKills = mosquitoKills;
	}
	
	public void mosquitoKill(){
		mosquitoKills++;
		mosquitoCount--;
	}
	
	public void mosquitoOutOfBounds(){
		mosquitoCount--;
	}
	
		
	public void mosquitoSpawn(){
		mosquitoCount++;
	}
	
	public int mosquitoCount(){
		return mosquitoCount;
	}
	
	public int playerLife(){
		return playerLife;
	}
	
	public int mosquitoKills(){
		return mosquitoKills;
	}
	
	
	
	public void mosquitoBreach(){
		playerLife--;
		mosquitoCount--;
	}
	
	public boolean noLife(){
		if(playerLife == 0){
			return true;
		}
		return false;
	}
}
