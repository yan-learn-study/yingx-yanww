package com.baizhi;

import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/26 10:23
 */
@SpringBootTest
public class ApachePOI {
    @Autowired
    private LogMapper logMapper;

    @Test
    void test() {
        Workbook sheets = new HSSFWorkbook();
        Sheet sheet = sheets.createSheet("1学生信息表");
        //Sheet sheet1 = sheets.createSheet("2学生成绩表");

        //创建一行   参数：行下标（下标从0开始）
        Row row = sheet.createRow(1);

        //创建单元格   参数:单元格下标（下标从0开始）
        Cell cell = row.createCell(3);

        //给单元格设置内容
        cell.setCellValue("这是第二行第4个单元格");

        try {
            //导出Excel文档
            sheets.write(new FileOutputStream(new File("D://2005Poi.xls")));

            //释放资源
            sheets.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testExport() {
        //获取数据
        List<Log> logs = logMapper.selectAll();
        //创建xls文件
        Workbook sheets = new HSSFWorkbook();
        //创建工作空间
        Sheet sheet = sheets.createSheet("日志信息表");
        //创建标题行
        CellRangeAddress cellAddresses = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(cellAddresses);
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("日志信息");
        //创建表头行
        Row headRow = sheet.createRow(1);
        String[] heads = {"日志Id", "操作人", "操作时间", "执行操作", "状态"};
        //表头行样式
        CellStyle headCellStyle = sheets.createCellStyle();
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(headCellStyle);
        //数据行样式
        CellStyle dateCellStyle = sheets.createCellStyle();
        //创建日期对象
        DataFormat dataFormat = sheets.createDataFormat();
        //设置日期格式
        dateCellStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //向表头行写入数据
        for (int i = 0; i < heads.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(headCellStyle);
        }
        //处理数据行
        for (int i = 0; i < logs.size(); i++) {
            //遍历一次创建一行
            Row row = sheet.createRow(i + 2);
            row.setRowStyle(headCellStyle);
            //每行对应的数据
            Cell cell1 = row.createCell(0);
            cell1.setCellStyle(headCellStyle);
            cell1.setCellValue(logs.get(i).getId());
            Cell cell2 = row.createCell(1);
            cell2.setCellStyle(headCellStyle);
            cell2.setCellValue(logs.get(i).getUsername());
            LocalDateTime optionTime = logs.get(i).getOptionTime();
            Date date = Date.from(optionTime.atZone(ZoneId.systemDefault()).toInstant());
            Cell cell = row.createCell(2);
            cell.setCellStyle(dateCellStyle);
            cell.setCellValue(date);
            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(headCellStyle);
            cell3.setCellValue(logs.get(i).getOptions());
            Cell cell4 = row.createCell(4);
            cell4.setCellStyle(headCellStyle);
            cell4.setCellValue(logs.get(i).getStatus());
        }
        try {
            sheets.write(new FileOutputStream(new File("E:/日志信息.xls")));
            sheets.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testImport() {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File("E:/日志信息.xls")));
            HSSFSheet sheet = workbook.getSheet("日志信息表");
            ArrayList<Log> logs = new ArrayList<>();
            for (int i = 2; i < sheet.getLastRowNum(); i++) {
                Log log = new Log();
                HSSFRow row = sheet.getRow(i);
                log.setId(row.getCell(0).getStringCellValue());
                log.setUsername(row.getCell(1).getStringCellValue());
                HSSFCell cell = row.getCell(2);
                cell.setCellType(CellType.STRING);
                long time = new SimpleDateFormat("yyyy-MM-dd").parse(cell.getStringCellValue()).getTime();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), zoneId);
                log.setOptionTime(localDateTime);
                log.setOptions(row.getCell(3).getStringCellValue());
                log.setStatus(row.getCell(4).getStringCellValue());
                logs.add(log);
            }
            logs.forEach(log -> System.out.println(log));
            workbook.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
