package com.learning.search.commons;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.filechooser.FileSystemView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static final int Excel2003 = 0;

    public static final int Excel2007 = 1;

    /**
     * 根据版本号，获取Excel poi对象
     */
    public static Workbook getWorkbook(int edition, InputStream in) throws IOException {
        if (edition == 0) {
            return new HSSFWorkbook(in);
        } else if (edition == 1) {
            return new XSSFWorkbook(in);
        }
        return null;
    }

    /**
     * 从指定excel表格中逐行读取数据
     */
    public static List<List<String>> getExcelString(Workbook workbook, int startRow, int startCol, int indexSheet) {
        List<List<String>> stringTable = new ArrayList<>();
        // 获取指定表对象
        Sheet sheet = workbook.getSheetAt(indexSheet);
        // 获取最大行数
        int rowNum = sheet.getLastRowNum();
        for (int i = startRow; i <= rowNum; i++) {
            List<String> oneRow = new ArrayList<>();
            Row row = sheet.getRow(i);
            // 根据当前指针所在行数计算最大列数
            int colNum = row.getLastCellNum();
            for (int j = startCol; j <= colNum; j++) {
                // 确定当前单元格
                Cell cell = row.getCell(j);
                String cellValue = null;
                if (cell != null) {
                    // 验证每一个单元格的类型
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            // 表格中返回的数字类型是科学计数法因此不能直接转换成字符串格式
                            cellValue = new BigDecimal(cell.getNumericCellValue()).toPlainString();
                            break;
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        case FORMULA:
                            cellValue = new BigDecimal(cell.getNumericCellValue()).toPlainString();
                            break;
                        case BLANK:
                            cellValue = "";
                            break;
                        case BOOLEAN:
                            cellValue = Boolean.toString(cell.getBooleanCellValue());
                            break;
                        case ERROR:
                            cellValue = "ERROR";
                            break;
                        default:
                            cellValue = "UNDEFINED";
                    }
                } else {
                    cellValue = "";
                }
                // 生成一行数据
                oneRow.add(cellValue);
            }
            stringTable.add(oneRow);
        }
        return stringTable;
    }

    /**
     * 还需要生成列名
     * 根据给定的数据直接生成workbook
     */
    public static Workbook createExcel(Workbook workbook, String sheetName, List<List<String>> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        for (int i = 0; i < data.size(); i++) {
            List<String> oneRow = data.get(i);
            Row row = sheet.createRow(i);
            for (int j = 0; j < oneRow.size(); j++) {
                row.createCell(j).setCellValue(oneRow.get(j));
            }
        }
        return workbook;
    }

    /**
     * 设置指定行的行高
     */
    public static Workbook setRowHeight(Workbook workbook, int rowHight, int sheetIndex, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Row row = sheet.getRow(rowIndex);
        row.setHeight((short) rowHight);
        return workbook;
    }

    /**
     * 设置列宽
     */
    public static Workbook setColumnWidth(Workbook workbook, int columnWidth, int sheetIndex, int columnIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        sheet.setColumnWidth(columnIndex, columnWidth);
        return workbook;
    }

    public static void write(Workbook workbook, String path){
        //如果未传入导出路径，则导出到桌面
        if(org.apache.commons.lang.StringUtils.isBlank(path)) {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            path = fsv.getHomeDirectory().getPath();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            workbook.write(out);//保存Excel文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();//关闭文件流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
