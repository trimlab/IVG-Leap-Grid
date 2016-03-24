import java.awt.*;
import javax.swing.*;

public class GridPanel extends JPanel{
	private String name;
	public GridPanel(String name){
		this.name = name;
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(getWidth(), 0, getWidth(), getHeight());
        g.drawLine(0, getHeight(), getWidth(), getHeight());
        g.setFont(new Font("Arial", Font.PLAIN, getWidth() / 15)); 
        int nameWidth = g.getFontMetrics().stringWidth(name);
        g.drawString(name, getWidth() / 2 - nameWidth / 2, getHeight() / 4);
    }
    
    public String getName(){
    	return name;
    }
}
