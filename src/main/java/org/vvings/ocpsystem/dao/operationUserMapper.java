package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.Role;
import org.vvings.ocpsystem.pojo.operationUser;

import java.util.List;

public interface operationUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(operationUser record);

    int insertSelective(operationUser record);

    operationUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(operationUser record);

    int updateByPrimaryKey(operationUser record);

    operationUser selectByUsernameAndPassword(String username, String password);

    int selectByusername(String username);

    List<operationUser> searchUserByKeyWord(String username, Boolean status);

    List<operationUser> searchUserByPingYingFirst(String username, Boolean status);

    List<operationUser> searchUserByPingYing(String username, Boolean status);

    List<operationUser> selectAll();
}