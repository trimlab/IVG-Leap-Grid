import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;

import com.leapmotion.leap.*;

import LeapMouse.LeapMouseListener;
import Menu.Menu;

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

        GridFrame gui = new GridFrame(menu);
		gui.setTitle(prop.getProperty("window-title"));
		gui.setSize(Integer.parseInt(prop.getProperty("window-width")), Integer.parseInt(prop.getProperty("window-height")));
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//BufferedImage icon = ImageIO.read(new File("ivg.png"));
		//gui.setIconImage(icon);
		gui.setVisible(true);
		if(prop.getProperty("start-fullscreen").compareTo("true") == 0) gui.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
        //create listener and attach
		Vector max = new Vector(Integer.parseInt(prop.getProperty("max-x")), Integer.parseInt(prop.getProperty("max-y")), Integer.parseInt(prop.getProperty("max-z")));
		Vector min = new Vector(Integer.parseInt(prop.getProperty("min-x")), Integer.parseInt(prop.getProperty("min-y")), Integer.parseInt(prop.getProperty("min-z")));
		LeapMouseListener mouse = new LeapMouseListener(max, min);
        Controller controller = new Controller();
        controller.addListener(mouse);
        
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(mouse);
	}
}
