package org.vvings.ocpsystem.controller.backend;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.ManagerDealerInfo;
import org.vvings.ocpsystem.pojo.Role;
import org.vvings.ocpsystem.pojo.User;
import org.vvings.ocpsystem.pojo.operationUser;
import org.vvings.ocpsystem.service.loginService;
import org.vvings.ocpsystem.service.userService;
import org.vvings.ocpsystem.util.JacksonUtil;
import org.vvings.ocpsystem.util.UUIDUtil;
import org.vvings.ocpsystem.vo.AddUserVO;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @Author vvings
 * @Date 2020/12/18 19:56
 * @Version 1.0
 */
@Controller("/operationUser")
@RequestMapping("/operationUser")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class UserController {
    @Autowired
    private userService userService;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private loginService loginService;

    @RequestMapping(value = "/addOperationUser")
    @ResponseBody
    public ServerResponse<String> addOperationUser(@RequestBody AddUserVO user){
        return userService.addUser(user.getUser(),user.getRole());
    }

    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public ServerResponse<User> getUserInfo(Principal principal){
        if (principal==null){
            return ServerResponse.createByErrorCode(ResponseCode.RELOGIN.getCode(),ResponseCode.RELOGIN.getDesc());

        }
        String username=principal.getName();
        if (username==null){
            return ServerResponse.createByErrorCode(ResponseCode.RELOGIN.getCode(),ResponseCode.RELOGIN.getDesc());
        }
        User user=loginService.getUserByUsername(username);
        return ServerResponse.createBySuccess(user);
    }

    @ResponseBody
    @RequestMapping(value = "/delOperationUser")
    @Transactional
    public ServerResponse<String> delOperationUser( Integer[] uid){
        List<Integer> list=Arrays.asList(uid);
        Object savePoint= TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        if (list.stream().filter(integer -> (!userService.delUserByuid(integer))).count()==1){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return ServerResponse.createByErrorMessage("删除失败");
        };
        return ServerResponse.createBySuccess("删除成功");
    }



    @ResponseBody
    @RequestMapping(value = "/updateOperationUser")
    public ServerResponse<String> updateOperationUser(@RequestBody AddUserVO operationUser){
        return userService.updateUser(operationUser.getUser());
    }

    @ResponseBody
    @RequestMapping(value = "/startOrStopUser")
    public ServerResponse<String> startOrStopUser(Integer[] uid,String flag){
        List<Integer> list=Arrays.asList(uid);
        Set<User> onlineUser=userService.getAllOnlineUser(uid);
        if (list.stream().filter(integer -> (!userService.updateStatus(integer, Boolean.parseBoolean(flag)))).count()==1){
            return ServerResponse.createByErrorMessage("更新失败");
        }
        onlineUser.forEach(i ->{
            if (i!=null){
                loginService.startorStopUsr(i.getUsername(),Boolean.parseBoolean(flag));
                loginService.kickOutUser(i.getUsername());
            }
        });
        return ServerResponse.createBySuccess("更新成功");
    }
    @ResponseBody
    @RequestMapping(value = "/getAllUser")
    public ServerResponse<List<operationUser>> getAllUser(Principal principal){

        return userService.getAllUser();

    }

    @ResponseBody
    @RequestMapping(value = "/allocationRole")
    public ServerResponse<String> startOrStopUser(Integer[] uid,Integer[] rid){
        List<Integer> list=Arrays.asList(uid);
        Set<User> onlineUser=userService.getAllOnlineUser(uid);
        if (userService.removeRole(uid)&&list.stream().anyMatch(integer -> (!this.assign(integer,rid)))){
            return ServerResponse.createByErrorMessage("更新失败");
        };
        onlineUser.forEach(i ->{
            if (i!=null){
                loginService.kickOutUser(i.getUsername());
            }
        });
        return ServerResponse.createBySuccess("更新成功");
    }

    @ResponseBody
    @RequestMapping(value = "/searchUser")
    public ServerResponse<Set<operationUser>> searchUser( String username,  Boolean status){
        return userService.searchUser(username,status);
    }

    private  Boolean assign(Integer uid,Integer[] rid){
        List<Integer> list=Arrays.asList(rid);
        if (list.stream().anyMatch(i->!userService.allocationRole(uid,i))){
            return false;
        };
        return true;
    }


}
