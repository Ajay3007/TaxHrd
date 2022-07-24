package params;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.HurdleConstant;

public class EquityLoader {
	public static final byte INDEX_EQUITES = 1;
	
	public static final byte INDEX_BUYAMT = 8;
	public static final byte INDEX_SELLAMT = 12;
	public static final byte INDEX_DAYS_HOLD = 13;
	
	public int START_ROW = 25;
	public int END_ROW = 297;
	
	// Intraday
	public static final byte INDEX_SPECUL = 17;
	
	// STCG
	public static final byte INDEX_STCG = 15;
	
	
	private XSSFSheet equitesSheet;
	
	private XSSFWorkbook workbook;
	
	private double totalStcgBuy;
	private double totalStcgSell;
	private double totalStcg;
	private double totalIntraBuy;
	private double totalIntraSell;
	private double totalIntraTurnover;
	private double totalTurnover;
	
	
	public void initialize() throws Exception {
		try {
			System.out.println("Initializing Equity Loader...");
			FileInputStream fileInputStream = new FileInputStream(new File(HurdleConstant.TAX_CONFIG_FILE_PATH));
			this.workbook = new XSSFWorkbook(fileInputStream);
			this.equitesSheet = this.workbook.getSheetAt(INDEX_EQUITES);
//			for(int i=0; i<START_ROW-1; i++) {
//				System.out.println("removing row  " + i+1);
//				this.equitesSheet.removeRow(this.equitesSheet.getRow(0));
//			}
			fileInputStream.close();
			loadEquities();
			System.out.println("\nEquity loader initialized SUCCESSFULLY :)\n");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void loadEquities() {
		// TODO Auto-generated method stub
		Iterator<Row> rowIterator = this.equitesSheet.iterator();
		int days;
		double buyValue;
		double sellValue;
		double totalBuySTCG = 0;
		double totalSellSTCG = 0;
		double totalBuyINTRA = 0;
		double totalSellINTRA = 0;
		double stcg;
		double intraTurnover;
		double totalSTCG = 0;
		double totalIntraTurnover = 0;
		XSSFCell cell;
		int rowNum;
		int i=0;
		while(rowIterator.hasNext()) {
			XSSFRow row = (XSSFRow) rowIterator.next();
			rowNum = row.getRowNum();
			if(rowNum < START_ROW - 1) {
				continue;
			}
			//System.out.println("row num : " + rowNum);
			if(rowNum == END_ROW-1) {
				break;
			}
			String str = null; 
			
			cell = row.getCell(INDEX_BUYAMT);
			str = cell.getStringCellValue();
			buyValue = Double.parseDouble(str.trim());
			
			cell = row.getCell(INDEX_SELLAMT);
			str = cell.getStringCellValue();
			sellValue = Double.parseDouble(str.trim());
			
			cell = row.getCell(INDEX_DAYS_HOLD);
			str = cell.getStringCellValue();
			days = Integer.parseInt(str.trim());
			if(days!=0) {
				cell = row.getCell(INDEX_STCG);
				str = cell.getStringCellValue();
				stcg = Double.parseDouble(str.trim());
				totalBuySTCG += buyValue;
				totalSellSTCG += sellValue;
				totalSTCG += stcg;
			} else {
				cell = row.getCell(INDEX_SPECUL);
				str = cell.getStringCellValue();
				intraTurnover = Double.parseDouble(str.trim());
				if(intraTurnover < 0.0) {
					intraTurnover = intraTurnover * (-1);
				}
				totalBuyINTRA += buyValue;
				totalSellINTRA += sellValue;
				totalIntraTurnover += intraTurnover;
			}
			//System.out.println("buy : " + buyValue);
		}
		
		
		this.totalIntraBuy = totalBuyINTRA;
		this.totalIntraSell = totalSellINTRA;
		this.totalIntraTurnover = totalIntraTurnover;
		this.totalStcgBuy = totalBuySTCG;
		this.totalStcgSell = totalSellSTCG;
		this.totalStcg = totalSTCG;
		this.totalTurnover = totalIntraTurnover + totalSellSTCG;
	}
	
	public double getTotalStcgBuy() {
		return totalStcgBuy;
	}

	public void setTotalStcgBuy(double totalStcgBuy) {
		this.totalStcgBuy = totalStcgBuy;
	}

	public double getTotalStcgSell() {
		return totalStcgSell;
	}

	public void setTotalStcgSell(double totalStcgSell) {
		this.totalStcgSell = totalStcgSell;
	}

	public double getTotalStcg() {
		return totalStcg;
	}

	public void setTotalStcg(double totalStcg) {
		this.totalStcg = totalStcg;
	}

	public double getTotalIntraBuy() {
		return totalIntraBuy;
	}

	public void setTotalIntraBuy(double totalIntraBuy) {
		this.totalIntraBuy = totalIntraBuy;
	}

	public double getTotalIntraSell() {
		return totalIntraSell;
	}

	public void setTotalIntraSell(double totalIntraSell) {
		this.totalIntraSell = totalIntraSell;
	}

	public double getTotalIntraTurnover() {
		return totalIntraTurnover;
	}

	public void setTotalIntraTurnover(double totalIntraTurnover) {
		this.totalIntraTurnover = totalIntraTurnover;
	}

	public double getTotalTurnover() {
		return totalTurnover;
	}

	public void setTotalTurnover(double totalTurnover) {
		this.totalTurnover = totalTurnover;
	}

}
