package com.gaia.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ExcelData {

    public static List<List<String>> readExcelByInputStream(InputStream inputStream) throws IOException {
        List<List<String>> result = new ArrayList<>();
        // 读取整个Excel
        XSSFWorkbook sheets = new XSSFWorkbook(inputStream);
        // 获取第一个表单Sheet
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        // 循环获取每一行数据
        for (int i = 0; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            List<String> itemString = new ArrayList<>();
            XSSFRow row = sheetAt.getRow(i);
            // 读取每一格内容
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                XSSFCell cell = row.getCell(index);
                itemString.add(getStringVal(cell));
            }
            result.add(itemString);
        }
        return result;
    }

    private static String getStringVal(XSSFCell cell) {
        // 判断文件类型
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "true" : "false";
            case FORMULA:
                String value ;
                try {
                    value = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    value = String.valueOf(cell.getRichStringCellValue());
                }
                return value;
            case NUMERIC:
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return "";
        }
    }

    public static ArrayList<ArrayList<String>> CSV2Array(String path)
    {
        try {
            BufferedReader in =new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
            ArrayList<ArrayList<String>> all=new ArrayList<ArrayList<String>>();
            String line;
            String[] onerow;
            while ((line=in.readLine())!=null) {
                onerow = line.split(",");  //默认分割符为逗号，可以不使用逗号
                List<String> onerowlist = Arrays.asList(onerow);
                ArrayList<String> onerowaArrayList = new ArrayList<String>(onerowlist);
                all.add(onerowaArrayList);
            }
            in.close();
            return all;
        } catch (Exception e) {
            return null;
        }

    }

}

