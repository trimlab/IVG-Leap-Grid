import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.leapmotion.leap.*;

import LeapMouse.LeapMouseListener;
import Menu.Menu;
import RecordManager.RecordManager;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class Main {

	public static void main(String[] args) {
		//get settings

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("app.cfg");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Could not load properties file.");
					System.exit(0);
				}
			}
		}

		Menu menu = new Menu("menu.cfg");
		String file = JOptionPane.showInputDialog("Enter the file name: ");
		RecordManager record = new RecordManager(file);
		
		GridFrame gui = new GridFrame(menu);
		gui.setTitle(prop.getProperty("window-title"));
		gui.setSize(Integer.parseInt(prop.getProperty("window-width")), Integer.parseInt(prop.getProperty("window-height")));
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BufferedImage icon;
		try {
			icon = ImageIO.read(new File("ivg.png"));
			gui.setIconImage(icon);
		} catch (Exception e) {}
		gui.setVisible(true);
		if(prop.getProperty("start-fullscreen").compareTo("true") == 0) gui.setExtendedState(JFrame.MAXIMIZED_BOTH); 

		//create listener and attach
		Vector max = new Vector(Integer.parseInt(prop.getProperty("max-x")), Integer.parseInt(prop.getProperty("max-y")), Integer.parseInt(prop.getProperty("max-z")));
		Vector min = new Vector(Integer.parseInt(prop.getProperty("min-x")), Integer.parseInt(prop.getProperty("min-y")), Integer.parseInt(prop.getProperty("min-z")));
		LeapMouseListener mouse = new LeapMouseListener(max, min, record);
		Controller controller = new Controller();
		controller.addListener(mouse);

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample listener when done
		controller.removeListener(mouse);
		System.out.println("Exiting");
	}
}
