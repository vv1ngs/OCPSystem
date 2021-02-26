package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.UserToRole;

import java.util.List;

public interface UserToRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserToRole record);

    int insertSelective(UserToRole record);

    UserToRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserToRole record);

    int updateByPrimaryKey(UserToRole record);

    int deleteByuid(Integer integer);



    List<UserToRole> selectByUserID(Integer id);

    List<UserToRole> selectByRoleId(Integer i);
}