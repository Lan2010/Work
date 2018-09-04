package core.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static void main(String[] args) throws IOException {
		File file = new File("D:/1.xls");
		ExcelUtil.readToArray(file, 0, 0, (short) 0);
		// ExcelUtil.read(file);
	}

	/**
	 * 依据后缀名判断读取的是否为Excel文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isExcel(String filePath) {
		if (filePath.matches("^.+\\.(?i)(xls)$") || filePath.matches("^.+\\.(?i)(xlsx)$")) {
			return true;
		}
		return false;
	}

	/**
	 * 检查文件是否存在
	 */
	public static boolean fileExist(String filePath) {
		if (filePath == null || filePath.trim().equals(""))
			return false;
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * 依据内容判断是否为excel2003及以下
	 */
	public static boolean isExcel2003(String filePath) {
		try {
			return isExcel2003(new FileInputStream(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isExcel2003(InputStream is) {
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			if (POIFSFileSystem.hasPOIFSHeader(bis)) {
				System.out.println("Excel版本为excel2003及以下");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
		/*
		 * try { new HSSFWorkbook(is);
		 * System.out.println("Excel版本为excel2003及以下"); return true; } catch
		 * (Exception e) { return false; }
		 */
	}

	/**
	 * 依据内容判断是否为excel2007及以上
	 */
	public static boolean isExcel2007(String filePath) {
		try {
			return isExcel2007(new FileInputStream(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isExcel2007(InputStream is) {

		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			if (POIXMLDocument.hasOOXMLHeader(bis)) {
				System.out.println("Excel版本为excel2007及以上");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
		/*
		 * try { new XSSFWorkbook(is);
		 * System.out.println("Excel版本为excel2007及以上"); return true; } catch
		 * (Exception e) { return false; }
		 */
	}

	/**
	 * @描述：根据文件名读取excel文件
	 */
	public static Workbook getWorkbook(InputStream is, String excelPath) {
		Workbook wb = null;
		try {
			/** 验证文件是否合法 */
			/*
			 * if (!validateExcel(filePath)) { System.out.println(errorInfo);
			 * return null; }
			 */
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = false;
			boolean isExcel2007 = false;
			isExcel2003 = isExcel2003(new FileInputStream(excelPath));
			if (isExcel2003 == false) {
				isExcel2007 = isExcel2007(new FileInputStream(excelPath));
			}

			if (isExcel2003) {
				wb = new HSSFWorkbook(is);
			} else if (isExcel2007) {
				wb = new XSSFWorkbook(is);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wb;
	}

	/** 错误信息 */
	private static String errorInfo;

	/**
	 * 验证EXCEL文件是否合法
	 */
	public static boolean validateExcel(String filePath) {

		/** 判断文件名是否为空或者文件是否存在 */
		if (!fileExist(filePath)) {
			errorInfo = "文件不存在";
			return false;
		}

		/** 检查文件是否是Excel格式的文件 */
		if (!isExcel(filePath)) {
			errorInfo = "文件名不是excel格式";
			return false;
		}
		return true;
	}

	public static String[][] readToArray(File file, int ignoreRows, int sheetIndex, short ignoreColumns) {
		String[][] result = null;
		InputStream is = null;
		try {
			/** 验证文件是否合法 */
			/*
			 * if (!validateExcel(filePath)) { System.out.println(errorInfo);
			 * return null; }
			 */
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = false;
			boolean isExcel2007 = false;
			isExcel2003 = isExcel2003(new FileInputStream(file));
			if (isExcel2003 == false) {
				isExcel2007 = isExcel2007(new FileInputStream(file));
			}
			/** 调用本类提供的根据流读取的方法 */
			is = new FileInputStream(file);

			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(is);
				result = readToArray(wb, ignoreRows, sheetIndex, ignoreColumns);
			} else if (isExcel2007) {
				wb = new XSSFWorkbook(is);
				result = readToArray(wb, ignoreRows, sheetIndex, ignoreColumns);
			}

			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * @描述：读取数据
	 */
	public static String[][] readToArray(Workbook wb, int ignoreRows, int sheetIndex, short ignoreColumns) {
		List<String[]> result = new ArrayList<String[]>();

		/** 得到总的shell */
		int sheetAccount = wb.getNumberOfSheets();
		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(sheetIndex);
		/** 得到Excel的行数 */
		int rowCount = sheet.getPhysicalNumberOfRows();
		/** 也可以通过得到最后一行数 */
		int lastRowNum = sheet.getLastRowNum();
		/** 循环Excel的行 */
		for (int r = ignoreRows; r < rowCount; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			String[] values = new String[row.getLastCellNum()];
			Arrays.fill(values, "");
			boolean hasValue = false;
			/** 循环Excel的列 */
			for (int c = ignoreColumns; c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				String cellValue = "";
				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					// XSSFCell可以达到相同的效果
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						double d = cell.getNumericCellValue();
						if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期类型
							Date date = cell.getDateCellValue();
							// Date date = HSSFDateUtil.getJavaDate(d);
							cellValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

						} else {// 数值类型

							DecimalFormat df = new DecimalFormat("0");

							String whatYourWant = df.format(cell.getNumericCellValue());

							cellValue = whatYourWant + "";
						}
						break;
					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						cellValue = cell.getCellFormula() + "";
						break;
					case HSSFCell.CELL_TYPE_BLANK: // 空值
						cellValue = "";
						break;
					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "非法字符";
						break;
					default:
						cellValue = "未知类型";
						break;
					}
				}
				// System.out.print(cellValue + "\t");
				values[c] = rightTrim(cellValue);
				if (!"".equals(cellValue.trim())) {
					hasValue = true;
				}
			}
			// System.out.println();
			if (hasValue) {
				result.add(values);
			}
		}
		String[][] returnArray = new String[result.size()][rowCount];

		for (int i = 0; i < returnArray.length; i++) {

			returnArray[i] = (String[]) result.get(i);

		}
		return returnArray;
	}

	/**
	 * @描述：根据文件名读取excel文件
	 */
	public static List<List<String>> read(File file) {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		InputStream is = null;
		try {
			/** 验证文件是否合法 */
			/*
			 * if (!validateExcel(filePath)) { System.out.println(errorInfo);
			 * return null; }
			 */
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = false;
			boolean isExcel2007 = false;
			isExcel2003 = isExcel2003(new FileInputStream(file));
			if (isExcel2003 == false) {
				isExcel2007 = isExcel2007(new FileInputStream(file));
			}
			/** 调用本类提供的根据流读取的方法 */
			is = new FileInputStream(file);

			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(is);
				dataLst = read(wb);
			} else if (isExcel2007) {
				wb = new XSSFWorkbook(is);
				dataLst = read(wb);
			}

			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		return dataLst;
	}

	/**
	 * @描述：读取数据
	 */
	private static List<List<String>> read(Workbook wb) {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		/** 得到总的shell */
		int sheetAccount = wb.getNumberOfSheets();
		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(0);
		/** 得到Excel的行数 */
		int rowCount = sheet.getPhysicalNumberOfRows();
		/** 也可以通过得到最后一行数 */
		int lastRowNum = sheet.getLastRowNum();
		/** 循环Excel的行 */
		for (int r = 0; r < rowCount; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			List<String> rowLst = new ArrayList<String>();
			/** 循环Excel的列 */
			for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
				Cell cell = row.getCell(c);
				String cellValue = "";
				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					// XSSFCell可以达到相同的效果
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						double d = cell.getNumericCellValue();
						if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期类型
							Date date = cell.getDateCellValue();
							// Date date = HSSFDateUtil.getJavaDate(d);
							cellValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

						} else {// 数值类型

							DecimalFormat df = new DecimalFormat("0");

							String whatYourWant = df.format(cell.getNumericCellValue());

							cellValue = whatYourWant + "";
						}
						break;
					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						cellValue = cell.getCellFormula() + "";
						break;
					case HSSFCell.CELL_TYPE_BLANK: // 空值
						cellValue = "";
						break;
					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "非法字符";
						break;
					default:
						cellValue = "未知类型";
						break;
					}
				}
				System.out.print(cellValue + "\t");
				rowLst.add(cellValue);
			}
			System.out.println();
			dataLst.add(rowLst);
		}
		return dataLst;
	}

	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if (null != cell) {
			// 以下是判断数据的类型
			switch (cell.getCellType()) {
			// XSSFCell可以达到相同的效果
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				double d = cell.getNumericCellValue();
				if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期类型
					Date date = cell.getDateCellValue();
					// Date date = HSSFDateUtil.getJavaDate(d);
					cellValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

				} else {// 数值类型

					DecimalFormat df = new DecimalFormat("0");

					String whatYourWant = df.format(cell.getNumericCellValue());

					cellValue = whatYourWant + "";
				}
				break;
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				cellValue = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				cellValue = cell.getBooleanCellValue() + "";
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				cellValue = cell.getCellFormula() + "";
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
				cellValue = "";
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
				cellValue = "非法字符";
				break;
			default:
				cellValue = "未知类型";
				break;
			}
		}
		return rightTrim(cellValue);
	}

	public static String rightTrim(String str) {

		if (str == null) {

			return "";

		}

		int length = str.length();

		for (int i = length - 1; i >= 0; i--) {

			if (str.charAt(i) != 0x20) {

				break;

			}

			length--;

		}

		return str.substring(0, length);

	}

	/**
	 * 获取一张2003excel工作表
	 * 
	 * @param result
	 *            结果集list (list里边放的是map)
	 * @param sheetName
	 *            工作表名
	 * @param titleList
	 *            表头 例如：姓名 地址
	 * @param columList
	 *            列名 对应map中的key,每一列分别于表头对应
	 * @return
	 */
	public static HSSFWorkbook createWorkBook2003(List<Map<String, Object>> result, String sheetName, List<String> titleList, List<String> columList) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = workBook.createSheet(sheetName);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFFont font = workBook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellStyle = workBook.createCellStyle();// 创建格式
		cellStyle.setFont(font);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		for (int i = 0; i < titleList.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(titleList.get(i).toString());
			cell.setCellStyle(cellStyle);
			sheet.setColumnWidth(i, 5000);
		}

		for (int i = 0; i < result.size(); i++) {
			row = sheet.createRow((int) i + 1);
			Map<String, Object> resultMap = (Map<String, Object>) result.get(i);
			// 第四步，创建单元格，并设置值
			for (int j = 0; j < columList.size(); j++) {
				String columString = columList.get(j).toString();
				row.createCell(j).setCellValue(resultMap.get(columString) == null ? "0" : resultMap.get(columString).toString());
			}
		}

		// // 通过文件输出流生成Excel文件
		// String basePath = "";
		// String exportFileName = "excel.xls";// 导出文件名
		// File file = new File(basePath + exportFileName);
		// FileOutputStream outStream = new FileOutputStream(file);
		// workBook.write(outStream);
		// outStream.flush();
		// outStream.close();
		// System.out.println("Excel 2003文件导出完成！导出文件路径：" + file.getPath());
		//
		// // Web形式输出Excel
		// // 表示以附件的形式把文件发送到客户端
		// response.setHeader("Content-Disposition", "attachment;filename=" +
		// new String((exportFileName).getBytes(), "ISO-8859-1"));// 设定输出文件头
		// response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		// // 定义输出类型
		// // 通过response的输出流把工作薄的流发送浏览器形成文件
		// // OutputStream
		// outStream = response.getOutputStream();
		// workBook.write(outStream);
		// outStream.flush();

		return workBook;
	}
	
	/**
	 * 创建一个2003Excel工作表
	 * @return HSSFWorkbook
	 * @author dongshenglu
	 */
	public static HSSFWorkbook createWorkBook2003New(List<Map<String, Object>> result, String sheetName, List<String> titleList, List<String> columList) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = workBook.createSheet(sheetName);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFFont font = workBook.createFont();
		font.setFontName("微软雅黑");
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short)11);
		HSSFCellStyle cellStyle = workBook.createCellStyle();// 创建格式
		cellStyle.setFont(font);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		for (int i = 0; i < titleList.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(titleList.get(i).toString());
			cell.setCellStyle(cellStyle);
			if(i==0)
			{
				sheet.setColumnWidth(i, 10000);
			}else if(i==2 || i==3){
				sheet.setColumnWidth(i, 8000);
			}else{
				sheet.setColumnWidth(i, 5000);
			}
			
		}

		for (int i = 0; i < result.size(); i++) {
			row = sheet.createRow((int) i + 1);
			Map<String, Object> resultMap = (Map<String, Object>) result.get(i);
			// 第四步，创建单元格，并设置值
			for (int j = 0; j < columList.size(); j++) {
				String columString = columList.get(j).toString();
				HSSFFont fontt = workBook.createFont();
				fontt.setFontName("微软雅黑");
				fontt.setColor(HSSFFont.COLOR_NORMAL);
				fontt.setFontHeightInPoints((short)11);
				HSSFCellStyle cellStylee = workBook.createCellStyle();// 创建格式
				cellStylee.setFont(fontt);
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(cellStylee);
				cell.setCellValue(resultMap.get(columString) == null ? "1" : resultMap.get(columString).toString());
			}
		}
		return workBook;
	}

	/**
	 * 获取一张2007excel工作表
	 * 
	 * @param result
	 *            结果集list (list里边放的是map)
	 * @param sheetName
	 *            工作表名
	 * @param titleList
	 *            表头 例如：姓名 地址
	 * @param columList
	 *            列名 对应map中的key,每一列分别于表头对应
	 * @return
	 */
	public static XSSFWorkbook createWorkBook2007(List result, String sheetName, List titleList, List columList) {
		// 第一步，创建一个webbook，对应一个Excel文件
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = workBook.createSheet();
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		XSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		XSSFFont font = workBook.createFont();
		font.setColor(XSSFFont.COLOR_NORMAL);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		XSSFCellStyle cellStyle = workBook.createCellStyle();// 创建格式
		cellStyle.setFont(font);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		for (int i = 0; i < titleList.size(); i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(titleList.get(i).toString());
			cell.setCellStyle(cellStyle);
			sheet.setColumnWidth(i, 5000);
		}

		for (int i = 0; i < result.size(); i++) {
			row = sheet.createRow((int) i + 1);
			Map resultMap = (Map) result.get(i);
			// 第五步，创建单元格，并设置值
			for (int j = 0; j < columList.size(); j++) {
				String columString = columList.get(j).toString();
				row.createCell(j).setCellValue(resultMap.get(columString) == null ? "" : resultMap.get(columString).toString());
			}
		}

		// // 通过文件输出流生成Excel文件
		// String basePath = "";
		// String exportFileName = "excel.xls";// 导出文件名
		// File file = new File(basePath + exportFileName);
		// FileOutputStream outStream = new FileOutputStream(file);
		// workBook.write(outStream);
		// outStream.flush();
		// outStream.close();
		// System.out.println("Excel 2007文件导出完成！导出文件路径：" + file.getPath());
		// // Web形式输出Excel
		// // 表示以附件的形式把文件发送到客户端
		// response.setHeader("Content-Disposition", "attachment;filename=" +
		// new String((exportFileName).getBytes(), "ISO-8859-1"));// 设定输出文件头
		// response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		// // 定义输出类型
		// // 通过response的输出流把工作薄的流发送浏览器形成文件
		// // OutputStream
		// outStream = response.getOutputStream();
		// workBook.write(outStream);
		// outStream.flush();

		return workBook;
	}
	
	
	/**
	 * 为excel 追加一列
	 * @author wubin
	 * @param path 源文件路径
	 * @param rowValue  行
	 * @param value 值
	 */
	public static void createCellForExcel(String path,int rowValue,int cellValue,String value){
		
		try {
			FileInputStream fs=new FileInputStream(path);  //获取d://test.xls
			POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
			HSSFWorkbook wb=new HSSFWorkbook(ps);  
			HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
			HSSFRow row=sheet.getRow(rowValue);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值
			//System.out.println(sheet.getLastRowNum()+" "+row.getLastCellNum());  //分别得到最后一行的行号，和一条记录的最后一个单元格
			
			FileOutputStream out=new FileOutputStream(path);  //向d://test.xls中写数据
			//row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据
			//row.createCell(0).setCellValue("leilei"); //设置第一个（从0开始）单元格的数据
			
			row.createCell(cellValue).setCellValue(value); //设置值

			out.flush();
			wb.write(out);  
			out.close();  
			//System.out.println(row.getPhysicalNumberOfCells()+" "+row.getLastCellNum()); 
			
		} catch (Exception e) {
			System.out.println("追加失败");
		}

	}
	
	/**
	 * 为excel 追加一列
	 * @author wubin
	 * @param path 源文件路径
	 * @param rowValue  行
	 * @param value 值
	 */
	public static void createCellForExcelWithParam(String path,int rowValue,int cellValue,String value,int param){
		
		try {
			FileInputStream fs=new FileInputStream(path);  //获取d://test.xls
			POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
			HSSFWorkbook wb=new HSSFWorkbook(ps);  
			HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
			HSSFRow row=sheet.getRow(rowValue);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值
			
			HSSFFont font = wb.createFont();
			font.setFontName("微软雅黑");
			font.setColor(HSSFFont.COLOR_NORMAL);
			if(param==1)
			{
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			}
			font.setFontHeightInPoints((short)11);
			HSSFCellStyle cellStyle = wb.createCellStyle();// 创建格式
			cellStyle.setFont(font);
			
			FileOutputStream out=new FileOutputStream(path);  //向d://test.xls中写数据
			HSSFCell cell = row.createCell(cellValue);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(value); //设置值

			out.flush();
			wb.write(out);  
			out.close();  
			
		} catch (Exception e) {
			System.out.println("追加失败");
		}

	}
	
	
	public static String getCell(HSSFCell cell) {
		DecimalFormat df = new DecimalFormat("#");
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(
						HSSFDateUtil.getJavaDate(cell.getNumericCellValue()))
						.toString();
			}
			return df.format(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_STRING:
			System.out.println(cell.getStringCellValue());
			return cell.getStringCellValue();
		case HSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case HSSFCell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue() + "";
		}
		return "";
	}
	
	public static String getDoubleCell(HSSFCell cell) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(
						HSSFDateUtil.getJavaDate(cell.getNumericCellValue()))
						.toString();
			}
			return df.format(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_STRING:
			System.out.println(cell.getStringCellValue());
			return cell.getStringCellValue();
		case HSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case HSSFCell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue() + "";
		}
		return "";
	}
	
}
