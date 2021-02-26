package org.vvings.ocpsystem.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.UserToRoleMapper;
import org.vvings.ocpsystem.pojo.Authority;
import org.vvings.ocpsystem.pojo.Role;
import org.vvings.ocpsystem.pojo.User;
import org.vvings.ocpsystem.pojo.UserToRole;
import org.vvings.ocpsystem.service.impl.loginServiceImpl;
import org.vvings.ocpsystem.service.loginService;
import org.vvings.ocpsystem.service.roleService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
/**
 * @Author vvings
 * @Date 2020/12/18 22:42
 * @Version 1.0
 */
@Controller("/role")
@RequestMapping("/role")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class RoleController {
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private roleService roleService;
    @Autowired
    private loginService loginService;
    @RequestMapping(value = "/addRole")
    @ResponseBody
    public ServerResponse<String> addRole(Role role){
        return roleService.addRole(role);
    }

    @RequestMapping("/getAllRole")
    @ResponseBody
    public ServerResponse<List<Role>> getAllRole(){
        return roleService.gerAllRole();
    }

    @RequestMapping("/getAllAuthority")
    @ResponseBody
    public ServerResponse<List<Authority>> getAllAuthority(){
        return roleService.getAllAuthority();
    }

    @ResponseBody
    @RequestMapping(value = "/getRoleByOperationUser")
    public ServerResponse<List<Role>> getRoleByOperationUser(String username){
        User user=loginService.getUserByUsername(username);
        return  roleService.getRoleByUid(user.getId());
    }

    @RequestMapping("/getAssociatedAuthority")
    @ResponseBody
    public ServerResponse<Set<Authority>> getAssociatedAuthority(Integer rid){
        return roleService.getAllAuthorityBy(rid);
    }
    @RequestMapping("/associatedAuthority")
    @ResponseBody
    public ServerResponse<List<Authority>> associatedAuthority(Integer[] rid,Integer[] aid){
        List<Integer> list=Arrays.asList(rid);
        Set<User> onlineUsers=roleService.getAllUserByRoleId(rid);
        if (roleService.removeAllAuthority(rid) & list.stream().anyMatch(integer -> (!this.assign(integer, aid)))){
            return ServerResponse.createBySuccess("更新失败");
        };
        onlineUsers.forEach(i ->{
            if (i!=null){
                loginService.kickOutUser(i.getUsername());
            }
        });
        return ServerResponse.createBySuccess("更新成功");
    }



    private boolean assign(Integer rid, Integer[] aid) {
        List<Integer> list=Arrays.asList(aid);
        if (list.stream().allMatch(authorityId->roleService.associatedAuthority(rid,authorityId))){
            return true;
        }
        return false;
    }

    @RequestMapping("/delRole")
    @ResponseBody
    public ServerResponse delRole(Integer[] rid){
        Set<User> onlineUsers=roleService.getAllUserByRoleId(rid);
        List<Integer> list= Arrays.asList(rid);
        if (list.stream().filter(integer -> (!roleService.delUserByuid(integer))).count()==1){
            return ServerResponse.createBySuccess("删除失败");
        };
        onlineUsers.forEach(i ->{
            if (i!=null){
                loginService.kickOutUser(i.getUsername());
            }
        });
        return ServerResponse.createBySuccess("删除成功");
    }
    @RequestMapping("/startOrStopRole")
    @ResponseBody
    public ServerResponse startOrStopRole(Integer[] rid,String flag){
        Set<User> onlineUsers=roleService.getAllUserByRoleId(rid);
        List<Integer> list= Arrays.asList(rid);
        if (list.stream().filter(integer -> (!roleService.updateStatus(integer,Boolean.parseBoolean(flag)))).count()==1){
            return ServerResponse.createBySuccess("更新失败");
        };
        onlineUsers.forEach(i ->{
            if (i!=null){
                loginService.kickOutUser(i.getUsername());
            }
        });
        return ServerResponse.createBySuccess("更新成功");
    }

    @RequestMapping("/updateRole")
    @ResponseBody
    public ServerResponse updateRole(Role role){
        return roleService.updateRole(role);
    }
    @RequestMapping("/searchRole")
    @ResponseBody
    public ServerResponse<Set<Role>> searchRole(String name, Boolean status){
        return roleService.searchRole(name,status);
    }


}
