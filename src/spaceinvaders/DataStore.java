package spaceinvaders;

import java.io.Serializable;

public class DataStore implements Serializable {
	private int x;
	private int y;
	private String type;
	
	public DataStore(){
		
	}
	
	public DataStore(int x, int y, String type){
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getY(){
		return y;
	}
	
	public int getX(){
		return x;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
}
