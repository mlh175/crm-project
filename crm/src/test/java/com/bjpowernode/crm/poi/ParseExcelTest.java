package com.bjpowernode.crm.poi;

import com.bjpowernode.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *使用apache-poi解析excel文件
 */
public class ParseExcelTest {
    public static void main(String[] args) throws IOException {
        //根据指定的excel文件生成HSSFWorkbook对象，封装多有的excel文件的所有信息
        InputStream is= new FileInputStream("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\aaa.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        //根据wb获取HSSFSheet对象，封装一页的信息
        HSSFSheet sheetAt = wb.getSheetAt(0);//页的下标，从0开始，依次递增
        //根据sheetAt获取HSSFRow对象，封装了一行的信息
        HSSFRow row=null;
        HSSFCell cell=null;
        for (int i = 0; i <=sheetAt.getLastRowNum(); i++) {//sheetAt.getLastRowNum() 最后一行的下标
            row = sheetAt.getRow(i);//行的下标，从0开始，依次递增
            for (int j = 0; j < row.getLastCellNum(); j++) {// row.getLastCellNum() 最后一列的下标+1
                //根据row获取HSSFCell对象，封装了一列的所有信息
                cell=row.getCell(j);//列的下标，下标从0开始，依次递增
                //获取列中的数据
                System.out.print(HSSFUtils.getCellValueForStr(cell)+" ");
            }
            //每一行中所有列都打完，打印换行
            System.out.println();

        }

    }

}
