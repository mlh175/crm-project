package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Jun 28 23:40:42 CST 2022
     */
    int deleteByPrimaryKey(String id);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Jun 28 23:40:42 CST 2022
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Jun 28 23:40:42 CST 2022
     */
    Activity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Jun 28 23:40:42 CST 2022
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Tue Jun 28 23:40:42 CST 2022
     */
    int updateByPrimaryKey(Activity record);

    /**
     * 保存创建的市场活动
     */
    int insertActivity(Activity activity);

    /**
     * 更具条件查询市场活动的列表
     */
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询市场活动的总条数
     */
    int selectCountOfActivityByCondition(Map<String,Object> map);

    /**
     * 根据ids批量删除市场活动
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids);

    /**
     *根据id查询市场活动的信息
     */
    Activity selectActivityById(String id);

    /**
     * 保存修改的市场活动
     * @param activity
     * @return
     */
    int updateActivity(Activity activity);

    /**
     * 查询所有的市场活动
     * @return
     */
    List<Activity> selectAllActivities();

    /**
     * 批量保存创建的市场活动
     */
    int insertActivityByList(List<Activity> activities);

    /**
     * 根据id查询市场活动的明细信息
     */
    Activity selectActivityForDetailById(String id);

    /**
     * 根据clueId查询该线索相关联的市场活动的明细信息
     * @param clueId
     * @return
     */
    List<Activity> selectActivityForDetailByClueId(String clueId);

    /**
     * 根据name模糊查询市场活动，并且把已经跟clueId关联过的市场活动排除
     * @param map
     * @return
     */
    List<Activity> selectActivityForDetailByNameClueId(Map<String,Object> map);

    /**
     * 根据ids查询市场活动的明细信息
     * @param ids
     * @return
     */
    List<Activity> selectActivityForDetailByIds(String[] ids);

    /**
     * 根据name模糊查询市场活动，并且查询那些跟clueId关联过的数据活动
     * @param map
     * @return
     */
    List<Activity> selectActivityForConvertByNameClueId(Map<String,Object> map);

}