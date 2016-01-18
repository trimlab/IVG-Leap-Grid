import java.io.IOException;
import com.leapmotion.leap.*;

public class Main {

	public static void main(String[] args) {
        //Create listener and attach
        LeapListener listener = new LeapListener();
        Controller controller = new Controller();
        controller.addListener(listener);

        //run until enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.removeListener(listener);
	}

}
