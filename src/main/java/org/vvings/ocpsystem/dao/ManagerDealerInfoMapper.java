package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.ManagerDealerInfo;
import org.vvings.ocpsystem.pojo.Role;

import java.util.List;

public interface ManagerDealerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ManagerDealerInfo record);

    int insertSelective(ManagerDealerInfo record);

    ManagerDealerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManagerDealerInfo record);

    int updateByPrimaryKey(ManagerDealerInfo record);

    int checkUserName(String username);

    int checkName(String name);

    List<ManagerDealerInfo> searchRoleByKeyWord(String name, String username, Boolean status);

    List<ManagerDealerInfo> searchRoleByPingYingFirst(String name, String username, Boolean status);

    List<ManagerDealerInfo> searchRoleByPingYing(String name, String username, Boolean status);

    ManagerDealerInfo selectByUsername(String username);
}