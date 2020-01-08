package io.renren.common.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 读取指定列的指定行数excel
 * 
 * @author 70634
 *
 */
public class ExcelReadUtils {
	/**
	 *
	 * @param filePath 需要读取的文件路径
	 * @param column   指定需要获取的列数，例如第一列 1
	 * @param startRow 指定从第几行开始读取数据
	 * @param endRow   指定结束行
	 * @return 返回读取列数据的set
	 */
	public static List<String> getColumnSet(MultipartFile file, int column, int startRow,int endRow,int sheetAt) {
		Workbook wb = readExcel(file); // 文件
		Sheet sheet = wb.getSheetAt(sheetAt); // sheet
		int rownum = sheet.getPhysicalNumberOfRows(); // 行数
		Row row = null;
		List<String> result=new ArrayList<String>();
		String cellData = null;
		if (wb != null) {
			for (int i = startRow - 1; i < rownum; i++) {
//				System.out.println(i);
				row = sheet.getRow(i);
				if (row != null) {
					cellData = (String) getCellFormatValue(row.getCell(column - 1));
					result.add(cellData.replaceAll(" ", ""));
				} else {
					break;
				}
			}
		}
		return result;
	}

	/**
	 *
	 * @param filePath 需要读取的文件路径
	 * @param column   指定需要获取的列数，例如第一列 1
	 * @param startRow 指定从第几行开始读取数据
	 * @return 返回读取列数据的set
	 */
	public static HashSet<String> getColumnSet(MultipartFile filePath, int column, int startRow,int sheetAt) {
		Workbook wb = readExcel(filePath); // 文件
		Sheet sheet = wb.getSheetAt(sheetAt); // sheet
		int rownum = sheet.getPhysicalNumberOfRows(); // 行数

		return getColumnSet(filePath, column, startRow, rownum - 1);
	}

	// 读取excel
	public static Workbook readExcel(MultipartFile filePath) {
		Workbook wb = null;
		if (filePath == null) {
			return null;
		}
		//获得文件名
		String fileName=filePath.getOriginalFilename();
		String extString = fileName.substring(fileName.lastIndexOf("."));
		InputStream is = null;
		try {
			is=filePath.getInputStream();
			if (".xls".equals(extString)) {
				return wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(extString)) {
				return wb = new XSSFWorkbook(is);
			} else {
				return wb = null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	public static Object getCellFormatValue(Cell cell) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df = new DecimalFormat("0");
		Object val = null;
		if (null == cell) {
			return "";
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				val = fmt.format(cell.getDateCellValue()); // 日期型
			} else {
				//val = df.format(cell.getNumericCellValue()); // 数字型
				// 返回数值类型的值
//				Object inputValue = null;// 单元格值Math.round
//				Long longVal = Math.round((cell.getNumericCellValue()));
//				Double doubleVal = cell.getNumericCellValue();
//				if(Double.parseDouble(longVal + ".0") == doubleVal){   //判断是否含有小数位.0
//					inputValue = longVal;
//				}
//				else{
//					inputValue = doubleVal;
//				}
//				DecimalFormat dft = new DecimalFormat("#.######");    //格式化为四位小数，按自己需求选择；
//				val=dft.format(inputValue);
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
	                return getRealStringValueOfDouble(cell.getNumericCellValue());
	            }
	            cell.setCellType(Cell.CELL_TYPE_STRING);
	            return cell.getStringCellValue().trim();
			}
			break;
		case HSSFCell.CELL_TYPE_STRING: // 文本类型
			val = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 公式
			try {
				val = String.valueOf(cell.getStringCellValue());
			} catch (IllegalStateException e) {
				val = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK: // 空
			val = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:// 布尔
			val = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_ERROR:// 错误
			val = "未知数据错误";
			break;
		default:
			val = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
		}
		return val;
	}
	
    private static String getRealStringValueOfDouble(Double d) {
        String doubleStr = d.toString();
        boolean b = doubleStr.contains("E");
        int indexOfPoint = doubleStr.indexOf('.');
        if (b) {
            int indexOfE = doubleStr.indexOf('E');
            BigInteger xs = new BigInteger(doubleStr.substring(indexOfPoint
                    + BigInteger.ONE.intValue(), indexOfE));
            int pow = Integer.valueOf(doubleStr.substring(indexOfE
                    + BigInteger.ONE.intValue()));
            int xsLen = xs.toByteArray().length;
            int scale = xsLen - pow > 0 ? xsLen - pow : 0;
            doubleStr = String.format("%." + scale + "f", d);
        } else {
            java.util.regex.Pattern p = Pattern.compile(".0$");
            java.util.regex.Matcher m = p.matcher(doubleStr);
            if (m.find()) {
                doubleStr = doubleStr.replace(".0", "");
            }
        }
        return doubleStr;
    }
//	public static void main(String[] args) {
//		HashSet<String> excelParamsSet = ExcelReadUtils.getColumnSet("G:\\renren-fast\\材料应力应变曲线-模板_wu_20191219-20191230112039.xlsx", 2, 1,20,0);//读取指定列的指定行数数据
//	}
}
