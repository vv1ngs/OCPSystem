package org.vvings.ocpsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.*;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.manageDealerService;
import org.vvings.ocpsystem.util.MD5Util;
import org.vvings.ocpsystem.validator.ValidationResult;
import org.vvings.ocpsystem.validator.ValidatorImpl;

import java.util.*;

/**
 * @Author vvings
 * @Date 2020/12/19 22:57
 * @Version 1.0
 */
@Service
public class manageDealerServiceImpl implements manageDealerService {
    @Autowired
    private DealerInfoMapper dealerInfoMapper;
    @Autowired
    private ManagerDealerInfoMapper managerDealerInfoMapper;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserToRoleMapper userToRoleMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> addManageDealer(ManagerDealerInfo managerDealerInfo,String loginName) {
        ValidationResult result=validator.validate(managerDealerInfo);
        if (result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), result.getErrMsg());
        }
        managerDealerInfo.setCreateTime(new Date());
        managerDealerInfo.setUpdateTime(new Date());
        managerDealerInfo.setPassword(MD5Util.MD5EncodeUtf8("123456"));
        managerDealerInfo.setLastModifier(loginName);
        if (managerDealerInfoMapper.checkUserName(managerDealerInfo.getUsername())==1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), "用户名已存在，请重新输入");
        }
        if (managerDealerInfoMapper.checkName(managerDealerInfo.getName())==1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), "经销商名称已存在");

        }
        if (managerDealerInfoMapper.insertSelective(managerDealerInfo)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        User user=new User();
        user.setUsername(managerDealerInfo.getUsername());
        user.setPassword(managerDealerInfo.getPassword());
        user.setCategoryId(1);
        user.setRoleId(2);
        if (userMapper.insertSelective(user)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        UserToRole userToRole=new UserToRole();
        userToRole.setRoleId(2);
        userToRole.setUid(user.getId());
        if (userToRoleMapper.insert(userToRole)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("成功注册");
    }

    @Override
    public ServerResponse<String> associatedDealer(Integer id,Integer mid) {
        DealerInfo dealerInfo=new DealerInfo();
        dealerInfo=dealerInfoMapper.selectByPrimaryKey(id);
        if (dealerInfo==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (dealerInfo.getManagerId()!=0){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"该经销商已被关联");
        }
        if (!dealerInfo.getStatus()){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"该经销商已被禁用");
        }
        dealerInfo.setManagerId(mid);
        if (dealerInfoMapper.updateByPrimaryKeySelective(dealerInfo)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(), ResponseCode.UPDATE_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("成功关联");
    }

    @Override
    public ServerResponse<List<DealerInfo>> getAllDealer() {
        List<DealerInfo> list = dealerInfoMapper.selectAll();
        return ServerResponse.createBySuccess(list);
    }

    @Override
    public ServerResponse<String> removeAssociatedDealer(Integer id, Integer mid) {
        DealerInfo dealerInfo=dealerInfoMapper.selectByPrimaryKey(id);
        if (dealerInfo==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (dealerInfo.getManagerId()!=mid){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        dealerInfo.setManagerId(0);
        dealerInfoMapper.updateByPrimaryKeySelective(dealerInfo);
        return ServerResponse.createBySuccess("成功解除关联");
    }

    @Override
    public ServerResponse<List<DealerInfo>> getAllAssociatedDealer(Integer mid) {
        List<DealerInfo> list=dealerInfoMapper.selectAllAssociatedBymid(mid);
        return ServerResponse.createBySuccess(list);
    }

    @Override
    public boolean updateStatus(Integer integer, Boolean aBoolean) {
        ManagerDealerInfo managerDealerInfo=new ManagerDealerInfo();
        managerDealerInfo.setId(integer);
        managerDealerInfo.setStatus(aBoolean);
        if (managerDealerInfoMapper.updateByPrimaryKeySelective(managerDealerInfo)==1){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delUserByuid(Integer integer) {
        ManagerDealerInfo managerDealerInfo=managerDealerInfoMapper.selectByPrimaryKey(integer);
        List<DealerInfo> dealerInfos=dealerInfoMapper.selectByManageId(integer);
        dealerInfos.forEach(i -> {
            i.setManagerId(0);
            dealerInfoMapper.updateByPrimaryKeySelective(i);
        });
        if (managerDealerInfo==null){
            return false;
        }
        if (userMapper.deleteByUsername(managerDealerInfo.getUsername())==1&&managerDealerInfoMapper.deleteByPrimaryKey(integer)==1){
            return true;
        }
        else return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponse<String> updateManageDealer(ManagerDealerInfo managerDealerInfo) {
        ValidationResult result=validator.validate(managerDealerInfo);
        managerDealerInfo.setPassword(MD5Util.MD5EncodeUtf8("123456"));
        managerDealerInfo.setUpdateTime(new Date());
        if (managerDealerInfo.getId()==null||result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(), result.getErrMsg());
        }
        ManagerDealerInfo manager=managerDealerInfoMapper.selectByPrimaryKey(managerDealerInfo.getId());
        if (manager==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        if (!manager.getUsername().equals(managerDealerInfo.getUsername())){
            if (managerDealerInfoMapper.checkUserName(managerDealerInfo.getUsername())==1){
                return ServerResponse.createByErrorCode(ResponseCode.NAME_SAME.getCode(), ResponseCode.NAME_SAME.getDesc());
            }
            User user=userMapper.findUserByUserName(manager.getUsername());
            user.setUsername(managerDealerInfo.getUsername());
            userMapper.updateByPrimaryKeySelective(user);
        }
        if (managerDealerInfoMapper.updateByPrimaryKeySelective(managerDealerInfo)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(),ResponseCode.UPDATE_FAIL.getDesc());
        }

        return ServerResponse.createBySuccess("修改成功");
    }

    @Override
    public ServerResponse<Set<ManagerDealerInfo>> searchManageDealer(String name, String username, Boolean status) {
        Set<ManagerDealerInfo> set=new HashSet<>();
        List<ManagerDealerInfo> managerDealerInfos=managerDealerInfoMapper.searchRoleByKeyWord(name,username,status);
        managerDealerInfos.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        managerDealerInfos=managerDealerInfoMapper.searchRoleByPingYingFirst(name,username,status);
        managerDealerInfos.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        managerDealerInfos=managerDealerInfoMapper.searchRoleByPingYing(name,username,status);
        managerDealerInfos.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        return ServerResponse.createBySuccess(set);
    }



    public ServerResponse<List<Category>> getAllCategory() {
        return ServerResponse.createBySuccess(categoryMapper.selectAll());
    }

    @Override
    public List<User> getUserByuid(Integer[] uid) {
        List<ManagerDealerInfo> managerDealerInfos=new ArrayList<>();
        Arrays.stream(uid).forEach(i -> managerDealerInfos.add(managerDealerInfoMapper.selectByPrimaryKey(i)));
        List<User> users=new ArrayList<>();
        managerDealerInfos.forEach(i -> users.add(userMapper.findUserByUserName(i.getUsername())));
        return users;
    }

    @Override
    public ManagerDealerInfo getUserByUsername(String username) {
        return managerDealerInfoMapper.selectByUsername(username);
    }

    @Override
    public ServerResponse<Set<DealerInfo>> searchDealer(String name, int category, Integer status) {
        Set<DealerInfo> set=new HashSet<>();
        List<DealerInfo> dealerInfos=dealerInfoMapper.searchRoleByKeyWord(name,category,status);
        dealerInfos.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        dealerInfos=dealerInfoMapper.searchRoleByPingYingFirst(name,category,status);
        dealerInfos.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        dealerInfos=dealerInfoMapper.searchRoleByPingYing(name,category,status);
        dealerInfos.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        return ServerResponse.createBySuccess(set);
    }
}
