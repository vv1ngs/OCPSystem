package org.vvings.ocpsystem.controller.portal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.ManagerDealerInfoMapper;
import org.vvings.ocpsystem.pojo.Category;
import org.vvings.ocpsystem.pojo.DealerChild;
import org.vvings.ocpsystem.pojo.ManagerDealerInfo;
import org.vvings.ocpsystem.pojo.User;
import org.vvings.ocpsystem.service.dealerService;
import org.vvings.ocpsystem.service.loginService;
import org.vvings.ocpsystem.service.manageDealerService;
import org.vvings.ocpsystem.util.JacksonUtil;
import org.vvings.ocpsystem.util.UUIDUtil;
import org.vvings.ocpsystem.service.dealerService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author vvings
 * @Date 2020/12/20 18:34
 * @Version 1.0
 */
@Controller("/dealerManagement")
@RequestMapping("/dealerManagement")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class DealerController {
    @Autowired
    private dealerService dealerService;
    @Autowired
    private loginService loginService;
    @Autowired
    private manageDealerService manageDealerService;
    @RequestMapping(value = "/addChildDealer")
    @ResponseBody
    public ServerResponse<String> addChildDealer(@RequestBody  DealerChild dealerChild, Principal principal) {
        String username=principal.getName();
        User user=loginService.getUserByUsername(username);
        return dealerService.addChildDealer(dealerChild,user.getUsername());
    }

    @ResponseBody
    @RequestMapping(value = "/startOrStopDealer")
    public ServerResponse<String> startOrStopDealer(Integer[] uid, String flag){
        List<Integer> list= Arrays.asList(uid);
        List<User> users=dealerService.getUserByuid(uid);
        if (list.stream().filter(integer -> (!dealerService.updateStatus(integer, Boolean.parseBoolean(flag)))).count()==1){
            return ServerResponse.createBySuccess("更新失败");
        }
        users.forEach(i-> loginService.startorStopUsr(i.getUsername(),Boolean.parseBoolean(flag)));
        return ServerResponse.createBySuccess("更新成功");
    }

    @ResponseBody
    @RequestMapping(value = "/delDealer")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<String> delDealer(Integer[] uid){
        List<Integer> list=Arrays.asList(uid);
        List<User> users=dealerService.getUserByuid(uid);
        if (list.stream().filter(integer -> (!dealerService.delDealerByid(integer))).count()==1){
            return ServerResponse.createBySuccess("删除失败");
        };
        users.forEach(i->loginService.dealUserByUsername(i.getUsername()));
        return ServerResponse.createBySuccess("删除成功");
    }

    @ResponseBody
    @RequestMapping(value = "/updateDealer")
    public ServerResponse<String> updateDealer(@RequestBody DealerChild dealerChild){
        return dealerService.updateDealer(dealerChild);
    }
    @ResponseBody
    @RequestMapping(value = "/getAllCategory")
    public ServerResponse<Set<Category>> getAllCategory(Principal principal){
        String username=principal.getName();
        User user=loginService.getUserByUsername(username);

        ManagerDealerInfo managerDealerInfo=manageDealerService.getUserByUsername(user.getUsername());
        return dealerService.getAllCategory(managerDealerInfo.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/getChildCategory")
    public ServerResponse<List<Category>> getChildCategory(Integer childId){
        return dealerService.getChildCategory(childId);
    }

    @ResponseBody
    @RequestMapping(value = "/assignCategory")
    public ServerResponse<String> assignCategory( Integer uid,Integer[] cid,Principal principal){
        User user=loginService.getUserByUsername(principal.getName());

        ManagerDealerInfo managerDealerInfo=manageDealerService.getUserByUsername(user.getUsername());
        Set<Category> categories=dealerService.getAllCategory(managerDealerInfo.getId()).getData();

        if (Arrays.stream(cid).anyMatch(i -> !categories.contains(new Category(i)))){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> list=Arrays.asList(uid);
        if (dealerService.removeAllCategory(uid)& list.stream().anyMatch(integer -> (!this.assign(integer, cid)))){
            return ServerResponse.createBySuccess("分配失败");
        };
        return ServerResponse.createBySuccess("分配成功");
    }

    /**
     *
     * @param uid
     * @param cid
     * @return 只要有一个插入失败返回false 全部插入成功返回true
     */
    public Boolean assign(Integer uid,Integer[] cid){
        List<Integer> list=Arrays.asList(cid);
        return list.stream().anyMatch(integer -> !(dealerService.allocationRole(integer, uid)));
    }
    @ResponseBody
    @RequestMapping(value = "/getAllChildDealer")
    public ServerResponse<List<DealerChild>> getAllChildDealer(Principal principal){
        if (principal==null){
            return ServerResponse.createByErrorCode(ResponseCode.RELOGIN.getCode(),ResponseCode.RELOGIN.getDesc());
        }
        String username=principal.getName();
        if (username==null){
            return ServerResponse.createByErrorCode(ResponseCode.RELOGIN.getCode(),ResponseCode.RELOGIN.getDesc());
        }
        User user=loginService.getUserByUsername(username);
        return dealerService.getAllChildDealer(username);
    }
    @ResponseBody
    @RequestMapping(value = "/searchChildDealer")
    public ServerResponse<Set<DealerChild>> searchChildDealer(@RequestBody DealerChild dealerChild){
        if (StringUtils.isEmpty(dealerChild.getUsername())){
            dealerChild.setUsername(null);
        }
        if (StringUtils.isEmpty(dealerChild.getRealname())){
            dealerChild.setRealname(null);
        }
        if (StringUtils.isEmpty(dealerChild.getPhone())){
            dealerChild.setPhone(null);
        }
        if (StringUtils.isEmpty(dealerChild.getUsername())){
            dealerChild.setUsername(null);
        }
        return dealerService.searchChildDealer(dealerChild);
    }
}
