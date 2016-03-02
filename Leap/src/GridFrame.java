import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import Menu.Menu;
import Menu.MenuNode;

public class GridFrame extends JFrame{
	
	private Container container;
	private JPanel header;
	private JPanel center;
	private Menu menu;
	
	public GridFrame(Menu menu){
		this.menu = menu;
		
		//set graphic info
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("cursor.png");
		Cursor c = toolkit.createCustomCursor(image , new Point(0, 0), "img");
		setCursor (c);
		
		//setup display
		header = new JPanel();
		center = new JPanel();
		container = getContentPane();
		container.add(header, BorderLayout.NORTH);
		container.add(center, BorderLayout.CENTER);
		refreshDisplay();
		
		//container.add(panel, BorderLayout.CENTER);
		//panel.setBackground(Color.white);
	}
	
	private void refreshDisplay(){
		GridLayout layout = new GridLayout(menu.getCursor().getColumns(), menu.getCursor().getRows());
		center.removeAll();
		center.revalidate();
		center.repaint();
		center.setLayout(layout);
		for(int i = 0; i < menu.getCursor().getNumPanels(); i++){
			GridPanel newPanel = new GridPanel(menu.getCursor().getChild(i).getName());
			newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			newPanel.setBackground(Color.WHITE);
			newPanel.addMouseListener(new PanelListener());
			center.add(newPanel);
		}
	}
	
	private class PanelListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			GridPanel panel = (GridPanel) e.getSource();
			System.out.println("clicked " + panel.getName());
			
			MenuNode node = menu.getCursor().getChild(panel.getName());			
			menu.moveTo(node.isLeaf() ? "parent" : panel.getName());
			refreshDisplay();
			
		}
	}
	
}
