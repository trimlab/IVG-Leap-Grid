import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class BlockPanel extends JFrame{
	Container container;
	JList list = new JList();
	
	public BlockPanel(){

		container = getContentPane();
		container.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
	}

}
