package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:马立皓
 * @time:0:47 2022/7/18
 */
@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvertClue(Map<String, Object> map) {
        String clueId = (String) map.get("clueId");
        String isCreateTran = (String) map.get("isCreateTran");
        User user = (User) map.get(Contants.SESSION_USER);
        //根据id查询线索的信息
        Clue clue = clueMapper.selectClueById(clueId);
        //把线索中有关公司的信息转换到客户表中
        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(customer);
        //把该线索中有关个人的信息转换到联系人中
        Contacts contacts=new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
         contactsMapper.insertContacts(contacts);

         //根据clueId查询该线索下所有的备注
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //把该线索下的所有备注转换成客户备注表中的一份,把该线索下所有的备注转换成联系人备注表中的一份
        if (clueRemarkList!=null&&clueRemarkList.size()>0){
            //遍历clueRemarkList，封装客户表
            CustomerRemark cur=null;
            ContactsRemark cor=null;
            List<CustomerRemark> curList=new ArrayList<>();
            List<ContactsRemark> corList=new ArrayList<>();
            for (ClueRemark cr:clueRemarkList){
               cur= new CustomerRemark();
               cur.setCreateBy(cr.getCreateBy());
               cur.setCreateTime(cr.getCreateTime());
               cur.setCustomerId(customer.getId());
               cur.setEditBy(cr.getEditBy());
               cur.setEditTime(cr.getEditTime());
               cur.setEditFlag(cr.getEditFlag());
               cur.setId(UUIDUtils.getUUID());
               cur.setNoteContent(cr.getNoteContent());
               curList.add(cur);

              cor= new ContactsRemark();
              cor.setContactsId(contacts.getId());
              cor.setCreateBy(cr.getCreateBy());
              cor.setCreateTime(cr.getCreateTime());
              cor.setEditBy(cr.getEditBy());
              cor.setEditFlag(cr.getEditFlag());
              cor.setEditTime(cr.getEditTime());
              cor.setId(UUIDUtils.getUUID());
              cor.setNoteContent(cr.getNoteContent());
              corList.add(cor);
            }

            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }
            //根据clueId查询该线索和市场活动的关联关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        //把该线索和市场活动的关联关系转换为联系人和市场活动的关联关系表中
        if (carList!=null&&carList.size()>0){
            ContactsActivityRelation coar=null;
            List<ContactsActivityRelation> coarList=new ArrayList<>();
            for (ClueActivityRelation car:carList){
                 coar = new ContactsActivityRelation();
                 coar.setActivityId(car.getActivityId());
                 coar.setContactsId(contacts.getId());
                 coar.setId(UUIDUtils.getUUID());
                 coarList.add(coar);
            }
            contactsActivityRelationMapper.insertContactActivityRelationByList(coarList);
        }
        //如果需要创建交易，则往交易表中添加交易，吧该线索下的备注转换到交易备注表中一份
        if ("true".equals(isCreateTran)){
            String activityId = (String) map.get("activityId");
            Tran tran=new Tran();
            tran.setActivityId(activityId);
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formateDateTime(new Date()));
            tran.setCustomerId(customer.getId());
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("Stage"));
            tranMapper.insertTran(tran);

            if (clueRemarkList!=null&&clueRemarkList.size()>0){
                TranRemark tr=null;
                List<TranRemark> trList=new ArrayList<>();
                for (ClueRemark cr:clueRemarkList){
                    tr=new TranRemark();
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());
                    tr.setEditBy(cr.getEditBy());
                    tr.setEditFlag(cr.getEditFlag());
                    tr.setEditTime(cr.getEditTime());
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setTranId(tran.getId());
                    trList.add(tr);
                }
                tranRemarkMapper.insertTranRemarkByList(trList);
            }

        }
            //删除该线索下所有的备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //删除该线索和市场活动的关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //删除线索
        clueMapper.deleteClueById(clueId);
    }
}
