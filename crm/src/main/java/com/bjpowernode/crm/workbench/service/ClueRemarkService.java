package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

/**
 * @author:马立皓
 * @time:9:16 2022/7/18
 */
public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByCueId(String clueId);
}
