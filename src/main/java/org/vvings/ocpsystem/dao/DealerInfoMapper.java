package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.DealerInfo;

import java.util.List;

public interface DealerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DealerInfo record);

    int insertSelective(DealerInfo record);

    DealerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DealerInfo record);

    int updateByPrimaryKey(DealerInfo record);

    List<DealerInfo> selectAll();

    List<DealerInfo> selectAllAssociatedBymid(Integer mid);

    List<DealerInfo> searchRoleByKeyWord(String name, int category, Integer status);

    List<DealerInfo> searchRoleByPingYingFirst(String name, int category, Integer status);

    List<DealerInfo> searchRoleByPingYing(String name, int category, Integer status);

    List<DealerInfo> selectByManageId(Integer id);

    List<DealerInfo> searchDealerByKeyWord(String name, String dealerId);

    List<DealerInfo> searchDealerByPingYingFirst(String name, String dealerId);

    List<DealerInfo> searchDealerByPingYing(String name, String dealerId);
}