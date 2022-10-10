package display;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private int 	width;
	private int 	height;

	
	public Frame(String title,int width,int height){
		
		this.width  = width;
		this.height = height;
		
		this.setTitle(title);	
		
		Dimension size = new Dimension(width,height);
		this.setFrameSize(size); 
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void addToFrame(Component comp) {
		
		this.add(comp);
		this.pack();
	}
	
	public int getFrameWidth() {
		return this.width;
	}
	
	public int getFrameHeight() {
		return this.height;
	}


	private void setFrameSize(Dimension d) {
		
		this.setPreferredSize(d);
		this.setMaximumSize(d);
		this.setMinimumSize(d);
	}
	

}
