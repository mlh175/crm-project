package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @author:马立皓
 * @time:21:36 2022/6/28
 */
@Controller
public class ActivityController {
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询所有用户
        List<User> list = userService.queryAllUsers();
        //把数据放在request域中
        request.setAttribute("list",list);
        //请求转发到市场活动的主页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存创建的市场活动
            int i = activityService.saveCreateActivity(activity);
            if (i>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试。。。");
        }
        return returnObject;

    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate
                                                    ,int beginNo,int pageSize){
        //封装对象
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(beginNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service层方法，查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //根据x查询结果信息，生成响应信息
        Map<String,Object> map1=new HashMap<>();
        map1.put("activityList",activityList);
        map1.put("totalRows",totalRows);
        return map1;
    }

    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    @ResponseBody
    public Object deleteActivityIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        //调用service层方法，删除市场活动
        try {
            int ids = activityService.deleteActivityByIds(id);
            if (ids>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
            return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        //调用service层方法，查询市场活动
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装对象
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        activity.setEditBy(user.getId());
        //调用service曾方法，保存修改的市场活动信息
        ReturnObject returnObject = new ReturnObject();
        try {
           int editActivity = activityService.saveEditActivity(activity);
           if (editActivity>0){
               returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
           }else {
               returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
               returnObject.setMessage("系统忙，请稍后重试...");
           }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
            return returnObject;
    }
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        //1.设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");//application/octet-stream: 应用程序产生的二进制文件
        //2.获取输出流
        //PrintWriter writer = response.getWriter();//字符流：只能写文本字符串
        OutputStream out = response.getOutputStream();//字节流：可以输出二进制文件

        //浏览器接收到响应信息之后，默认情况下，直接在显示窗口打开响应信息；即使打不开，也会调用应用程序打开；只有实在打不开，才会激活文件下载窗口。
        //可以设置响应头信息，使浏览器接收到响应的信息后，直接激活文件的下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=myStudentList.xls");

        //读取excel文件（InputStream），把输出到浏览器（OutPutStream）
        InputStream inputStream = new FileInputStream("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\ActivityList.xls");
        byte[] buff=new byte[256];//建立缓存区
        int len=0;
        while ((len=inputStream.read(buff))!=-1) {//每次读一个缓存区,读到buff中
            out.write(buff,0,len);
        }
        //关闭资源：谁开启了，谁关
        inputStream.close();
        out.flush();
    }

        @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws IOException {
        //调用service层方法，查询所有市场活动
        List<Activity> activities = activityService.queryAllActivities();
        //创建exel文件，并且把activityList写入exel文件中
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");

        //遍历activityList，创建HSSFRow对象，生成所有的数据行
        if (activities!=null && activities.size()>0) {
            Activity activity=null;
            for (int i = 0; i < activities.size(); i++) {
                activity=activities.get(i);

                //每遍历一个activity，生成一行
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        //根据wb对象生成excel文件
       /*OutputStream outputStream = new FileOutputStream("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\ActivityList.xls");
        wb.write(outputStream);
        //关闭资源
        outputStream.close();
        wb.close();*/
        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=myStudentList.xls");
        OutputStream out = response.getOutputStream();
       /* InputStream InputStream = new FileInputStream("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\ActivityList.xls");
        byte[] buff=new byte[256];
        int len=0;
        while ((len=InputStream.read(buff))!=-1){
            out.write(buff,0,len);
        }
        InputStream.close();*/
        wb.write(out);
        wb.close();
        out.flush();
    }

    /**
     * 配置springMVC的文件上传解析器
     */
    @RequestMapping("/workbench/activity/fileUpload.do")
        @ResponseBody
        public Object fileUpload(String userName, MultipartFile myFile) throws IOException {
            //把文本数据打印到控制台
        System.out.println("username:"+userName);
        //把文件在服务指定的目录中生成同一个同样的文件
        String filename = myFile.getOriginalFilename();
        File file = new File("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\"+filename);
        myFile.transferTo(file);
        //返回响应信息
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMessage("成功");
        return returnObject;
    }
        @RequestMapping("/workbench/activity/importActivity.do")
        @ResponseBody
        public Object importActivity(MultipartFile activityFile,HttpSession session) {
            User user = (User) session.getAttribute(Contants.SESSION_USER);
            ReturnObject returnObject = new ReturnObject();
            //把excel文件写到写道磁盘目录中
            try {
               /*String filename = activityFile.getOriginalFilename();
                File file = new File("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\" + filename);
                activityFile.transferTo(file);*/

                //根据指定的excel文件生成HSSFWorkbook对象，封装多有的excel文件的所有信息
                //InputStream is = new FileInputStream("D:\\Project-packages\\Powernode-Bstationresources\\笔记（SSM版CRM项目）\\CRM项目（SSM框架版）(1)\\serverDir\\" + filename);

                InputStream inputStream = activityFile.getInputStream();

                HSSFWorkbook wb = new HSSFWorkbook(inputStream);
                //根据wb获取HSSFSheet对象，封装一页的信息
                HSSFSheet sheetAt = wb.getSheetAt(0);//页的下标，从0开始，依次递增
                //根据sheetAt获取HSSFRow对象，封装了一行的信息
                HSSFRow row = null;
                HSSFCell cell = null;
                Activity activity = null;
                List<Activity> activityList=new ArrayList<>();
                for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {//sheetAt.getLastRowNum() 最后一行的下标
                    row = sheetAt.getRow(i);//行的下标，从0开始，依次递增
                    activity = new Activity();
                    activity.setId(UUIDUtils.getUUID());
                    activity.setOwner(user.getId());
                    activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                    activity.setCreateBy(user.getId());
                    for (int j = 0; j < row.getLastCellNum(); j++) {// row.getLastCellNum() 最后一列的下标+1
                        //根据row获取HSSFCell对象，封装了一列的所有信息
                        cell = row.getCell(j);//列的下标，下标从0开始，依次递增

                        //获取列中的数据
                        String value = HSSFUtils.getCellValueForStr(cell);
                        if (j == 0) {
                            activity.setName(value);
                        } else if (j == 1) {
                            activity.setStartDate(value);
                        } else if (j == 2) {
                            activity.setEndDate(value);
                        } else if (j == 3) {
                            activity.setCost(value);
                        } else if (j == 4) {
                            activity.setDescription(value);
                        }
                    }
                    //每一行的所有的列都封装完成后，把activity保存到list中
                    activityList.add(activity);

                }

                //调用service层方法，保存市场活动
                int ret = activityService.saveCreateActivityByList(activityList);
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(ret);
            } catch (IOException e) {

                e.printStackTrace();
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙...");
            }
            return returnObject;
        }

        @RequestMapping("workbench/activity/detailActivity.do")
        public String detailActivity(String id,HttpServletRequest request){
        //调用service层方法，查询数据
            Activity activity = activityService.queryActivityForDetailById(id);
            List<ActivityRemark> activityRemarks = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
            //把数据保存到作用域中
            request.setAttribute("activity",activity);
            request.setAttribute("activityRemarks",activityRemarks);

            //跳转到：请求转发
            return "workbench/activity/detail";
        }

}
