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
        g.drawString(name, 75, 75);
    }
    
    public String getName(){
    	return name;
    }
}
