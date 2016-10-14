package Manager;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

import LeapMouse.LeapMouseListener;
import TTS.TTS;

public class BlockControl {
	private String testName;
	private String blockName;
	private BlockingQueue<String> taskQueue;
	private RecordManager recordManager;
	private Lock recordLock;
	private TTS voice;
	private Properties globalSettings;
	private Properties blockSettings;
	private LeapMouseListener leapListener;
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public BlockingQueue<String> getTaskQueue() {
		return taskQueue;
	}
	public void setTaskQueue(BlockingQueue<String> taskQueue) {
		this.taskQueue = taskQueue;
	}
	public RecordManager getRecordManager() {
		return recordManager;
	}
	public void setRecordManager(RecordManager recordManager) {
		this.recordManager = recordManager;
	}
	public Lock getRecordLock() {
		return recordLock;
	}
	public void setRecordLock(Lock recordLock) {
		this.recordLock = recordLock;
	}
	public TTS getVoice() {
		return voice;
	}
	public void setVoice(TTS voice) {
		this.voice = voice;
	}
	public Properties getGlobalSettings() {
		return globalSettings;
	}
	public void setGlobalSettings(Properties globalSettings) {
		this.globalSettings = globalSettings;
	}
	public Properties getBlockSettings() {
		return blockSettings;
	}
	public void setBlockSettings(Properties blockSettings) {
		this.blockSettings = blockSettings;
	}
	public LeapMouseListener getLeapListener() {
		return leapListener;
	}
	public void setLeapListener(LeapMouseListener leapListener) {
		this.leapListener = leapListener;
	}
	
	
}
