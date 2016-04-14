package TTS;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS implements Runnable{
	Properties prop;
	String str;

	public TTS() {
		prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("app.cfg");
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not load properties file.");
			System.exit(0);
		}
	}
	
	public TTS(String str){
		this.str = str;
		prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("app.cfg");
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not load properties file.");
			System.exit(0);
		}
	}

	public void say(String str) {
		(new Thread(new TTS(str))).start();
	}

	public void run() {
		Voice voice;
		VoiceManager voiceManager = VoiceManager.getInstance();
		voice = voiceManager.getVoice(prop.getProperty("voice"));
		voice.setRate((float) Double.parseDouble(prop.getProperty("rate")));
		voice.setPitchShift((float) Double.parseDouble(prop.getProperty("pitch")));
		voice.allocate();
		voice.speak(str);
	}
}
