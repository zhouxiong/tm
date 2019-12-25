package io.renren.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    /**
     * 读取excel 第1张sheet （xls和xlsx）
     *
     * @param filePath excel路径
     *
     * @author wangliming ,2019-12-24
     * @return
     */
    public List<Map<String, String>> readExcel(String filePath) {
        Sheet sheet = null;
        Row row = null;
        Row rowHeader = null;
        List<Map<String, String>> list = null;
        String cellData = null;
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
            if (wb != null) {
                // 用来存放表中数据
                list = new ArrayList<Map<String, String>>();
                // list = new ArrayList<String>();
                // 获取sheets
                int numberOfSheets = wb.getNumberOfSheets();
                for (int k = 0; k < numberOfSheets; k++) {
                    // 获取第一个sheet
                    sheet = wb.getSheetAt(k);
                    String sheetName = sheet.getSheetName();
                    // 获取最大行数
                    int rownum = sheet.getPhysicalNumberOfRows();
                    // 获取第一行
                    rowHeader = sheet.getRow(0);
                    row = sheet.getRow(0);
                    // 获取最大列数
                    int colnum = row.getPhysicalNumberOfCells();
                    for (int i = 1; i < rownum; i++) {
                        Map<String, String> map = new LinkedHashMap<String, String>();
                        row = sheet.getRow(i);
                        if (row != null) {
                            for (int j = 0; j < colnum; j++) {
                                // if(columns[j].equals(getCellFormatValue(rowHeader.getCell(j)))){
                                cellData = (String) getCellFormatValue(row.getCell(j));
                                map.put(sheetName, cellData);
                                // }
                                // list.add(cellData);
                            }
                        } else {
                            break;
                        }
                        list.add(map);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取单个单元格数据
     *
     * @param cell
     * @return
     * @author lizixiang ,2018-05-08
     */
    public Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        // 数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    // --------------------------------
    // 取值 excel中所有的数据
    public List<List<List>> readExcelColumn(String filePath) {
        Sheet sheet = null;
        Row row = null;
        Row rowHeader = null;
        Map<String, List> map = null;
        Map<String, List> mapSheetName = null;
        List<List<List>> listValue = new ArrayList<List<List>>();

        List<List> listLcssID =  new ArrayList<List>();
        List<List> listLcsrID = new ArrayList<List>();
        List<List> listTableID = new ArrayList<List>();
        String strCurveID = null;
        String strCurveX = null;
        String strCurveY = null;

        String cellData = null;
        String cellData0 = null;
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
            if (wb != null) {
                // 用来存放表中数据
                // 获取sheets
                int numberOfSheets = wb.getNumberOfSheets();
                for (int k = 0; k < numberOfSheets; k++) {
                    mapSheetName = new LinkedHashMap<String, List>();
                    // 获取第一个sheet
                    sheet = wb.getSheetAt(k);
                    String sheetName = sheet.getSheetName();
                    System.out.println("sheetName==="+sheetName);
                    // 获取最大行数
                    int rownum = sheet.getPhysicalNumberOfRows();
                    // 获取第一行曲线ID
                    rowHeader = sheet.getRow(0);
                    row = sheet.getRow(0);
                    // 获取最大列数
                    int colnum = row.getPhysicalNumberOfCells();
                    //获取 lcss 曲线 数据
                    if(sheetName.equals("LCSS")) {
                        for (int i = 1; i < colnum; i++) {
                            strCurveID = (String) getCellFormatValue(sheet.getRow(0).getCell(i));
                            if(!strCurveID.isEmpty()) {
                                //判断带入的曲线的ID 30001是否会出现30001.0的情况 存在就清除小数
                                if (strCurveID.indexOf(".") > -1) {
                                    strCurveID = strCurveID.split("\\.")[0];
                                }
                                for (int j = 2; j < rownum; j++) {
                                    ArrayList<String> listTemp = new ArrayList<String>();//临时数组
                                    strCurveX = (String) getCellFormatValue(sheet.getRow(j).getCell(i - 1));
                                    if (!strCurveX.isEmpty()) {
                                        strCurveY = (String) getCellFormatValue(sheet.getRow(j).getCell(i));
                                        listTemp.add(strCurveID);
                                        listTemp.add(strCurveX);
                                        listTemp.add(strCurveY);
                                        listLcssID.add(listTemp);
                                    }
                                }
                                i++;
                            }
                        }
                    }
                    //获取 lcsR 曲线 数据
                    if(sheetName.equals("LCSR")) {
                        for (int i = 1; i < colnum; i++) {
                            strCurveID = (String) getCellFormatValue(sheet.getRow(0).getCell(i));
                            if(!strCurveID.isEmpty()) {
                                //判断带入的曲线的ID 30001是否会出现30001.0的情况 存在就清除小数
                                if (strCurveID.indexOf(".") > -1) {
                                    strCurveID = strCurveID.split("\\.")[0];
                                }
                                for (int j = 2; j < rownum; j++) {
                                    ArrayList<String> listTemp = new ArrayList<String>();//临时数组
                                    strCurveX = (String) getCellFormatValue(sheet.getRow(j).getCell(i - 1));
                                    if (!strCurveX.isEmpty()) {
                                        strCurveY = (String) getCellFormatValue(sheet.getRow(j).getCell(i));
                                        listTemp.add(strCurveID);
                                        listTemp.add(strCurveX);
                                        listTemp.add(strCurveY);
                                        listLcsrID.add(listTemp);
                                    }
                                }
                                i++;
                            }
                        }
                    }

                    //获取 lcsR 曲线 数据
                    if(sheetName.equals("TABLE")) {
                        for (int i = 1; i < colnum; i++) {
                            strCurveID = (String) getCellFormatValue(sheet.getRow(0).getCell(i));
                            if(!strCurveID.isEmpty()) {
                                //判断带入的曲线的ID 30001是否会出现30001.0的情况 存在就清除小数
                                if (strCurveID.indexOf(".") > -1) {
                                    strCurveID = strCurveID.split("\\.")[0];
                                }
                                for (int j = 2; j < rownum; j++) {
                                    ArrayList<String> listTemp = new ArrayList<String>();//临时数组
                                    strCurveX = (String) getCellFormatValue(sheet.getRow(j).getCell(i - 1));
                                    if (!strCurveX.isEmpty()) {
                                        strCurveY = (String) getCellFormatValue(sheet.getRow(j).getCell(i));
                                        listTemp.add(strCurveID);
                                        listTemp.add(strCurveX);
                                        listTemp.add(strCurveY);
                                        listTableID.add(listTemp);
                                    }
                                }
                                i++;
                            }
                        }
                    }
                }
            }
            listValue.add(listLcssID);
            listValue.add(listLcsrID);
            listValue.add(listTableID);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         System.out.println(listValue);
        return listValue;
    }
}
