package TaskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class TaskManager extends Thread {
	private BlockingQueue<String> queue;
	private String[] tasks;
	private int task = -1;
	
	public TaskManager(String file, BlockingQueue<String> queue){
		this.queue = queue;
		//read each line of file into tasks
		Scanner sc = null;
		try {
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
		System.out.println("Executing task: " + tasks[task]);
		//sleep for n seconds if task is 'wait n'
		if(tasks[task].toLowerCase().contains("wait")){
			try {
				int sleep = Integer.parseInt(tasks[task].replaceAll("[^0-9]","")) * 1000;
				System.out.println("Sleeping for " + sleep + "ms");
				Thread.sleep(sleep);
				nextTask();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		while(true){
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
