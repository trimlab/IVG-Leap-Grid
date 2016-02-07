package Menu;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Menu {

	private MenuNode cursor;
	
	public Menu(String file){
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;

		    while ((text = reader.readLine()) != null) {
		        list.add(text);
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}

		cursor = processMenuList(list, 0, 0);
		System.out.println(cursor);
	}
	
	private MenuNode processMenuList(List<String> list, int depth, int line){
		MenuNode m;
		//System.out.println("Hit: " + list.get(line).replace("\t", "") + "\n\tLine: " + line + "\n\tDepth:" + depth);
		
		//if next line is indented once more
		if(line + 1 < list.size() && countTabs(list.get(line + 1)) == depth + 1){
			m = new MenuNode(list.get(line).replace("\t", ""), list.get(line + 1).replace("\t", ""));
			line += 2; //skip this line and dimensions line
			
			//create and add each child
			for(int i = 0; countTabs(list.get(line)) == depth + 1; i++){
				m.setChild(i, processMenuList(list, depth + 1, line));
				line = findNextLine(list, line);
				if(line < 0) break;
			}
		}else{
			//no children
			m = new MenuNode(list.get(line).replace("\t", ""));
		}
	
		return m;
	}
	
	public MenuNode getCursor(){
		return cursor;
	}
	
	public void moveTo(MenuNode n){
		cursor = n;
	}
	
	public boolean moveTo(String str){
		if(str.toLowerCase().compareTo("parent") == 0 && cursor.getParent() != null){
			cursor = cursor.getParent();
			return true;
		}else{
			String[] names = cursor.getChildrenNames();
			for(int i = 0; i < names.length; i++){
				if(str.toLowerCase().compareTo(names[i]) == 0){
					cursor = cursor.getChild(i);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
	private int countTabs(String str){
		return str.length() - str.replace("\t", "").length();
	}
	
	private int findNextLine(List<String> list, int line){
		int i = line;
		while(true){
			if(i + 1 >= list.size()) return -1;
			else if(countTabs(list.get(++i)) <= countTabs(list.get(line))) return i;
		}
	}
}
