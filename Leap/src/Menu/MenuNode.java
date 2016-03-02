package Menu;

public class MenuNode{
		private MenuNode parent = null;
		private MenuNode[] children;
		private String name = "";
		private int rows;
		private int cols;
		
		public MenuNode(String name, String dimensions){
			this.name = name;
			String[] d = dimensions.replace("#", "").split("x");
			cols = Integer.parseInt(d[0]);
			rows = Integer.parseInt(d[1]);
			children = new MenuNode[cols*rows];
		}
		
		public MenuNode(String name){
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

		public MenuNode getChild(String name){
			for(int i = 0; i < children.length; i++){
				if(children[i].getName().toLowerCase().compareTo(name.toLowerCase()) == 0) 
					return children[i];		
			}
			return null;
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
		
		public int getNumPanels(){
			return cols * rows;
		}
		
		public int getColumns(){
			return cols;
		}
		
		public int getRows(){
			return rows;
		}
		
		public String toString(){
			String ret = name;
			ret += "\nDimensions: " + cols + "x" + rows + "\n";
			if(children != null){
				ret += "\nChildren: " + String.join(", ", getChildrenNames()) + "\n\n";
				for(int i = 0; i < children.length; i++){
					ret += children[i].toString();
				}
			}
			else ret += "\nNo Children\n\n";
			return ret;
		}
	}