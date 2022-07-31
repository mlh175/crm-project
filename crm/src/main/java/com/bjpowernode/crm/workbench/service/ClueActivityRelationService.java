package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

/**
 * @author:马立皓
 * @time:15:01 2022/7/18
 */
public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);


    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
