package TTS;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS{
	private Properties prop;
	private ArrayList<Speaker> speakers;
	private VoiceManager voiceManager = VoiceManager.getInstance();
	private boolean allowOverlap;
	private boolean prioritizeTask;
	
	private class Speaker implements Runnable{
		private String str;
		private Voice voice;
		private boolean active = true;
		private boolean isTask;
		public Speaker(String str, boolean isTask){
			this.str = str;
			this.isTask = isTask;
		}

		public void run() {
			voice = voiceManager.getVoice(prop.getProperty("voice"));
			voice.setRate((float) Double.parseDouble(prop.getProperty("rate")));
			voice.setPitchShift((float) Double.parseDouble(prop.getProperty("pitch")));
			voice.allocate();
			
			voice.setVolume(100);
			if(active) voice.speak(str);
			active = false;
		}
		
		public void stop(){
			if(voice != null){
				voice.getAudioPlayer().cancel();
				active = false;
			}
		}
		
		public boolean stillActive(){
			return active;
		}
		
		public boolean isTask(){
			return isTask;
		}
	}

	public TTS(boolean allowOverlap, boolean prioritizeTask) {
		this.allowOverlap = allowOverlap;
		this.prioritizeTask = prioritizeTask;
		
		prop = new Properties();
		speakers = new ArrayList<Speaker>();
		
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

	public void say(String str, boolean isTask) {
		if(!speakers.isEmpty() && !allowOverlap){
			for(int i = speakers.size() - 1; i >= 0 ; i--){
				if(speakers.get(i).stillActive()){
					if(speakers.get(i).isTask() && prioritizeTask) continue;
					else speakers.get(i).stop();
				}
			}
			
		}

		if(isTask){
			speakers.add(0, new Speaker(str, isTask));
			(new Thread(speakers.get(0))).start();
		}else{
			if(!speakers.isEmpty() && speakers.get(0).isTask() && speakers.get(0).stillActive() && prioritizeTask) return; 
			speakers.add(new Speaker(str, isTask));
			(new Thread(speakers.get(speakers.size() - 1))).start();
		}
		
		
	}
}
