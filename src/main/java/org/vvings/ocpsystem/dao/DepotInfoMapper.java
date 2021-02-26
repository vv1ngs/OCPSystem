package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.DepotInfo;

public interface DepotInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepotInfo record);

    int insertSelective(DepotInfo record);

    DepotInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepotInfo record);

    int updateByPrimaryKey(DepotInfo record);
}