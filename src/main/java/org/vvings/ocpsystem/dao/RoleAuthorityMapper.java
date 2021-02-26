package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.RoleAuthority;

import java.util.List;

public interface RoleAuthorityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleAuthority record);

    int insertSelective(RoleAuthority record);

    RoleAuthority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleAuthority record);

    int updateByPrimaryKey(RoleAuthority record);

    List<RoleAuthority> selectByRoleId(Integer id);

    int deleteByRoleId(Integer i);
}