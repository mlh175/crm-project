package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author:马立皓
 * @time:20:32 2022/7/17
 */
public interface DicValueService {
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
