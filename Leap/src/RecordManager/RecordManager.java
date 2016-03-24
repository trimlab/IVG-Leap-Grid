package RecordManager;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import com.leapmotion.leap.Vector;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.*;

public class RecordManager {
	private String name;
	private WritableWorkbook workbook = null;
	private WritableSheet sheet = null;
	private int sheetRow = 2;
	
	public RecordManager(String name){
		this.name = name;
		try {
			workbook = Workbook.createWorkbook(new File(name + ".xls"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	
	//Add new sheet with given name. Used for each task.
	public void addSheet(String sheetName){
		try{
			sheet = workbook.createSheet(sheetName, workbook.getSheets().length - 1);
			
		    WritableCellFormat header = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.SINGLE));
		    header.setAlignment(Alignment.CENTRE);
		    WritableCellFormat title = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false, UnderlineStyle.SINGLE));
		    title.setAlignment(Alignment.CENTRE);
		    
		    CellView cv = new CellView();
		    cv.setFormat(header);

		    //merge for title
		    sheet.mergeCells(0, 0, 5, 0);
		    sheet.addCell(new Label(0, 0, sheetName, title));
		    
		    //Set up headers
		    sheet.setColumnView(0, 30);
		    sheet.setColumnView(1, 15);
		    sheet.setColumnView(2, 15);
		    sheet.setColumnView(3, 15);
		    sheet.setColumnView(4, 15);
		    sheet.setColumnView(5, 15);
		    
		    sheet.addCell(new Label(0, 1, "Timestamp", header));
		    sheet.addCell(new Label(1, 1, "Leap X", header));
		    sheet.addCell(new Label(2, 1, "Leap Y", header));
		    sheet.addCell(new Label(3, 1, "Leap Z", header));
		    sheet.addCell(new Label(4, 1, "Screen X", header));
		    sheet.addCell(new Label(5, 1, "Screen Y", header));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addRecord(String timestamp, Vector v, Point p){
		try {
			sheet.addCell(new Label(0, sheetRow, timestamp));
			sheet.addCell(new Label(1, sheetRow, "" + v.getX()));
			sheet.addCell(new Label(2, sheetRow, "" + v.getY()));
			sheet.addCell(new Label(3, sheetRow, "" + v.getZ()));
			sheet.addCell(new Label(4, sheetRow, "" + p.x));
			sheet.addCell(new Label(5, sheetRow, "" + p.y));
			
			if(sheetRow > 5000){
				workbook.write();
				workbook.close();
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheetRow++;
		System.out.println(sheetRow);
	}
}
