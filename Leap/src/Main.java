import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.leapmotion.leap.*;

import Grid.GridFrame;
import LeapMouse.LeapMouseListener;
import Manager.BlockControl;
import Manager.RecordManager;
import Manager.TaskManager;
import Menu.Menu;
import TTS.TTS;


/*
 * TODO
 * Readme file - make a readme file to give instructions on how to use config files to set up experiment
 * OSC messages - send out data (all the data in the excel) through osc messages
 * Spacing between squares - not a big priority right now
 * Lists - not a big priority right now
 * 
 */


public class Main {
	private static ArrayList<String> usedBlocks = new ArrayList<String>();

	public static void main(String[] args) {
		//get settings
		BlockControl blockControl = new BlockControl();

		Properties prop = getProperties("app.cfg");
		blockControl.setGlobalSettings(prop);

		//used to talk to timing thread
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		blockControl.setTaskQueue(queue);
		Lock recordLock = new ReentrantLock();
		blockControl.setRecordLock(recordLock);

		//get test name
		String testName;
		File dir;
		while(true){
			testName = JOptionPane.showInputDialog("Enter the file name: ").replaceAll("[^A-Za-z0-9-]", "");
			
			dir = new File("output/" + testName);
			if(testName != "" && !dir.exists()){
				break;
			}
		}
		blockControl.setTestName(testName);

		//make directory for test
		try{
			dir.mkdir();
		} 
		catch(Exception e){
			e.printStackTrace();
		} 

		//get box coordinate settings
		Vector max = new Vector(Integer.parseInt(prop.getProperty("max-x")), Integer.parseInt(prop.getProperty("max-y")), Integer.parseInt(prop.getProperty("max-z")));
		Vector min = new Vector(Integer.parseInt(prop.getProperty("min-x")), Integer.parseInt(prop.getProperty("min-y")), Integer.parseInt(prop.getProperty("min-z")));

		//create listener and attach
		LeapMouseListener leapListener = new LeapMouseListener(max, min);
		blockControl.setLeapListener(leapListener);
		Controller controller = new Controller();
		controller.addListener(leapListener);
		
		//create TTS and attach
		blockControl.setVoice(new TTS(prop.getProperty("allow-overlap").compareTo("true") == 0 ? true : false, prop.getProperty("prioritize-task").compareTo("true") == 0 ? true : false));

		init(blockControl);

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample listener when done
		controller.removeListener(leapListener);
	}

	//start block selection, open menu, and start recording/task management
	private static void init(BlockControl blockControl){
		//get selected block
		String blockName = getBlock();
		blockControl.setBlockName(blockName);
		usedBlocks.add(blockName);
		Properties block = getProperties("blocks/" + blockName);
		blockControl.setBlockSettings(block);

		RecordManager record = new RecordManager(blockControl.getTestName() + "/" + blockName.replace(".cfg", ""), blockControl.getRecordLock());
		blockControl.getLeapListener().setRecordManager(record);
		blockControl.setRecordManager(record);

		//create menu and task manager
		Menu menu = new Menu("menus/" + block.getProperty("menu"));

		TaskManager tasks = new TaskManager(blockControl);
		tasks.start();


		//create GUI and set settings
		GridFrame gui = new GridFrame(menu, blockControl);
		gui.setTitle(blockControl.getGlobalSettings().getProperty("window-title"));
		gui.setSize(Integer.parseInt(blockControl.getGlobalSettings().getProperty("window-width")), Integer.parseInt(blockControl.getGlobalSettings().getProperty("window-height")));

		//set grid on mouse to allow changing from leap
		blockControl.getLeapListener().setGridFrame(gui);

		WindowListener exitListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				init(blockControl);
			}
		};
		
		gui.addWindowListener(exitListener);
		gui.setVisible(true);
		if(blockControl.getGlobalSettings().getProperty("start-fullscreen").compareTo("true") == 0) gui.setExtendedState(JFrame.MAXIMIZED_BOTH); 

		//create GUI icon
		BufferedImage icon;
		try {
			icon = ImageIO.read(new File("ivg.png"));
			gui.setIconImage(icon);
		} catch (Exception e) {}

	}
	//return properties object from file
	private static Properties getProperties(String file){
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(file);
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not load properties file.");
			System.exit(0);
		}

		return prop;
	}

	//read all block files and get user selection
	private static String getBlock(){
		File folder = new File("blocks/");
		File[] files = folder.listFiles();
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> optionFiles = new ArrayList<String>();

		//System.out.println("used: " + usedBlocks.toString());
		
		for (int i = 0; i < files.length; i++) {
			//System.out.println(files[i] + ": " + usedBlocks.contains(files[i].getName()));
			if (files[i].isFile() && !usedBlocks.contains(files[i].getName())) {
				//block was not already used
				//if(){

					Properties prop = new Properties();
					InputStream input = null;

					try {
						input = new FileInputStream(folder + "/" + files[i].getName());
						prop.load(input);
						options.add(prop.getProperty("name"));
						optionFiles.add(files[i].getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				//}
			}
		}
		
		//all blocks used
		if(options.size() == 0) System.exit(0);
		
		int n = JOptionPane.showOptionDialog(null,
				"Please select which block you would like to use.",
				"Block Selection",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.DEFAULT_OPTION,
				null,
				options.toArray(),
				options.get(0));  

		//if closed, exit
		if(n == -1) System.exit(0);

		return optionFiles.get(n);
	}

}
