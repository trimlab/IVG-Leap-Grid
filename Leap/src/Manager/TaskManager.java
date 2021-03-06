package Manager;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

import com.leapmotion.leap.Vector;

import TTS.TTS;

public class TaskManager extends Thread {
	private volatile boolean running = true;
	private TTS voice;
	private BlockingQueue<String> queue;
	private RecordManager record;
	private String[] tasks;
	private Properties prop;
	private int task = -1;
	
	public TaskManager(BlockControl blockControl){
		this.queue = blockControl.getTaskQueue();
		this.record = blockControl.getRecordManager();
		this.prop = blockControl.getGlobalSettings();
		this.voice = blockControl.getVoice();
		
		//read each line of file into tasks
		Scanner sc = null;
		try {
			String file = "tasks/" + blockControl.getBlockSettings().getProperty("task");
			sc = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<String> lines = new ArrayList<String>();
		while (sc.hasNextLine()) {
		  lines.add(sc.nextLine());
		}
		tasks = lines.toArray(new String[0]);
	}
	
	//start next task
	private void nextTask(){
		task++;
		
		if(task >= tasks.length){
			record.finalWrite();
			running = false;
			return;
		}
		
		System.out.println("Executing task: " + tasks[task]);
		//sleep for n seconds if task is 'wait n'
		if(tasks[task].toLowerCase().trim().length() >= 4 && tasks[task].toLowerCase().trim().substring(0, 4).compareTo("wait") == 0){
			try {
				record.enabled(false);
				int sleep = Integer.parseInt(tasks[task].replaceAll("[^0-9]","")) * 1000;
				Thread.sleep(sleep);
				nextTask();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			//say the task
			voice.say(prop.getProperty("task-prefix") + " " + tasks[task] + " " + prop.getProperty("task-suffix"), true);
			
			record.addSheet(tasks[task]);
			record.enabled(true);

		    //insert initial timestamp
			record.addRecord(new Vector(0, 0, 0), new Point(0, 0));
		}
	}
	
	public void checkTask(String menuName){
		//move to next task if complete
		if(menuName.toLowerCase().compareTo(tasks[task].toLowerCase()) == 0){
			nextTask();
		}
	}

	public void run(){
		nextTask();
		while(running){
			try {
				//wait for queue input
				String data = queue.take();
				checkTask(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
