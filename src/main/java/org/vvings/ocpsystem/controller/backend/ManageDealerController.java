package org.vvings.ocpsystem.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.Category;
import org.vvings.ocpsystem.pojo.DealerInfo;
import org.vvings.ocpsystem.pojo.ManagerDealerInfo;
import org.vvings.ocpsystem.pojo.User;
import org.vvings.ocpsystem.service.loginService;
import org.vvings.ocpsystem.service.manageDealerService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/19 22:55
 * @Version 1.0
 */
@Controller("/managerDealer")
@RequestMapping("/managerDealer")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ManageDealerController {

    @Autowired
    private manageDealerService manageDealerService;
    @Autowired
    private loginService loginService;
    @ResponseBody
    @RequestMapping(value = "/addManageDealer")
    public ServerResponse<String> addManageDealer(@RequestBody  ManagerDealerInfo managerDealerInfo, Principal principal){
        if (principal!=null){
            return manageDealerService.addManageDealer(managerDealerInfo,principal.getName());
        }
        return manageDealerService.addManageDealer(managerDealerInfo,null);
    }


    @ResponseBody
    @RequestMapping(value = "/associatedDealer")
    public ServerResponse<String> associatedDealer(Integer manageId,Integer dealerId){
        return manageDealerService.associatedDealer(dealerId,manageId);
    }
    @ResponseBody

    @RequestMapping(value = "/removeAssociatedDealer")
    public ServerResponse<String> removeAssociatedDealer(Integer dealerId,Integer manageId){
        return manageDealerService.removeAssociatedDealer(dealerId,manageId);
    }
    @ResponseBody
    @RequestMapping(value = "/getAllDealer")
    public ServerResponse<List<DealerInfo>> getAllDealer(){
        return manageDealerService.getAllDealer();
    }

    @ResponseBody
    @RequestMapping(value = "/getAllAssociatedDealer")
    public ServerResponse<List<DealerInfo>> getAllAssociatedDealer(Integer mid){
        return manageDealerService.getAllAssociatedDealer(mid);
    }

    @ResponseBody
    @RequestMapping(value = "/startOrStopManageDealer")
    public ServerResponse<String> startOrStopUser(Integer[] uid, String flag){
        List<Integer> list= Arrays.asList(uid);
        List<User> users=manageDealerService.getUserByuid(uid);
        if (list.stream().anyMatch(integer -> (!manageDealerService.updateStatus(integer, Boolean.parseBoolean(flag))))){
            return ServerResponse.createBySuccess("更新失败");
        };
        users.forEach(i -> loginService.startorStopUsr(i.getUsername(),Boolean.parseBoolean(flag)));
        return ServerResponse.createBySuccess("更新成功");
    }

    @ResponseBody
    @RequestMapping(value = "/delManageDealer")
    public ServerResponse<String> delManageDealer(Integer[] uid){
        List<Integer> list= Arrays.asList(uid);
        if (list.stream().anyMatch(integer -> (!manageDealerService.delUserByuid(integer)))){
            return ServerResponse.createByErrorMessage("删除失败");
        };
        return ServerResponse.createBySuccess("删除成功");
    }

    @ResponseBody
    @RequestMapping(value = "/updateManageDealer")
    public ServerResponse<String> updateManageDealer(@RequestBody  ManagerDealerInfo managerDealerInfo){
        return manageDealerService.updateManageDealer(managerDealerInfo);
    }
    @ResponseBody
    @RequestMapping(value = "/searchManageDealer")
    public ServerResponse<Set<ManagerDealerInfo>> searchManageDealer(String name, String username, Boolean status){
        return manageDealerService.searchManageDealer(name,username,status);
    }

    @RequestMapping("/getAllCategory")
    @ResponseBody
    public ServerResponse<List<Category>> getAllCategory(){
        return manageDealerService.getAllCategory();
    }

    @ResponseBody
    @RequestMapping(value = "/searchDealer")
    public ServerResponse<Set<DealerInfo>> searchDealer(String name, int category, Integer status){
        return manageDealerService.searchDealer(name,category,status);
    }
}
