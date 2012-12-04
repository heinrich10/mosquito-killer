package spaceinvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameCanvas extends Canvas {
	private BufferStrategy strategy;
	Graphics2D g = null;
	
	JFrame container = null;
	
	private String message = "";
	Sprite sprite = null;
	
	public GameCanvas(KeyInputHandler kih){
		container = new JFrame("Mosquito Killer");
		
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,800));
		panel.setLayout(null);

		setBounds(0,0,800,800);
		panel.add(this);
		
		setIgnoreRepaint(true);
		
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		addKeyListener(kih);
		
		requestFocus();

		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		sprite = SpriteStore.get().getSprite("sprites/copy.jpg");
		
	}
	public void updateGraphics(){
		g = (Graphics2D) strategy.getDrawGraphics();
		sprite.draw(g, 0, 0);
	}
	public void draw(Entity entity) {
		// TODO Auto-generated method stub
		entity.draw(g);
	}
	public void destroy() {
		// TODO Auto-generated method stub
		g.dispose();
	}
	public void showx() {
		// TODO Auto-generated method stub
		strategy.show();
	}
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		container.setTitle(title);
	}
	
	public void waitForKeyPress(){
		g.setColor(Color.white);
		g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
		g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public void rainDrops(Sprite sprite, int x, int y){
		sprite.draw(g, x, y);
	}
	
}
