package spaceinvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameCanvas extends Canvas {
	private BufferStrategy strategy;
	Graphics2D g = null;
	
	JFrame container = null;
	
	public GameCanvas(KeyInputHandler kih){
		container = new JFrame("Space Invaders 102");
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,800));
		panel.setLayout(null);
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,800);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		//addKeyListener(new KeyInputHandler());
		addKeyListener(kih);
		
		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// initialise the entities in our game so there's something
		// to see at startup
		//initEntities();
	}
	public void updateGraphics(){
		g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,800,800);
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

	
}
