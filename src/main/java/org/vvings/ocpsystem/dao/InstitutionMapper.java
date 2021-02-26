package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.Institution;

import java.util.List;

public interface InstitutionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Institution record);

    int insertSelective(Institution record);

    Institution selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Institution record);

    int updateByPrimaryKey(Institution record);

    List<Institution> selectByChildrenAndParentId(Integer institutionId);
}