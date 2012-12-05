package spaceinvaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class SaveLoadHelper {
	
	public void saveFileEntities(Object object){
		try {
			FileOutputStream saveFile = new FileOutputStream("D:\\mosquitoKiller.data");
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(object);
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveFileAttributes(Object object){
		try {
			FileOutputStream saveFile = new FileOutputStream("D:\\mosquitoKiller.attributes");
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(object);
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object loadFileEntity(){
		
		Object loadedObject = null;
		try {
			FileInputStream saveFile = new FileInputStream("D:\\mosquitoKiller.data");
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			
			loadedObject = restore.readObject();
			restore.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loadedObject;
	}
	
public Object loadFileAtrribute(){
		
		Object loadedObject = null;
		try {
			FileInputStream saveFile = new FileInputStream("D:\\mosquitoKiller.attributes");
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			
			loadedObject = restore.readObject();
			restore.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loadedObject;
	}
}
