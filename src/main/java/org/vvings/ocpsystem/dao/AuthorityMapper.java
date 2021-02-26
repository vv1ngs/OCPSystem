package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.Authority;

import java.util.List;

public interface AuthorityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Authority record);

    int insertSelective(Authority record);

    Authority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Authority record);

    int updateByPrimaryKey(Authority record);

    Authority selectByRoleId(Integer id);

    List<Authority> getAll();

}