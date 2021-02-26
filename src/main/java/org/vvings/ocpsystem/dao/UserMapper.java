package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.User;
import org.vvings.ocpsystem.pojo.operationUser;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findUserByUserName(String username);


    int deleteByUsername(String username);

    int updateUsernameByUsername(String username, String oldusername);

    List<User> selectAll();


    int updateStatusbyusername(String username, Boolean flag);
}