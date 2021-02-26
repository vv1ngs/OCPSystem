package org.vvings.ocpsystem.service;

import org.springframework.stereotype.Service;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.vo.OrderVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/22 16:32
 * @Version 1.0
 */

public interface orderService {
    ServerResponse<List<OrderVo>> getAllFirstOrder(Integer status);

    ServerResponse<String> verifyOrder(Integer orderId,Integer status,String reason,String username);

    ServerResponse<List<String>> getFile(Integer orderId);

    ServerResponse<Set<OrderVo>> searchOrder(String name, Integer categoryId, String dealerId, Integer orderId, Integer status, Integer startyear, Integer endyear, Integer startMonth, Integer endMonth, Integer startxun, Integer endxun, BigDecimal startAmount, BigDecimal endAmount);

    ServerResponse<OrderVo> getAllOrderByID(Integer orderID);
}
