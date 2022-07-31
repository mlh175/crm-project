package com.bjpowernode.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author:马立皓
 * @time:16:09 2022/7/13
 */
public class CreatExcelTest {
    public static void main(String[] args) throws IOException {
        //创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //是使用wb创建的HSSFSheet对象，对应wb文件中的一页
        HSSFSheet sheet = wb.createSheet("学生列表");
        //使用sheet创建HSSFRow对象，对应sheet中的一行
        HSSFRow row = sheet.createRow(0);//行号：从0开始，依次增加
        //使用row创建HSSFCell对象，对应row中的列
        HSSFCell cell = row.createCell(0);//列的编号：从0开始，依次增加
        cell.setCellValue("学号");
        cell=row.createCell(1);
        cell.setCellValue("姓名");
        cell=row.createCell(2);
        cell.setCellValue("年龄");

        //生成HSSFCellStyle样式对象
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //使用sheet创建10个HSSFRow对象，对应sheet中的10行
        for (int i = 1; i < 10; i++) {
             row = sheet.createRow(i);

            cell = row.createCell(0);//列的编号：
            cell.setCellValue(100+i);
            cell=row.createCell(1);
            cell.setCellValue("姓名"+i);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(20+i);
        }
        //调用工具函数生成excel文件
        //目录必须手动创建，文件如果不存在，poi会自动生成
        FileOutputStream os = new FileOutputStream("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\studentList.xls");
        wb.write(os);

        //关闭资源
        os.close();
        wb.close();

        System.out.println("======creat ok===========");
    }
}
