package org.vvings.ocpsystem.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.*;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.dealerService;
import org.vvings.ocpsystem.util.MD5Util;
import org.vvings.ocpsystem.validator.ValidationResult;
import org.vvings.ocpsystem.validator.ValidatorImpl;

import java.util.*;

/**
 * @Author vvings
 * @Date 2020/12/20 18:35
 * @Version 1.0
 */
@Service
public class dealerServiceImpl implements dealerService {
    @Autowired
    private ManagerDealerInfoMapper managerDealerInfoMapper;
    @Autowired
    private DealerChildMapper dealerChildMapper;
    @Autowired
    private DealerInfoMapper dealerInfoMapper;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DealerChildCategoryMapper dealerChildCategoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserToRoleMapper userToRoleMapper;
    @Override
    public ServerResponse<String> addChildDealer(DealerChild dealerChild,String username) {
        dealerChild.setRoleId(3);
        ValidationResult result=validator.validate(dealerChild);
        if (result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), result.getErrMsg());
        }
        ManagerDealerInfo managerDealerInfo=managerDealerInfoMapper.selectByUsername(username);
        if (managerDealerInfo==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        dealerChild.setParentId(managerDealerInfo.getId());
        dealerChild.setPassword(MD5Util.MD5EncodeUtf8("123456"));
        if (dealerChildMapper.selectName(dealerChild.getUsername()) > 0){
            return ServerResponse.createByErrorCode(ResponseCode.NAME_SAME.getCode(),ResponseCode.NAME_SAME.getDesc());
        }
        if (dealerChildMapper.insertSelective(dealerChild)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        User user=new User();
        user.setPassword(dealerChild.getPassword());
        user.setCategoryId(2);
        user.setRoleId(3);
        user.setUsername(dealerChild.getUsername());
        if (userMapper.insertSelective(user)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        UserToRole userToRole=new UserToRole();
        userToRole.setRoleId(3);
        userToRole.setUid(user.getId());
        if (userToRoleMapper.insert(userToRole)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("新增子账号成功");
    }

    @Override
    public boolean updateStatus(Integer integer, Boolean aBoolean) {
        DealerChild operationUser=new DealerChild();
        operationUser.setStatus(aBoolean);
        operationUser.setId(integer);
        if (dealerChildMapper.updateByPrimaryKeySelective(operationUser)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean delDealerByid(Integer integer) {
        if (dealerChildMapper.deleteByPrimaryKey(integer)==1){
            return true;
        }
        return false;
    }

    @Override
    public ServerResponse<String> updateDealer(DealerChild dealerChild) {
        ValidationResult result=validator.validate(dealerChild);
        if (dealerChild.getId()==null||result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.REGISTER_FAIL.getCode(), result.getErrMsg());
        }
        DealerChild old=dealerChildMapper.selectByPrimaryKey(dealerChild.getId());
        if (!old.getUsername().equals(dealerChild.getUsername())){
            if (dealerChildMapper.selectName(dealerChild.getUsername())==1){
                return ServerResponse.createByErrorCode(ResponseCode.NAME_SAME.getCode(),ResponseCode.NAME_SAME.getDesc());
            }
            userMapper.updateUsernameByUsername(dealerChild.getUsername(),old.getUsername());
        }
        if (dealerChildMapper.updateByPrimaryKeySelective(dealerChild)!=1){
            return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(),ResponseCode.UPDATE_FAIL.getDesc());
        }
        return ServerResponse.createBySuccess("修改成功");
    }

    @Override
    public ServerResponse<Set<Category>> getAllCategory(Integer id) {
        List <DealerInfo> lists=dealerInfoMapper.selectByManageId(id);
        Set<Category> categories=new HashSet<>();
        lists.forEach(n -> {
            categories.add(categoryMapper.selectByPrimaryKey(n.getCategoryId()));
        });


        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public boolean allocationRole(Integer integer, Integer uid) {
        DealerChildCategory dealerChildCategory=new DealerChildCategory();
        dealerChildCategory.setCategoryId(integer);
        dealerChildCategory.setDealerId(uid);
        dealerChildCategoryMapper.insert(dealerChildCategory);
        return true;

    }

    @Override
    public ServerResponse<Set<DealerChild>> searchChildDealer(DealerChild dealerChild) {
        Set<DealerChild> set=new HashSet<>();
        List<DealerChild> dealerChildList=dealerChildMapper.searchRoleByKeyWord(dealerChild);
        dealerChildList.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        dealerChildList=dealerChildMapper.searchRoleByPingYingFirst(dealerChild);
        dealerChildList.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        dealerChildList=dealerChildMapper.searchRoleByPingYing(dealerChild);
        dealerChildList.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        return ServerResponse.createBySuccess(set);
    }

    @Override
    public boolean removeAllCategory(Integer uid) {
        List<Integer> list= Arrays.asList(uid);
        if (list.stream().anyMatch(integer -> (dealerChildCategoryMapper.deleteByuid(integer) != 1))){
            return false;
        }
        return true;
    }

    @Override
    public ServerResponse<List<Category>> getChildCategory(Integer childId) {
        List<DealerChildCategory> list=dealerChildCategoryMapper.selectByDealerId(childId);
        List<Category> categories=new ArrayList<>();
        list.forEach(i ->{
            Category category=categoryMapper.selectByPrimaryKey(i.getCategoryId());
            if (category!=null){
                categories.add(categoryMapper.selectByPrimaryKey(i.getCategoryId()));
            }
        });
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public List<User> getUserByuid(Integer[] uid) {
        List<DealerChild> dealerChildList=new ArrayList<>();
        List<User> users=new ArrayList<>();;
        Arrays.stream(uid).forEach(i -> dealerChildList.add(dealerChildMapper.selectByPrimaryKey(i)));
        dealerChildList.forEach(i -> users.add(userMapper.findUserByUserName(i.getUsername())));
        return users;
    }

    @Override
    public DealerChild getUserByusername(String username) {
        return dealerChildMapper.selectByUsername(username);
    }

    @Override
    public ServerResponse<List<DealerChild>> getAllChildDealer(String username) {
        ManagerDealerInfo dealerInfo=managerDealerInfoMapper.selectByUsername(username);
        List<DealerChild> list= dealerChildMapper.selectByparentID(dealerInfo.getId());
        return ServerResponse.createBySuccess(list);
    }


}
