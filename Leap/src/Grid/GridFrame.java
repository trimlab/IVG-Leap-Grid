package Grid;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Manager.BlockControl;
import Manager.RecordManager;
import Menu.Menu;
import Menu.MenuNode;
import TTS.TTS;

public class GridFrame extends JFrame{

	private Container container;
	private GridPanel back;
	private GridPanel home;
	private JPanel center;
	private Menu menu;
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private RecordManager record;
	private TTS voice;
	private boolean speak;
	private boolean speakOverlap;
	private int speakDelay;
	private boolean speakWav;
	private boolean leafReturnToRoot;

	public GridFrame(Menu menu, BlockControl blockControl){
		this.menu = menu;
		this.queue = blockControl.getTaskQueue();
		this.record = blockControl.getRecordManager();
		this.voice = blockControl.getVoice();
		
		this.speak = (blockControl.getBlockSettings().getProperty("use-speech").compareTo("true") == 0 ? true : false);
		this.speakOverlap = (blockControl.getGlobalSettings().getProperty("allow-overlap").compareTo("true") == 0 ? true : false);
		this.speakDelay = Integer.parseInt(blockControl.getGlobalSettings().getProperty("delay"));
		this.speakWav = (blockControl.getBlockSettings().getProperty("use-speech-files").compareTo("true") == 0 ? true : false);
		this.leafReturnToRoot = (blockControl.getBlockSettings().getProperty("return-home-on-leaf").compareTo("true") == 0 ? true : false);

		//set graphic info
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("cursor.png");
		Cursor c = toolkit.createCustomCursor(image , new Point(0, 0), "img");
		setCursor (c);

		//setup display
		center = new JPanel();
		container = getContentPane();
		//container.add(header, BorderLayout.NORTH);
		//container.add(center, BorderLayout.CENTER);

		container.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		gbc.gridx = 0;
		gbc.gridy = 0;
		
		if(blockControl.getBlockSettings().getProperty("show-header").compareTo("true") == 0){
			back = new GridPanel("Back", false);
			back.setBackground(new Color(127, 235, 255));
			back.addMouseListener(new PanelListener());
			home = new GridPanel("Home", false);
			home.setBackground(new Color(255, 182, 0));
			home.addMouseListener(new PanelListener());
			
			container.add(back, gbc);
			gbc.gridx = 1;
			container.add(home, gbc);
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.ipady = 650;
			gbc.gridwidth = 2;
		}
		container.add(center, gbc);

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
		int numPanels = menu.getCursor().getNumPanels();
		GridPanel panels[] = new GridPanel[numPanels];
		
		for(int i = 0; i < numPanels; i++){
			panels[i] = new GridPanel(menu.getCursor().getChild(i).getName(), true);
			panels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			panels[i].setBackground(Color.WHITE);
			panels[i].addMouseListener(new PanelListener());
			center.add(panels[i]);
		}
		
		for(int i = 0; i < numPanels; i++){
			panels[i].setVisible(true);
		}
		this.setVisible(true);
	}

	public void changePanel(String name) {
		//if at root and trying to move to parent or root
		if((name.compareTo("Back") == 0 || name.compareTo("Home") == 0) && menu.getCursor().getParent() == null){
			return;
		}

		//send clicked name to task manager
		queue.offer(name);

		//send clicked name to record manager
		record.addClick(name);
		
		if(name.compareTo("Back") == 0)
			menu.moveTo("parent");
		else if(name.compareTo("Home") == 0)
			menu.moveTo("root");

		//get current node and moved to clicked node
		else if(menu.getCursor().getChild(name).isLeaf()){
			if(leafReturnToRoot)
				menu.moveTo("root");
		}else{
			menu.moveTo(name);
		}
		
		refreshDisplay();
	}
	
	private class PanelListener extends MouseAdapter{
		private Timer t;

		public void mouseClicked(MouseEvent e){
			GridPanel panel = (GridPanel) e.getSource();

			changePanel(panel.getName());

		}
		public void mouseEntered(MouseEvent e){
			if(speak){
				GridPanel panel = (GridPanel) e.getSource();
				if(speakDelay == 0){
					sayName(panel.getName());
				}else{
					t = new Timer(true);
					t.schedule(new TimerTask(){
						public void run(){
							sayName(panel.getName());
						}
					}, speakDelay);
				}
			}
		}

		public void mouseExited(MouseEvent e){
			if(t != null) t.cancel();
		}

		private void sayName(String str){
			File f = new File("sounds/" + str + ".wav");
			if(speakWav && f.exists() && !f.isDirectory()) { 
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(f));
					clip.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				voice.say(str, false);
			}
		}
	}

}
