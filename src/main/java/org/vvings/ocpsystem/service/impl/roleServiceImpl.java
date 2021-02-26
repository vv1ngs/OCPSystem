package org.vvings.ocpsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.*;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.roleService;
import org.vvings.ocpsystem.validator.ValidationResult;
import org.vvings.ocpsystem.validator.ValidatorImpl;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Objects.hash;

/**
 * @Author vvings
 * @Date 2020/12/18 22:44
 * @Version 1.0
 */
@Service
public class roleServiceImpl  implements roleService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    private UserToRoleMapper userToRoleMapper;
    @Override
    public ServerResponse<String> addRole(Role role) {
        ValidationResult result=validator.validate(role);
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        if (result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), result.getErrMsg());
        }
        if (roleMapper.selectName(role.getName())>0){
            return ServerResponse.createByErrorCode(ResponseCode.NAME_SAME.getCode(),ResponseCode.NAME_SAME.getDesc());
        }
        if (roleMapper.insertSelective(role)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("新增角色成功");

    }

    @Override
    public ServerResponse<List<Role>> gerAllRole() {
        return ServerResponse.createBySuccess(roleMapper.selectAll());
    }

    @Override
    public boolean delUserByuid(Integer integer) {
        if (roleMapper.deleteByPrimaryKey(integer)==1){
            return true;
        }
        else return false;
    }

    @Override
    public boolean updateStatus(Integer integer, Boolean aBoolean) {
        Role role=new Role();
        role.setId(integer);
        role.setStatus(aBoolean);
        if (roleMapper.updateByPrimaryKeySelective(role)==1){
            return true;
        }
        return false;
    }

    @Override
    public ServerResponse updateRole(Role role) {
        ValidationResult result=validator.validate(role);
        role.setUpdateTime(new Date());
        if (role.getId()==null||result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(), result.getErrMsg());
        }
        if (roleMapper.updateByPrimaryKeySelective(role)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(), ResponseCode.UPDATE_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("更新角色成功");
    }

    @Override
    public ServerResponse<Set<Role>> searchRole(String username, Boolean status) {
        Set<Role> set=new HashSet<>();
        List<Role> roles=roleMapper.searchRoleByKeyWord(username,status);
        roles.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        roles=roleMapper.searchRoleByPingYingFirst(username,status);
        roles.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        roles=roleMapper.searchRoleByPingYing(username,status);
        roles.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        return ServerResponse.createBySuccess(set);
    }

    @Override
    public ServerResponse<List<Authority>> getAllAuthority() {
        return ServerResponse.createBySuccess(authorityMapper.getAll());
    }

    @Override
    public ServerResponse<List<Authority>> getAssociatedAuthority(String username) {
        User user= userMapper.findUserByUserName(username);
        if (user==null||user.getCategoryId()!=0){
            return ServerResponse.createByError();
        }
        return null;
    }

    @Override
    public boolean removeAllAuthority(Integer[] rid) {

        Arrays.stream(rid).forEach(i -> roleAuthorityMapper.deleteByRoleId(i));
        return true;
    }

    @Override
    public boolean associatedAuthority(Integer rid, Integer authorityId) {
        RoleAuthority roleAuthority=new RoleAuthority();
        roleAuthority.setAuthorityId(authorityId);
        roleAuthority.setRoleId(rid);
        if (roleAuthorityMapper.insert(roleAuthority)!=1){
            return false;
        }
        return true;
    }

    @Override
    public ServerResponse<Set<Authority>> getAllAuthorityBy(Integer rid) {
        List<RoleAuthority> roleAuthorities=roleAuthorityMapper.selectByRoleId(rid);
        Set<Authority> authorities=new HashSet<>();
        roleAuthorities.forEach(i ->authorities.add(authorityMapper.selectByPrimaryKey(i.getAuthorityId())));
        return ServerResponse.createBySuccess(authorities);
    }

    @Override
    public Set<User> getAllUserByRoleId(Integer[] rid) {
        Set <User> users=new HashSet<>();
        Set<UserToRole> userToRoles=new HashSet<>();
        Arrays.stream(rid).forEach(i -> userToRoles.addAll(userToRoleMapper.selectByRoleId(i)));
        userToRoles.forEach(i-> users.add(userMapper.selectByPrimaryKey(i.getUid())));
        return users;
    }

    @Override
    public ServerResponse<List<Role>> getRoleByUid(Integer id) {
        List<UserToRole> userToRoles=userToRoleMapper.selectByUserID(id);
        List<Role> list=new ArrayList<>();
        userToRoles.forEach(i ->list.add(roleMapper.selectByPrimaryKey(i.getRoleId())));
        return ServerResponse.createBySuccess(list);
    }

}
