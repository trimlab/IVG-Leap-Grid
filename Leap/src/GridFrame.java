import java.awt.*;
import javax.swing.*;

public class GridFrame extends JFrame{

	private GridPanel panel = new GridPanel();
	private Container container;
	
	public GridFrame(){

		panel.setBackground(Color.white);

		container = getContentPane();
		container.add(panel, BorderLayout.CENTER);
	}
	
}
