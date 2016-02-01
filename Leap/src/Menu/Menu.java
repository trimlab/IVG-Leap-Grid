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

		cursor = processMenuList(list);
	}
	
	private MenuNode processMenuList(List<String> list){
		
        //int tabs = text.length() - text.replaceAll("\t", "").length();
		
		return null;
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
	
	private class MenuNode{
		private MenuNode parent = null;
		private MenuNode[] children;
		private String name = "";
		private int rows;
		private int cols;
		
		public void MenuNode(String name, String dimensions){
			this.name = name;
			String[] d = dimensions.split("x");
			cols = Integer.parseInt(d[0]);
			rows = Integer.parseInt(d[1]);
			children = new MenuNode[cols*rows];
		}
		
		public void MenuNode(String name){
			this.name = name;
			children = null;
		}
		
		public void setParent(MenuNode p){
			parent = p;
		}		
		
		public MenuNode getParent(){
			return parent;
		}
		
		public void setChild(int i, MenuNode c){
			children[i] = c;
		}
		
		public MenuNode getChild(int i){
			return children[i];
		}
		
		public String getName(){
			return name;
		}
		
		public boolean isLeaf(){
			if(children == null) return true;
			else return false;
		}
		
		public String[] getChildrenNames(){
			String[] ret = new String[children.length];
			for(int i = 0; i < children.length; i++){
				ret[i] = children[i].getName();			
			}
			return ret;
		}
		
		public String toString(){
			String ret = name;
			ret += "\nDimensions: " + cols + "x" + rows + "\n";
			ret += "\nChildren: " + String.join(", ", getChildrenNames()) + "\n\n";
			for(int i = 0; i < children.length; i++){
				ret += children[i].toString();
			}
			return ret;
		}
	}
}
