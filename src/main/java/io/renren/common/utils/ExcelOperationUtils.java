package io.renren.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelOperationUtils {
	
	private Logger logger=LoggerFactory.getLogger(ExcelOperationUtils.class);
	
	/**
    *
    * @param filePath  需要读取的文件路径
    * @param column  指定需要获取的列数，例如第一列 1
    * @param startRow 指定从第几行开始读取数据
    * @param  endRow 指定结束行
    * @return 返回读取列数据的set
    */
   public static List getColumnSet(String filePath, int column, int startRow , int endRow){
       Workbook wb = readExcel(filePath); //文件
       Sheet sheet = wb.getSheetAt(0); //sheet
       int rownum = sheet.getPhysicalNumberOfRows(); //行数
       Row row = null;
//       HashSet<String> result = new HashSet<>();
//       HashSet<String> set = new HashSet<String>();
       ArrayList list=new ArrayList();
       String cellData = null;
       if(wb != null){
           for (int i=startRow-1; i<rownum; i++){
             
               row = sheet.getRow(i);
               if(row !=null){
                       cellData = (String) getCellFormatValue(row.getCell(column-1));
                      // result.add(cellData.replaceAll(" ", ""));
               }else{
                   break;
               }
               list.add(cellData);
              
           }
       }
       return  list;
   }

   /**
    *
    * @param filePath 需要读取的文件路径
    * @param column 指定需要获取的列数，例如第一列 1
    * @param startRow 指定从第几行开始读取数据
    * @return  返回读取列数据的set
    */
//   public static HashSet<String> getColumnSet(String filePath, int column, int startRow){
//       Workbook wb = readExcel(filePath); //文件
//       Sheet sheet = wb.getSheetAt(0); //sheet
//       int rownum = sheet.getPhysicalNumberOfRows(); //行数
//       System.out.println("sumrows " + rownum);
//
//       return  getColumnSet(filePath, column, startRow , rownum-1);
//   }



   //读取excel
   public static Workbook readExcel(String filePath){
       Workbook wb = null;
       if(filePath==null){
           return null;
       }
       String extString = filePath.substring(filePath.lastIndexOf("."));
       InputStream is = null;
       try {
           is = new FileInputStream(filePath);
           if(".xls".equals(extString)){
               return wb = new HSSFWorkbook(is);
           }else if(".xlsx".equals(extString)){
               return wb = new XSSFWorkbook(is);
           }else{
               return wb = null;
           }
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return wb;
   }

   public static Object getCellFormatValue(Cell cell){
       Object cellValue = null;
       if(cell!=null){
           //判断cell类型
           switch(cell.getCellType()){
               case Cell.CELL_TYPE_NUMERIC:{
                   cell.setCellType(Cell.CELL_TYPE_STRING);  //将数值型cell设置为string型
                   cellValue = cell.getStringCellValue();
                   break;
               }
               case Cell.CELL_TYPE_FORMULA:{
                   //判断cell是否为日期格式
                   if(DateUtil.isCellDateFormatted(cell)){
                    //转换为日期格式YYYY-mm-dd
                       cellValue = cell.getDateCellValue();
                   }else{
                           //数字
                       cellValue = String.valueOf(cell.getNumericCellValue());
                   }
                   break;
               }
               case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
               }
               default:
                   cellValue = "";
           }
       }else{
           cellValue = "";
       }
       return cellValue;
   }
   

}
