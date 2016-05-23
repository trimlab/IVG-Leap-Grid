package Grid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GridPanel extends JPanel{
	private String name;
	private boolean drawBackground;
	public GridPanel(String name, boolean drawBackground){
		this.name = name;
		this.drawBackground = drawBackground;
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(drawBackground){
	        BufferedImage image = null;
			try {
				image = ImageIO.read(new File("menuitem.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
        
        g.drawLine(getWidth(), 0, getWidth(), getHeight());
        g.drawLine(0, getHeight(), getWidth(), getHeight());
        g.setFont(new Font("Arial", Font.PLAIN, getWidth() / 15)); 
        int nameWidth = g.getFontMetrics().stringWidth(name);
        g.drawString(name, getWidth() / 2 - nameWidth / 2, getHeight() / 2);
    }
    
    public String getName(){
    	return name;
    }
}
