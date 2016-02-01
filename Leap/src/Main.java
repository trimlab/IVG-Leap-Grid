import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;

import com.leapmotion.leap.*;

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

        GridFrame gui = new GridFrame();
		gui.setTitle(prop.getProperty("window-title"));
		gui.setSize(Integer.parseInt(prop.getProperty("window-width")), Integer.parseInt(prop.getProperty("window-height")));
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//BufferedImage icon = ImageIO.read(new File("trigstar.png"));
		//gui.setIconImage(icon);
		gui.setVisible(true);
		gui.setResizable(false);
		
        //create listener and attach
        LeapListener listener = new LeapListener();
        Controller controller = new Controller();
        //controller.addListener(listener);
	}
}
