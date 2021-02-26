package org.vvings.ocpsystem.service;


import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.ManagerDealerInfo;
import org.vvings.ocpsystem.pojo.Role;
import org.vvings.ocpsystem.pojo.User;
import org.vvings.ocpsystem.pojo.operationUser;

import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/18 20:02
 * @Version 1.0
 */

public interface userService {


    ServerResponse<String> addUser(operationUser user,Integer rid);


    boolean delUserByuid(Integer i);

    boolean updateStatus(Integer integer,boolean flag);

    ServerResponse<String> updateUser(operationUser user);

    boolean allocationRole(Integer uid, Integer integer);

    ServerResponse<Set<operationUser>> searchUser(String username, Boolean status);


    boolean removeRole(Integer[] uid);

    ServerResponse<List<operationUser>> getAllUser();

    Set<User> getAllOnlineUser(Integer[] uid);


}
