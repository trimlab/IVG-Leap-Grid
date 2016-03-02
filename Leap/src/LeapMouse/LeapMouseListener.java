package LeapMouse;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.Type;

public class LeapMouseListener extends Listener {
	private Robot robot;
	private Vector max;
	private Vector min;
	
	public LeapMouseListener(Vector max, Vector min){
		this.max = max;
		this.min = min;
	}

	public void onInit(Controller controller){
		//controller setup
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		controller.config().setFloat("Gesture.ScreenTap.MinForwardVelocity", (float) 30.0);
		controller.config().setFloat("Gesture.ScreenTap.HistorySeconds", (float) .5);
		controller.config().setFloat("Gesture.ScreenTap.MinDistance", (float) 1.0);
		controller.config().save();
		System.out.println("Mouse activated");
		
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	//on leap device connected
	public void onConnect(Controller controller){
		System.out.println("Leap Connected");
	}	
	//getting gestures performed on frame	
	public void onFrame(Controller controller){
		Frame frame = controller.frame();		//giving device control to frame
		InteractionBox box = frame.interactionBox();	
		
		//for moving mouse cursor over frame when right hand is enable and all fingers are not extended
		for(Hand hand: frame.hands()){
			if(hand.isRight()){
				//Vector pos = hand.stabilizedPalmPosition();	
				Vector pos = hand.palmPosition();		
				
				Point screenPos = normalize(pos);
				if(screenPos != null){
					//move mouse
					robot.mouseMove(screenPos.x, screenPos.y);	
					
					//check for click
					for(Gesture gesture: frame.gestures()){
						if(gesture.type() == Type.TYPE_KEY_TAP){
							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
							robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						}
					}
				}
			}
		}
		
	}
	
	//normalize a vector to match screen position and check if pos is in config constrains
	private Point normalize(Vector pos){
		if( pos.getX() > max.getX() || pos.getX() < min.getX() ||
			pos.getY() > max.getY() || 
			pos.getZ() > max.getZ() || pos.getZ() < min.getZ())	return null;
		
		float width = Math.abs(max.getX()) - min.getX();
		float height = Math.abs(max.getZ()) - min.getZ();
		double percentX = (pos.getX() - min.getX()) / width;
		double percentZ = (pos.getZ() - min.getZ())/ height;
		
		Dimension screen=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		return new Point((int) (percentX * screen.width), (int) (percentZ * screen.height));
	}
}
