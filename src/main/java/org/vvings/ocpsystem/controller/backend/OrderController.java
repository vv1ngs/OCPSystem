package org.vvings.ocpsystem.controller.backend;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.service.orderService;
import org.vvings.ocpsystem.vo.OrderVo;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/22 16:31
 * @Version 1.0
 */
@Controller("/order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class OrderController {
    @Autowired
    private orderService orderService;
    @ResponseBody
    @RequestMapping("/getAllOrderByStatus")
    public ServerResponse<List<OrderVo>> getAllFirstOrder(Integer status){
        return orderService.getAllFirstOrder(status);
    }
    @ResponseBody
    @RequestMapping("/getAllOrderByOrderID")
    public ServerResponse<OrderVo> getOrderById(Integer orderID){
        return orderService.getAllOrderByID(orderID);
    }

    @ResponseBody
    @RequestMapping("/verifyOrder")
    public ServerResponse<String> verifyOrder(Integer orderId, Integer status, String reason, Principal principal){
        return orderService.verifyOrder(orderId,status,reason,principal.getName());
    }

    @ResponseBody
    @RequestMapping("/getFile")
    public ServerResponse<List<String>> getFile(Integer orderId){
        return orderService.getFile(orderId);
    }

    /**
     *
     * @param dealerName 经销商名字
     * @param categoryId 区域
     * @param dealerId 经销商编码
     * @param orderId 订单号
     * @param status 订单状态
     * @param startyear 开始年
     * @param endyear 结束年
     * @param startMonth 开始月
     * @param endMonth 结束月
     * @param startxun 开始旬
     * @param endxun 结束旬
     * @param startAmount 开始金额
     * @param endAmount 结束金额
     * @return
     */
    @ResponseBody
    @RequestMapping("/searchOrder")
        public ServerResponse<Set<OrderVo>> searchOrder(String dealerName, Integer categoryId, String dealerId, Integer orderId, Integer status, Integer startyear, Integer endyear, Integer startMonth, Integer endMonth, Integer startxun, Integer endxun, BigDecimal startAmount,BigDecimal endAmount){
        if (StringUtils.isEmpty(dealerName)){
            dealerName=null;
        }
        if (StringUtils.isEmpty(dealerId)){
            dealerId=null;
        }
        if (startAmount.toString().equals("0")){
            startAmount=null;
        }
        if (endAmount.toString().equals("0")){
            endAmount=null;
        }
        return orderService.searchOrder(dealerName,categoryId,dealerId,orderId,status,startyear,endyear,startMonth,endMonth,startxun,endxun,startAmount,endAmount);

    }
}
