package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.Role;

import java.util.List;
import java.util.Set;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    int selectName(String name);

    List<Role> selectAll();

    int checkRoleId(Integer rid);

    List<Role> searchRoleByKeyWord(String username, Boolean status);

    List<Role> searchRoleByPingYingFirst(String username, Boolean status);

    List<Role> searchRoleByPingYing(String username, Boolean status);


    Role selectByRoleId(Integer roleId);
}