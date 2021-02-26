package org.vvings.ocpsystem.service;

import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.Authority;
import org.vvings.ocpsystem.pojo.Role;
import org.vvings.ocpsystem.pojo.User;

import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/18 22:43
 * @Version 1.0
 */
public interface roleService {
    ServerResponse<String> addRole(Role role);

    ServerResponse<List<Role>> gerAllRole();

    boolean delUserByuid(Integer integer);

    boolean updateStatus(Integer integer, Boolean aBoolean);

    ServerResponse updateRole(Role role);

    ServerResponse<Set<Role>> searchRole(String username, Boolean status);

    ServerResponse<List<Authority>> getAllAuthority();

    ServerResponse<List<Authority>> getAssociatedAuthority(String username);

    boolean removeAllAuthority(Integer[] uid);

    boolean associatedAuthority(Integer rid, Integer authorityId);

    ServerResponse<Set<Authority>> getAllAuthorityBy(Integer rid);

    Set<User> getAllUserByRoleId(Integer[] rid);

    ServerResponse<List<Role>> getRoleByUid(Integer id);
}
