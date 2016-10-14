package TTS;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS{
	private Properties prop;
	private Speaker currentSpeaker;
	private VoiceManager voiceManager = VoiceManager.getInstance();
	
	private class Speaker implements Runnable{
		private String str;
		private Voice voice;
		public Speaker(String str){
			this.str = str;

			voice = voiceManager.getVoice(prop.getProperty("voice"));
			voice.setRate((float) Double.parseDouble(prop.getProperty("rate")));
			voice.setPitchShift((float) Double.parseDouble(prop.getProperty("pitch")));
			voice.allocate();
		}

		public void run() {
			voice.setVolume(100);
			voice.speak(str);
		}
		
		public void stop(){
			voice.getAudioPlayer().cancel();
		}
		
	}

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

	public void say(String str, boolean allowOverlap) {
		if(currentSpeaker != null && !allowOverlap){
			System.out.println("Stopping currentVoice");
			currentSpeaker.stop();
		}
		//currentVoice = new Thread(new Speaker(str));
		//currentVoice.start();
		currentSpeaker = new Speaker(str);
		currentSpeaker.stop();
		(new Thread(currentSpeaker)).start();
		
	}
}
