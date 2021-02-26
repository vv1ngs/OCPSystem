package org.vvings.ocpsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.*;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.userService;
import org.vvings.ocpsystem.util.MD5Util;
import org.vvings.ocpsystem.validator.ValidationResult;
import org.vvings.ocpsystem.validator.ValidatorImpl;

import java.util.*;

/**
 * @Author vvings
 * @Date 2020/12/18 20:07
 * @Version 1.0
 */
@Service
public class userServiceImpl implements userService {
    @Autowired
    private operationUserMapper operationUserMapper;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserToRoleMapper userToRoleMapper;
    @Autowired
    private ManagerDealerInfoMapper managerDealerInfoMapper;
    @Autowired
    private DealerInfoMapper dealerInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ServerResponse<String> addUser(operationUser user,Integer rid) {
        ValidationResult result=validator.validate(user);
        user.setInstitutionId(0);
        if (result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), result.getErrMsg());
        }
        if (rid==null){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), "角色不能为空");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8("123456"));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (operationUserMapper.selectByusername(user.getUsername())>0){
            return ServerResponse.createByErrorCode(ResponseCode.NAME_SAME.getCode(),ResponseCode.NAME_SAME.getDesc());
        }
        if (roleMapper.checkRoleId(rid)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        if (operationUserMapper.insertSelective(user)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }

        User newuser=new User();
        newuser.setPassword(user.getPassword());
        newuser.setUsername(user.getUsername());
        newuser.setRoleId(rid);
        newuser.setCategoryId(0);
        newuser.setStatus(true);
        if (userMapper.insert(newuser)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }

        UserToRole userToRole=new UserToRole();
        userToRole.setUid(newuser.getId());
        userToRole.setRoleId(rid);
        if (userToRoleMapper.insert(userToRole)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("新增成功运营账号成功");
    }

    @Override
    public boolean delUserByuid(Integer i) {
        operationUser operationUser=operationUserMapper.selectByPrimaryKey(i);
        if (operationUser==null){
            return false;
        }
        User user=userMapper.findUserByUserName(operationUser.getUsername());
        if (user==null)
            return false;
        if (userMapper.deleteByUsername(operationUser.getUsername())!=1){
            return false;
        }
        if (operationUserMapper.deleteByPrimaryKey(i)!=1){
            return false;
        }
        userToRoleMapper.deleteByuid(user.getId());
        return true;
    }

    @Override
    public boolean updateStatus(Integer integer,boolean flag) {
        operationUser operationUser=new operationUser();
        operationUser.setStatus(flag);
        operationUser.setId(integer);
        if (operationUserMapper.updateByPrimaryKeySelective(operationUser)==1){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ServerResponse<String> updateUser(operationUser user) {
        ValidationResult result=validator.validate(user);
        user.setPassword(MD5Util.MD5EncodeUtf8("123456"));
        user.setUpdateTime(new Date());
        if (user.getId()==null||result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), result.getErrMsg());
        }
        operationUser operationUser=operationUserMapper.selectByPrimaryKey(user.getId());
        if (operationUser==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (!operationUser.getUsername().equals(user.getUsername())){
            if (operationUserMapper.selectByusername(user.getUsername())>0){
                return ServerResponse.createByErrorCode(ResponseCode.NAME_SAME.getCode(),ResponseCode.NAME_SAME.getDesc());
            }
            userMapper.updateUsernameByUsername(user.getUsername(),operationUser.getUsername());
        }
        if (operationUserMapper.updateByPrimaryKeySelective(user)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(),ResponseCode.UPDATE_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("修改成功");
    }

    @Override
    public boolean allocationRole(Integer uid, Integer integer) {
        operationUser operationUser=operationUserMapper.selectByPrimaryKey(uid);
        if(operationUser==null){
            return false;
        }
        User user=userMapper.findUserByUserName(operationUser.getUsername());
        if (user==null){
            return false;
        }
        UserToRole userToRole=new UserToRole();
        userToRole.setRoleId(integer);
        userToRole.setUid(user.getId());
        if (userToRoleMapper.insert(userToRole)==1){
            return true;
        }
        return false;
    }

    @Override
    public ServerResponse<Set<operationUser>> searchUser(String username, Boolean status) {
        Set<operationUser> set=new HashSet<>();
        List<operationUser> roles=operationUserMapper.searchUserByKeyWord(username,status);
        roles.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        roles=operationUserMapper.searchUserByPingYingFirst(username,status);
        roles.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        roles=operationUserMapper.searchUserByPingYing(username,status);
        roles.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        set.forEach(n->n.setPassword(null));
        return ServerResponse.createBySuccess(set);
    }

    @Override
    public boolean removeRole(Integer[] uid) {
        List<Integer> list= Arrays.asList(uid);
        List<operationUser> operationUsers= new ArrayList<>();
        list.forEach(i ->operationUsers.add(operationUserMapper.selectByPrimaryKey(i)));
        List<User> users=new ArrayList<>();
        operationUsers.forEach(i -> users.add(userMapper.findUserByUserName(i.getUsername())));
        users.forEach(user -> userToRoleMapper.deleteByuid(user.getId()) );

        return true;
    }

    @Override
    public ServerResponse<List<operationUser>> getAllUser() {
        return ServerResponse.createBySuccess(operationUserMapper.selectAll());
    }

    @Override
    public Set<User> getAllOnlineUser(Integer[] uid) {
        Set<User> users=new HashSet<>();
        List<operationUser> list=new ArrayList<>();
        Arrays.stream(uid).forEach(i ->list.add(operationUserMapper.selectByPrimaryKey(i)) );
         list.forEach(i -> users.add(userMapper.findUserByUserName(i.getUsername())));
         return users;
    }




}
