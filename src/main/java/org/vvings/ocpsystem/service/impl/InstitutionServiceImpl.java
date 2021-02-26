package org.vvings.ocpsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.InstitutionMapper;
import org.vvings.ocpsystem.pojo.Institution;
import org.vvings.ocpsystem.service.InstitutionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/18 21:30
 * @Version 1.0
 */
@Service
public class InstitutionServiceImpl implements InstitutionService {
    @Autowired
    private InstitutionMapper institutionMapper;
    @Override
    public ServerResponse<List<Institution>> getChildrenParallelInstitution(Integer institutionId) {
        List<Institution> institutionList=institutionMapper.selectByChildrenAndParentId(institutionId);
        return ServerResponse.createBySuccess(institutionList);
    }

    @Override
    public ServerResponse<List<Integer>> getInstitutionAndChildren(Integer institutionId) {
        Set<Institution> institutionSet = new HashSet();
        findChildCategory(institutionSet,institutionId);


        List<Integer> institutionList = new ArrayList<>();
        if(institutionId != null){
            for(Institution institutionItem : institutionSet){
                institutionList.add(institutionItem.getId());
            }
        }
        return ServerResponse.createBySuccess(institutionList);
    }
    private Set<Institution> findChildCategory(Set<Institution> institutionSet ,Integer institutionId){
        Institution institution = institutionMapper.selectByPrimaryKey(institutionId);
        if(institution != null){
            institutionSet.add(institution);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Institution> institutionList = institutionMapper.selectByChildrenAndParentId(institutionId);
        for(Institution institutionItem : institutionList){
            findChildCategory(institutionSet,institutionItem.getId());
        }
        return institutionSet;
    }
}
